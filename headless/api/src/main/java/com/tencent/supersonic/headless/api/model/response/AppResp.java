package com.tencent.supersonic.headless.api.model.response;


import com.google.common.collect.Lists;
import com.tencent.supersonic.common.pojo.RecordInfo;
import com.tencent.supersonic.headless.api.model.enums.AppStatusEnum;
import com.tencent.supersonic.headless.api.model.pojo.AppConfig;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Data
public class AppResp extends RecordInfo {

    private Long id;

    private String name;

    private String description;

    private AppStatusEnum appStatus;

    private AppConfig config;

    private Date endDate;

    private Integer qps;

    private List<String> owners;

    private boolean hasAdminRes;

    public void setOwner(String owner) {
        if (StringUtils.isBlank(owner)) {
            owners = Lists.newArrayList();
            return;
        }
        owners = Arrays.asList(owner.split(","));
    }

}
