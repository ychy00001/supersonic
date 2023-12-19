package com.tencent.supersonic.auth.api.authorization.response;

import com.tencent.supersonic.auth.api.authorization.enmus.AuthObjTypeEnum;
import com.tencent.supersonic.auth.api.authorization.pojo.AuthResGrp;
import com.tencent.supersonic.auth.api.authorization.pojo.DimensionFilter;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class AuthObjectResp {

    private AuthObjTypeEnum type;

    private List<String> objIds;
}
