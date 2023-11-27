package com.tencent.supersonic.common.util.xktime.utils;

import com.tencent.supersonic.common.util.xktime.formatter.DateTimeFormatterUtil;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.*;
import java.util.*;

public class DateParseUtil {

    private static final String[] DATE_ITEM = new String[]{"_month", "_day", "_year", "_quarter", "日期", "时间", "time", "date", "timestamp"};

    /**
     * 判断字符串是否是描述日期的
     *
     * @param str
     * @return
     */
    public static boolean isDateStr(String str) {
        str = str.toLowerCase();
        for (String item : DATE_ITEM) {
            if (str.contains(item)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 获取季度时间范围
     *
     * @param baseTime yyyy-MM-dd
     * @param offsetQuarter 根据baseTime前后几个
     * @return [0] startTime [1] endTime
     * 示例：
     * 获取上个季度的时间段：
     * List<LocalDateTime> timeList = getQuarterDateTime(null, -1)
     * LocalDateTime start = timeList[0];
     * LocalDateTime end = timeList[1];
     */
    public static List<LocalDateTime> getQuarterDateTime(String baseTime, Long offsetQuarter) {
        LocalDate baseDate;
        if (null == baseTime) {
            baseDate = LocalDate.now();
        } else {
            Date data = DateTimeFormatterUtil.parseToDate(baseTime, DateTimeFormatterUtil.YYYY_MM_DD_FMT);
            Calendar calendar = new GregorianCalendar();
            calendar.setTime(data);
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH)+1;
            int day = calendar.get(Calendar.DAY_OF_MONTH);
            baseDate = LocalDate.of(year, month, day);
        }

        // 当前月份减去 3 个月，即落在上个季度中
        Long offsetMonth = offsetQuarter * 3;
        LocalDate lastQuarterDate = baseDate;
        if (offsetMonth > 0) {
            lastQuarterDate = baseDate.plusMonths(offsetMonth);
        } else if (offsetMonth < 0) {
            lastQuarterDate = baseDate.minusMonths(-1 * offsetMonth);
        }
        // 通过 firstMonthOfQuarter 获取所在季度第一个月
        Month firstMonthOfQuarter = lastQuarterDate.getMonth().firstMonthOfQuarter();
        // 所在季度第一个月加上 2，即为所在季度最后一个月
        Month endMonthOfQuarter = Month.of(firstMonthOfQuarter.getValue() + 2);
        // 获得上面得到的季度第一个月的第一天
        LocalDate beginDate = LocalDate.of(lastQuarterDate.getYear(), firstMonthOfQuarter, 1);
        // 获得上面得到的季度最后一个月的最后一天
        LocalDate endDate = LocalDate.of(lastQuarterDate.getYear(), endMonthOfQuarter, endMonthOfQuarter.length(beginDate.isLeapYear()));
        // 获取季度开始时间
        LocalDateTime beginTime = LocalDateTime.of(beginDate, LocalTime.MIN);
        // 获取季度结束时间
        LocalDateTime endTime = LocalDateTime.of(endDate, LocalTime.MAX);
        List<LocalDateTime> timeList = new ArrayList<>();
        timeList.add(beginTime);
        timeList.add(endTime);
        return timeList;
    }

    /**
     * 获取周时间范围，返回的是一周时间
     *
     * @param baseTime yyyy-MM-dd
     * @param offsetWeek 根据baseTime前后几个周
     * @return [0] startTime [1] endTime
     * 示例：
     * 获取上周的时间段：
     * List<LocalDateTime> timeList = getWeekDateTime(null, -1)
     * LocalDateTime start = timeList[0];
     * LocalDateTime end = timeList[1];
     */
    public static List<LocalDateTime> getWeekDateTime(String baseTime, Integer offsetWeek) {
        LocalDate baseDate;
        if (null == baseTime) {
            baseDate = LocalDate.now();
        } else {
            Date data = DateTimeFormatterUtil.parseToDate(baseTime, DateTimeFormatterUtil.YYYY_MM_DD_FMT);
            Calendar calendar = new GregorianCalendar();
            calendar.setTime(data);
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH)+1;
            int day = calendar.get(Calendar.DAY_OF_MONTH);
            baseDate = LocalDate.of(year, month, day);
        }

        LocalDate lastWeekDate = baseDate;
        if (offsetWeek > 0) {
            lastWeekDate = baseDate.plusWeeks(offsetWeek);
        } else if (offsetWeek < 0) {
            lastWeekDate = baseDate.minusWeeks(-1 * offsetWeek);
        }
        // 通过 lastWeekDate 获取所在周第一个天
        TemporalField weekDayField = WeekFields.of(DayOfWeek.MONDAY, 1).dayOfWeek();
        LocalDate firstDay = lastWeekDate.with(weekDayField, 1);
        LocalDate lastDay = lastWeekDate.with(weekDayField, 7);
        // 获取周开始时间
        LocalDateTime beginTime = LocalDateTime.of(firstDay, LocalTime.MIN);
        // 获取周结束时间
        LocalDateTime endTime = LocalDateTime.of(lastDay, LocalTime.MAX);
        List<LocalDateTime> timeList = new ArrayList<>();
        timeList.add(beginTime);
        timeList.add(endTime);
        return timeList;
    }

    /**
     * 获取周时间范围，返回的是与baseTime间隔的范围时间
     *
     * @param baseTime yyyy-MM-dd
     * @param offsetWeek 根据baseTime前后几个周
     * @return [0] startTime [1] endTime
     * 示例：
     * 获取上周的时间段：
     * List<LocalDateTime> timeList = getWeekDateTimeSpan(null, -1)
     * LocalDateTime start = timeList[0];
     * LocalDateTime end = timeList[1];
     */
    public static List<LocalDateTime> getWeekDateTimeSpan(String baseTime, Integer offsetWeek) {
        LocalDate baseDate;
        if (null == baseTime) {
            baseDate = LocalDate.now();
        } else {
            Date data = DateTimeFormatterUtil.parseToDate(baseTime, DateTimeFormatterUtil.YYYY_MM_DD_FMT);
            Calendar calendar = new GregorianCalendar();
            calendar.setTime(data);
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH)+1;
            int day = calendar.get(Calendar.DAY_OF_MONTH);
            baseDate = LocalDate.of(year, month, day);
        }
        LocalDate startWeekDate = baseDate;
        LocalDate endWeekDate = baseDate;
        if (offsetWeek > 0) {
            endWeekDate = baseDate.plusWeeks(offsetWeek);
        } else if (offsetWeek < 0) {
            startWeekDate = baseDate.minusWeeks(-1 * offsetWeek);
        }
        // 通过 lastWeekDate 获取所在周第一个天
        TemporalField weekDayField = WeekFields.of(DayOfWeek.MONDAY, 1).dayOfWeek();
        LocalDate firstDay = startWeekDate.with(weekDayField, 1);
        LocalDate lastDay = endWeekDate.with(weekDayField, 7);
        // 获取周开始时间
        LocalDateTime beginTime = LocalDateTime.of(firstDay, LocalTime.MIN);
        // 获取周结束时间
        LocalDateTime endTime = LocalDateTime.of(lastDay, LocalTime.MAX);
        List<LocalDateTime> timeList = new ArrayList<>();
        timeList.add(beginTime);
        timeList.add(endTime);
        return timeList;
    }

    /**
     * 获取年时间范围，返回的是与baseTime间隔的范围时间
     *
     * @param baseTime yyyy-MM-dd
     * @param offsetYear 根据baseTime前后几个年
     * @return [0] startTime [1] endTime
     * 示例：
     * 获取上周的时间段：
     * List<LocalDateTime> timeList = getYearDateTimeSpan(null, -1)
     * LocalDateTime start = timeList[0];
     * LocalDateTime end = timeList[1];
     */
    public static List<LocalDateTime> getYearDateTimeSpan(String baseTime, Integer offsetYear) {
        LocalDate baseDate;
        if (null == baseTime) {
            baseDate = LocalDate.now();
        } else {
            Date data = DateTimeFormatterUtil.parseToDate(baseTime, DateTimeFormatterUtil.YYYY_MM_DD_FMT);
            Calendar calendar = new GregorianCalendar();
            calendar.setTime(data);
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH)+1;
            int day = calendar.get(Calendar.DAY_OF_MONTH);
            baseDate = LocalDate.of(year, month, day);
        }
        LocalDate startYearDate = baseDate;
        LocalDate endYearDate = baseDate;
        if (offsetYear > 0) {
            endYearDate = baseDate.plusYears(offsetYear);
        } else if (offsetYear < 0) {
            startYearDate = baseDate.minusYears(-1 * offsetYear);
        }
        // 通过 lastDayOfYear 获取所有在年最后一天和第一天
        LocalDate firstDay = startYearDate.with(TemporalAdjusters.firstDayOfYear());
        LocalDate lastDay = endYearDate.with(TemporalAdjusters.lastDayOfYear());
        // 获取年开始时间
        LocalDateTime beginTime = LocalDateTime.of(firstDay, LocalTime.MIN);
        // 获取年结束时间
        LocalDateTime endTime = LocalDateTime.of(lastDay, LocalTime.MAX);
        List<LocalDateTime> timeList = new ArrayList<>();
        timeList.add(beginTime);
        timeList.add(endTime);
        return timeList;
    }

    /**
     * 获取年时间范围，返回的是与baseTime间隔的范围时间
     *
     * @param baseTime yyyy-MM-dd
     * @param quarter 根据baseTime前后几个年
     * @return [0] startTime [1] endTime
     * 示例：
     * 获取上周的时间段：
     * List<LocalDateTime> timeList = getQuarterDateTimeSpan(null, 1)
     * LocalDateTime start = timeList[0];
     * LocalDateTime end = timeList[1];
     */
    public static List<LocalDateTime> getQuarterDateTimeSpan(String baseTime, Integer quarter) {
        LocalDate baseDate;
        if (null == baseTime) {
            baseDate = LocalDate.now();
        } else {
            Date data = DateTimeFormatterUtil.parseToDate(baseTime, DateTimeFormatterUtil.YYYY_MM_DD_FMT);
            Calendar calendar = new GregorianCalendar();
            calendar.setTime(data);
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH)+1;
            int day = calendar.get(Calendar.DAY_OF_MONTH);
            baseDate = LocalDate.of(year, month, day);
        }

        LocalDate startQuarterDate = baseDate.withMonth((quarter-1) * 3 + 1);
        LocalDate endQuarterDate = baseDate.withMonth((quarter-1) * 3 + 3);

        // 通过 lastWeekDate 获取所在季度第一天和最后一天
        LocalDate firstDay = startQuarterDate.with(TemporalAdjusters.firstDayOfMonth());
        LocalDate lastDay = endQuarterDate.with(TemporalAdjusters.lastDayOfMonth());
        // 获取年开始时间
        LocalDateTime beginTime = LocalDateTime.of(firstDay, LocalTime.MIN);
        // 获取年结束时间
        LocalDateTime endTime = LocalDateTime.of(lastDay, LocalTime.MAX);
        List<LocalDateTime> timeList = new ArrayList<>();
        timeList.add(beginTime);
        timeList.add(endTime);
        return timeList;
    }

    public static void main(String[] args) {
        // 上季度
//        List<LocalDateTime> timeList = getQuarterDateTime(null, -1L);
//        for (LocalDateTime item : timeList) {
//            System.out.println(DateTimeFormatterUtil.format(item, DateTimeFormatterUtil.YYYY_MM_DD_HH_MM_SS_FMT));
//        }

        // 上周
//        List<LocalDateTime> timeList = getWeekDateTime(null, -1);
//        for (LocalDateTime item : timeList) {
//            System.out.println(DateTimeFormatterUtil.format(item, DateTimeFormatterUtil.YYYY_MM_DD_HH_MM_SS_FMT));
//        }

//        // 近两周
//        List<LocalDateTime> timeList = getWeekDateTimeSpan(null, -1);
//        for (LocalDateTime item : timeList) {
//            System.out.println(DateTimeFormatterUtil.format(item, DateTimeFormatterUtil.YYYY_MM_DD_HH_MM_SS_FMT));
//        }

        // 近2年
//        List<LocalDateTime> timeList = getYearDateTimeSpan(null, -2);
//        for (LocalDateTime item : timeList) {
//            System.out.println(DateTimeFormatterUtil.format(item, DateTimeFormatterUtil.YYYY_MM_DD_HH_MM_SS_FMT));
//        }

        // 今年第一季度
        List<LocalDateTime> timeList = getQuarterDateTimeSpan("2022-01-01", 3);
        for (LocalDateTime item : timeList) {
            System.out.println(DateTimeFormatterUtil.format(item, DateTimeFormatterUtil.YYYY_MM_DD_HH_MM_SS_FMT));
        }
    }
}
