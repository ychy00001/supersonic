package com.tencent.supersonic.common.util;

import static com.tencent.supersonic.common.pojo.Constants.PARENTHESES_END;
import static com.tencent.supersonic.common.pojo.Constants.PARENTHESES_START;
import static com.tencent.supersonic.common.pojo.Constants.SPACE;
import static com.tencent.supersonic.common.pojo.Constants.SYS_VAR;

import com.tencent.supersonic.common.pojo.Constants;
import com.tencent.supersonic.common.pojo.Criterion;
import com.tencent.supersonic.common.pojo.Filter;
import com.tencent.supersonic.common.pojo.enums.FilterOperatorEnum;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.StringJoiner;
import java.util.regex.Pattern;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;


@Component
@Slf4j
public class SqlFilterUtils {

    private static String pattern = "^'.*?'$";
    private static String numericPattern = "^[0-9]+$";

    public List<String> getFiltersCol(List<Filter> filters) {
        List<String> filterCols = new ArrayList<>();
        if (CollectionUtils.isEmpty(filters)) {
            return filterCols;
        }
        for (Filter filter : filters) {
            filterCols.addAll(getFilterCol(filter));
        }
        return filterCols;
    }

    private List<String> getFilterCol(Filter filter) {
        List<String> filterCols = new ArrayList<>();
        if (Filter.Relation.FILTER.equals(filter.getRelation())) {
            if (Strings.isNotEmpty(filter.getBizName())) {
                filterCols.add(filter.getBizName());
            }
        }

        List<Filter> children = filter.getChildren();
        if (!CollectionUtils.isEmpty(children)) {
            for (Filter child : children) {
                filterCols.addAll(getFilterCol(child));
            }
        }
        return filterCols;
    }

    public String getWhereClause(List<Filter> filters, boolean isBizName) {
        StringJoiner joiner = new StringJoiner(Constants.AND_UPPER);

        if (!CollectionUtils.isEmpty(filters)) {
            filters.stream()
                    .forEach(filter -> {
                        if (Strings.isNotEmpty(dealFilter(filter, isBizName))) {
                            joiner.add(SPACE + dealFilter(filter, isBizName) + SPACE);
                        }
                    });
            log.info("getWhereClause, where sql : {}", joiner.toString());
            return joiner.toString();
        }

        return "";
    }

    public String getWhereClause(List<Filter> filters) {
        return getWhereClause(filters, true);
    }

    public String dealFilter(Filter filter, boolean isBizName) {
        if (Objects.isNull(filter)) {
            return "";
        }
        if (Strings.isNotEmpty(filter.getBizName()) && filter.getBizName().endsWith(SYS_VAR)) {
            return "";
        }
        StringBuilder condition = new StringBuilder();
        if (Filter.Relation.FILTER.equals(filter.getRelation())) {
            return dealSingleFilter(filter, isBizName);
        }

        List<Filter> children = filter.getChildren();
        condition.append(PARENTHESES_START);
        StringJoiner joiner = new StringJoiner(SPACE + filter.getRelation().name() + SPACE);
        for (Filter child : children) {
            joiner.add(dealFilter(child, isBizName));
        }
        condition.append(joiner.toString());
        condition.append(PARENTHESES_END);
        return condition.toString();
    }

    // todo deal metric filter
    private String dealSingleFilter(Filter filter, boolean isBizName) {
        String name = filter.getBizName();
        if (!isBizName) {
            name = filter.getName();
        }
        Object value = filter.getValue();
        FilterOperatorEnum operator = filter.getOperator();

        String dataType = Criterion.StringDataType.STRING.name();

        Criterion criterion = new Criterion(name, operator, value, dataType);
        return generator(criterion);
    }

    private String generator(Criterion criterion) {
        log.info("criterion :{}", criterion);
        String sqlPart;
        switch (criterion.getOperator()) {
            case SQL_PART:
                sqlPart = sqlPartLogic(criterion);
                break;

            case IS_NULL:
            case IS_NOT_NULL:
                sqlPart = judgeNullLogic(criterion);
                break;

            case EQUALS:
            case NOT_EQUALS:
            case GREATER_THAN:
            case GREATER_THAN_EQUALS:
            case MINOR_THAN:
            case MINOR_THAN_EQUALS:
                sqlPart = singleValueLogic(criterion);
                break;

            case BETWEEN:
                sqlPart = betweenLogic(criterion);
                break;

            case IN:
            case NOT_IN:
                sqlPart = inLogic(criterion);
                break;
            case LIKE:
            case NOT_LIKE:
                sqlPart = likeLogic(criterion);
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + criterion.getOperator());
        }
        return sqlPart;
    }

    private String likeLogic(Criterion criterion) {
        if (Objects.isNull(criterion) || Objects.isNull(criterion.getValue())) {
            throw new RuntimeException("criterion.getValue() can not be null");
        }
        StringBuilder whereClause = new StringBuilder();
        whereClause.append(criterion.getColumn() + SPACE + criterion.getOperator().getValue() + SPACE);
        String value = criterion.getValue().toString();
        if (criterion.isNeedApostrophe() && !Pattern.matches(pattern, value)) {
            // like click => 'like%'
            if(value.contains(Constants.PERCENT_SIGN)){
                whereClause.append(Constants.APOSTROPHE + value + Constants.APOSTROPHE);
            }else{
                whereClause.append(Constants.APOSTROPHE + value + Constants.PERCENT_SIGN + Constants.APOSTROPHE);
            }
        } else {
            // like 'click' => 'like%'
            if(value.contains(Constants.PERCENT_SIGN)){
                whereClause.append(Constants.APOSTROPHE + value.replaceAll(Constants.APOSTROPHE, "")
                        + Constants.APOSTROPHE);
            }else{
                whereClause.append(Constants.APOSTROPHE + value.replaceAll(Constants.APOSTROPHE, Constants.PERCENT_SIGN)
                        + Constants.APOSTROPHE);
            }

        }
        return whereClause.toString();
    }

    private String inLogic(Criterion criterion) {
        if (Objects.isNull(criterion) || Objects.isNull(criterion.getValue())) {
            throw new RuntimeException("criterion.getValue() can not be null");
        }

        StringBuilder whereClause = new StringBuilder();
        whereClause.append(criterion.getColumn() + SPACE + criterion.getOperator().getValue() + SPACE);
        List values = (List) criterion.getValue();
        whereClause.append(PARENTHESES_START);
        StringJoiner joiner = new StringJoiner(",");
        if (criterion.isNeedApostrophe()) {
            values.stream().forEach(value -> joiner.add(valueApostropheLogic(value.toString())));
        } else {
            values.stream().forEach(value -> joiner.add(value.toString()));
        }
        whereClause.append(joiner);
        whereClause.append(PARENTHESES_END);
        return whereClause.toString();
    }

    private String betweenLogic(Criterion criterion) {
        if (Objects.isNull(criterion) || Objects.isNull(criterion.getValue())) {
            throw new RuntimeException("criterion.getValue() can not be null");
        }
        List values = (List) criterion.getValue();
        if (values.size() != 2) {
            throw new RuntimeException("between value size should be 2");
        }

        if (criterion.isNeedApostrophe()) {
            return String.format("(%s >= %s and %s <= %s)", criterion.getColumn(),
                    valueApostropheLogic(values.get(0).toString()),
                    criterion.getColumn(), valueApostropheLogic(values.get(1).toString()));
        }
        return String.format("(%s >= %s and %s <= %s)", criterion.getColumn(), values.get(0).toString(),
                criterion.getColumn(), values.get(1).toString());
    }

    private String singleValueLogic(Criterion criterion) {
        if (Objects.isNull(criterion) || Objects.isNull(criterion.getValue())) {
            throw new RuntimeException("criterion.getValue() can not be null");
        }
        StringBuilder whereClause = new StringBuilder();
        whereClause.append(criterion.getColumn() + SPACE + criterion.getOperator().getValue() + SPACE);
        String value = criterion.getValue().toString();
        if (criterion.isNeedApostrophe()) {
            value = valueApostropheLogic(value);
        }
        whereClause.append(value);
        return whereClause.toString();
    }

    private String valueApostropheLogic(String value) {
        if (Pattern.matches(pattern, value) || Pattern.matches(numericPattern, value)) {
            return value;
        }
        return Constants.APOSTROPHE + value + Constants.APOSTROPHE;
    }

    private String judgeNullLogic(Criterion criterion) {

        if (Objects.isNull(criterion) || Objects.isNull(criterion.getColumn())) {
            throw new RuntimeException("criterion.getColumn() can not be null");
        }
        return String.format("( %s %s)", criterion.getColumn(), criterion.getOperator().getValue());
    }

    private String sqlPartLogic(Criterion criterion) {
        if (Objects.isNull(criterion) || Objects.isNull(criterion.getValue())) {
            throw new RuntimeException("criterion.getValue() can not be null");
        }
        return PARENTHESES_START + SPACE + criterion.getValue().toString() + SPACE + PARENTHESES_END;
    }

}
