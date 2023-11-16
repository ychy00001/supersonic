package com.tencent.supersonic.chat.parser.llm.cw;

public class MySqlFuncUtil {
    public static final String YEAR_FUNC_FMT = "YEAR(%s)";
    public static final String MONTH_FUNC_FMT = "DATE_FORMAT(%s, '%%Y-%%m')";
    public static final String DAY_FUNC_FMT = "DATE_FORMAT(%s, '%%Y-%%m-%%d')";
    public static final String QUARTER_FUNC_FMT = "QUARTER(%s)";
    // 0 模式 ，一周的第一天是周日  1模式1周的第一天是周一
    public static final String WEEK_FUNC_FMT = "WEEK(%s,1)";
    public static final String AVG_FUNC = "AVG";
}
