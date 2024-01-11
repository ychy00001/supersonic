package com.tencent.supersonic.headless.api.model.pojo;

import java.util.List;
import com.google.common.collect.Lists;
import lombok.Data;

@Data
public class MetricTypeParams {

    private List<Measure> measures = Lists.newArrayList();

    private String expr;

}
