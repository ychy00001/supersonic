package com.tencent.supersonic.headless.api.query.request;

import com.tencent.supersonic.common.pojo.DateConf;
import com.tencent.supersonic.headless.api.model.pojo.Item;
import lombok.Data;

@Data
public class QueryApiPreviewReq {

    private Item item;

    private DateConf dateConf = new DateConf();

}
