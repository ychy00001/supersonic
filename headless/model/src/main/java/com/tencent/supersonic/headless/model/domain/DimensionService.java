package com.tencent.supersonic.headless.model.domain;

import com.github.pagehelper.PageInfo;
import com.tencent.supersonic.auth.api.authentication.pojo.User;
import com.tencent.supersonic.common.pojo.DataItem;
import com.tencent.supersonic.common.pojo.enums.EventType;
import com.tencent.supersonic.headless.api.model.pojo.DimValueMap;
import com.tencent.supersonic.headless.api.model.request.DimensionReq;
import com.tencent.supersonic.headless.api.model.request.MetaBatchReq;
import com.tencent.supersonic.headless.api.model.request.PageDimensionReq;
import com.tencent.supersonic.headless.api.model.response.DimensionResp;
import com.tencent.supersonic.headless.model.domain.pojo.MetaFilter;

import java.util.List;

public interface DimensionService {

    List<DimensionResp> getDimensions(MetaFilter metaFilter);

    DimensionResp getDimension(String bizName, Long modelId);

    void batchUpdateStatus(MetaBatchReq metaBatchReq, User user);

    void createDimension(DimensionReq dimensionReq, User user) throws Exception;

    void createDimensionBatch(List<DimensionReq> dimensionReqs, User user) throws Exception;

    void updateDimension(DimensionReq dimensionReq, User user) throws Exception;

    PageInfo<DimensionResp> queryDimension(PageDimensionReq pageDimensionReq);

    void deleteDimension(Long id, User user);

    List<DimensionResp> getDimensionInModelCluster(Long modelId);

    List<DataItem> getDataItems(Long modelId);

    List<String> mockAlias(DimensionReq dimensionReq, String mockType, User user);

    List<DimValueMap> mockDimensionValueAlias(DimensionReq dimensionReq, User user);

    void sendDimensionEventBatch(List<Long> modelIds, EventType eventType);
}
