package com.tencent.supersonic.headless.model.domain.pojo;

import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;


@Data
@NoArgsConstructor
public class MetaFilter {

    private String id;

    private String name;

    private String bizName;

    private String createdBy;

    private List<Long> modelIds;

    private Integer sensitiveLevel;

    private Integer status;

    private String key;

    private List<Long> ids;

    public MetaFilter(List<Long> modelIds) {
        this.modelIds = modelIds;
    }
}
