package com.tencent.supersonic.chat.parser.cw;

import com.tencent.supersonic.chat.agent.NL2SQLTool;
import com.tencent.supersonic.chat.api.pojo.*;
import com.tencent.supersonic.chat.api.pojo.request.QueryFilter;
import com.tencent.supersonic.chat.parser.cw.utils.FuzzyRangeItem;
import com.tencent.supersonic.chat.parser.cw.utils.FuzzyRangeMatchUtil;
import com.tencent.supersonic.chat.parser.sql.llm.ParseResult;
import com.tencent.supersonic.chat.query.QueryManager;
import com.tencent.supersonic.chat.query.llm.LLMSemanticQuery;
import com.tencent.supersonic.chat.query.llm.s2sql.LLMResp;
import com.tencent.supersonic.chat.query.llm.s2sql.LLMSqlQuery;
import com.tencent.supersonic.chat.utils.QueryReqBuilder;
import com.tencent.supersonic.common.pojo.Constants;
import com.tencent.supersonic.common.pojo.Order;
import com.tencent.supersonic.common.pojo.RatioDateConf;
import com.tencent.supersonic.common.pojo.enums.AggregateTypeEnum;
import com.tencent.supersonic.common.pojo.enums.FilterOperatorEnum;
import com.tencent.supersonic.common.util.ContextUtils;
import com.tencent.supersonic.common.util.JsonUtil;
import com.tencent.supersonic.common.util.StringUtil;
import com.tencent.supersonic.common.util.jsqlparser.SqlParserEqualHelper;
import com.tencent.supersonic.common.util.jsqlparser.SqlParserSelectFunctionHelper;
import com.tencent.supersonic.common.util.jsqlparser.SqlParserSelectHelper;
import com.tencent.supersonic.common.util.xktime.formatter.DateTimeFormatterUtil;
import com.tencent.supersonic.common.util.xktime.nlp.TimeNLP;
import com.tencent.supersonic.common.util.xktime.nlp.TimeNLPUtil;
import com.tencent.supersonic.headless.api.query.request.QueryStructReq;
import com.tencent.supersonic.headless.query.persistence.pojo.QueryStatement;
import com.tencent.supersonic.headless.query.service.HeadlessQueryEngine;
import com.tencent.supersonic.knowledge.service.SchemaService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.map.HashedMap;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
public class CwResponseService {

    private final HeadlessQueryEngine headlessQueryEngine;

    public CwResponseService(HeadlessQueryEngine headlessQueryEngine) {
        this.headlessQueryEngine = headlessQueryEngine;
    }

    public SemanticParseInfo addParseInfo(QueryContext queryCtx, CwParseResult parseResult, CwResp cwResp) throws Exception {
//        headlessQueryEngine = SpringContextUtil.getBean(HeadlessQueryEngine.class);

        LLMSemanticQuery semanticQuery = QueryManager.createLLMQuery(CwQuery.QUERY_MODE);
        SemanticParseInfo parseInfo = semanticQuery.getParseInfo();
        parseInfo.setModel(parseResult.getModelCluster());
        NL2SQLTool commonAgentTool = parseResult.getCommonAgentTool();
        parseInfo.getElementMatches().addAll(queryCtx.getModelClusterMapInfo()
                .getMatchedElements(parseInfo.getModelClusterKey()));

        Map<String, Object> properties = new HashMap<>();
        properties.put(Constants.CONTEXT, parseResult);
        properties.put("type", "internal");
        properties.put("name", commonAgentTool.getName());

        parseInfo.setProperties(properties);
        parseInfo.setScore(queryCtx.getRequest().getQueryText().length() * (1 + 1));
        parseInfo.setQueryMode(semanticQuery.getQueryMode());
        parseInfo.getSqlInfo().setS2SQL(JsonUtil.prettyToString(cwResp.getJsonDsl()));
        parseInfo.setModel(parseResult.getModelCluster());

        // 核心：根据模型返回内容构建sql信息, 结果填充至cwResp中
        parserLLMResultToSql(cwResp, parseInfo);
        parseInfo.getSqlInfo().setQuerySQL(cwResp.getCorrectorSql());

        queryCtx.getCandidateQueries().add(semanticQuery);
        return parseInfo;
    }

    /**
     * 根据llm返回的内容构建合理的sql
     *
     * @param cwResp          LLM返回的内容
     * @param cwDslParserInfo 这是当前Query的基础ParserInfo信息，我们构建Sql语句需要用到这里面的内容
     */
    private void parserLLMResultToSql(CwResp cwResp, SemanticParseInfo cwDslParserInfo) throws Exception {
        /**
         * 帮我查询前年每个季度的平均销售价是多少？
         * "{
         *     ""Entity"": [
         *         ""前年每个季度"",
         *         ""平均"",
         *         ""销售价""
         *     ],
         *     ""Dimension"": [
         *         ""销售日期""
         *     ],
         *     ""Filters"": {""销售日期"":""前年""},
         *     ""Metrics"": [
         *         ""实际售价""
         *     ],
         *     ""Operator"": ""求均值"",
         *     ""Groupby"": [""销售日期按季度""]
         * }"
         *
         * SELECT 销售日期,求均值(实际售价) FROM t_1 WHERE 销售日期 = 前年 GROUP BY 销售日期按季度
         *
         * SELECT QUARTER(buy_time),AVG(sell_price) FROM car_sell WHERE YEAR(buy_time) = YEAR(DATE_SUB(NOW(), INTERVAL 2 YEAR)) GROUP BY QUARTER(buy_time) ORDER BY QUARTER(buy_time)
         */
        // 通过SemanticParseInfo的信息构建一个查询的结构体让Calcite引擎渲染成sql
        SemanticParseInfo dslParseInfo = buildParseInfoByCwRsp(cwResp, cwDslParserInfo);

        QueryStatement dslStatement = new QueryStatement();
        QueryStructReq dslStruct = QueryReqBuilder.buildStructReqNew(dslParseInfo);
        dslStatement.setQueryStructReq(dslStruct);
        QueryStatement resultStatement;
        try {
            resultStatement = headlessQueryEngine.simplePlan(dslStatement);
        } catch (Exception e) {
            log.error("build cw sql err ", e);
            return;
        }
//        queryStatement.setSql("SELECT QUARTER(buy_time),AVG(sell_price) FROM t_1 WHERE YEAR(buy_time) = YEAR(DATE_SUB(NOW(), INTERVAL 2 YEAR)) GROUP BY QUARTER(buy_time) ORDER BY QUARTER(buy_time)");
        // 填充sql信息
        cwResp.setCorrectorSql(resultStatement.getSql());
        cwResp.setSqlSourceId(resultStatement.getSourceId());
    }

    /**
     * llm原始响应数据构建SemanticParseInfo
     *
     * @param cwResp llm原始响应的数据
     */
    private SemanticParseInfo buildParseInfoByCwRsp(CwResp cwResp, SemanticParseInfo cwDslParserInfo) throws Exception {
        SemanticParseInfo tmpSqlParseInfo = new SemanticParseInfo();

        BeanUtils.copyProperties(cwDslParserInfo, tmpSqlParseInfo);
        List<SchemaElement> metrics = new ArrayList<>();
        Set<SchemaElement> dimensions = tmpSqlParseInfo.getDimensions();
        Set<QueryFilter> metricFilters = tmpSqlParseInfo.getMetricFilters();
        Set<QueryFilter> dimensionFilters = tmpSqlParseInfo.getDimensionFilters();
        Set<Order> orders = tmpSqlParseInfo.getOrders();
        List<SchemaElementMatch> elementMatches = tmpSqlParseInfo.getElementMatches();
        dimensions.clear();
        metricFilters.clear();
        dimensionFilters.clear();
        orders.clear();
        elementMatches.clear();

        // TODO 后续优化 此处可以借鉴getCorrectorSql中的sql纠正链

        // 1. 根据维度指标关键词 去向量库读取对应的列
        List<CwVecDBResp.SimilaritySearchItem> dimensionMappers = requestCwVecDB(
                cwResp.getDimension(),
                new VecFilterBO(VecFilterBO.VecFilterTypeEnum.DIMENSION));
        List<CwVecDBResp.SimilaritySearchItem> metricsMappers = requestCwVecDB(
                cwResp.getMetrics(),
                new VecFilterBO(VecFilterBO.VecFilterTypeEnum.METRIC));

        List<CwVecDBResp.SimilaritySearchItem> dimMetAllMappers = new ArrayList<>();
        if (null != dimensionMappers && dimensionMappers.size() > 0) {
            dimMetAllMappers.addAll(dimensionMappers);
        }
        if (null != metricsMappers && metricsMappers.size() > 0) {
            dimMetAllMappers.addAll(metricsMappers);
        }

        List<CwVecDBResp.SimilaritySearchItem> filterMappers = new ArrayList<>();
        List<String> filterQuery = new ArrayList<>(cwResp.getFilters().keySet());
        Iterator<String> filterQueryIterator = filterQuery.iterator();
        while (filterQueryIterator.hasNext()) {
            String filterKey = filterQueryIterator.next();
            // 维度指标已经查询过的key直接填充
            for (CwVecDBResp.SimilaritySearchItem item : dimMetAllMappers) {
                if (item.getOrigin_name().equals(filterKey)) {
                    filterMappers.add(item);
                    filterQueryIterator.remove();
                    break;
                }
            }
        }
        if (filterQuery.size() > 0) {
            List<CwVecDBResp.SimilaritySearchItem> queryMappers = requestCwVecDB(
                    new ArrayList<>(filterQuery),
                    null);
            if (null != queryMappers && queryMappers.size() > 0) {
                filterMappers.addAll(queryMappers);
            }
        }

        List<CwVecDBResp.SimilaritySearchItem> groupByMappers = requestCwVecDB(
                cwResp.getGroupBy(),
                new VecFilterBO(VecFilterBO.VecFilterTypeEnum.DIMENSION));
//        if (null != dimensionMappers) {
//            for (CwVecDBResp.SimilaritySearchItem dim : dimensionMappers) {
//                if (dim.getSearch().size() == 0) {
//                    continue;
//                }
//                SchemaElement element = SchemaElement.builder()
//                        .type(SchemaElementType.DIMENSION)
//                        .id(dim.getSearch().get(0).getMetadata().getDimension_id())
//                        .bizName(dim.getSearch().get(0).getMetadata().getBiz_name())
//                        .name(dim.getSearch().get(0).getPage_content())
//                        .build();
//                dimensions.add(element);
//            }
//        }

        if (null != metricsMappers) {
            for (CwVecDBResp.SimilaritySearchItem met : metricsMappers) {
                if (met.getSearch().size() == 0) {
                    continue;
                }
                SchemaElement element = SchemaElement.builder()
                        .type(SchemaElementType.METRIC)
                        .id(met.getSearch().get(0).getMetadata().getMetric_id())
                        .bizName(met.getSearch().get(0).getMetadata().getBiz_name())
                        .name(met.getSearch().get(0).getPage_content())
                        .build();
                metrics.add(element);
            }
        }

        // 2. 根据函数找到对应的函数信息 以及可能的存储过程定义
        List<CwFuncDBResp.SimilaritySearchItem> funcMappers = requestCwFuncDB(
                cwResp.getOperator()
        );
        if (null != funcMappers && funcMappers.size() > 0) {
            processFuncMapper(tmpSqlParseInfo, funcMappers.get(0), metrics);
        }

        // 3. 处理SELECT的元素信息（会有函数包含）

        // 4. 处理WHERE条件的函数
        if (null != filterMappers) {
            for (CwVecDBResp.SimilaritySearchItem fil : filterMappers) {
                if (fil.getSearch().size() == 0) {
                    continue;
                }
                List<QueryFilter> element = formatToQueryFilter(
                        fil,
                        cwResp
                );
                String simItemType = fil.getSearch().get(0).getMetadata().getItem_type();
                // 次数目前仅使用dimensionFilter用来过滤where条件，因为使用metricFilter后，在SimpleAggConvert中获取不了metricFilter条件，为了不破坏规则流程，暂不使用metricFilter
                dimensionFilters.addAll(element);
//                if (simItemType.equals("DIMENSION")) {
//                    dimensionFilters.addAll(element);
//                } else if (simItemType.equals("METRIC")) {
//                    metricFilters.addAll(element);
//                } else {
//                    log.error(" cw parse filter type error");
//                }
            }
        }

        // 5. 处理GROUP_BY的函数
        // TODO 这里按道理直接用groupby对应的数据进行维度映射即可，维度会直接体现在sql的groupby中
        // TODO 后续优化可以用groupby的数据和维度的数据进行求交匹配 获取更准确的维度信息
        if (null != groupByMappers) {
            for (CwVecDBResp.SimilaritySearchItem grop : groupByMappers) {
                if (grop.getSearch().size() == 0) {
                    continue;
                }
                // TODO 这里需要根据原始信息 解析出维度可能用到的时间函数 或 其他函数，重点是时间函数, 同时设置DataInfo
                SchemaElement element = formatToGroupBy(tmpSqlParseInfo, grop, tmpSqlParseInfo);
                // 去重
                List<SchemaElement> exist = dimensions.stream().filter(item -> item.getBizName().equals(element.getBizName())).collect(Collectors.toList());
                if (exist.size() == 0) {
                    dimensions.add(element);
                }
            }
        }
        // 6. 处理可能的ORDER_BY 没有的话默认用指标OrderBy
        // TODO 暂时不处理排序逻辑 优先把其他内容调通

        // 7. 处理LIMIT
        // 需要将metrics赋值还原，这里不是引用赋值
        tmpSqlParseInfo.setMetricsByList(metrics);
        return tmpSqlParseInfo;
    }

    /**
     * 请求向量库获取列表中的关联数据字段
     *
     * @param requestItem 待匹配字段
     * @return 相似匹配内容
     */
    private List<CwVecDBResp.SimilaritySearchItem> requestCwVecDB(List<String> requestItem, VecFilterBO vecFilterBO) {
        CwParserConfig cwParserConfig = ContextUtils.getBean(CwParserConfig.class);
        String questUrl = cwParserConfig.getUrl() + cwParserConfig.getVecSimilaritySearchPath();
        long startTime = System.currentTimeMillis();
        log.info("requestCwVecDB :{}", requestItem.toString());
        RestTemplate restTemplate = ContextUtils.getBean(RestTemplate.class);
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            Map<String, Object> request = new HashedMap();
            request.put("query_text_list", requestItem);
            if (null != vecFilterBO) {
                request.put("query_filter", vecFilterBO);
            }
            HttpEntity<String> entity = new HttpEntity<>(JsonUtil.toString(request), headers);
            ResponseEntity<CwVecDBResp> responseEntity = restTemplate.exchange(questUrl, HttpMethod.POST, entity,
                    CwVecDBResp.class);

            log.info("requestCwVecDB response,cost:{}, questUrl:{} \n entity:{} \n body:{}",
                    System.currentTimeMillis() - startTime, questUrl, entity, responseEntity.getBody());
            return Objects.requireNonNull(responseEntity.getBody()).getData();
        } catch (Exception e) {
            log.error("requestCwVecDB error", e);
        }
        return null;
    }

    private List<CwFuncDBResp.SimilaritySearchItem> requestCwFuncDB(String operator) {
        CwParserConfig cwParserConfig = ContextUtils.getBean(CwParserConfig.class);
        String questUrl = cwParserConfig.getUrl() + cwParserConfig.getFunSimilaritySearchPath();
        long startTime = System.currentTimeMillis();
        log.info("requestCwFuncDB :{}", operator);
        RestTemplate restTemplate = ContextUtils.getBean(RestTemplate.class);
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<String> entity = new HttpEntity<>(JsonUtil.toString(Collections.singletonList(operator)), headers);
            ResponseEntity<CwFuncDBResp> responseEntity = restTemplate.exchange(questUrl, HttpMethod.POST, entity,
                    CwFuncDBResp.class);

            log.info("requestCwFuncDB response,cost:{}, questUrl:{} \n entity:{} \n body:{}",
                    System.currentTimeMillis() - startTime, questUrl, entity, responseEntity.getBody());
            return Objects.requireNonNull(responseEntity.getBody()).getData();
        } catch (Exception e) {
            log.error("requestCwFuncDB error", e);
        }
        return null;
    }

    private List<CwDimDBResp.SimilaritySearchItem> requestCwDimDB(String dimNLVal, DimFilterBO dimFilterBO) {
        CwParserConfig cwParserConfig = ContextUtils.getBean(CwParserConfig.class);
        String questUrl = cwParserConfig.getUrl() + cwParserConfig.getDimValSimilaritySearchPath();
        long startTime = System.currentTimeMillis();
        log.info("requestCwDimDB :{}", dimNLVal);
        RestTemplate restTemplate = ContextUtils.getBean(RestTemplate.class);
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            Map<String, Object> request = new HashedMap();
            request.put("query_text_list", Collections.singletonList(dimNLVal));
            if (null != dimFilterBO) {
                request.put("query_filter", dimFilterBO);
            }
            HttpEntity<String> entity = new HttpEntity<>(JsonUtil.toString(request), headers);
            ResponseEntity<CwDimDBResp> responseEntity = restTemplate.exchange(questUrl, HttpMethod.POST, entity,
                    CwDimDBResp.class);

            log.info("requestCwDimDB response,cost:{}, questUrl:{} \n entity:{} \n body:{}",
                    System.currentTimeMillis() - startTime, questUrl, entity, responseEntity.getBody());
            return Objects.requireNonNull(responseEntity.getBody()).getData();
        } catch (Exception e) {
            log.error("requestCwDimDB error", e);
        }
        return null;
    }

    /**
     * 处理不用的Operator
     *
     * @param tmpSqlParseInfo 需要填充的实体类
     * @param searchItem      查出来的数据
     */
    private void processFuncMapper(SemanticParseInfo tmpSqlParseInfo, CwFuncDBResp.SimilaritySearchItem searchItem, List<SchemaElement> metricList) throws Exception {
        List<CwFuncDBResp.MatchInfo> simList = searchItem.getSearch();
        String originName = searchItem.getOrigin_name();
        if (simList.size() <= 0) {
            return;
        }
        if (metricList.size() <= 0) {
            return;
        }

        AggregateTypeEnum type = AggregateTypeEnum.of(simList.get(0).getMetadata().getFunc_name());
        SchemaElement metricElement = metricList.get(0);
        if (simList.get(0).getMetadata().getFunc_name().equals("TOP")) {
            // 最高默认聚合为SUM，增加OrderBy 降序和LIMIT=1
            type = AggregateTypeEnum.SUM;
            tmpSqlParseInfo.setLimit(1L);
            if (StringUtils.isNotEmpty(simList.get(0).getMetadata().getFunc_content())) {
                tmpSqlParseInfo.setLimit(Long.parseLong(simList.get(0).getMetadata().getFunc_content()));
            }
            tmpSqlParseInfo.getOrders().add(new Order(metricElement.getBizName(), Constants.DESC_UPPER));
        }
        tmpSqlParseInfo.setAggType(type);
        if (type == AggregateTypeEnum.RATIO_OVER || type == AggregateTypeEnum.RATIO_ROLL) {
            // 出现同比环比 需要构建一个日期信息，存储同比环比的对应的日期字段
            RatioDateConf dataConf = new RatioDateConf();
            dataConf.setDateMode(RatioDateConf.DateMode.BETWEEN);
            dataConf.setPeriod(Constants.DAY);
            tmpSqlParseInfo.setRatioDataInfo(dataConf);
        }
    }

    /**
     * 解析过滤条件值
     *
     * @param fil    相似度匹配返回的条目信息
     * @param cwResp 模型返回的信息
     * @return List<QueryFilter> 条件合集
     */
    private List<QueryFilter> formatToQueryFilter(CwVecDBResp.SimilaritySearchItem fil, CwResp cwResp) throws Exception {
        CwParserConfig cwParserConfig = ContextUtils.getBean(CwParserConfig.class);
        List<QueryFilter> resultFilters = new ArrayList<>();
        // 0. 获取当前条件的基础信息key value, 准备结果元素
        Map<String, String> filterMap = cwResp.getFilters();
        String filterKey = fil.getSearch().get(0).getPage_content();
        String filterNLValue = filterMap.get(fil.getOrigin_name());

        String resultKey = filterKey;
        String resultValue = filterNLValue;
        FilterOperatorEnum filterOperator = FilterOperatorEnum.EQUALS;
        String simItemType = fil.getSearch().get(0).getMetadata().getItem_type();
        String simDbType = fil.getSearch().get(0).getMetadata().getDb_type();
        // 1. 根据filterNLValue  ""Filters"": {""销售日期"":""前年""},格式化真正的值

        // 解析是否是日期 去年5月 2022-05-01 00:00:00
        String bizName = fil.getSearch().get(0).getMetadata().getBiz_name();
        List<TimeNLP> timeNLPList = TimeNLPUtil.parse(filterNLValue);
        List<FuzzyRangeItem> fuzzyRangeItems = FuzzyRangeMatchUtil.fuzzyRangeAnalysis(filterNLValue);
        if (simItemType.equals("DIMENSION") && simDbType.equals("timestamp")) {
            if (timeNLPList.size() == 0) {
                // TODO 未匹配到，需要给一个默认的日期
            }
            // TODO 判断返回 年月日 还是年 还是月 还是日 季度 周
            if (filterNLValue.contains("年") && !filterNLValue.contains("月") && !filterNLValue.contains("日")) {
                String year = DateTimeFormatterUtil.format(timeNLPList.get(0).getTime(), DateTimeFormatterUtil.YYYY_FMT);
                QueryFilter element = QueryFilter.builder()
                        .bizName(String.format(MySqlFuncUtil.YEAR_FUNC_FMT, bizName))
                        .name(resultKey)
                        .value(year)
                        .alia(bizName + "_" + Constants.YEAR)
                        .operator(filterOperator)
                        .build();
                resultFilters.add(element);
            } else if (filterNLValue.contains("月") && !filterNLValue.contains("日")) {
                String month = DateTimeFormatterUtil.format(timeNLPList.get(0).getTime(), DateTimeFormatterUtil.YYYY_MM_FMT);
                QueryFilter element = QueryFilter.builder()
                        .bizName(String.format(MySqlFuncUtil.MONTH_FUNC_FMT,
                                bizName))
                        .name(resultKey)
                        .value(month)
                        .alia(bizName + "_" + Constants.MONTH)
                        .operator(filterOperator)
                        .build();
                resultFilters.add(element);
            } else if (filterNLValue.contains("日")) {
                String day = DateTimeFormatterUtil.format(timeNLPList.get(0).getTime(), DateTimeFormatterUtil.YYYY_MM_DD_FMT);
                QueryFilter element = QueryFilter.builder()
                        .bizName(String.format(MySqlFuncUtil.DAY_FUNC_FMT,
                                bizName))
                        .name(resultKey)
                        .alia(bizName + "_" + Constants.DAY)
                        .value(day)
                        .operator(filterOperator)
                        .build();
                resultFilters.add(element);
            } else {
                // 处理其他日期 去年 本周
                if (timeNLPList.size() == 2) {
                    QueryFilter startElement = QueryFilter.builder()
                            .bizName(bizName)
                            .name(resultKey)
                            .value(DateTimeFormatterUtil.formatToDateTimeStr(timeNLPList.get(0).getTime()))
                            .operator(FilterOperatorEnum.GREATER_THAN_EQUALS)
                            .build();
                    QueryFilter endElement = QueryFilter.builder()
                            .bizName(bizName)
                            .name(resultKey)
                            .value(DateTimeFormatterUtil.formatToDateTimeStr(timeNLPList.get(1).getTime()))
                            .operator(FilterOperatorEnum.MINOR_THAN_EQUALS)
                            .build();
                    resultFilters.add(startElement);
                    resultFilters.add(endElement);
                }
            }
        } else if (fuzzyRangeItems.size() > 0) {
            for (FuzzyRangeItem item : fuzzyRangeItems) {
                String possibleValue = item.getReasonableValue();
                possibleValue = FuzzyRangeMatchUtil.fuzzyMetricValueFormat(possibleValue);
                QueryFilter element = QueryFilter.builder()
                        .bizName(bizName)
                        .name(resultKey)
                        .value(possibleValue)
                        .operator(item.getFilterOperator())
                        .build();
                resultFilters.add(element);
            }
        } else if (simItemType.equals("DIMENSION") && StringUtil.isContainChinese(filterNLValue)) {

            // 非时间类纬度值处理 如果是汉字 则进行纬度值匹配
            List<CwDimDBResp.SimilaritySearchItem> similaritySearchItems = requestCwDimDB(filterNLValue, new DimFilterBO());
            String fixDimValue = resultValue;
            if (null != similaritySearchItems && similaritySearchItems.size() > 0 && similaritySearchItems.get(0).getSearch().size() > 0) {
                fixDimValue = similaritySearchItems.get(0).getSearch().get(0).getMetadata().getDim_value();
            }
            QueryFilter element = QueryFilter.builder()
                    .bizName(bizName)
                    .name(resultKey)
                    .value(fixDimValue)
                    .operator(filterOperator)
                    .build();
            resultFilters.add(element);
        } else {
            // 其他情况下只构造普通value
            QueryFilter element = QueryFilter.builder()
                    .bizName(bizName)
                    .name(resultKey)
                    .value(resultValue)
                    .operator(filterOperator)
                    .build();
            resultFilters.add(element);
        }

        // 2. 确定运算操作符


        // 3. 构建查询条件
        if (resultFilters.size() == 0) {
            QueryFilter element = QueryFilter.builder()
                    .bizName(fil.getSearch().get(0).getMetadata().getBiz_name())
                    .name(resultKey)
                    .value(resultValue)
                    .operator(filterOperator)
                    .build();
            resultFilters.add(element);
        }

        return resultFilters;
    }

    /**
     * 处理聚合内容
     * @param semanticParseInfo
     * @param grop
     * @param tmpSqlParseInfo
     * @return
     */
    private SchemaElement formatToGroupBy(SemanticParseInfo semanticParseInfo, CwVecDBResp.SimilaritySearchItem grop, SemanticParseInfo tmpSqlParseInfo) {
        String groupNLValue = grop.getOrigin_name();
        // TODO 判断返回 年月日 还是年 还是月 还是日 季度 周 这里当前的判断有问题 还需要处理
        // TODO 这里需要根据数据库中的字段信息来用不同的函数处理日期格式，当前统一先使用DATE_FORMAT
        RatioDateConf ratioDataInfo = semanticParseInfo.getRatioDataInfo();
        String bizName = grop.getSearch().get(0).getMetadata().getBiz_name();
        String funName = "";
        String alia = bizName;
        String dataType = "";
        if (groupNLValue.contains("按年")) {
            dataType = Constants.YEAR;
            funName = String.format(MySqlFuncUtil.YEAR_FUNC_FMT,
                    grop.getSearch().get(0).getMetadata().getBiz_name());
            alia = bizName + "_" + dataType;
        } else if (groupNLValue.contains("按月")) {
            dataType = Constants.MONTH;
            alia = bizName + "_" + Constants.MONTH;
            funName = String.format(MySqlFuncUtil.MONTH_FUNC_FMT,
                    grop.getSearch().get(0).getMetadata().getBiz_name());
        } else if (groupNLValue.contains("按日")) {
            dataType = Constants.DAY;
            alia = bizName + "_" + Constants.DAY;
            funName = String.format(MySqlFuncUtil.DAY_FUNC_FMT,
                    grop.getSearch().get(0).getMetadata().getBiz_name());
        } else if (groupNLValue.contains("按季度")) {
            dataType = Constants.QUARTER;
            alia = bizName + "_" + Constants.QUARTER;
            funName = String.format(MySqlFuncUtil.QUARTER_FUNC_FMT,
                    grop.getSearch().get(0).getMetadata().getBiz_name());
        } else if (groupNLValue.contains("按周")) {
            dataType = Constants.WEEK;
            alia = bizName + "_" + Constants.WEEK;
            funName = String.format(MySqlFuncUtil.WEEK_FUNC_FMT,
                    grop.getSearch().get(0).getMetadata().getBiz_name());
        }
        if (ratioDataInfo != null && !StringUtils.isEmpty(dataType)) {
            ratioDataInfo.setPeriod(dataType);
            ratioDataInfo.setRatioDateColumn(bizName);
            ratioDataInfo.setRatioDateAlias(alia);
        }
        SchemaElement.SchemaElementBuilder builder = SchemaElement.builder()
                .type(SchemaElementType.DIMENSION)
                .id(grop.getSearch().get(0).getMetadata().getDimension_id())
                .name(grop.getSearch().get(0).getPage_content());
        builder.bizName(StringUtils.isEmpty(funName) ? bizName : funName);
        if (!StringUtils.isEmpty(alia)) {
            builder.alias(Collections.singletonList(alia));
            semanticParseInfo.getOrders().add(new Order(alia, Constants.ASC_UPPER));
        }
        if (!StringUtils.isEmpty(dataType)) {
            builder.dataType(dataType);
        }
        return builder.build();
    }

}
