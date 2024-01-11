package com.tencent.supersonic.headless.query.parser;

import com.tencent.supersonic.common.util.StringUtil;
import com.tencent.supersonic.headless.api.query.enums.AggOption;
import com.tencent.supersonic.headless.api.query.pojo.MetricTable;
import com.tencent.supersonic.headless.api.query.request.MetricReq;
import com.tencent.supersonic.headless.api.query.request.ParseSqlReq;
import com.tencent.supersonic.headless.api.query.request.QueryStructReq;
import com.tencent.supersonic.headless.model.domain.Catalog;
import com.tencent.supersonic.headless.query.parser.convert.SimpleAggConverter;
import com.tencent.supersonic.headless.query.persistence.pojo.QueryStatement;
import com.tencent.supersonic.headless.query.utils.ComponentFactory;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

@Component
@Slf4j
@Primary
public class QueryParser {


    private final Catalog catalog;

    public QueryParser(Catalog catalog) {
        this.catalog = catalog;
    }

    public QueryStatement logicSql(QueryStatement queryStatement) throws Exception {
        QueryStructReq queryStructReq = queryStatement.getQueryStructReq();
        if (Objects.isNull(queryStatement.getParseSqlReq())) {
            queryStatement.setParseSqlReq(new ParseSqlReq());
        }
        if (Objects.isNull(queryStatement.getMetricReq())) {
            queryStatement.setMetricReq(new MetricReq());
        }
        log.info("SemanticConverter before [{}]", queryStructReq);
        for (HeadlessConverter headlessConverter : ComponentFactory.getSemanticConverters()) {
            if (headlessConverter.accept(queryStatement)) {
                log.info("SemanticConverter accept [{}]", headlessConverter.getClass().getName());
                headlessConverter.converter(catalog, queryStructReq, queryStatement.getParseSqlReq(),
                        queryStatement.getMetricReq());
            }
        }
        log.info("SemanticConverter after {} {} {}", queryStructReq, queryStatement.getParseSqlReq(),
                queryStatement.getMetricReq());
        if (!queryStatement.getParseSqlReq().getSql().isEmpty()) {
            return parser(queryStatement.getParseSqlReq(), queryStatement);
        }

        queryStatement.getMetricReq().setNativeQuery(queryStructReq.getQueryType().isNativeAggQuery());
        return parser(queryStatement);

    }

    /**
     * 更简洁的构建sql的逻辑，解决先GROUP BY 后WHERE条件的构建逻辑导致部分SQL解析异常
     */
    public QueryStatement simpleLogicSql(QueryStatement queryStatement) throws Exception {
        QueryStructReq queryStructReq = queryStatement.getQueryStructReq();
        if (Objects.isNull(queryStatement.getParseSqlReq())) {
            queryStatement.setParseSqlReq(new ParseSqlReq());
        }
        if (Objects.isNull(queryStatement.getMetricReq())) {
            queryStatement.setMetricReq(new MetricReq());
        }
        log.info("SemanticConverter before [{}]", queryStructReq);
        // TODO 此处为了适配不同的查询格式来抽象出的不同Converter，目前先满足Agg类型的Converter
        HeadlessConverter headlessConverter = ComponentFactory.getBean("SimpleAggConverter", SimpleAggConverter.class);
        headlessConverter.converter(catalog, queryStructReq, queryStatement.getParseSqlReq(),
                queryStatement.getMetricReq());
        log.info("SemanticConverter after {} {} {}", queryStructReq, queryStatement.getParseSqlReq(),
                queryStatement.getMetricReq());
        if (!queryStatement.getParseSqlReq().getSql().isEmpty()) {
            return simpleParser(queryStatement.getParseSqlReq(), queryStatement);
        }

        queryStatement.getMetricReq().setNativeQuery(queryStructReq.getQueryType().isNativeAggQuery());
        return simpleParser(queryStatement);
    }

    public QueryStatement parser(ParseSqlReq sqlCommend, QueryStatement queryStatement) {
        log.info("parser MetricReq [{}] ", sqlCommend);
        try {
            if (!CollectionUtils.isEmpty(sqlCommend.getTables())) {
                List<String[]> tables = new ArrayList<>();
                String sourceId = "";
                for (MetricTable metricTable : sqlCommend.getTables()) {
                    MetricReq metricReq = new MetricReq();
                    metricReq.setMetrics(metricTable.getMetrics());
                    metricReq.setDimensions(metricTable.getDimensions());
                    metricReq.setWhere(StringUtil.formatSqlQuota(metricTable.getWhere()));
                    metricReq.setNativeQuery(!AggOption.isAgg(metricTable.getAggOption()));
                    metricReq.setRootPath(sqlCommend.getRootPath());
                    QueryStatement tableSql = new QueryStatement();
                    tableSql.setIsS2SQL(false);
                    tableSql.setMetricReq(metricReq);
                    tableSql = parser(tableSql, metricTable.getAggOption());
                    if (!tableSql.isOk()) {
                        queryStatement.setErrMsg(String.format("parser table [%s] error [%s]", metricTable.getAlias(),
                                tableSql.getErrMsg()));
                        return queryStatement;
                    }
                    tables.add(new String[]{metricTable.getAlias(), tableSql.getSql()});
                    sourceId = tableSql.getSourceId();
                }

                if (!tables.isEmpty()) {
                    String sql = "";
                    if (sqlCommend.isSupportWith()) {
                        sql = "with " + String.join(",",
                                tables.stream().map(t -> String.format("%s as (%s)", t[0], t[1])).collect(
                                        Collectors.toList())) + "\n" + sqlCommend.getSql();
                    } else {
                        sql = sqlCommend.getSql();
                        for (String[] tb : tables) {
                            sql = StringUtils.replace(sql, tb[0],
                                    "(" + tb[1] + ") " + (sqlCommend.isWithAlias() ? "" : tb[0]), -1);
                        }
                    }
                    queryStatement.setSql(sql);
                    queryStatement.setSourceId(sourceId);
                    queryStatement.setParseSqlReq(sqlCommend);
                    return queryStatement;
                }
            }
        } catch (Exception e) {
            log.error("physicalSql error {}", e);
            queryStatement.setErrMsg(e.getMessage());
        }
        return queryStatement;
    }

    public QueryStatement parser(QueryStatement queryStatement) {
        return parser(queryStatement, AggOption.getAggregation(queryStatement.getMetricReq().isNativeQuery()));
    }

    public QueryStatement parser(QueryStatement queryStatement, AggOption isAgg) {
        MetricReq metricCommand = queryStatement.getMetricReq();
        log.info("parser MetricReq [{}] isAgg [{}]", metricCommand, isAgg);
        if (metricCommand.getRootPath().isEmpty()) {
            queryStatement.setErrMsg("rootPath empty");
            return queryStatement;
        }
        try {
            queryStatement = ComponentFactory.getSqlParser().explain(queryStatement, isAgg, catalog);
            return queryStatement;
        } catch (Exception e) {
            queryStatement.setErrMsg(e.getMessage());
            log.error("parser error MetricCommand[{}] error [{}]", metricCommand, e);
        }
        return queryStatement;
    }


    /**
     * 带解析数据已经包含了sql DSL解析
     */
    public QueryStatement simpleParser(ParseSqlReq sqlCommend, QueryStatement queryStatement) {
        log.info("parser MetricReq [{}] ", sqlCommend);
        try {
            if (!CollectionUtils.isEmpty(sqlCommend.getTables())) {
                List<String[]> tables = new ArrayList<>();
                String sourceId = "";
                for (MetricTable metricTable : sqlCommend.getTables()) {
                    MetricReq metricReq = new MetricReq();
                    metricReq.setMetrics(metricTable.getMetrics());
                    metricReq.setDimensions(metricTable.getDimensions());
                    metricReq.setWhere(StringUtil.formatSqlQuota(metricTable.getWhere()));
                    metricReq.setNativeQuery(!AggOption.isAgg(metricTable.getAggOption()));
                    metricReq.setRootPath(sqlCommend.getRootPath());
                    QueryStatement tableSql = new QueryStatement();
                    tableSql.setIsS2SQL(false);
                    tableSql.setMetricReq(metricReq);
                    tableSql = simpleParser(tableSql, metricTable.getAggOption());
                    if (!tableSql.isOk()) {
                        queryStatement.setErrMsg(String.format("parser table [%s] error [%s]", metricTable.getAlias(),
                                tableSql.getErrMsg()));
                        return queryStatement;
                    }
                    tables.add(new String[]{metricTable.getAlias(), tableSql.getSql()});
                    sourceId = tableSql.getSourceId();
                }

                if (!tables.isEmpty()) {
                    String sql = "";
                    if (sqlCommend.isSupportWith()) {
                        sql = "with " + String.join(",",
                                tables.stream().map(t -> String.format("%s as (%s)", t[0], t[1])).collect(
                                        Collectors.toList())) + "\n" + sqlCommend.getSql();
                    } else {
                        sql = sqlCommend.getSql();
                        for (String[] tb : tables) {
                            sql = StringUtils.replace(sql, tb[0],
                                    "(" + tb[1] + ") " + (sqlCommend.isWithAlias() ? "" : tb[0]), -1);
                        }
                    }
                    queryStatement.setSql(sql);
                    queryStatement.setSourceId(sourceId);
                    queryStatement.setParseSqlReq(sqlCommend);
                    return queryStatement;
                }
            }
        } catch (Exception e) {
            log.error("physicalSql error {}", e);
            queryStatement.setErrMsg(e.getMessage());
        }
        return queryStatement;
    }

    public QueryStatement simpleParser(QueryStatement queryStatement) {
        return parser(queryStatement, AggOption.getAggregation(queryStatement.getMetricReq().isNativeQuery()));
    }

    public QueryStatement simpleParser(QueryStatement queryStatement, AggOption isAgg) {
        MetricReq metricCommand = queryStatement.getMetricReq();
        log.info("parser MetricReq [{}] isAgg [{}]", metricCommand, isAgg);
        if (metricCommand.getRootPath().isEmpty()) {
            queryStatement.setErrMsg("rootPath empty");
            return queryStatement;
        }
        try {
            queryStatement = ComponentFactory.getSqlParser().simpleExplain(queryStatement, isAgg, catalog);
            return queryStatement;
        } catch (Exception e) {
            queryStatement.setErrMsg(e.getMessage());
            log.error("parser error MetricCommand[{}] error [{}]", metricCommand, e);
        }
        return queryStatement;
    }

}
