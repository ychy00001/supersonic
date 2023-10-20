package com.tencent.supersonic.common.util.xktime.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DateCharParseUtil {

    String[] dataCharList = {"年", "月", "日", "季度", "周"};

    /**
     * 获取指定字符串出现的次数
     *
     * @param srcText 源字符串
     * @param findText 要查找的字符串
     * @return
     */
    public static int appearNumber(String srcText, String findText) {
        int count = 0;
        Pattern p = Pattern.compile(findText);
        Matcher m = p.matcher(srcText);
        while (m.find()) {
            count++;
        }
        return count;
    }
}
