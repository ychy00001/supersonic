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

    /**
     *
     * @param v1
     * @param v2
     * @return  value 0 if v1 equal to v2; less than 0 if v1 is less than v2; greater than 0 if v1 is greater than v2
     */
    public static int compareVersion(String v1, String v2) {
        String[] v1s = v1.split("\\.");
        String[] v2s = v2.split("\\.");
        int length = Math.min(v1s.length, v2s.length);
        for (int i = 0; i < length; i++) {
            Integer vv1 = Integer.parseInt(v1s[i]);
            Integer vv2 = Integer.parseInt(v2s[i]);
            int compare = vv1.compareTo(vv2);
            if (compare != 0) {
                return compare;
            }
        }
        return v1s.length - v2s.length;
    }

}