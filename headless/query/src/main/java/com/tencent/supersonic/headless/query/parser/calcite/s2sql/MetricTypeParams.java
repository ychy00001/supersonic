package com.tencent.supersonic.headless.query.parser.calcite.s2sql;

import java.util.List;
import lombok.Data;

@Data
public class MetricTypeParams {

    private List<Measure> measures;

    private String expr;

}
