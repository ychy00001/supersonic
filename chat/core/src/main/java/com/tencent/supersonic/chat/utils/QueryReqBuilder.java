package com.tencent.supersonic.chat.utils;

import com.google.common.collect.Lists;
import com.tencent.supersonic.chat.api.pojo.SchemaElement;
import com.tencent.supersonic.chat.api.pojo.SemanticParseInfo;
import com.tencent.supersonic.chat.query.QueryManager;
import com.tencent.supersonic.common.pojo.*;
import com.tencent.supersonic.common.pojo.enums.AggOperatorEnum;
import com.tencent.supersonic.common.pojo.enums.AggregateTypeEnum;
import com.tencent.supersonic.semantic.api.model.enums.TimeDimensionEnum;
import com.tencent.supersonic.semantic.api.query.pojo.Filter;
import com.tencent.supersonic.semantic.api.query.request.QueryS2QLReq;
import com.tencent.supersonic.semantic.api.query.request.QueryMultiStructReq;
import com.tencent.supersonic.semantic.api.query.request.QuerySqlReq;
import com.tencent.supersonic.semantic.api.query.request.QueryStructReq;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.BeanUtils;
import org.springframework.util.CollectionUtils;

@Slf4j
public class QueryReqBuilder {

    public static QueryStructReq buildStructReq(SemanticParseInfo parseInfo) {
        QueryStructReq queryStructCmd = new QueryStructReq();
        queryStructCmd.setModelId(parseInfo.getModelId());
        queryStructCmd.setNativeQuery(parseInfo.getNativeQuery());
        queryStructCmd.setDateInfo(rewrite2Between(parseInfo.getDateInfo()));

        List<Filter> dimensionFilters = parseInfo.getDimensionFilters().stream()
                .filter(chatFilter -> Strings.isNotEmpty(chatFilter.getBizName()))
                .map(chatFilter -> new Filter(chatFilter.getBizName(), chatFilter.getOperator(), chatFilter.getValue()))
                .collect(Collectors.toList());
        queryStructCmd.setDimensionFilters(dimensionFilters);

        List<Filter> metricFilters = parseInfo.getMetricFilters().stream()
                .map(chatFilter -> new Filter(chatFilter.getBizName(), chatFilter.getOperator(), chatFilter.getValue()))
                .collect(Collectors.toList());
        queryStructCmd.setMetricFilters(metricFilters);

        addDateDimension(parseInfo);
        List<String> dimensions = parseInfo.getDimensions().stream().map(SchemaElement::getBizName)
                .collect(Collectors.toList());
        queryStructCmd.setGroups(dimensions);
        queryStructCmd.setLimit(parseInfo.getLimit());
        // only one metric is queried at once
        Set<SchemaElement> metrics = parseInfo.getMetrics();
        if (!CollectionUtils.isEmpty(metrics)) {
            SchemaElement metricElement = parseInfo.getMetrics().iterator().next();
            Set<Order> order = getOrder(parseInfo.getOrders(), parseInfo.getAggType(), metricElement);
            queryStructCmd.setAggregators(getAggregatorByMetric(parseInfo.getAggType(), metricElement));
            queryStructCmd.setOrders(new ArrayList<>(order));
        }

        deletionDuplicated(queryStructCmd);

        return queryStructCmd;
    }

    public static QueryStructReq buildStructReqNew(SemanticParseInfo parseInfo) {
        QueryStructReq queryStructCmd = new QueryStructReq();
        queryStructCmd.setModelId(parseInfo.getModelId());
        queryStructCmd.setNativeQuery(parseInfo.getNativeQuery());
        queryStructCmd.setRationDateInfo(rewrite2Between(parseInfo.getRatioDataInfo()));
        queryStructCmd.setDateInfo(rewrite2Between(parseInfo.getDateInfo()));
        List<Filter> dimensionFilters = parseInfo.getDimensionFilters().stream()
                .filter(chatFilter -> Strings.isNotEmpty(chatFilter.getBizName()))
                .map(chatFilter -> new Filter(chatFilter.getBizName(), chatFilter.getOperator(), chatFilter.getValue(), chatFilter.getAlia()))
                .collect(Collectors.toList());
        queryStructCmd.setDimensionFilters(dimensionFilters);

        List<Filter> metricFilters = parseInfo.getMetricFilters().stream()
                .map(chatFilter -> new Filter(chatFilter.getBizName(), chatFilter.getOperator(), chatFilter.getValue(), chatFilter.getAlia()))
                .collect(Collectors.toList());
        queryStructCmd.setMetricFilters(metricFilters);

        addDateDimension(parseInfo);
        List<String> dimensions = parseInfo.getDimensions().stream().map(item -> {
            if(null != item.getAlias() && item.getAlias().size() > 0 ){
                return item.getBizName() + " AS " + item.getAlias().get(0);
            }
            return item.getBizName();
        }).collect(Collectors.toList());
        queryStructCmd.setGroups(dimensions);
        queryStructCmd.setLimit(parseInfo.getLimit());
        // only one metric is queried at once
        Set<SchemaElement> metrics = parseInfo.getMetrics();
        if (!CollectionUtils.isEmpty(metrics)) {
            SchemaElement metricElement = parseInfo.getMetrics().iterator().next();
            Set<Order> order = getOrder(parseInfo.getOrders(), parseInfo.getAggType(), metricElement);
            queryStructCmd.setAggregators(getAggregatorByMetric(parseInfo.getAggType(), metricElement));
            queryStructCmd.setOrders(new ArrayList<>(order));
        }

        deletionDuplicated(queryStructCmd);

        return queryStructCmd;
    }


    private static void deletionDuplicated(QueryStructReq queryStructReq) {
        if (!CollectionUtils.isEmpty(queryStructReq.getGroups()) && queryStructReq.getGroups().size() > 1) {
            Set<String> groups = new HashSet<>();
            groups.addAll(queryStructReq.getGroups());
            queryStructReq.getGroups().clear();
            queryStructReq.getGroups().addAll(groups);
        }
    }

    private static DateConf rewrite2Between(DateConf dateInfo) {
        if (null == dateInfo) {
            return null;
        }
        DateConf dateInfoNew = new DateConf();
        BeanUtils.copyProperties(dateInfo, dateInfoNew);
        if (Objects.nonNull(dateInfo) && DateConf.DateMode.RECENT.equals(dateInfo.getDateMode())) {
            int unit = dateInfo.getUnit();
            int days = 1;
            switch (dateInfo.getPeriod()) {
                case Constants.DAY:
                    days = 1;
                    break;
                case Constants.WEEK:
                    days = 7;
                    break;
                case Constants.MONTH:
                    days = 30;
                    break;
                case Constants.YEAR:
                    days = 365;
                    break;
                default:
                    break;
            }
            String startDate = LocalDate.now().plusDays(-(unit * days)).toString();
            String endDate = LocalDate.now().plusDays(-1).toString();
            dateInfoNew.setDateMode(DateConf.DateMode.BETWEEN);
            dateInfoNew.setStartDate(startDate);
            dateInfoNew.setEndDate(endDate);
        }
        return dateInfoNew;
    }
    private static RatioDateConf rewrite2Between(RatioDateConf dateInfo) {
        if (null == dateInfo) {
            return null;
        }
        RatioDateConf dateInfoNew = new RatioDateConf();
        BeanUtils.copyProperties(dateInfo, dateInfoNew);
        if (Objects.nonNull(dateInfo) && DateConf.DateMode.RECENT.equals(dateInfo.getDateMode())) {
            int unit = dateInfo.getUnit();
            int days = 1;
            switch (dateInfo.getPeriod()) {
                case Constants.DAY:
                    days = 1;
                    break;
                case Constants.WEEK:
                    days = 7;
                    break;
                case Constants.MONTH:
                    days = 30;
                    break;
                case Constants.YEAR:
                    days = 365;
                    break;
                default:
                    break;
            }
            String startDate = LocalDate.now().plusDays(-(unit * days)).toString();
            String endDate = LocalDate.now().plusDays(-1).toString();
            dateInfoNew.setDateMode(RatioDateConf.DateMode.BETWEEN);
            dateInfoNew.setStartDate(startDate);
            dateInfoNew.setEndDate(endDate);
        }
        return dateInfoNew;
    }

    public static QueryMultiStructReq buildMultiStructReq(SemanticParseInfo parseInfo) {
        QueryStructReq queryStructReq = buildStructReq(parseInfo);
        QueryMultiStructReq queryMultiStructReq = new QueryMultiStructReq();
        List<QueryStructReq> queryStructReqs = Lists.newArrayList();
        for (Filter dimensionFilter : queryStructReq.getDimensionFilters()) {
            QueryStructReq req = new QueryStructReq();
            BeanUtils.copyProperties(queryStructReq, req);
            req.setDimensionFilters(Lists.newArrayList(dimensionFilter));
            queryStructReqs.add(req);
        }
        queryMultiStructReq.setQueryStructReqs(queryStructReqs);
        return queryMultiStructReq;
    }

    /**
     * convert to QueryS2QLReq
     *
     * @param querySql
     * @param modelId
     * @return
     */
    public static QueryS2QLReq buildS2QLReq(String querySql, Long modelId) {
        QueryS2QLReq queryS2QLReq = new QueryS2QLReq();
        if (Objects.nonNull(querySql)) {
            queryS2QLReq.setSql(querySql);
        }
        queryS2QLReq.setModelId(modelId);
        return queryS2QLReq;
    }


    /**
     * convert to QueryDslReq
     *
     * @param querySql
     * @param modelId
     * @return
     */
    public static QuerySqlReq buildPureSqlReq(String querySql, Long modelId, String sourceId) {
        QuerySqlReq queryDslReq = new QuerySqlReq();
        if (Objects.nonNull(querySql)) {
            queryDslReq.setSql(querySql);
        }
        queryDslReq.setModelId(modelId);
        queryDslReq.setSourceId(sourceId);
        return queryDslReq;
    }

    private static List<Aggregator> getAggregatorByMetric(AggregateTypeEnum aggregateType, SchemaElement metric) {
        List<Aggregator> aggregators = new ArrayList<>();
        if (metric != null) {
            String agg = (aggregateType == null || aggregateType.equals(AggregateTypeEnum.NONE)) ? ""
                    : aggregateType.name();
            aggregators.add(new Aggregator(metric.getBizName(), AggOperatorEnum.of(agg)));
        }
        return aggregators;
    }

    private static void addDateDimension(SemanticParseInfo parseInfo) {
        if (parseInfo != null) {
            String queryMode = parseInfo.getQueryMode();
            if (parseInfo.getDateInfo() == null) {
                return;
            }
            if (parseInfo.getAggType() != null && (parseInfo.getAggType().equals(AggregateTypeEnum.MAX)
                    || parseInfo.getAggType().equals(AggregateTypeEnum.MIN)) && !CollectionUtils.isEmpty(
                    parseInfo.getDimensions())) {
                return;
            }
            DateConf dateInfo = parseInfo.getDateInfo();
            String dateField = getDateField(dateInfo);

            for (SchemaElement dimension : parseInfo.getDimensions()) {
                if (dimension.getBizName().equalsIgnoreCase(dateField)) {
                    return;
                }
            }

            if (Objects.nonNull(parseInfo.getAggType()) && !parseInfo.getAggType().equals(AggregateTypeEnum.NONE)) {
                return;
            }

            SchemaElement dimension = new SchemaElement();
            dimension.setBizName(dateField);

            if (QueryManager.isMetricQuery(queryMode)) {
                List<String> timeDimensions = Arrays.asList(TimeDimensionEnum.DAY.getName(),
                        TimeDimensionEnum.WEEK.getName(), TimeDimensionEnum.MONTH.getName());
                Set<SchemaElement> dimensions = parseInfo.getDimensions().stream()
                        .filter(d -> !timeDimensions.contains(d.getBizName().toLowerCase())).collect(
                                Collectors.toSet());
                dimensions.add(dimension);
                parseInfo.setDimensions(dimensions);
            }
        }
    }

    public static Set<Order> getOrder(Set<Order> parseOrder, AggregateTypeEnum aggregator, SchemaElement metric) {
        if (!CollectionUtils.isEmpty(parseOrder)) {
            return parseOrder;
        }
        Set<Order> orders = new LinkedHashSet();
        if (metric == null) {
            return orders;
        }

        if ((AggregateTypeEnum.TOPN.equals(aggregator) || AggregateTypeEnum.MAX.equals(aggregator)
                || AggregateTypeEnum.MIN.equals(
                aggregator))) {
            Order order = new Order();
            order.setColumn(metric.getBizName());
            order.setDirection("desc");
            orders.add(order);
        }
        return orders;
    }

    public static String getDateField(DateConf dateConf) {
        if (Objects.isNull(dateConf)) {
            return "";
        }
        String dateField = TimeDimensionEnum.DAY.getName();
        if (Constants.MONTH.equals(dateConf.getPeriod())) {
            dateField = TimeDimensionEnum.MONTH.getName();
        }
        if (Constants.WEEK.equals(dateConf.getPeriod())) {
            dateField = TimeDimensionEnum.WEEK.getName();
        }
        return dateField;
    }

    public static QueryStructReq buildStructRatioReq(SemanticParseInfo parseInfo, SchemaElement metric,
                                                     AggOperatorEnum aggOperatorEnum) {
        QueryStructReq queryStructCmd = buildStructReq(parseInfo);
        queryStructCmd.setNativeQuery(false);
        queryStructCmd.setOrders(new ArrayList<>());
        List<Aggregator> aggregators = new ArrayList<>();
        Aggregator ratioRoll = new Aggregator(metric.getBizName(), aggOperatorEnum);
        aggregators.add(ratioRoll);
        queryStructCmd.setAggregators(aggregators);
        return queryStructCmd;
    }
}
