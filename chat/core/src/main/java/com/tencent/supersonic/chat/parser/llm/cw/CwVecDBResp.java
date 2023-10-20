package com.tencent.supersonic.chat.parser.llm.cw;

import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * 用于接收向量数据库响应的结果
 */
@Data
public class CwVecDBResp {

    private Integer code;
    private String message;
    private List<SimilaritySearchItem> data;

    @Data
    public static class SimilaritySearchItem {
        /**
         * 原始数据
         */
        private String origin_name;
        /**
         * 匹配到的相似数据
         */
        private List<MatchInfo> search;
    }

    @Data
    public static class MatchInfo {
        /**
         * 名称
         */
        private String page_content;
        /**
         * 匹配的文本元数据
         */
        private Metadata metadata;
    }

    @Data
    public static class Metadata {
        /**
         * 数据的类型（DIMENSION、METRIC。。。）
         */
        private String item_type;
        private Long domain_id;
        private Long model_id;
        private Long metric_id;
        private Long dimension_id;
        /**
         * 对应数据库表的字段名称
         */
        private String biz_name;
    }
}
