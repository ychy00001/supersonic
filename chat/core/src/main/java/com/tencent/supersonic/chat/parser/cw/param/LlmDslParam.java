package com.tencent.supersonic.chat.parser.cw.param;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.beans.ConstructorProperties;
import java.io.Serializable;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class LlmDslParam implements Serializable {
    private Boolean allowTruncation = false;
    private List<MessageItem> messages;
    private Parameter parameters;
    private String assistantPrefix = "";
    private String userPrefix = "";

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @ToString
    public static class MessageItem{
        private String role;
        private String content;
    }

    @ToString
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Parameter{
        private Boolean do_sample = false;
        private Integer max_new_tokens = 900;
        private Double repetition_penalty = 1.03;
        private String[] stop = new String[]{"</s>", "User"};
        private Double temperature = 0.1;
        private Integer top_k = 50;
        private Double top_p = 0.95;
    }
}
