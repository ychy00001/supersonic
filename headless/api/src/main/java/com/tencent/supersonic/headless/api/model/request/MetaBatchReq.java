package com.tencent.supersonic.headless.api.model.request;

import lombok.Data;
import java.util.List;

@Data
public class MetaBatchReq {

    private List<Long> ids;

    private Integer status;

}
