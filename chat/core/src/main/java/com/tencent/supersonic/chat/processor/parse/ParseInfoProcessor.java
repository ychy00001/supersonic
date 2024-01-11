package com.tencent.supersonic.chat.processor.parse;

import com.google.common.collect.Lists;
import com.tencent.supersonic.chat.api.component.SemanticQuery;
import com.tencent.supersonic.chat.api.pojo.ChatContext;
import com.tencent.supersonic.chat.api.pojo.QueryContext;
import com.tencent.supersonic.chat.api.pojo.SchemaElement;
import com.tencent.supersonic.chat.api.pojo.SemanticParseInfo;
import com.tencent.supersonic.chat.api.pojo.SemanticSchema;
import com.tencent.supersonic.chat.api.pojo.request.QueryFilter;
import com.tencent.supersonic.chat.api.pojo.response.ParseResp;
import com.tencent.supersonic.chat.api.pojo.response.SqlInfo;
import com.tencent.supersonic.common.pojo.DateConf;
import com.tencent.supersonic.common.pojo.enums.QueryType;
import com.tencent.supersonic.common.pojo.enums.FilterOperatorEnum;
import com.tencent.supersonic.common.pojo.enums.TimeDimensionEnum;
import com.tencent.supersonic.common.util.ContextUtils;
import com.tencent.supersonic.common.util.jsqlparser.FieldExpression;
import com.tencent.supersonic.common.util.jsqlparser.SqlParserSelectHelper;
import com.tencent.supersonic.knowledge.service.SchemaService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * ParseInfoProcessor extracts structured info from S2SQL so that
 * users get to know the details.
 **/
@Slf4j
public class ParseInfoProcessor implements ParseResultProcessor {

    @Override
    public void process(ParseResp parseResp, QueryContext queryContext, ChatContext chatContext) {
        List<SemanticQuery> candidateQueries = queryContext.getCandidateQueries();
        if (CollectionUtils.isEmpty(candidateQueries)) {
            return;
        }
        List<SemanticParseInfo> candidateParses = candidateQueries.stream()
                .map(SemanticQuery::getParseInfo).collect(Collectors.toList());
        candidateParses.forEach(this::updateParseInfo);
    }

    public void updateParseInfo(SemanticParseInfo parseInfo) {
        SqlInfo sqlInfo = parseInfo.getSqlInfo();
        String correctS2SQL = sqlInfo.getCorrectS2SQL();
        if (StringUtils.isBlank(correctS2SQL)) {
            return;
        }
        // if S2SQL equals correctS2SQL, then not update the parseInfo.
        if (correctS2SQL.equals(sqlInfo.getS2SQL())) {
            return;
        }
        List<FieldExpression> expressions = SqlParserSelectHelper.getFilterExpression(correctS2SQL);
        //set dataInfo
        try {
            if (!org.apache.commons.collections.CollectionUtils.isEmpty(expressions)) {
                DateConf dateInfo = getDateInfo(expressions);
                if (dateInfo != null && parseInfo.getDateInfo() == null) {
                    parseInfo.setDateInfo(dateInfo);
                }
            }
        } catch (Exception e) {
            log.error("set dateInfo error :", e);
        }

        //set filter
        Set<Long> modelIds = parseInfo.getModel().getModelIds();
        try {
            Map<String, SchemaElement> fieldNameToElement = getNameToElement(modelIds);
            List<QueryFilter> result = getDimensionFilter(fieldNameToElement, expressions);
            parseInfo.getDimensionFilters().addAll(result);
        } catch (Exception e) {
            log.error("set dimensionFilter error :", e);
        }

        SemanticSchema semanticSchema = ContextUtils.getBean(SchemaService.class).getSemanticSchema();
        if (Objects.isNull(semanticSchema)) {
            return;
        }
        List<String> allFields = getFieldsExceptDate(SqlParserSelectHelper.getAllFields(sqlInfo.getCorrectS2SQL()));
        Set<SchemaElement> metrics = getElements(modelIds, allFields, semanticSchema.getMetrics());
        parseInfo.setMetrics(metrics);
        if (QueryType.METRIC.equals(parseInfo.getQueryType())) {
            List<String> groupByFields = SqlParserSelectHelper.getGroupByFields(sqlInfo.getCorrectS2SQL());
            List<String> groupByDimensions = getFieldsExceptDate(groupByFields);
            parseInfo.setDimensions(getElements(modelIds, groupByDimensions, semanticSchema.getDimensions()));
        } else if (QueryType.TAG.equals(parseInfo.getQueryType())) {
            List<String> selectFields = SqlParserSelectHelper.getSelectFields(sqlInfo.getCorrectS2SQL());
            List<String> selectDimensions = getFieldsExceptDate(selectFields);
            parseInfo.setDimensions(getElements(modelIds, selectDimensions, semanticSchema.getDimensions()));
        }
    }

    private Set<SchemaElement> getElements(Set<Long> modelIds, List<String> allFields, List<SchemaElement> elements) {
        return elements.stream()
                .filter(schemaElement -> modelIds.contains(schemaElement.getModel()) && allFields.contains(
                        schemaElement.getName())
                ).collect(Collectors.toSet());
    }

    private List<String> getFieldsExceptDate(List<String> allFields) {
        if (org.springframework.util.CollectionUtils.isEmpty(allFields)) {
            return new ArrayList<>();
        }
        return allFields.stream()
                .filter(entry -> !TimeDimensionEnum.DAY.getChName().equalsIgnoreCase(entry))
                .collect(Collectors.toList());
    }

    private List<QueryFilter> getDimensionFilter(Map<String, SchemaElement> fieldNameToElement,
            List<FieldExpression> fieldExpressions) {
        List<QueryFilter> result = Lists.newArrayList();
        for (FieldExpression expression : fieldExpressions) {
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

    private DateConf getDateInfo(List<FieldExpression> fieldExpressions) {
        List<FieldExpression> dateExpressions = fieldExpressions.stream()
                .filter(expression -> TimeDimensionEnum.DAY.getChName().equalsIgnoreCase(expression.getFieldName()))
                .collect(Collectors.toList());
        if (org.apache.commons.collections.CollectionUtils.isEmpty(dateExpressions)) {
            return null;
        }
        DateConf dateInfo = new DateConf();
        dateInfo.setDateMode(DateConf.DateMode.BETWEEN);
        FieldExpression firstExpression = dateExpressions.get(0);

        FilterOperatorEnum firstOperator = FilterOperatorEnum.getSqlOperator(firstExpression.getOperator());
        if (FilterOperatorEnum.EQUALS.equals(firstOperator) && Objects.nonNull(firstExpression.getFieldValue())) {
            dateInfo.setStartDate(firstExpression.getFieldValue().toString());
            dateInfo.setEndDate(firstExpression.getFieldValue().toString());
            dateInfo.setDateMode(DateConf.DateMode.BETWEEN);
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

    private boolean containOperators(FieldExpression expression, FilterOperatorEnum firstOperator,
            FilterOperatorEnum... operatorEnums) {
        return (Arrays.asList(operatorEnums).contains(firstOperator) && Objects.nonNull(
                expression.getFieldValue()));
    }

    private boolean hasSecondDate(List<FieldExpression> dateExpressions) {
        return dateExpressions.size() > 1 && Objects.nonNull(dateExpressions.get(1).getFieldValue());
    }

    protected Map<String, SchemaElement> getNameToElement(Set<Long> modelIds) {
        SemanticSchema semanticSchema = ContextUtils.getBean(SchemaService.class).getSemanticSchema();
        List<SchemaElement> dimensions = semanticSchema.getDimensions(modelIds);
        List<SchemaElement> metrics = semanticSchema.getMetrics(modelIds);

        List<SchemaElement> allElements = Lists.newArrayList();
        allElements.addAll(dimensions);
        allElements.addAll(metrics);
        //support alias
        return allElements.stream()
                .flatMap(schemaElement -> {
                    Set<Pair<String, SchemaElement>> result = new HashSet<>();
                    result.add(Pair.of(schemaElement.getName(), schemaElement));
                    List<String> aliasList = schemaElement.getAlias();
                    if (!org.springframework.util.CollectionUtils.isEmpty(aliasList)) {
                        for (String alias : aliasList) {
                            result.add(Pair.of(alias, schemaElement));
                        }
                    }
                    return result.stream();
                })
                .collect(Collectors.toMap(pair -> pair.getLeft(), pair -> pair.getRight(),
                        (value1, value2) -> value2));
    }

}