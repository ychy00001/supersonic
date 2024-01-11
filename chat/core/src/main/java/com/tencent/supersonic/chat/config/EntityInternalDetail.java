package com.tencent.supersonic.chat.config;


import com.tencent.supersonic.headless.api.model.response.DimSchemaResp;
import com.tencent.supersonic.headless.api.model.response.MetricSchemaResp;
import java.util.List;
import lombok.Data;

@Data
public class EntityInternalDetail {

    List<DimSchemaResp> dimensionList;
    List<MetricSchemaResp> metricList;
}