package com.tencent.supersonic.chat.parser.llm.cw;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CwResp {

    /**
     * 需要查询的字符串
     */
    private String originQueryText;

    /**
     * 最终可以执行的正确sql
     * 这个不是模型返回的，而是方便给后续的execute执行传递的参数
     * 非模型返回字段
     */
    private String correctorSql;

    /**
     * 最终正确sql的SourceId
     */
    private String sqlSourceId;

    /**
     * 通过语义模型转换的sql
     * 非模型返回字段
     */
    private String buildSql;

    /**
     * "{
     *     ""Entity"": [
     *         ""前年每个季度"",
     *         ""平均"",
     *         ""销售价""
     *     ],
     *     ""Dimension"": [
     *         ""销售日期""
     *     ],
     *     ""Filters"": {""销售日期"":""前年""},
     *     ""Metrics"": [
     *         ""实际售价""
     *     ],
     *     ""Operator"": ""求均值"",
     *     ""Groupby"": [""销售日期按季度""]
     * }"
     */
    private List<String> Entity;
    private List<String> Dimension;
    private Map<String,String> Filters;
    private List<String> Metrics;
    private String Operator;
    private List<String> Groupby;
}
