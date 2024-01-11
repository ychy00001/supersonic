package com.tencent.supersonic.headless.query.parser.calcite.s2sql;

import java.util.List;
import lombok.Builder;
import lombok.Data;
import org.apache.commons.lang3.tuple.Triple;

@Data
@Builder
public class JoinRelation {

    private String left;
    private String right;
    private String joinType;
    private List<Triple<String, String, String>> joinCondition;

}
