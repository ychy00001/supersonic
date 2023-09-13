package com.tencent.supersonic.common.util.jsqlparser;

import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import net.sf.jsqlparser.expression.ExpressionVisitorAdapter;
import net.sf.jsqlparser.schema.Column;

@Slf4j
public class FieldReplaceVisitor extends ExpressionVisitorAdapter {

    ParseVisitorHelper parseVisitorHelper = new ParseVisitorHelper();
    private Map<String, String> fieldToBizName;
    private boolean exactReplace;

    public FieldReplaceVisitor(Map<String, String> fieldToBizName, boolean exactReplace) {
        this.fieldToBizName = fieldToBizName;
        this.exactReplace = exactReplace;
    }

    @Override
    public void visit(Column column) {
        parseVisitorHelper.replaceColumn(column, fieldToBizName, exactReplace);
    }
}