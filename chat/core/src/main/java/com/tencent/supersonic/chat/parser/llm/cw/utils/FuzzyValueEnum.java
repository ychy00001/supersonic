package com.tencent.supersonic.chat.parser.llm.cw.utils;

public enum FuzzyValueEnum {

    HUNDRED("\\d+(?=百(?!(万|亿)))", 100d),
    THOUSAND("\\d+(?=千(?!(万|亿)))", 1000d),
    TEN_THOUSAND("\\d+(?=万(?!亿))", 10000d),
    HUNDRED_MILLION("\\d+(?=亿)", 100000000d);

    private final String regex;
    private final Double unit;

    FuzzyValueEnum(String regex, Double unit) {
        this.regex = regex;
        this.unit = unit;
    }

    public String getRegex() {
        return this.regex;
    }

    public String getReasonableValue(Double originValue) {
        return String.valueOf(originValue * unit);
    }
}
