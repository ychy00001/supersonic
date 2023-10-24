package com.tencent.supersonic.common.util.jsqlparser;

import java.util.Map;
import java.util.Objects;
import lombok.extern.slf4j.Slf4j;
import net.sf.jsqlparser.expression.DoubleValue;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.ExpressionVisitorAdapter;
import net.sf.jsqlparser.expression.Function;
import net.sf.jsqlparser.expression.LongValue;
import net.sf.jsqlparser.expression.StringValue;
import net.sf.jsqlparser.expression.operators.relational.ComparisonOperator;
import net.sf.jsqlparser.expression.operators.relational.EqualsTo;
import net.sf.jsqlparser.expression.operators.relational.GreaterThan;
import net.sf.jsqlparser.expression.operators.relational.GreaterThanEquals;
import net.sf.jsqlparser.expression.operators.relational.MinorThan;
import net.sf.jsqlparser.expression.operators.relational.MinorThanEquals;
import net.sf.jsqlparser.schema.Column;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.CollectionUtils;

@Slf4j
public class FieldlValueReplaceVisitor extends ExpressionVisitorAdapter {

    ParseVisitorHelper parseVisitorHelper = new ParseVisitorHelper();
    private boolean exactReplace;
    private Map<String, Map<String, String>> filedNameToValueMap;

    public FieldlValueReplaceVisitor(boolean exactReplace, Map<String, Map<String, String>> filedNameToValueMap) {
        this.exactReplace = exactReplace;
        this.filedNameToValueMap = filedNameToValueMap;
    }

    @Override
    public void visit(EqualsTo expr) {
        replaceComparisonExpression(expr);
    }

    public void visit(GreaterThan expr) {
        replaceComparisonExpression(expr);
    }

    public void visit(GreaterThanEquals expr) {
        replaceComparisonExpression(expr);
    }

    public void visit(MinorThanEquals expr) {
        replaceComparisonExpression(expr);
    }

    public void visit(MinorThan expr) {
        replaceComparisonExpression(expr);
    }

    public <T extends Expression> void replaceComparisonExpression(T expression) {
        Expression leftExpression = ((ComparisonOperator) expression).getLeftExpression();
        Expression rightExpression = ((ComparisonOperator) expression).getRightExpression();

        if (!(leftExpression instanceof Column || leftExpression instanceof Function)) {
            return;
        }
        if (CollectionUtils.isEmpty(filedNameToValueMap)) {
            return;
        }
        if (Objects.isNull(rightExpression) || Objects.isNull(leftExpression)) {
            return;
        }
        String columnName = SqlParserSelectHelper.getColumnName(leftExpression);
        if (StringUtils.isEmpty(columnName)) {
            return;
        }
        Map<String, String> valueMap = filedNameToValueMap.get(columnName);
        if (Objects.isNull(valueMap) || valueMap.isEmpty()) {
            return;
        }
        if (rightExpression instanceof LongValue) {
            LongValue rightStringValue = (LongValue) rightExpression;
            String replaceValue = getReplaceValue(valueMap, String.valueOf(rightStringValue.getValue()));
            if (StringUtils.isNotEmpty(replaceValue)) {
                rightStringValue.setValue(Long.parseLong(replaceValue));
            }
        }
        if (rightExpression instanceof DoubleValue) {
            DoubleValue rightStringValue = (DoubleValue) rightExpression;
            String replaceValue = getReplaceValue(valueMap, String.valueOf(rightStringValue.getValue()));
            if (StringUtils.isNotEmpty(replaceValue)) {
                rightStringValue.setValue(Double.parseDouble(replaceValue));
            }
        }
        if (rightExpression instanceof StringValue) {
            StringValue rightStringValue = (StringValue) rightExpression;
            String replaceValue = getReplaceValue(valueMap, String.valueOf(rightStringValue.getValue()));
            if (StringUtils.isNotEmpty(replaceValue)) {
                rightStringValue.setValue(replaceValue);
            }
        }
    }

    private String getReplaceValue(Map<String, String> valueMap, String beforeValue) {
        String afterValue = valueMap.get(String.valueOf(beforeValue));
        if (StringUtils.isEmpty(afterValue) && !exactReplace) {
            return parseVisitorHelper.getReplaceValue(beforeValue, valueMap, false);
        }
        return afterValue;
    }
}
