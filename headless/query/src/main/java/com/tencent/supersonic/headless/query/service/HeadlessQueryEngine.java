package com.tencent.supersonic.headless.query.service;

import com.tencent.supersonic.headless.api.model.response.QueryResultWithSchemaResp;
import com.tencent.supersonic.headless.api.query.request.MetricReq;
import com.tencent.supersonic.headless.api.query.request.ParseSqlReq;
import com.tencent.supersonic.headless.api.query.request.QueryStructReq;
import com.tencent.supersonic.headless.query.persistence.pojo.QueryStatement;
import com.tencent.supersonic.headless.query.executor.QueryExecutor;

public interface HeadlessQueryEngine {

    QueryStatement plan(QueryStatement queryStatement) throws Exception;

    QueryStatement simplePlan(QueryStatement queryStatement) throws Exception;

    QueryExecutor route(QueryStatement queryStatement);

    QueryResultWithSchemaResp execute(QueryStatement queryStatement);

    QueryStatement physicalSql(QueryStructReq queryStructCmd, ParseSqlReq sqlCommend) throws Exception;

    QueryStatement physicalSql(QueryStructReq queryStructCmd, MetricReq sqlCommend) throws Exception;
}
