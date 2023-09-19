package com.tencent.supersonic.common.util.jsqlparser;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import lombok.extern.slf4j.Slf4j;
import net.sf.jsqlparser.JSQLParserException;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.Function;
import net.sf.jsqlparser.expression.operators.relational.ComparisonOperator;
import net.sf.jsqlparser.parser.CCJSqlParserUtil;
import net.sf.jsqlparser.schema.Column;
import net.sf.jsqlparser.schema.Table;
import net.sf.jsqlparser.statement.Statement;
import net.sf.jsqlparser.statement.select.GroupByElement;
import net.sf.jsqlparser.statement.select.OrderByElement;
import net.sf.jsqlparser.statement.select.PlainSelect;
import net.sf.jsqlparser.statement.select.Select;
import net.sf.jsqlparser.statement.select.SelectBody;
import net.sf.jsqlparser.statement.select.SelectItem;
import org.springframework.util.CollectionUtils;

/**
 * Sql Parser Select Helper
 */
@Slf4j
public class SqlParserSelectHelper {

    public static List<FilterExpression> getFilterExpression(String sql) {
        PlainSelect plainSelect = getPlainSelect(sql);
        if (Objects.isNull(plainSelect)) {
            return new ArrayList<>();
        }
        Set<FilterExpression> result = new HashSet<>();
        Expression where = plainSelect.getWhere();
        if (Objects.nonNull(where)) {
            where.accept(new FieldAndValueAcquireVisitor(result));
        }
        return new ArrayList<>(result);
    }

    public static List<String> getWhereFields(String sql) {
        PlainSelect plainSelect = getPlainSelect(sql);
        if (Objects.isNull(plainSelect)) {
            return new ArrayList<>();
        }
        Set<String> result = new HashSet<>();
        getWhereFields(plainSelect, result);
        return new ArrayList<>(result);
    }

    private static void getWhereFields(PlainSelect plainSelect, Set<String> result) {
        Expression where = plainSelect.getWhere();
        if (Objects.nonNull(where)) {
            where.accept(new FieldAcquireVisitor(result));
        }
    }


    public static List<String> getSelectFields(String sql) {
        PlainSelect plainSelect = getPlainSelect(sql);
        if (Objects.isNull(plainSelect)) {
            return new ArrayList<>();
        }
        return new ArrayList<>(getSelectFields(plainSelect));
    }

    public static Set<String> getSelectFields(PlainSelect plainSelect) {
        List<SelectItem> selectItems = plainSelect.getSelectItems();
        Set<String> result = new HashSet<>();
        for (SelectItem selectItem : selectItems) {
            selectItem.accept(new FieldAcquireVisitor(result));
        }
        return result;
    }

    public static PlainSelect getPlainSelect(String sql) {
        Select selectStatement = getSelect(sql);
        if (selectStatement == null) {
            return null;
        }
        SelectBody selectBody = selectStatement.getSelectBody();

        if (!(selectBody instanceof PlainSelect)) {
            return null;
        }
        return (PlainSelect) selectBody;
    }

    public static Select getSelect(String sql) {
        Statement statement = null;
        try {
            statement = CCJSqlParserUtil.parse(sql);
        } catch (JSQLParserException e) {
            log.error("parse error", e);
            return null;
        }

        if (!(statement instanceof Select)) {
            return null;
        }
        return (Select) statement;
    }


    public static List<String> getAllFields(String sql) {

        PlainSelect plainSelect = getPlainSelect(sql);
        if (Objects.isNull(plainSelect)) {
            return new ArrayList<>();
        }
        Set<String> result = getSelectFields(plainSelect);

        getGroupByFields(plainSelect, result);

        getOrderByFields(plainSelect, result);

        getWhereFields(plainSelect, result);

        getHavingFields(plainSelect, result);

        return new ArrayList<>(result);
    }

    private static void getHavingFields(PlainSelect plainSelect, Set<String> result) {
        Expression having = plainSelect.getHaving();
        if (Objects.nonNull(having)) {
            having.accept(new FieldAcquireVisitor(result));
        }

    }

    public static Expression getHavingExpression(String sql) {
        PlainSelect plainSelect = getPlainSelect(sql);
        Expression having = plainSelect.getHaving();
        if (Objects.nonNull(having)) {
            if (!(having instanceof ComparisonOperator)) {
                return null;
            }
            ComparisonOperator comparisonOperator = (ComparisonOperator) having;
            if (comparisonOperator.getLeftExpression() instanceof Function) {
                return comparisonOperator.getLeftExpression();
            } else if (comparisonOperator.getRightExpression() instanceof Function) {
                return comparisonOperator.getRightExpression();
            }
        }
        return null;
    }

    public static List<String> getOrderByFields(String sql) {
        PlainSelect plainSelect = getPlainSelect(sql);
        if (Objects.isNull(plainSelect)) {
            return new ArrayList<>();
        }
        Set<String> result = new HashSet<>();
        getOrderByFields(plainSelect, result);
        return new ArrayList<>(result);
    }

    private static void getOrderByFields(PlainSelect plainSelect, Set<String> result) {
        List<OrderByElement> orderByElements = plainSelect.getOrderByElements();
        if (!CollectionUtils.isEmpty(orderByElements)) {
            for (OrderByElement orderByElement : orderByElements) {
                orderByElement.accept(new OrderByAcquireVisitor(result));
            }
        }
    }

    public static List<String> getGroupByFields(String sql) {
        PlainSelect plainSelect = getPlainSelect(sql);
        if (Objects.isNull(plainSelect)) {
            return new ArrayList<>();
        }
        HashSet<String> result = new HashSet<>();
        getGroupByFields(plainSelect, result);
        return new ArrayList<>(result);
    }

    private static void getGroupByFields(PlainSelect plainSelect, Set<String> result) {
        GroupByElement groupBy = plainSelect.getGroupBy();
        if (groupBy != null) {
            List<Expression> groupByExpressions = groupBy.getGroupByExpressions();
            for (Expression expression : groupByExpressions) {
                if (expression instanceof Column) {
                    Column column = (Column) expression;
                    result.add(column.getColumnName());
                }
            }
        }
    }

    public static String getTableName(String sql) {
        Select selectStatement = getSelect(sql);
        if (selectStatement == null) {
            return null;
        }
        SelectBody selectBody = selectStatement.getSelectBody();
        PlainSelect plainSelect = (PlainSelect) selectBody;

        Table table = (Table) plainSelect.getFromItem();
        return table.getName();
    }


    public static boolean hasAggregateFunction(String sql) {
        Select selectStatement = getSelect(sql);
        SelectBody selectBody = selectStatement.getSelectBody();

        if (!(selectBody instanceof PlainSelect)) {
            return false;
        }
        PlainSelect plainSelect = (PlainSelect) selectBody;
        List<SelectItem> selectItems = plainSelect.getSelectItems();
        AggregateFunctionVisitor visitor = new AggregateFunctionVisitor();
        for (SelectItem selectItem : selectItems) {
            selectItem.accept(visitor);
        }
        boolean selectFunction = visitor.hasAggregateFunction();
        if (selectFunction) {
            return true;
        }
        GroupByElement groupBy = plainSelect.getGroupBy();
        if (Objects.nonNull(groupBy)) {
            GroupByVisitor replaceVisitor = new GroupByVisitor();
            groupBy.accept(replaceVisitor);
            return replaceVisitor.isHasAggregateFunction();
        }
        return false;
    }
}

