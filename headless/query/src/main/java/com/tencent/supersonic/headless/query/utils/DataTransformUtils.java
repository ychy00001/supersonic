package com.tencent.supersonic.headless.query.utils;

import com.google.common.collect.Lists;
import com.tencent.supersonic.common.pojo.Constants;
import com.tencent.supersonic.common.pojo.DateConf;
import com.tencent.supersonic.common.pojo.enums.TimeDimensionEnum;
import org.apache.commons.lang3.StringUtils;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class DataTransformUtils {

    public static List<Map<String, Object>> transform(List<Map<String, Object>> originalData, String metric,
                                                      List<String> groups, DateConf dateConf) {
        List<String> dateList = dateConf.getDateList();
        List<Map<String, Object>> transposedData = new ArrayList<>();
        for (Map<String, Object> originalRow : originalData) {
            Map<String, Object> transposedRow = new HashMap<>();
            for (String key : originalRow.keySet()) {
                if (groups.contains(key)) {
                    transposedRow.put(key, originalRow.get(key));
                }
            }
            transposedRow.put(String.valueOf(originalRow.get(getTimeDimension(dateConf))),
                    originalRow.get(metric));
            transposedData.add(transposedRow);
        }
        Map<String, List<Map<String, Object>>> dataMerge = transposedData.stream()
                .collect(Collectors.groupingBy(row -> getRowKey(row, groups)));
        List<Map<String, Object>> resultData = Lists.newArrayList();
        for (List<Map<String, Object>> data : dataMerge.values()) {
            Map<String, Object> rowData = new HashMap<>();
            for (Map<String, Object> row : data) {
                for (String key : row.keySet()) {
                    rowData.put(key, row.get(key));
                }
            }
            for (String date : dateList) {
                if (!rowData.containsKey(date)) {
                    rowData.put(date, "");
                }
            }
            resultData.add(rowData);
        }
        return resultData;
    }

    private static String getRowKey(Map<String, Object> originalRow, List<String> groups) {
        List<Object> values = Lists.newArrayList();
        for (String key : originalRow.keySet()) {
            if (groups.contains(key) && !TimeDimensionEnum.getNameList().contains(key)) {
                values.add(originalRow.get(key));
            }
        }
        return StringUtils.join(values, "_");
    }

    private static String getTimeDimension(DateConf dateConf) {
        if (Constants.MONTH.equals(dateConf.getPeriod())) {
            return TimeDimensionEnum.MONTH.getName();
        } else if (Constants.WEEK.equals(dateConf.getPeriod())) {
            return TimeDimensionEnum.WEEK.getName();
        } else {
            return TimeDimensionEnum.DAY.getName();
        }
    }

}
