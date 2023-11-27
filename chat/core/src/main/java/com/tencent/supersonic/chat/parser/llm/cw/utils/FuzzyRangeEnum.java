package com.tencent.supersonic.chat.parser.llm.cw.utils;

import com.tencent.supersonic.semantic.api.query.enums.FilterOperatorEnum;

public enum FuzzyRangeEnum {
    /**
     * LIKE A%
     * LIKE %A
     * LIKE %A%
     */
    LEFT_LIKE("((?<=开头是).*|(?<=以).+(?=开头))", "%s%%"),
    RIGHT_LIKE("((?<=(末|结)尾是).+(?=的)|(?<=(末|结)尾是).+|(?<=以).+(?=结尾))", "%%%s"),
    FULL_LIKE("(?<=(存在|包含)).+", "%%%s%%"),

    /**
     * NOT LIKE A%
     * NOT LIKE %A
     * NOT LIKE %A%
     */
    LEFT_NOT_LIKE("((?<=开头不是).*|(?<=不以).+(?=开头))", "%s%%"),
    RIGHT_NOT_LIKE("((?<=(末|结)尾不是).+(?=的)|(?<=(末|结)尾不是).+|(?<=不是?以).+(?=结尾))", "%%%s"),
    FULL_NOT_LIKE("(?<=(不存在|不包含)).+", "%%%s%%"),

    /**
     * > A
     * >= A
     * < A
     * <= A
     */
    GREATER_THEN("((?<=(大于|超过)).*|(?<=>).+|(?<=比).+(?=大))", "%s"),
    GREATER_EQUAL_THEN("((?<=大于等于).+|(?<=>=).+)", "%s"),
    LESS_THEN("((?<=小于).*|(?<=<).+|(?<=比).+(?=小))", "%s"),
    LESS_EQUAL_THEN("((?<=小于等于).+|(?<=<=).+)", "%s");

    private final String regex;
    private final String valueFormat;

    FuzzyRangeEnum(String regex, String valueFormat) {
        this.regex = regex;
        this.valueFormat = valueFormat;
    }

    public String getRegex() {
        return this.regex;
    }

    public String getReasonableValue(String originValue) {
        return String.format(valueFormat, originValue);
    }
}
