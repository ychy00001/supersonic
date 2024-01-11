package com.tencent.supersonic.headless.api.model.yaml;


import java.util.List;
import lombok.Data;


@Data
public class MetricYamlTpl {

    private String name;

    private List<String> owners;

    private String type;

    private MetricTypeParamsYamlTpl typeParams;


}
