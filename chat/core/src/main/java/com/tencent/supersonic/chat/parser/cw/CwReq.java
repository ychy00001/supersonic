package com.tencent.supersonic.chat.parser.cw;

import com.tencent.supersonic.chat.parser.cw.param.LlmDslParam;
import lombok.Data;

import java.util.List;

@Data
public class CwReq {

    private String queryText;

    private CwSchema schema;

    private List<ElementValue> linking;

    private String currentDate;

    private LlmDslParam requestBody;

    @Data
    public static class ElementValue {

        private String fieldName;

        private String fieldValue;

    }

    @Data
    public static class CwSchema {

        private String domainName;

        private String modelName;

        private List<String> fieldNameList;

    }
}
