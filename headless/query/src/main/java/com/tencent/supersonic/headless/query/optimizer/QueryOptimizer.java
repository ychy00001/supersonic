package com.tencent.supersonic.headless.query.optimizer;

import com.tencent.supersonic.headless.api.query.request.QueryStructReq;
import com.tencent.supersonic.headless.query.persistence.pojo.QueryStatement;

public interface QueryOptimizer {
    void rewrite(QueryStructReq queryStructCmd, QueryStatement queryStatement);
}
