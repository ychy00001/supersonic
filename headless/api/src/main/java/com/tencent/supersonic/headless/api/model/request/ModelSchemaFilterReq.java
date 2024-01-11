package com.tencent.supersonic.headless.api.model.request;

import lombok.Data;

import java.util.List;

@Data
public class ModelSchemaFilterReq {

    /**
     * if domainIds is empty, get all domain info
     */
    private List<Long> modelIds;
}