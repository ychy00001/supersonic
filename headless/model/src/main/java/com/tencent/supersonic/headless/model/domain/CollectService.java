package com.tencent.supersonic.headless.model.domain;

import com.tencent.supersonic.auth.api.authentication.pojo.User;
import com.tencent.supersonic.headless.model.domain.dataobject.CollectDO;

import java.util.List;


/**
 * @author yannsu
 */

public interface CollectService {

    Boolean createCollectionIndicators(User user, Long id);

    Boolean deleteCollectionIndicators(User user, Long id);

    List<CollectDO> getCollectList(String username);

}
