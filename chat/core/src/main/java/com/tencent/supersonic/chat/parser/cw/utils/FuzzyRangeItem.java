package com.tencent.supersonic.chat.parser.cw.utils;

import com.tencent.supersonic.common.pojo.enums.FilterOperatorEnum;
import lombok.Data;

@Data
public class FuzzyRangeItem {
    /**
     * 示例：末尾是A
     */
    private String originStr;
    /**
     * 示例：LEFT_LIKE
     */
    private FuzzyRangeEnum type;

    /**
     * A
     */
    private String matchValue;

    /**
     * %A
     */
    private String reasonableValue;

    public String getReasonableValue(){
        return type.getReasonableValue(this.matchValue);
    }

    public FilterOperatorEnum getFilterOperator() {
        switch (this.type) {
            case LEFT_LIKE:
            case RIGHT_LIKE:
            case FULL_LIKE:
                return FilterOperatorEnum.LIKE;
            case LEFT_NOT_LIKE:
            case RIGHT_NOT_LIKE:
            case FULL_NOT_LIKE:
                return FilterOperatorEnum.NOT_LIKE;
            case GREATER_THEN:
                return FilterOperatorEnum.GREATER_THAN;
            case GREATER_EQUAL_THEN:
                return FilterOperatorEnum.GREATER_THAN_EQUALS;
            case LESS_THEN:
                return FilterOperatorEnum.MINOR_THAN;
            case LESS_EQUAL_THEN:
                return FilterOperatorEnum.MINOR_THAN_EQUALS;
            default:
                return null;
        }
    }
}
