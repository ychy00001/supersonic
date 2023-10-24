package com.tencent.supersonic.common.util;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


@Slf4j
public class StringUtil {

    public static final String COMMA_WRAPPER = "'%s'";
    public static final String SPACE_WRAPPER = " %s ";

    public static String getCommaWrap(String value) {
        return String.format(COMMA_WRAPPER, value);
    }

    public static String getSpaceWrap(String value) {
        return String.format(SPACE_WRAPPER, value);
    }

    /**
     * 字符串是否包含中文
     *
     * @param str 待校验字符串
     * @return true 包含中文字符 false 不包含中文字符
     * @throws Exception
     */
    public static boolean isContainChinese(String str) throws Exception {

        if (StringUtils.isEmpty(str)) {
            throw new Exception("sms context is empty!");
        }
        Pattern p = Pattern.compile("[\u4E00-\u9FA5|\\！|\\，|\\。|\\（|\\）|\\《|\\》|\\“|\\”|\\？|\\：|\\；|\\【|\\】]");
        Matcher m = p.matcher(str);
        if (m.find()) {
            return true;
        }
        return false;
    }

    public static String formatSqlQuota(String where) {
        if (StringUtils.isEmpty(where)) {
            return where;
        }
        return where.replace("\"", "\\\\\"");
    }

}