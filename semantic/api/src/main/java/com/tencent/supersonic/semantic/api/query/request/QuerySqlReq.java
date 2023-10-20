package com.tencent.supersonic.semantic.api.query.request;

import lombok.Data;
import lombok.ToString;

import java.util.Map;

@Data
@ToString
public class QuerySqlReq {

    private Long modelId;

    private String sql;

    private String sourceId;

    private Map<String, String> variables;

}
