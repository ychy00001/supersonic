package com.tencent.supersonic.semantic.query.parser.convert;


import com.tencent.supersonic.common.pojo.Aggregator;
import com.tencent.supersonic.common.pojo.Constants;
import com.tencent.supersonic.common.pojo.RatioDateConf;
import com.tencent.supersonic.common.pojo.enums.AggOperatorEnum;
import com.tencent.supersonic.semantic.api.model.response.DatabaseResp;
import com.tencent.supersonic.semantic.api.query.enums.AggOption;
import com.tencent.supersonic.semantic.api.query.pojo.Filter;
import com.tencent.supersonic.semantic.api.query.pojo.MetricTable;
import com.tencent.supersonic.semantic.api.query.request.MetricReq;
import com.tencent.supersonic.semantic.api.query.request.ParseSqlReq;
import com.tencent.supersonic.semantic.api.query.request.QueryStructReq;
import com.tencent.supersonic.semantic.model.domain.Catalog;
import com.tencent.supersonic.semantic.model.domain.pojo.EngineTypeEnum;
import com.tencent.supersonic.semantic.query.parser.SemanticConverter;
import com.tencent.supersonic.semantic.query.service.SemanticQueryEngine;
import com.tencent.supersonic.semantic.query.utils.QueryStructUtils;
import com.tencent.supersonic.semantic.query.utils.SqlGenerateUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;


@Component("SimpleAggConverter")
@Slf4j
public class SimpleAggConverter implements SemanticConverter {


    private final SemanticQueryEngine parserService;
    private final QueryStructUtils queryStructUtils;
    private final SqlGenerateUtils sqlGenerateUtils;
    private final Catalog catalog;

    @Value("${metricParser.agg.default:sum}")
    private String metricAggDefault;

    @Value("${metricParser.agg.mysql.lowVersion:8.0}")
    private String mysqlLowVersion;


    public SimpleAggConverter(
            SemanticQueryEngine parserService,
            @Lazy QueryStructUtils queryStructUtils,
            SqlGenerateUtils sqlGenerateUtils, Catalog catalog) {
        this.parserService = parserService;
        this.queryStructUtils = queryStructUtils;
        this.sqlGenerateUtils = sqlGenerateUtils;
        this.catalog = catalog;
    }

    public interface EngineSql {

        String sql(QueryStructReq queryStructCmd, boolean isOver, String metricSql);
    }

    public ParseSqlReq generateSqlCommend(QueryStructReq queryStructCmd, EngineTypeEnum engineTypeEnum, String version)
            throws Exception {
        // 同环比
        if (isRatioAccept(queryStructCmd)) {
            return generateRatioSqlCommand(queryStructCmd, engineTypeEnum, version);
        }
        ParseSqlReq sqlCommand = new ParseSqlReq();
        sqlCommand.setRootPath(catalog.getModelFullPath(queryStructCmd.getModelId()));
        String metricTableName = "v_metric_tb_tmp";
        MetricTable metricTable = new MetricTable();
        metricTable.setAlias(metricTableName);
        metricTable.setMetrics(queryStructCmd.getMetrics());
        metricTable.setDimensions(queryStructCmd.getGroups());
        String where = queryStructUtils.generateWhere(queryStructCmd);
        log.info("in generateSqlCommand, complete where:{}", where);
        metricTable.setWhere(where);
        metricTable.setAggOption(AggOption.AGGREGATION);
        sqlCommand.setTables(new ArrayList<>(Collections.singletonList(metricTable)));
        String sql = String.format("select %s from %s  %s %s %s", sqlGenerateUtils.getSelect(queryStructCmd),
                metricTableName,
                sqlGenerateUtils.getGroupBy(queryStructCmd), sqlGenerateUtils.getOrderBy(queryStructCmd),
                sqlGenerateUtils.getLimit(queryStructCmd));
        if (!queryStructUtils.isSupportWith(engineTypeEnum, version)) {
            sqlCommand.setSupportWith(false);
            sql = String.format("select %s from %s t0 %s %s %s", sqlGenerateUtils.getSelect(queryStructCmd),
                    metricTableName,
                    sqlGenerateUtils.getGroupBy(queryStructCmd), sqlGenerateUtils.getOrderBy(queryStructCmd),
                    sqlGenerateUtils.getLimit(queryStructCmd));
        }
        sqlCommand.setSql(sql);
        return sqlCommand;
    }


    @Override
    public boolean accept(QueryStructReq queryStructCmd) {
        if (queryStructCmd.getNativeQuery()) {
            return false;
        }
        if (CollectionUtils.isEmpty(queryStructCmd.getAggregators())) {
            return false;
        }

        int nonSumFunction = 0;
        for (Aggregator agg : queryStructCmd.getAggregators()) {
            if (agg.getFunc() == null || "".equals(agg.getFunc())) {
                return false;
            }
            if (agg.getFunc().equals(AggOperatorEnum.UNKNOWN)) {
                return false;
            }
            if (agg.getFunc() != null) {
                nonSumFunction++;
            }
        }
        return nonSumFunction > 0;
    }

    @Override
    public void converter(Catalog catalog, QueryStructReq queryStructCmd, ParseSqlReq sqlCommend,
                          MetricReq metricCommand) throws Exception {
        DatabaseResp databaseResp = catalog.getDatabaseByModelId(queryStructCmd.getModelId());
        ParseSqlReq parseSqlReq = generateSqlCommend(queryStructCmd,
                EngineTypeEnum.valueOf(databaseResp.getType().toUpperCase()), databaseResp.getVersion());
        sqlCommend.setSql(parseSqlReq.getSql());
        sqlCommend.setTables(parseSqlReq.getTables());
        sqlCommend.setRootPath(parseSqlReq.getRootPath());
        sqlCommend.setVariables(parseSqlReq.getVariables());
        sqlCommend.setSupportWith(parseSqlReq.isSupportWith());
    }


    /**
     * Ratio
     */

    public boolean isRatioAccept(QueryStructReq queryStructCmd) {
        Long ratioFuncNum = queryStructCmd.getAggregators().stream()
                .filter(f -> (f.getFunc().equals(AggOperatorEnum.RATIO_ROLL) || f.getFunc()
                        .equals(AggOperatorEnum.RATIO_OVER))).count();
        if (ratioFuncNum > 0) {
            return true;
        }
        return false;
    }

    public ParseSqlReq generateRatioSqlCommand(QueryStructReq queryStructCmd, EngineTypeEnum engineTypeEnum,
                                               String version)
            throws Exception {
//        check(queryStructCmd);
        ParseSqlReq sqlCommand = new ParseSqlReq();
        sqlCommand.setRootPath(catalog.getModelFullPath(queryStructCmd.getModelId()));
        String metricTableName = "v_metric_tb_tmp";
        MetricTable metricTable = new MetricTable();
        metricTable.setAlias(metricTableName);
        metricTable.setMetrics(queryStructCmd.getMetrics());
        metricTable.setDimensions(queryStructCmd.getGroups());
        String where = queryStructUtils.generateWhereWithoutRationData(queryStructCmd);
        log.info("in generateSqlCommend, complete where:{}", where);
        metricTable.setWhere(where);
        metricTable.setAggOption(AggOption.AGGREGATION);
        sqlCommand.setTables(new ArrayList<>(Collections.singletonList(metricTable)));
        boolean isOver = isOverRatio(queryStructCmd);
        String sql = "";
        switch (engineTypeEnum) {
            case H2:
                sql = new H2EngineSql().sql(queryStructCmd, isOver, metricTableName);
                break;
            case MYSQL:
            case DORIS:
            case CLICKHOUSE:
                if (!queryStructUtils.isSupportWith(engineTypeEnum, version)) {
                    sqlCommand.setSupportWith(false);
                    sql = new MysqlEngineSql().sql(queryStructCmd, isOver, metricTableName);
                } else {
                    sql = new CkEngineSql().sql(queryStructCmd, isOver, metricTableName);
                }
                break;
            default:
        }
        sqlCommand.setSql(sql);
        return sqlCommand;
    }

    public class H2EngineSql implements EngineSql {

        public String getOverSelect(QueryStructReq queryStructCmd, boolean isOver) {
            String aggStr = queryStructCmd.getAggregators().stream().map(f -> {
                if (f.getFunc().equals(AggOperatorEnum.RATIO_OVER) || f.getFunc().equals(AggOperatorEnum.RATIO_ROLL)) {
                    return String.format("( (%s-%s_roll)/cast(%s_roll as DOUBLE) ) as %s_%s,%s",
                            f.getColumn(), f.getColumn(), f.getColumn(), f.getColumn(),
                            f.getFunc().getOperator(), f.getColumn());
                } else {
                    return f.getColumn();
                }
            }).collect(Collectors.joining(","));
            return CollectionUtils.isEmpty(queryStructCmd.getGroups()) ? aggStr
                    : String.join(",", queryStructCmd.getGroups()) + "," + aggStr;
        }

        public String getTimeSpan(QueryStructReq queryStructCmd, boolean isOver, boolean isAdd) {
            if (Objects.nonNull(queryStructCmd.getDateInfo())) {
                String addStr = isAdd ? "" : "-";
                if (queryStructCmd.getDateInfo().getPeriod().equalsIgnoreCase(Constants.DAY)) {
                    return "day," + (isOver ? addStr + "7" : addStr + "1");
                }
                if (queryStructCmd.getDateInfo().getPeriod().equalsIgnoreCase(Constants.WEEK)) {
                    return isOver ? "month," + addStr + "1" : "day," + addStr + "7";
                }
                if (queryStructCmd.getDateInfo().getPeriod().equalsIgnoreCase(Constants.MONTH)) {
                    return isOver ? "year," + addStr + "1" : "month," + addStr + "1";
                }
            }
            return "";
        }

        public String getJoinOn(QueryStructReq queryStructCmd, boolean isOver, String aliasLeft, String aliasRight) {
            String timeDim = getTimeDim(queryStructCmd);
            String timeSpan = "INTERVAL  " + getTimeSpan(queryStructCmd, isOver, true);
            String aggStr = queryStructCmd.getAggregators().stream().map(f -> {
                if (f.getFunc().equals(AggOperatorEnum.RATIO_OVER) || f.getFunc().equals(AggOperatorEnum.RATIO_ROLL)) {
                    if (queryStructCmd.getDateInfo().getPeriod().equals(Constants.MONTH)) {
                        return String.format("%s = DATE_FORMAT(date_add(CONCAT(%s,'-01'), %s),'%%Y-%%m') ",
                                aliasLeft + timeDim, aliasRight + timeDim, timeSpan);
                    }
                    if (queryStructCmd.getDateInfo().getPeriod().equals(Constants.WEEK) && isOver) {
                        return String.format("to_monday(date_add(%s ,INTERVAL %s) ) = %s",
                                aliasLeft + timeDim, getTimeSpan(queryStructCmd, isOver, false), aliasRight + timeDim);
                    }
                    return String.format("%s = date_add(%s,%s) ",
                            aliasLeft + timeDim, aliasRight + timeDim, timeSpan);
                } else {
                    return f.getColumn();
                }
            }).collect(Collectors.joining(" and "));
            List<String> groups = new ArrayList<>();
            for (String group : queryStructCmd.getGroups()) {
                if (group.equalsIgnoreCase(timeDim)) {
                    continue;
                }
                groups.add(aliasLeft + group + " = " + aliasRight + group);
            }
            return CollectionUtils.isEmpty(groups) ? aggStr
                    : String.join(" and ", groups) + " and " + aggStr + " ";
        }

        @Override
        public String sql(QueryStructReq queryStructCmd, boolean isOver, String metricSql) {
            String sql = String.format(
                    "select %s from ( select %s , %s from %s t0 left join %s t1 on %s ) metric_tb_src %s %s ",
                    getOverSelect(queryStructCmd, isOver), getAllSelect(queryStructCmd, "t0."),
                    getAllJoinSelect(queryStructCmd, "t1."), metricSql, metricSql,
                    getJoinOn(queryStructCmd, isOver, "t0.", "t1."),
                    getOrderBy(queryStructCmd), getLimit(queryStructCmd));
            return sql;
        }
    }

    public class CkEngineSql extends MysqlEngineSql {

        public String getJoinOn(QueryStructReq queryStructCmd, boolean isOver, String aliasLeft, String aliasRight) {
            String timeDim = getTimeDim(queryStructCmd);
            String timeSpan = "INTERVAL  " + getTimeSpan(queryStructCmd, isOver, true);
            String aggStr = queryStructCmd.getAggregators().stream().map(f -> {
                if (f.getFunc().equals(AggOperatorEnum.RATIO_OVER) || f.getFunc().equals(AggOperatorEnum.RATIO_ROLL)) {
                    if (queryStructCmd.getDateInfo().getPeriod().equals(Constants.MONTH)) {
                        return String.format("toDate(CONCAT(%s,'-01')) = date_add(toDate(CONCAT(%s,'-01')),%s)  ",
                                aliasLeft + timeDim, aliasRight + timeDim, timeSpan);
                    }
                    if (queryStructCmd.getDateInfo().getPeriod().equals(Constants.WEEK) && isOver) {
                        return String.format("toMonday(date_add(%s ,INTERVAL %s) ) = %s",
                                aliasLeft + timeDim, getTimeSpan(queryStructCmd, isOver, false), aliasRight + timeDim);
                    }
                    return String.format("%s = date_add(%s,%s) ",
                            aliasLeft + timeDim, aliasRight + timeDim, timeSpan);
                } else {
                    return f.getColumn();
                }
            }).collect(Collectors.joining(" and "));
            List<String> groups = new ArrayList<>();
            for (String group : queryStructCmd.getGroups()) {
                if (group.equalsIgnoreCase(timeDim)) {
                    continue;
                }
                groups.add(aliasLeft + group + " = " + aliasRight + group);
            }
            return CollectionUtils.isEmpty(groups) ? aggStr
                    : String.join(" and ", groups) + " and " + aggStr + " ";
        }

        @Override
        public String sql(QueryStructReq queryStructCmd, boolean isOver, String metricSql) {
            String sql = String.format(
                    ",t0 as (select * from %s),t1 as (select * from %s) select %s from ( select %s , %s "
                            + "from  t0 left join t1 on %s ) metric_tb_src %s %s ",
                    metricSql, metricSql, getOverSelect(queryStructCmd, isOver, ""), getAllSelect(queryStructCmd, "t0."),
                    getAllJoinSelect(queryStructCmd, "t1."),
                    getJoinOn(queryStructCmd, isOver, "t0.", "t1."),
                    getOrderBy(queryStructCmd), getLimit(queryStructCmd));
            return sql;
        }
    }

    public class MysqlEngineSql implements EngineSql {
        // isOver为同比判断
        public String getTimeSpan(QueryStructReq queryStructCmd, boolean isOver, boolean isAdd) {
            if (Objects.nonNull(queryStructCmd.getRationDateInfo())) {
                String addStr = isAdd ? "" : "-";
                if (queryStructCmd.getRationDateInfo().getPeriod().equalsIgnoreCase(Constants.DAY)) {
                    return isOver ? addStr + "7 day" : addStr + "1 day";
                }
                if (queryStructCmd.getRationDateInfo().getPeriod().equalsIgnoreCase(Constants.WEEK)) {
                    return isOver ? addStr + "1 month" : addStr + "7 day";
                }
                if (queryStructCmd.getRationDateInfo().getPeriod().equalsIgnoreCase(Constants.MONTH)) {
                    return isOver ? addStr + "1 year" : addStr + "1 month";
                }
                if (queryStructCmd.getRationDateInfo().getPeriod().equalsIgnoreCase(Constants.YEAR)) {
                    return addStr + "1 year";
                }
            }
            return "";
        }

        public String getOverSelect(QueryStructReq queryStructCmd, boolean isOver, String aliaName) {
            String aggStr = queryStructCmd.getAggregators().stream().map(f -> {
                if (f.getFunc().equals(AggOperatorEnum.RATIO_OVER) || f.getFunc().equals(AggOperatorEnum.RATIO_ROLL)) {
                    return String.format(
                            "if(%s_roll!=0,  (%s-%s_roll)/%s_roll , 0) as %s_%s,%s",
                            f.getColumn(), f.getColumn(), f.getColumn(), f.getColumn(),
                            f.getColumn(), f.getFunc().getOperator(), f.getColumn());
                } else {
                    return f.getColumn();
                }
            }).collect(Collectors.joining(","));

            List<String> groups = new ArrayList<>();
            for (String group : queryStructCmd.getGroups()) {
                // 移除上述日期已经处理的维度信息
                if (group.contains(" AS ")) {
                    group = group.split(" AS ")[1];
                }
                groups.add(aliaName + group);
            }

            return CollectionUtils.isEmpty(groups) ? aggStr
                    : String.join(",", groups) + "," + aggStr;
        }

        public String getJoinOn(QueryStructReq queryStructCmd, boolean isOver, String aliasLeft, String aliasRight, String suffix) {
            String timeDim = getTimeDim(queryStructCmd);
            String timeSpan = "INTERVAL  " + getTimeSpan(queryStructCmd, isOver, true);
            String aggStr = queryStructCmd.getAggregators().stream().map(f -> {
                if (f.getFunc().equals(AggOperatorEnum.RATIO_OVER) || f.getFunc().equals(AggOperatorEnum.RATIO_ROLL)) {
                    if (queryStructCmd.getRationDateInfo().getPeriod().equals(Constants.MONTH)) {
                        return String.format("%s = DATE_FORMAT(DATE_ADD(CONCAT(%s,'-01'), %s),'%%Y-%%m') ",
                                aliasLeft + timeDim, aliasRight + timeDim + suffix, timeSpan);
                    }
                    if (queryStructCmd.getRationDateInfo().getPeriod().equals(Constants.WEEK) && isOver) {
                        return String.format("to_monday(DATE_ADD(%s ,INTERVAL %s) ) = %s",
                                aliasLeft + timeDim, getTimeSpan(queryStructCmd, isOver, false), aliasRight + timeDim + suffix);
                    }
                    if (queryStructCmd.getRationDateInfo().getPeriod().equals(Constants.YEAR) && !isOver) {
                        return String.format("%s = YEAR(DATE_ADD(CONCAT(%s,'-01-01') , %s))",
                                aliasLeft + timeDim, aliasRight + timeDim + suffix, timeSpan);
                    }
                    return String.format("%s = DATE_ADD(%s,%s) ",
                            aliasLeft + timeDim, aliasRight + timeDim + suffix, timeSpan);
                } else {
                    return f.getColumn();
                }
            }).collect(Collectors.joining(" and "));
            List<String> groups = new ArrayList<>();
            for (String group : queryStructCmd.getGroups()) {
                // 移除上述日期已经处理的维度信息
                if (group.contains(" AS ")) {
                    group = group.split(" AS ")[1];
                }
                if (group.equalsIgnoreCase(timeDim)) {
                    continue;
                }
                groups.add(aliasLeft + group + " = " + aliasRight + group + suffix);
            }
            return CollectionUtils.isEmpty(groups) ? aggStr
                    : String.join(" and ", groups) + " and " + aggStr + " ";
        }

        @Override
        public String sql(QueryStructReq queryStructCmd, boolean isOver, String metricSql) {
//            String sql = "select  area_type, buy_time_month, buy_time_month_roll, sell_price_sum,sell_price_sum_roll,\n" +
//                    "       concat(ifnull(round((sell_price_sum-sell_price_sum_roll)/sell_price_sum_roll*100,2),0),'%') 环比\n" +
//                    "FROM (\n" +
//                    "        select  DATE_FORMAT(buy_time, '%Y-%m') as buy_time_month,\n" +
//                    "                area_type,\n" +
//                    "                sum(sell_price) as sell_price_sum\n" +
//                    "        FROM v_metric_tb_tmp as t_source_1\n" +
//                    "        GROUP BY buy_time_month,area_type\n" +
//                    ") as t0\n" +
//                    "LEFT JOIN (\n" +
//                    "          select  DATE_FORMAT(buy_time, '%Y-%m') as buy_time_month_roll ,\n" +
//                    "                  area_type as area_type_roll,\n" +
//                    "                  sum(sell_price) as sell_price_sum_roll\n" +
//                    "          FROM v_metric_tb_tmp as t_source_2\n" +
//                    "          GROUP BY buy_time_month_roll,area_type\n" +
//                    ") as t1\n" +
//                    "    on t0.area_type = t1.area_type_roll\n" +
//                    "     and t0.buy_time_month = DATE_FORMAT(date_add(CONCAT(t1.buy_time_month_roll,'-01'), INTERVAL  1 month),'%Y-%m')\n" +
//                    "WHERE DATE_FORMAT(buy_time,'%Y-%m') = '2023-08' " +
//                    "ORDER BY buy_time_month DESC";
            String sql = String.format(
                    "select  %s\n" +
                            "FROM (\n" +
                            "     %s   \n" +
                            ") as t0\n" +
                            "LEFT JOIN (\n" +
                            "    %s\n" +
                            ") as t1\n" +
                            "    on %s\n" +
                            "WHERE %s\n" +
                            " %s %s",
                    getOverSelect(queryStructCmd, isOver, "t0."),
                    getSummaryTableSql(queryStructCmd, "tmp0", metricSql, ""),
                    getSummaryTableSql(queryStructCmd, "tmp1", metricSql, "_roll"),
                    getJoinOn(queryStructCmd, isOver, "t0.", "t1.", "_roll"),
                    getWhereOnlyAggData(queryStructCmd),
                    getOrderBy(queryStructCmd),
                    getLimit(queryStructCmd));
            return sql;
//            String sql = String.format(
//                    "select %s from ( select %s , %s from %s t0 left join %s t1 on %s ) metric_tb_src %s %s ",
//                    getOverSelect(queryStructCmd, isOver), getAllSelect(queryStructCmd, "t0."),
//                    getAllJoinSelect(queryStructCmd, "t1."), metricSql, metricSql,
//                    getJoinOn(queryStructCmd, isOver, "t0.", "t1."),
//                    getOrderBy(queryStructCmd), getLimit(queryStructCmd));
//            return sql;
        }

        /**
         * 获取别名并且拼接成合理的可以通过mysql转换的字符串
         * @return
         */
        public String getAliaWithFullDateWithMySql(RatioDateConf rationDateInfo){
            if(rationDateInfo.getPeriod().equals(Constants.MONTH)){
                return String.format("CONCAT(%s,'-01')", rationDateInfo.getRatioDateAlias());
            }else if(rationDateInfo.getPeriod().equals(Constants.YEAR)){
                return String.format("CONCAT(%s,'-01-01')", rationDateInfo.getRatioDateAlias());
            }
            return rationDateInfo.getRatioDateAlias();
        }

        private String getWhereOnlyAggData(QueryStructReq queryStructCmd) {
            return getWhere(queryStructCmd, false, true);
        }

        private String getWhereWithOutAggData(QueryStructReq queryStructCmd) {
            return getWhere(queryStructCmd, true, false);
        }

        private String getWhere(QueryStructReq queryStructCmd, boolean noAggData, boolean onlyAggData) {
            RatioDateConf rationDataInfo = queryStructCmd.getRationDateInfo();

            List<Filter> dimensionFilters = queryStructCmd.getDimensionFilters();
            List<Filter> metricFilters = queryStructCmd.getMetricFilters();
            List<Filter> all = Stream.concat(dimensionFilters.stream(), metricFilters.stream())
                    .collect(Collectors.toList());
            StringBuilder sb = new StringBuilder();
            for (Filter item : all) {
                String whereKey = item.getBizName();
                if (!StringUtils.isEmpty(item.getAlia())) {
                    whereKey = item.getAlia();
                }
                String originWhereKey = queryStructUtils.getColumnWithoutFunc(item.getBizName());
                if (noAggData) {
                    if (rationDataInfo.getRatioDateColumn().equals(originWhereKey)
                            || originWhereKey.startsWith(rationDataInfo.getRatioDateColumn())) {
                        continue;
                    }
                }
                if (onlyAggData) {
                    if (rationDataInfo.getRatioDateColumn().equals(originWhereKey)
                            || originWhereKey.startsWith(rationDataInfo.getRatioDateColumn())) {
                        if (item.getAlia().equals(rationDataInfo.getRatioDateAlias())) {
                            // 已经存在的列元素使用别名，示例：buy_time_YEAR等
                            sb.append(item.getAlia())
                                    .append(item.getOperator().getValue())
                                    .append(String.format("'%s'", item.getValue())).append(",");
                        } else {
                            // 不属于当前当前聚合类型日期的查询，使用聚合字段二次处理，示例：按月聚合，WHERE YEAR(CONCAT(buy_time_MONTH,'-01')) = 2023
                            sb.append(item.getBizName().replace(rationDataInfo.getRatioDateColumn(), getAliaWithFullDateWithMySql(rationDataInfo)))
                                    .append(item.getOperator().getValue())
                                    .append(String.format("'%s'", item.getValue())).append(",");
                        }
                        break;
                    }
                }
                sb.append(whereKey)
                        .append(item.getOperator().getValue())
                        .append(String.format("'%s'", item.getValue())).append(",");
            }
            if (sb.length() > 0) {
                sb.deleteCharAt(sb.length() - 1);
            } else {
                sb.append("1=1");
            }
            return sb.toString();
        }

        private String getSummaryTableSql(QueryStructReq queryStructCmd, String alia, String metricSql, String columnSuffix) {
            /**
             * select  buy_time_month,
             *                 area_type,
             *                 sum(sell_price) as sell_price_sum
             *         FROM v_metric_tb_tmp
             *         GROUP BY buy_time_month,area_type
             */
            // 拼接Select 存在后缀添加后缀
            StringBuilder selectStr = new StringBuilder();
            if (!CollectionUtils.isEmpty(queryStructCmd.getGroups())) {
                for (String gropu : queryStructCmd.getGroups()) {
                    selectStr.append(gropu).append(columnSuffix).append(", ");
                }
            }
//             此处先补充日期列，用于外层WHERE条件
//            String aggDateSelect = String.format("%s as %s%s, ",
//                    queryStructCmd.getRationDateInfo().getRatioDateColumn(),
//                    queryStructCmd.getRationDateInfo().getRatioDateColumn(),
//                    columnSuffix);
            // 不存在时间列则查询原始数据
//            if (selectStr.indexOf(queryStructCmd.getRationDateInfo().getRatioDateAlias()) < 0) {
//                selectStr.append(aggDateSelect);
//            }
            if (selectStr.length() == 0) {
                selectStr.append("*, ");
            }
            List<Aggregator> aggregators = queryStructCmd.getAggregators();
            if (null != aggregators && aggregators.size() > 0) {
                for (Aggregator aggregator : aggregators) {
                    String funcName = aggregator.getFunc().name();
                    if (aggregator.getFunc().equals(AggOperatorEnum.RATIO_OVER) || aggregator.getFunc().equals(AggOperatorEnum.RATIO_ROLL)) {
                        funcName = AggOperatorEnum.SUM.name();
                    }
                    String aggSelectItem = String.format("%s(%s) as %s%s, ",
                            funcName,
                            aggregator.getColumn(),
                            aggregator.getColumn(),
                            columnSuffix);
                    selectStr.append(aggSelectItem);
                }
            }

            if (selectStr.lastIndexOf(", ") > 0) {
                selectStr.delete(selectStr.length() - 2, selectStr.length());
            }
            StringBuilder groupByStr = new StringBuilder();
            if (!CollectionUtils.isEmpty(queryStructCmd.getGroups())) {
                for (String gropu : queryStructCmd.getGroups()) {
                    if (gropu.contains(" AS ")) {
                        groupByStr.append(gropu.split(" AS ")[1]);
                    } else {
                        groupByStr.append(gropu);
                    }
                    groupByStr.append(columnSuffix);
                    groupByStr.append(", ");
                }
            }
            if (groupByStr.lastIndexOf(", ") > 0) {
                groupByStr.delete(groupByStr.length() - 2, groupByStr.length());
            }
            return String.format("select %s FROM %s AS %s GROUP BY %s", selectStr.toString(), metricSql, alia, groupByStr.toString());
        }
    }


    private String getAllJoinSelect(QueryStructReq queryStructCmd, String alias) {
        String aggStr = queryStructCmd.getAggregators().stream()
                .map(f -> getSelectField(f, alias) + " as " + getSelectField(f, "")
                        + "_roll")
                .collect(Collectors.joining(","));
        List<String> groups = new ArrayList<>();
        for (String group : queryStructCmd.getGroups()) {
            groups.add(alias + group + " as " + group + "_roll");
        }
        return CollectionUtils.isEmpty(groups) ? aggStr
                : String.join(",", groups) + "," + aggStr;

    }


    private String getGroupDimWithOutTime(QueryStructReq queryStructCmd) {
        String timeDim = getTimeDim(queryStructCmd);
        return queryStructCmd.getGroups().stream().filter(f -> !f.equalsIgnoreCase(timeDim))
                .collect(Collectors.joining(","));
    }

    private static String getTimeDim(QueryStructReq queryStructCmd) {
        RatioDateConf ratioDateConf = queryStructCmd.getRationDateInfo();
        return ratioDateConf.getRatioDateColumn() + "_" + ratioDateConf.getPeriod();
//        DateUtils dateUtils = ContextUtils.getContext().getBean(DateUtils.class);
//        return dateUtils.getSysDateCol(queryStructCmd.getDateInfo());
    }

    private static String getLimit(QueryStructReq queryStructCmd) {
        if (queryStructCmd != null && queryStructCmd.getLimit() > 0) {
            return " limit " + String.valueOf(queryStructCmd.getLimit());
        }
        return "";
    }

    private String getAllSelect(QueryStructReq queryStructCmd, String alias) {
        String aggStr = queryStructCmd.getAggregators().stream().map(f -> getSelectField(f, alias))
                .collect(Collectors.joining(","));
        return CollectionUtils.isEmpty(queryStructCmd.getGroups()) ? aggStr
                : alias + String.join("," + alias, queryStructCmd.getGroups()) + "," + aggStr;
    }

    private String getSelectField(final Aggregator agg, String alias) {
        if (agg.getFunc().equals(AggOperatorEnum.RATIO_OVER) || agg.getFunc().equals(AggOperatorEnum.RATIO_ROLL)) {
            return alias + agg.getColumn();
        }
        if (CollectionUtils.isEmpty(agg.getArgs())) {
            return agg.getFunc() + "( " + alias + agg.getColumn() + " ) AS " + agg.getColumn() + " ";
        }
        return agg.getFunc() + "( " + agg.getArgs().stream().map(arg ->
                arg.equals(agg.getColumn()) ? arg : (StringUtils.isNumeric(arg) ? arg : ("'" + arg + "'"))
        ).collect(Collectors.joining(",")) + " ) AS " + agg.getColumn() + " ";
    }

    private String getGroupBy(QueryStructReq queryStructCmd) {
        if (CollectionUtils.isEmpty(queryStructCmd.getGroups())) {
            return "";
        }
        return "group by " + String.join(",", queryStructCmd.getGroups());
    }

    private static String getOrderBy(QueryStructReq queryStructCmd) {
        return "ORDER BY " + getTimeDim(queryStructCmd) + " desc";
    }

    private boolean isOverRatio(QueryStructReq queryStructCmd) {
        Long overCt = queryStructCmd.getAggregators().stream()
                .filter(f -> f.getFunc().equals(AggOperatorEnum.RATIO_OVER)).count();
        return overCt > 0;
    }

    private void check(QueryStructReq queryStructCmd) throws Exception {
        Long ratioOverNum = queryStructCmd.getAggregators().stream()
                .filter(f -> f.getFunc().equals(AggOperatorEnum.RATIO_OVER)).count();
        Long ratioRollNum = queryStructCmd.getAggregators().stream()
                .filter(f -> f.getFunc().equals(AggOperatorEnum.RATIO_ROLL)).count();
        if (ratioOverNum > 0 && ratioRollNum > 0) {
            throw new Exception("not support over ratio and roll ratio together ");
        }
        if (getTimeDim(queryStructCmd).isEmpty()) {
            throw new Exception("miss time filter");
        }

    }

    public static void main(String[] args) {
        System.out.println(String.format("%s = DATE_FORMAT(date_add(CONCAT(%s,'-01'), %s),'%%Y-%%m') ",
                "tmp_data", "timeDim", "timeSpan"));
        String sql = "select  area_type, buy_time_month, buy_time_month_roll, sell_price_sum,sell_price_sum_roll,\n" +
                "       concat(ifnull(round((sell_price_sum-sell_price_sum_roll)/sell_price_sum_roll*100,2),0),'%') 环比\n" +
                "FROM (\n" +
                "        select  DATE_FORMAT(buy_time, '%Y-%m') as buy_time_month,\n" +
                "                area_type,\n" +
                "                sum(sell_price) as sell_price_sum\n" +
                "        FROM v_metric_tb_tmp as t_source_1\n" +
                "        GROUP BY buy_time_month,area_type\n" +
                ") as t0\n" +
                "LEFT JOIN (\n" +
                "          select  DATE_FORMAT(buy_time, '%Y-%m') as buy_time_month_roll ,\n" +
                "                  area_type as area_type_roll,\n" +
                "                  sum(sell_price) as sell_price_sum_roll\n" +
                "          FROM v_metric_tb_tmp as t_source_2\n" +
                "          GROUP BY buy_time_month_roll,area_type\n" +
                ") as t1\n" +
                "    on t0.area_type = t1.area_type_roll\n" +
                "     and t0.buy_time_month = DATE_FORMAT(date_add(CONCAT(t1.buy_time_month_roll,'-01'), INTERVAL  1 month),'%Y-%m')\n" +
                "WHERE DATE_FORMAT(buy_time,'%Y-%m') = '2023-08' " +
                "ORDER BY buy_time_month DESC";
        System.out.println(sql);
    }
}
