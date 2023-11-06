package com.tencent.supersonic.chat.parser.llm.cw;

import com.google.common.collect.ImmutableMap;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class MockLLMResp {

    /**
     * 1. 帮我查询前年每个季度的平均销售价是多少？
     * "{
     * ""Entity"": [
     * ""前年每个季度"",
     * ""平均"",
     * ""销售价""
     * ],
     * ""Dimension"": [
     * ""销售日期""
     * ],
     * ""Filters"": {""销售日期"":""前年""},
     * ""Metrics"": [
     * ""实际售价""
     * ],
     * ""Operator"": ""求均值"",
     * ""Groupby"": [""销售日期按季度""]
     * }"
     */
    public static final CwResp query1 = CwResp.builder()
            .originQueryText("帮我查询前年每个季度的平均销售价是多少？")
            .Entity(Stream.of("前年每个季度", "平均", "销售价").collect(Collectors.toList()))
            .Dimension(Stream.of("销售日期").collect(Collectors.toList()))
            .Filters(ImmutableMap.of("销售日期", "前年"))
            .Metrics(Stream.of("销售价格").collect(Collectors.toList()))
            .Operator("求平均值")
            .Groupby(Stream.of("销售日期按季度").collect(Collectors.toList()))
            .build();

    /**
     * 2. 按销售渠道统计，今年哪个销售渠道的销售额最高？
     * "{
     * "Entity": [
     * "今年",
     * "销售渠道",
     * "销售额"
     * ],
     * "Dimension": [
     * "销售点名称"
     * ],
     * "Filters": {"销售日期":"今年"},
     * "Metrics": [
     * "销售金额"
     * ],
     * "Operator": "最高",
     * "Groupby": ["销售渠道名称"]
     * }"
     */
    public static final CwResp query2 = CwResp.builder()
            .originQueryText("按销售渠道统计，今年哪个销售渠道的销售额最高？")
            .Entity(Stream.of("今年", "销售渠道", "销售额").collect(Collectors.toList()))
            .Dimension(Stream.of("销售渠道名称").collect(Collectors.toList()))
            .Filters(ImmutableMap.of("销售日期", "今年"))
            .Metrics(Stream.of("销售价格").collect(Collectors.toList()))
            .Operator("最高")
            .Groupby(Stream.of("销售渠道名称").collect(Collectors.toList()))
            .build();

    /**
     * 3. 上个季度哪些车型的销售额最高？
     * {
     * "Entity": [
     * "上个季度",
     * "车型"",
     * "销售额最高"
     * ],
     * "Dimension": [
     * "销售日期",
     * "车型名称"
     * ],
     * "Filters": {"销售日期":"上个季度"},
     * "Metrics": [
     * "销售金额"
     * ],
     * "Operator": "最高",
     * "Groupby": ["车型"]
     * }
     */
    public static final CwResp query3 = CwResp.builder()
            .originQueryText("上个季度哪些车型的销售额最高？")
            .Entity(Stream.of("上个季度", "车型", "销售额").collect(Collectors.toList()))
            .Dimension(Stream.of("销售日期", "车型名称").collect(Collectors.toList()))
            .Filters(ImmutableMap.of("销售日期", "上个季度"))
            .Metrics(Stream.of("销售价格").collect(Collectors.toList()))
            .Operator("最高")
            .Groupby(Stream.of("车辆类型").collect(Collectors.toList()))
            .build();

    /**
     * 4. 购买SUV汽车的客户中，哪种职业类型人最多？
     * {
     * "Entity": [
     * "职业类型",
     * "SUV汽车",
     * "销售数量"
     * ],
     * "Dimension": [
     * "职业类型",
     * "车辆类型"
     * ],
     * "Filters": {
     * "车辆类型":"SUV汽车"
     * },
     * "Metrics": [
     * "销售数"
     * ],
     * "Operator": "最高",
     * "Groupby": ["职业类型"]
     * }
     */
    public static final CwResp query4 = CwResp.builder()
            .originQueryText("购买SUV汽车的客户中，哪种职业类型人最多？")
            .Entity(Stream.of("职业类型", "SUV汽车", "销售数量").collect(Collectors.toList()))
            .Dimension(Stream.of("职业", "车辆类型").collect(Collectors.toList()))
            .Filters(ImmutableMap.of("车辆类型", "SUV汽车"))
            .Metrics(Stream.of("销售数").collect(Collectors.toList()))
            .Operator("最高")
            .Groupby(Stream.of("职业").collect(Collectors.toList()))
            .build();

    /**
     * 5. 上个月各地区的销售额月环比增长率是多少？
     * {
     * "Entity": [
     * "各地区",
     * "上个月",
     * "销售金额",
     * "月环比增长率"
     * ],
     * "Dimension": [
     * "区域名称",
     * "销售日期"
     * ],
     * "Filters": {
     * "销售日期":"上个月"
     * },
     * "Metrics": [
     * "销售金额"
     * ],
     * "Operator": "环比增长率",
     * "Groupby": ["区域名称","销售日期按月"]
     * }
     */
    public static final CwResp query5 = CwResp.builder()
            .originQueryText("上个月各地区的销售额月环比增长率是多少？")
            .Entity(Stream.of("各地区", "上个月", "销售金额", "月环比增长率").collect(Collectors.toList()))
            .Dimension(Stream.of("地区", "销售日期").collect(Collectors.toList()))
            .Filters(ImmutableMap.of("销售日期", "上个月"))
            .Metrics(Stream.of("销售价格").collect(Collectors.toList()))
            .Operator("环比增长率")
            .Groupby(Stream.of("地区", "销售日期按月").collect(Collectors.toList()))
            .build();

    /**
     * 附加图表多曲线. 去年各职业按月统计销售总额是多少？
     * {
     * "Entity": [
     * "各地区",
     * "今年",
     * "销售总额"
     * ],
     * "Dimension": [
     * "区域名称",
     * "销售日期"
     * ],
     * "Filters": {
     * "销售日期":"去年"
     * },
     * "Metrics": [
     * "销售金额"
     * ],
     * "Operator": "求和",
     * "Groupby": ["区域名称","销售日期按月"]
     * }
     */
    public static final CwResp query_f1 = CwResp.builder()
            .originQueryText("去年各地区按月统计销售总额是多少？")
            .Entity(Stream.of("各地区", "去年", "销售金额", "求和").collect(Collectors.toList()))
            .Dimension(Stream.of("地区", "销售日期").collect(Collectors.toList()))
            .Filters(ImmutableMap.of("销售日期", "去年"))
            .Metrics(Stream.of("销售价格").collect(Collectors.toList()))
            .Operator("求和")
            .Groupby(Stream.of("地区", "销售日期按月").collect(Collectors.toList()))
            .build();

    public static Map<String, CwResp> queryMap = new HashMap<String, CwResp>() {
        {
            put(query1.getOriginQueryText(), query1);
            put(query2.getOriginQueryText(), query2);
            put(query3.getOriginQueryText(), query3);
            put(query4.getOriginQueryText(), query4);
            put(query5.getOriginQueryText(), query5);
            put(query_f1.getOriginQueryText(), query_f1);
        }
    };
}