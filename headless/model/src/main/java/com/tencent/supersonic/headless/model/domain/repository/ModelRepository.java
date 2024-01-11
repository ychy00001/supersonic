package com.tencent.supersonic.headless.model.domain.repository;


import com.tencent.supersonic.headless.model.domain.dataobject.ModelDO;
import com.tencent.supersonic.headless.model.domain.pojo.ModelFilter;

import java.util.List;

public interface ModelRepository {

    void createModel(ModelDO modelDO);

    void updateModel(ModelDO modelDO);

    List<ModelDO> getModelList(ModelFilter modelFilter);

    ModelDO getModelById(Long id);

    void batchUpdate(List<ModelDO> modelDOS);
}
