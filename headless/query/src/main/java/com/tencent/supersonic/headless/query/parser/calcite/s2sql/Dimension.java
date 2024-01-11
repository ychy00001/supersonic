package com.tencent.supersonic.headless.query.parser.calcite.s2sql;


import com.tencent.supersonic.headless.query.parser.calcite.schema.SemanticItem;
import lombok.Builder;
import lombok.Data;


@Data
@Builder
public class Dimension implements SemanticItem {

    String name;
    private String owners;
    private String type;
    private String expr;
    private DimensionTimeTypeParams dimensionTimeTypeParams;

    private DataType dataType = DataType.UNKNOWN;

    @Override
    public String getName() {
        return name;
    }
}
