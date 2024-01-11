package com.tencent.supersonic.common.pojo.enums;

public enum AggregateTypeEnum {
    SUM,
    AVG,
    MAX,
    MIN,
    TOPN,
    DISTINCT,
    COUNT,
    // 环比
    RATIO_ROLL,
    // 同比
    RATIO_OVER,
    NONE;

    public static AggregateTypeEnum of(String agg) {
        for (AggregateTypeEnum aggEnum : AggregateTypeEnum.values()) {
            if (aggEnum.name().equalsIgnoreCase(agg)) {
                return aggEnum;
            }
        }
        return AggregateTypeEnum.NONE;
    }
}
