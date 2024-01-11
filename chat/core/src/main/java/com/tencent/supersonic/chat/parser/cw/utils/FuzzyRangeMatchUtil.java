package com.tencent.supersonic.chat.parser.cw.utils;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Data
public class FuzzyRangeMatchUtil {

    public static List<FuzzyRangeItem> fuzzyRangeAnalysis(String analysisStr){
        List<FuzzyRangeItem> result = new ArrayList<>();
        for (FuzzyRangeEnum item: FuzzyRangeEnum.values()){
            Pattern pattern = Pattern.compile(item.getRegex());
            Matcher matcher = pattern.matcher(analysisStr);
            if(matcher.find()){
                String matcherVal = matcher.group();
                // 移除前后引号
                FuzzyRangeItem matchItem = new FuzzyRangeItem();
                matchItem.setType(item);
                matchItem.setOriginStr(analysisStr);
                matchItem.setMatchValue(removeQuote(matcherVal));
                result.add(matchItem);
            }
        }
        return result;
    }

    public static String removeQuote(String str){
        Pattern pattern = Pattern.compile("(?<=[’‘“”\"']).*(?=[’‘“”\"'])");
        Matcher matcher = pattern.matcher(str);
        if(matcher.find()){
            return matcher.group();
        }
        return str;
    }
    /**
     * 将聚合数据格式化为实际数据
     * 目前仅支持：数字+单位的处理，不支持一百万，三千五百万类似的处理，后续可以增加判断如果字符串中不存在数字，走纯文字转数字匹配
     * @param fuzzyValue 100万
     * @return 1000000
     */
    public static String fuzzyMetricValueFormat(String fuzzyValue){
        for (FuzzyValueEnum item: FuzzyValueEnum.values()){
            Pattern pattern = Pattern.compile(item.getRegex());
            Matcher matcher = pattern.matcher(fuzzyValue);
            if(matcher.find()){
                return item.getReasonableValue(Double.valueOf(matcher.group()));
            }
        }
        return fuzzyValue;
    }

}
