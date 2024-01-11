package com.tencent.supersonic.chat.parser.cw;

import lombok.Data;

import java.util.List;

/**
 * 用于接收向量数据库响应的结果
 */
@Data
public class CwFuncDBResp {

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
        private String func_name;
        private String func_format;
        private Integer arg_count;
        private String func_content;
    }
}
