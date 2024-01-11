package com.tencent.supersonic.headless.api.model.response;


import com.google.common.collect.Lists;
import com.tencent.supersonic.common.pojo.QueryAuthorization;
import com.tencent.supersonic.common.pojo.QueryColumn;
import com.tencent.supersonic.headless.api.model.enums.SemanticTypeEnum;
import com.tencent.supersonic.headless.api.model.pojo.QueryResult;
import lombok.Data;
import lombok.ToString;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Data
@ToString
public class QueryResultWithSchemaResp extends QueryResult<Map<String, Object>> {

    List<QueryColumn> columns = Lists.newArrayList();
    String sql;
    QueryAuthorization queryAuthorization;

    public List<QueryColumn> getMetricColumns() {
        return columns.stream()
                .filter(queryColumn -> SemanticTypeEnum.NUMBER.name().equals(queryColumn.getShowType()))
                .collect(Collectors.toList());
    }

    public List<QueryColumn> getDimensionColumns() {
        return columns.stream()
                .filter(queryColumn -> !SemanticTypeEnum.NUMBER.name().equals(queryColumn.getShowType()))
                .collect(Collectors.toList());
    }
}
