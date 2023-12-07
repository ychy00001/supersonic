package com.tencent.supersonic.chat.parser.llm.cw;

import com.google.common.collect.Lists;
import com.tencent.supersonic.chat.agent.tool.AgentToolType;
import com.tencent.supersonic.chat.agent.tool.CwTool;
import com.tencent.supersonic.chat.api.component.SemanticCorrector;
import com.tencent.supersonic.chat.api.component.SemanticInterpreter;
import com.tencent.supersonic.chat.api.component.SemanticParser;
import com.tencent.supersonic.chat.api.pojo.*;
import com.tencent.supersonic.chat.api.pojo.request.QueryFilter;
import com.tencent.supersonic.chat.api.pojo.request.QueryReq;
import com.tencent.supersonic.chat.parser.SatisfactionChecker;
import com.tencent.supersonic.chat.parser.llm.cw.param.LlmDslParam;
import com.tencent.supersonic.chat.parser.llm.cw.utils.FuzzyRangeItem;
import com.tencent.supersonic.chat.parser.llm.cw.utils.FuzzyRangeMatchUtil;
import com.tencent.supersonic.chat.parser.llm.s2ql.ModelResolver;
import com.tencent.supersonic.chat.parser.llm.s2ql.S2QLDateHelper;
import com.tencent.supersonic.chat.query.QueryManager;
import com.tencent.supersonic.chat.parser.llm.cw.CwReq.ElementValue;
import com.tencent.supersonic.chat.query.llm.s2ql.LLMReq;
import com.tencent.supersonic.chat.query.plugin.PluginSemanticQuery;
import com.tencent.supersonic.chat.service.AgentService;
import com.tencent.supersonic.chat.utils.ComponentFactory;
import com.tencent.supersonic.chat.utils.QueryReqBuilder;
import com.tencent.supersonic.common.pojo.Constants;
import com.tencent.supersonic.common.pojo.DateConf;
import com.tencent.supersonic.common.pojo.DateConf.DateMode;
import com.tencent.supersonic.common.pojo.Order;
import com.tencent.supersonic.common.pojo.RatioDateConf;
import com.tencent.supersonic.common.pojo.enums.AggregateTypeEnum;
import com.tencent.supersonic.common.util.ContextUtils;
import com.tencent.supersonic.common.util.DateUtils;
import com.tencent.supersonic.common.util.JsonUtil;
import com.tencent.supersonic.common.util.StringUtil;
import com.tencent.supersonic.common.util.jsqlparser.FilterExpression;
import com.tencent.supersonic.common.util.jsqlparser.SqlParserSelectFunctionHelper;
import com.tencent.supersonic.common.util.jsqlparser.SqlParserSelectHelper;
import com.tencent.supersonic.common.util.xktime.formatter.DateTimeFormatterUtil;
import com.tencent.supersonic.common.util.xktime.nlp.TimeNLP;
import com.tencent.supersonic.common.util.xktime.nlp.TimeNLPUtil;
import com.tencent.supersonic.knowledge.service.SchemaService;
import com.tencent.supersonic.semantic.api.model.enums.TimeDimensionEnum;
import com.tencent.supersonic.semantic.api.query.enums.FilterOperatorEnum;
import com.tencent.supersonic.semantic.api.query.request.QueryStructReq;
import com.tencent.supersonic.semantic.query.persistence.pojo.QueryStatement;
import com.tencent.supersonic.semantic.query.service.SemanticQueryEngine;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.map.HashedMap;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.http.*;
import org.springframework.util.CollectionUtils;
import org.springframework.web.client.RestTemplate;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
public class CwDslParser implements SemanticParser {

    public static final double function_bonus_threshold = 201;

    private SemanticQueryEngine semanticQueryEngine;

    @Override
    public void parse(QueryContext queryCtx, ChatContext chatCtx) {
        semanticQueryEngine = SpringContextUtil.getBean(SemanticQueryEngine.class);

        QueryReq request = queryCtx.getRequest();
        CwParserConfig cwParserConfig = ContextUtils.getBean(CwParserConfig.class);
        if (StringUtils.isEmpty(cwParserConfig.getUrl())) {
            log.info("cw parser url is empty, skip dsl parser, cwParserConfig:{}", cwParserConfig);
            return;
        }
        if (SatisfactionChecker.check(queryCtx)) {
            log.info("skip cw parser, queryText:{}", request.getQueryText());
            return;
        }

        try {
            // 获取当前查询条件下的数据模型
            Long modelId = getModelId(queryCtx, chatCtx, request.getAgentId());
            if (Objects.isNull(modelId) || modelId <= 0) {
                return;
            }

            CwTool cwTool = getCwTool(request, modelId);
            if (Objects.isNull(cwTool)) {
                log.info("no cw tool in this agent, skip dsl parser");
                return;
            }
            ModelSchema schemaInfo = getModelSchema(modelId);
            CwReq cwReq = getCwReq(queryCtx, modelId);
            CwResp cwResp = requestCwLLM(cwReq, modelId, cwParserConfig, schemaInfo);
            // TODO 当前模拟LLM返回
//            CwResp cwResp = MockLLMResp.queryMap.get(queryCtx.getRequest().getQueryText());

            if (Objects.isNull(cwResp)) {
                return;
            }

            // 构造一个独立的CwLLM解析器返回的全部信息
            CwParseResult dslParseResult = CwParseResult.builder().request(request)
                    .cwTool(cwTool).cwReq(cwReq).cwResp(cwResp).build();

            // SemanticParseInfo可以说是一个比较完整的一个构建sql的对象模型 包含在PluginSemanticQuery中
            // 当前ParseInfo包含基础模型信息并且添加至候选sql队列中
            SemanticParseInfo parseInfo = getParseInfo(queryCtx, modelId, cwTool, dslParseResult);

            // 核心：根据模型返回内容构建sql信息, 结果填充至cwResp中
            parserLLMResultToSql(cwResp, parseInfo, cwParserConfig);

            // 修正sql的语法规范 做一些函数转换 字段映射等信息
//            SemanticCorrectInfo semanticCorrectInfo = getCorrectorSql(queryCtx, parseInfo, cwResp.getBuildSql());
            // 这里暂时不修正sql 因为通过cw大模型拼接的sql为真实的sql
            SemanticCorrectInfo semanticCorrectInfo = new SemanticCorrectInfo();
            semanticCorrectInfo.setSql(cwResp.getBuildSql());

            // 这设置的sql应该是我们根据原始llm返回的dsl拼接的一个sql
            cwResp.setCorrectorSql(semanticCorrectInfo.getSql());

            // 修正PluginSemanticQuery中的数据
            updateParseInfo(semanticCorrectInfo, modelId, parseInfo, cwResp);

        } catch (Exception e) {
            log.error("CWDSLParser error", e);
        }
    }

    private ModelSchema getModelSchema(Long modelId) {
        SemanticInterpreter semanticInterpreter = ComponentFactory.getSemanticLayer();
        List<ModelSchema> modelSchema = semanticInterpreter.getModelSchema(Collections.singletonList(modelId));
        return modelSchema.get(0);
    }


    /**
     * 根据llm返回的内容构建合理的sql
     *
     * @param cwResp          LLM返回的内容
     * @param cwDslParserInfo 这是当前Query的基础ParserInfo信息，我们构建Sql语句需要用到这里面的内容
     */
    private void parserLLMResultToSql(CwResp cwResp, SemanticParseInfo cwDslParserInfo, CwParserConfig cwParserConfig) throws Exception {
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
        // 通过SemanticParseInfo的信息构建一个查询的结构体让Calicite引擎渲染成sql
        SemanticParseInfo info = buildParseInfoByCwRsp(cwResp, cwDslParserInfo, cwParserConfig);

        QueryStructReq queryStructReq = QueryReqBuilder.buildStructReqNew(info);
        QueryStatement queryStatement;
        try {
            queryStatement = semanticQueryEngine.simplePlan(queryStructReq);
        } catch (Exception e) {
            log.error("build cw sql err ", e);
            return;
        }
//        queryStatement.setSql("SELECT QUARTER(buy_time),AVG(sell_price) FROM t_1 WHERE YEAR(buy_time) = YEAR(DATE_SUB(NOW(), INTERVAL 2 YEAR)) GROUP BY QUARTER(buy_time) ORDER BY QUARTER(buy_time)");
        // 填充sql信息
        cwResp.setBuildSql(queryStatement.getSql());
        cwResp.setSqlSourceId(queryStatement.getSourceId());
    }

    /**
     * llm原始响应数据构建SemanticParseInfo
     *
     * @param cwResp llm原始响应的数据
     */
    private SemanticParseInfo buildParseInfoByCwRsp(CwResp cwResp, SemanticParseInfo cwDslParserInfo, CwParserConfig cwParserConfig) throws Exception {
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
                cwParserConfig,
                new VecFilterBO(VecFilterBO.VecFilterTypeEnum.DIMENSION));
        List<CwVecDBResp.SimilaritySearchItem> metricsMappers = requestCwVecDB(
                cwResp.getMetrics(),
                cwParserConfig,
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
            List<CwVecDBResp.SimilaritySearchItem> queryMappers = requestCwVecDB(new ArrayList<>(filterQuery), cwParserConfig, null);
            if (null != queryMappers && queryMappers.size() > 0) {
                filterMappers.addAll(queryMappers);
            }
        }

        List<CwVecDBResp.SimilaritySearchItem> groupByMappers = requestCwVecDB(
                cwResp.getGroupBy(), cwParserConfig, new VecFilterBO(VecFilterBO.VecFilterTypeEnum.DIMENSION));
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
        List<CwFuncDBResp.SimilaritySearchItem> funcMappers = requestCwFuncDB(cwResp.getOperator(), cwParserConfig);
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
                List<QueryFilter> element = formatToQueryFilter(fil, cwResp, cwParserConfig);
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

        // 5. 处理GROUP_BY的函数 TODO 这里按道理直接用groupby对应的数据进行维度映射即可，维度会直接体现在sql的groupby中
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
        // 6. 处理可能的ORDER_BY 没有的话默认用指标OrderBy TODO 暂时不处理排序逻辑 优先把其他内容调通

        // 7. 处理LIMIT
        // 需要将metrics赋值还原，这里不是引用赋值
        tmpSqlParseInfo.setMetricsByList(metrics);
        return tmpSqlParseInfo;
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

    private List<CwFuncDBResp.SimilaritySearchItem> requestCwFuncDB(String operator, CwParserConfig cwParserConfig) {
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

    private List<CwDimDBResp.SimilaritySearchItem> requestCwDimDB(String dimNLVal, CwParserConfig cwParserConfig, DimFilterBO dimFilterBO) {
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

    /**
     * 解析过滤条件值
     *
     * @param fil    相似度匹配返回的条目信息
     * @param cwResp 模型返回的信息
     * @return List<QueryFilter> 条件合集
     */
    private List<QueryFilter> formatToQueryFilter(CwVecDBResp.SimilaritySearchItem fil, CwResp cwResp, CwParserConfig cwParserConfig) throws Exception {
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
                if (simItemType.equals("METRIC")) {
                    possibleValue = FuzzyRangeMatchUtil.fuzzyMetricValueFormat(possibleValue);
                }
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
            List<CwDimDBResp.SimilaritySearchItem> similaritySearchItems = requestCwDimDB(filterNLValue, cwParserConfig, new DimFilterBO());
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
     * 请求向量库获取列表中的关联数据字段
     *
     * @param requestItem 待匹配字段
     * @return 相似匹配内容
     */
    private List<CwVecDBResp.SimilaritySearchItem> requestCwVecDB(List<String> requestItem, CwParserConfig cwParserConfig, VecFilterBO vecFilterBO) {
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

    private Set<SchemaElement> getElements(Long modelId, List<String> allFields, List<SchemaElement> elements) {
        return elements.stream()
                .filter(schemaElement -> modelId.equals(schemaElement.getModel())
                        && allFields.contains(schemaElement.getBizName())
                ).collect(Collectors.toSet());
    }

    private List<String> getFieldsExceptDate(List<String> allFields) {
        if (CollectionUtils.isEmpty(allFields)) {
            return new ArrayList<>();
        }
        return allFields.stream()
                .filter(entry -> !TimeDimensionEnum.getNameList().contains(entry))
                .collect(Collectors.toList());
    }

    public void updateParseInfo(SemanticCorrectInfo semanticCorrectInfo, Long modelId, SemanticParseInfo parseInfo, CwResp cwResp) {

        String correctorSql = semanticCorrectInfo.getSql();
        parseInfo.getSqlInfo().setLogicSql(JsonUtil.prettyToString(cwResp.getJsonDsl()));
        parseInfo.getSqlInfo().setQuerySql(correctorSql);

        List<FilterExpression> expressions = SqlParserSelectHelper.getFilterExpression(correctorSql);
        //set dataInfo
//        try {
//            if (!CollectionUtils.isEmpty(expressions)) {
//                DateConf dateInfo = getDateInfo(expressions);
//                parseInfo.setDateInfo(dateInfo);
//            }
//        } catch (Exception e) {
//            log.error("set dateInfo error :", e);
//        }

        //set filter
        try {
            Map<String, SchemaElement> fieldNameToElement = getNameToElement(modelId);
            List<QueryFilter> result = getDimensionFilter(fieldNameToElement, expressions);
            parseInfo.getDimensionFilters().addAll(result);
        } catch (Exception e) {
            log.error("set dimensionFilter error :", e);
        }

        SemanticSchema semanticSchema = ContextUtils.getBean(SchemaService.class).getSemanticSchema();

        if (Objects.isNull(semanticSchema)) {
            return;
        }
        List<String> allFields = getFieldsExceptDate(SqlParserSelectHelper.getAllFields(semanticCorrectInfo.getSql()));

        Set<SchemaElement> metrics = getElements(modelId, allFields, semanticSchema.getMetrics());
        parseInfo.setMetrics(metrics);

        if (SqlParserSelectFunctionHelper.hasAggregateFunction(semanticCorrectInfo.getSql())) {
            parseInfo.setNativeQuery(false);
            List<String> groupByFields = SqlParserSelectHelper.getGroupByFields(semanticCorrectInfo.getSql());
            List<String> groupByDimensions = getFieldsExceptDate(groupByFields);
            parseInfo.setDimensions(getElements(modelId, groupByDimensions, semanticSchema.getDimensions()));
        } else {
            parseInfo.setNativeQuery(true);
            List<String> selectFields = SqlParserSelectHelper.getSelectFields(semanticCorrectInfo.getSql());
            List<String> selectDimensions = getFieldsExceptDate(selectFields);
            parseInfo.setDimensions(getElements(modelId, selectDimensions, semanticSchema.getDimensions()));
        }
    }

    protected Map<String, SchemaElement> getNameToElement(Long modelId) {
        SemanticSchema semanticSchema = ContextUtils.getBean(SchemaService.class).getSemanticSchema();
        List<SchemaElement> dimensions = semanticSchema.getDimensions();
        List<SchemaElement> metrics = semanticSchema.getMetrics();

        List<SchemaElement> allElements = Lists.newArrayList();
        allElements.addAll(dimensions);
        allElements.addAll(metrics);
        return allElements.stream()
                .filter(schemaElement -> schemaElement.getModel().equals(modelId))
                .collect(Collectors.toMap(SchemaElement::getName, Function.identity(), (value1, value2) -> value2));
    }


    private List<QueryFilter> getDimensionFilter(Map<String, SchemaElement> fieldNameToElement,
                                                 List<FilterExpression> filterExpressions) {
        List<QueryFilter> result = Lists.newArrayList();
        for (FilterExpression expression : filterExpressions) {
            QueryFilter dimensionFilter = new QueryFilter();
            dimensionFilter.setValue(expression.getFieldValue());
            SchemaElement schemaElement = fieldNameToElement.get(expression.getFieldName());
            if (Objects.isNull(schemaElement)) {
                continue;
            }
            dimensionFilter.setName(schemaElement.getName());
            dimensionFilter.setBizName(schemaElement.getBizName());
            dimensionFilter.setElementID(schemaElement.getId());

            FilterOperatorEnum operatorEnum = FilterOperatorEnum.getSqlOperator(expression.getOperator());
            dimensionFilter.setOperator(operatorEnum);
            dimensionFilter.setFunction(expression.getFunction());
            result.add(dimensionFilter);
        }
        return result;
    }

    /**
     * 解析WHERE条件中的日期信息
     * <p>
     * 这里的日期是分片日期sys_imp_date 这个逻辑应该是如果数据库有则用 没有则不用
     * TODO 日期的构建还存在问题，后续看如何解决
     *
     * @param filterExpressions
     * @return
     */
    private DateConf getDateInfo(List<FilterExpression> filterExpressions) {
        List<FilterExpression> dateExpressions = filterExpressions.stream()
                .filter(expression -> DateUtils.DATE_FIELD.equalsIgnoreCase(expression.getFieldName()))
                .collect(Collectors.toList());
        if (CollectionUtils.isEmpty(dateExpressions)) {
            return new DateConf();
        }
        DateConf dateInfo = new DateConf();
        dateInfo.setDateMode(DateMode.BETWEEN);
        FilterExpression firstExpression = dateExpressions.get(0);

        FilterOperatorEnum firstOperator = FilterOperatorEnum.getSqlOperator(firstExpression.getOperator());
        if (FilterOperatorEnum.EQUALS.equals(firstOperator) && Objects.nonNull(firstExpression.getFieldValue())) {
            dateInfo.setStartDate(firstExpression.getFieldValue().toString());
            dateInfo.setEndDate(firstExpression.getFieldValue().toString());
            dateInfo.setDateMode(DateMode.BETWEEN);
            return dateInfo;
        }
        if (containOperators(firstExpression, firstOperator, FilterOperatorEnum.GREATER_THAN,
                FilterOperatorEnum.GREATER_THAN_EQUALS)) {
            dateInfo.setStartDate(firstExpression.getFieldValue().toString());
            if (hasSecondDate(dateExpressions)) {
                dateInfo.setEndDate(dateExpressions.get(1).getFieldValue().toString());
            }
        }
        if (containOperators(firstExpression, firstOperator, FilterOperatorEnum.MINOR_THAN,
                FilterOperatorEnum.MINOR_THAN_EQUALS)) {
            dateInfo.setEndDate(firstExpression.getFieldValue().toString());
            if (hasSecondDate(dateExpressions)) {
                dateInfo.setStartDate(dateExpressions.get(1).getFieldValue().toString());
            }
        }
        return dateInfo;
    }

    private boolean containOperators(FilterExpression expression, FilterOperatorEnum firstOperator,
                                     FilterOperatorEnum... operatorEnums) {
        return (Arrays.asList(operatorEnums).contains(firstOperator) && Objects.nonNull(expression.getFieldValue()));
    }

    private boolean hasSecondDate(List<FilterExpression> dateExpressions) {
        return dateExpressions.size() > 1 && Objects.nonNull(dateExpressions.get(1).getFieldValue());
    }

    private SemanticCorrectInfo getCorrectorSql(QueryContext queryCtx, SemanticParseInfo parseInfo, String sql) {

        SemanticCorrectInfo correctInfo = SemanticCorrectInfo.builder()
                .queryFilters(queryCtx.getRequest().getQueryFilters()).sql(sql)
                .parseInfo(parseInfo).build();

        List<SemanticCorrector> corrections = ComponentFactory.getSqlCorrections();

        corrections.forEach(correction -> {
            try {
                correction.correct(correctInfo);
                log.info("sqlCorrection:{} sql:{}", correction.getClass().getSimpleName(), correctInfo.getSql());
            } catch (Exception e) {
                log.error(String.format("correct error,correctInfo:%s", correctInfo), e);
            }
        });
        return correctInfo;
    }

    private SemanticParseInfo getParseInfo(QueryContext queryCtx, Long modelId, CwTool cwTool,
                                           CwParseResult cwParseResult) {
        PluginSemanticQuery semanticQuery = QueryManager.createPluginQuery(CwQuery.QUERY_MODE);
        SemanticParseInfo parseInfo = semanticQuery.getParseInfo();
        parseInfo.getElementMatches().addAll(queryCtx.getMapInfo().getMatchedElements(modelId));

        Map<String, Object> properties = new HashMap<>();
        properties.put(Constants.CONTEXT, cwParseResult);
        properties.put("type", "internal");
        properties.put("name", cwTool.getName());

        parseInfo.setProperties(properties);
        parseInfo.setScore(queryCtx.getRequest().getQueryText().length());
        parseInfo.setQueryMode(semanticQuery.getQueryMode());
        parseInfo.getSqlInfo().setS2QL(cwParseResult.getCwResp().getCorrectorSql());

        SemanticSchema semanticSchema = ContextUtils.getBean(SchemaService.class).getSemanticSchema();
        Map<Long, String> modelIdToName = semanticSchema.getModelIdToName();

        SchemaElement model = new SchemaElement();
        model.setModel(modelId);
        model.setId(modelId);
        model.setName(modelIdToName.get(modelId));
        parseInfo.setModel(model);
        queryCtx.getCandidateQueries().add(semanticQuery);
        return parseInfo;
    }

    /**
     * 根据当前模型Id，获取用户界面配置的Agent里面的一个工具
     *
     * @param request
     * @param modelId
     * @return
     */
    private CwTool getCwTool(QueryReq request, Long modelId) {
        AgentService agentService = ContextUtils.getBean(AgentService.class);
        List<CwTool> cwTools = agentService.getCwTools(request.getAgentId(), AgentToolType.CW);
        // 从工具里面选择一个存在当前模型的工具
        Optional<CwTool> cwToolOptional = cwTools.stream()
                .filter(tool -> {
                    List<Long> modelIds = tool.getModelIds();
                    if (agentService.containsAllModel(new HashSet<>(modelIds))) {
                        return true;
                    }
                    return modelIds.contains(modelId);
                })
                .findFirst();
        return cwToolOptional.orElse(null);
    }

    /**
     * 获取模型ID
     *
     * @param queryCtx 查询req
     * @param chatCtx  聊天上下文
     * @param agentId  代理ID
     * @return
     */
    private Long getModelId(QueryContext queryCtx, ChatContext chatCtx, Integer agentId) {
        AgentService agentService = ContextUtils.getBean(AgentService.class);
        // 读取当前Agent下所有工具的数据模型Id
        Set<Long> distinctModelIds = agentService.getCwToolsModelIds(agentId, AgentToolType.CW);
        if (agentService.containsAllModel(distinctModelIds)) {
            distinctModelIds = new HashSet<>();
        }
        ModelResolver modelResolver = ComponentFactory.getModelResolver();
        // 根据上下文选出一个合适的模型ID （仅支持单数据模型）TODO 当前先忽略此处逻辑后续补充了解
        Long modelId = modelResolver.resolve(queryCtx, chatCtx, distinctModelIds);
        log.info("resolve modelId:{},dslModels:{}", modelId, distinctModelIds);
        return modelId;
    }

    /**
     * 请求cw模型获取解析的内容
     *
     * @param cwReq
     * @param modelId
     * @param cwParserConfig
     * @return
     */
    private CwResp requestCwLLM(CwReq cwReq, Long modelId, CwParserConfig cwParserConfig, ModelSchema modelSchema) {

        LlmDslParam requestBody = getCwRequestParam(cwReq, modelSchema);
        String questUrl = cwParserConfig.getQueryToDslPath();
        long startTime = System.currentTimeMillis();
        log.info("requestLLM request, modelId:{},llmReq:{}", modelId, cwReq);
        RestTemplate restTemplate = ContextUtils.getBean(RestTemplate.class);
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<String> entity = new HttpEntity<>(JsonUtil.toString(requestBody), headers);
            ResponseEntity responseEntity = restTemplate.exchange(questUrl, HttpMethod.POST, entity,
                    Object.class);
            Object objectResponse = responseEntity.getBody();
            log.debug("cw llm parser objectResponse:{}", objectResponse);
            Map<String, Object> response = JsonUtil.objectToMap(objectResponse);
            CwResp cwResp;
            if (response.containsKey("generated_text")) {
                String dslJson = response.get("generated_text").toString();
                cwResp = JsonUtil.toObject(dslJson, CwResp.class);
                cwResp.setOriginQueryText(cwReq.getQueryText());
                cwResp.setJsonDsl(dslJson);
            } else {
                throw new RuntimeException("query result err , can't find generated_text");
            }
            log.info("requestLLM response,cost:{}, questUrl:{} \n entity:{} \n body:{}",
                    System.currentTimeMillis() - startTime, questUrl, entity, responseEntity.getBody());
            return cwResp;
        } catch (Exception e) {
            log.error("requestLLM error", e);
        }
        return null;
    }

    public static LlmDslParam getCwRequestParam(CwReq cwReq, ModelSchema schemaInfo) {
        String queryText = cwReq.getQueryText();
        LlmDslParam requestParam = new LlmDslParam();
        List<LlmDslParam.MessageItem> messageItemList = new ArrayList<>();
        messageItemList.add(new LlmDslParam.MessageItem("system", "You are an expert in SQL and data analysis and can extract keywords and schema info from user input and output it in the following JSON format\n" +
                "If the user's question is not related to data analysis, directly reply with [UNKNOWN].\n" +
                "{ \n" +
                "    \"Entity\": [], \n" +
                "    \"Dimension\": [], \n" +
                "    \"Filters\": {\n" +
                "        \"Key\":\"Value\",\n" +
                "        \"Key\":\"Value\",\n" +
                "        \"Key\":\"Value\",\n" +
                "        \"Key\":\"Value\"\n" +
                "    }, \n" +
                "    \"Metrics\": [],\n" +
                "    \"Operator\":\"\",\n" +
                "    \"Groupby\": []\n" +
                "} \n" +
                "\n" +
                "Let's work this out in a step by step way to be sure we have the right answer.\n" +
                "Only output JSON FORMAT."));
        String userFormat = "Return the 'Question' result based on the following database schema information:\n" +
                "\n" +
                "### Database Schema\n" +
                "|column|name|\n" +
                "|---|---|\n"+
                "%s" +
                "\n" +
                "%s" +
                "\n" +
                "Your response should be based on the provided database schema and should accurately retrieve the required information. pay special attention to filter and Groupby information. only return JSON in result, not include other things.\n" +
                "\n" +
                "如果Groupby包含日期信息，根据日期信息返回日期的实际单位和其他Groupby信息，示例：\n" +
                "月环比---->'Groupby': ['日期按月', ...]\n" +
                "按周统计---->'Groupby': ['日期按周', ...]\n" +
                "月环比---->'Groupby': ['日期按月', ...]\n" +
                "基于年---->'Groupby': ['日期按年', ...]\n" +
                "\n" +
                "### Question\n" +
                "%s";
        StringBuilder schemaInfoStr = new StringBuilder();
        StringBuilder dimValueStr = new StringBuilder();
        if (null != schemaInfo) {
            String schemaItemFormat = "|%s|%s|\n";
            for (SchemaElement item : schemaInfo.getDimensions()) {
                schemaInfoStr.append(String.format(schemaItemFormat, item.getBizName(), item.getName()));
            }
            for (SchemaElement item : schemaInfo.getMetrics()) {
                schemaInfoStr.append(String.format(schemaItemFormat, item.getBizName(), item.getName()));
            }
        }
        if (cwReq.getLinking().size() > 0) {
            dimValueStr.append("### Dimension Value\n" +
                    "|dim|value|\n" +
                    "|---|---|\n");
            String schemaItemFormat = "|%s|%s|\n";
            for (ElementValue item : cwReq.getLinking()) {
                dimValueStr.append(String.format(schemaItemFormat, item.getFieldName(), item.getFieldValue()));
            }
        }
        messageItemList.add(new LlmDslParam.MessageItem("user", String.format(userFormat, schemaInfoStr.toString(), dimValueStr.toString(), queryText)));
        requestParam.setMessages(messageItemList);
        requestParam.setParameters(new LlmDslParam.Parameter());
        log.info("cw dsl param:{}", requestParam);
        return requestParam;
    }

    /**
     * 构建请求体
     *
     * @param queryCtx
     * @param modelId
     * @return
     */
    private CwReq getCwReq(QueryContext queryCtx, Long modelId) {
        SemanticSchema semanticSchema = ContextUtils.getBean(SchemaService.class).getSemanticSchema();
        Map<Long, String> modelIdToName = semanticSchema.getModelIdToName();
        String queryText = queryCtx.getRequest().getQueryText();
        CwReq cwReq = new CwReq();
        cwReq.setQueryText(queryText);
        CwReq.CwSchema cwSchema = new CwReq.CwSchema();
        cwSchema.setModelName(modelIdToName.get(modelId));
        cwSchema.setDomainName(modelIdToName.get(modelId));
        List<String> fieldNameList = getFieldNameList(queryCtx, modelId, semanticSchema);
        cwSchema.setFieldNameList(fieldNameList);
        cwReq.setSchema(cwSchema);
        List<ElementValue> linking = new ArrayList<>();
        linking.addAll(getValueList(queryCtx, modelId, semanticSchema));
        cwReq.setLinking(linking);
        String currentDate = S2QLDateHelper.getReferenceDate(modelId);
        cwReq.setCurrentDate(currentDate);
        return cwReq;
    }

    protected List<ElementValue> getValueList(QueryContext queryCtx, Long modelId, SemanticSchema semanticSchema) {
        Map<Long, String> itemIdToName = getItemIdToName(modelId, semanticSchema);

        List<SchemaElementMatch> matchedElements = queryCtx.getMapInfo().getMatchedElements(modelId);
        if (CollectionUtils.isEmpty(matchedElements)) {
            return new ArrayList<>();
        }
        Set<ElementValue> valueMatches = matchedElements
                .stream()
                .filter(elementMatch -> !elementMatch.isInherited())
                .filter(schemaElementMatch -> {
                    SchemaElementType type = schemaElementMatch.getElement().getType();
                    return SchemaElementType.VALUE.equals(type) || SchemaElementType.ID.equals(type);
                })
                .map(elementMatch -> {
                    ElementValue elementValue = new ElementValue();
                    elementValue.setFieldName(itemIdToName.get(elementMatch.getElement().getId()));
                    elementValue.setFieldValue(elementMatch.getWord());
                    return elementValue;
                }).collect(Collectors.toSet());
        return new ArrayList<>(valueMatches);
    }


    protected Map<String, SchemaElement> getBizNameToElement(Long modelId) {
        SemanticSchema semanticSchema = ContextUtils.getBean(SchemaService.class).getSemanticSchema();
        List<SchemaElement> dimensions = semanticSchema.getDimensions();
        List<SchemaElement> metrics = semanticSchema.getMetrics();

        List<SchemaElement> allElements = Lists.newArrayList();
        allElements.addAll(dimensions);
        allElements.addAll(metrics);
        return allElements.stream()
                .filter(schemaElement -> schemaElement.getModel().equals(modelId))
                .collect(Collectors.toMap(SchemaElement::getBizName, Function.identity(), (value1, value2) -> value2));
    }


    protected List<String> getFieldNameList(QueryContext queryCtx, Long modelId, SemanticSchema semanticSchema) {
        Map<Long, String> itemIdToName = getItemIdToName(modelId, semanticSchema);

        List<SchemaElementMatch> matchedElements = queryCtx.getMapInfo().getMatchedElements(modelId);
        if (CollectionUtils.isEmpty(matchedElements)) {
            return new ArrayList<>();
        }
        Set<String> fieldNameList = matchedElements.stream()
                .filter(schemaElementMatch -> {
                    SchemaElementType elementType = schemaElementMatch.getElement().getType();
                    return SchemaElementType.METRIC.equals(elementType)
                            || SchemaElementType.DIMENSION.equals(elementType)
                            || SchemaElementType.VALUE.equals(elementType);
                })
                .map(schemaElementMatch -> {
                    SchemaElementType elementType = schemaElementMatch.getElement().getType();

                    if (!SchemaElementType.VALUE.equals(elementType)) {
                        return schemaElementMatch.getWord();
                    }
                    return itemIdToName.get(schemaElementMatch.getElement().getId());
                })
                .filter(name -> StringUtils.isNotEmpty(name) && !name.contains("%"))
                .collect(Collectors.toSet());
        return new ArrayList<>(fieldNameList);
    }

    protected Map<Long, String> getItemIdToName(Long modelId, SemanticSchema semanticSchema) {
        return semanticSchema.getDimensions().stream()
                .filter(entry -> modelId.equals(entry.getModel()))
                .collect(Collectors.toMap(SchemaElement::getId, SchemaElement::getName, (value1, value2) -> value2));
    }

    public static void main(String[] args) {
//        LlmDslParam cwRequestParam = getCwRequestParam("对销售额贡献最大的渠道是哪个？", null);
//        System.out.println(cwRequestParam);

//        String userFormat = "Return JSON DSL based on the following database schema information:\n" +
//                "\n" +
//                "### Database Schema\n" +
//                "|column|name|\n" +
//                "|---|---|\n" +
//                "%s" +
//                "\n" +
//                "Your response should be based on the provided database schema and should accurately retrieve the required information.\n" +
//                "\n" +
//                "### Input\n" +
//                "%s";
//        System.out.println(String.format(userFormat, "", ""));

        System.out.println(JsonUtil.prettyToString("{ 'Entity': ['计算机成绩大于95', '人的名字'],\n" +
                "'Dimension': ['成绩'],\n" +
                "'Filters': { '成绩': '>95' },\n" +
                "'Metrics': [],\n" +
                "'Operator': '',\n" +
                "'Groupby': ['姓名'] }"));
//        String resultTest = "{\"generated_text\":\"{'Entity': ['前年', '每个季度', '平均销售价'], 'Dimension': ['销售日期', '销售金额'], 'Filters': {'销售日期': '前年'}, 'Metrics': ['销售金额'], 'Operator': '求平均值', 'Groupby': ['销售日期按季度']} \"}";
//        Map<String, Object> response = JsonUtil.toMap(resultTest, String.class, Object.class);
//        CwResp cwResp;
//        if (response.containsKey("generated_text")) {
//            cwResp = JsonUtil.toObject(response.get("generated_text").toString(),CwResp.class);
//            cwResp.setOriginQueryText("哈哈哈");
//            System.out.println(cwResp);
//        }else{
//            throw new RuntimeException("query result err , can't find generated_text");
//        }
    }
}
