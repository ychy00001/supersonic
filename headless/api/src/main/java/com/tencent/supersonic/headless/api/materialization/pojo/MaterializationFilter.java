package com.tencent.supersonic.headless.api.materialization.pojo;

import com.tencent.supersonic.headless.api.model.enums.ModelSourceTypeEnum;
import com.tencent.supersonic.headless.api.materialization.enums.UpdateCycleEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MaterializationFilter {

    private Long materializationId;
    private String name;
    private ModelSourceTypeEnum materializedType;
    private UpdateCycleEnum updateCycle;
    private Long modelId;
    private Long databaseId;
    private Integer level;
    private String createdBy;
    private String destinationTable;
}