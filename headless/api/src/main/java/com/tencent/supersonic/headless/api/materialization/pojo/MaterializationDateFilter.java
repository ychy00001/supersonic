package com.tencent.supersonic.headless.api.materialization.pojo;

import lombok.Data;

import java.util.Date;
import java.util.List;


@Data
public class MaterializationDateFilter {

    private Long modelId;
    private List<String> dimensions;
    private List<String> metrics;
    private Date createdAt;
}