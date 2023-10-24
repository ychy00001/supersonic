package com.tencent.supersonic.semantic.query.service;

import com.tencent.supersonic.auth.api.authentication.pojo.User;
import com.tencent.supersonic.semantic.api.model.response.ExplainResp;
import com.tencent.supersonic.semantic.api.model.response.QueryResultWithSchemaResp;
import com.tencent.supersonic.semantic.api.query.request.*;
import com.tencent.supersonic.semantic.api.query.response.ItemUseResp;
import com.tencent.supersonic.semantic.query.persistence.pojo.QueryStatement;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

public interface QueryService {

    Object queryBySql(QueryS2QLReq querySqlCmd, User user) throws Exception;

    Object queryByPureSql(QuerySqlReq querySqlCmd, User user) throws Exception;

    QueryResultWithSchemaResp queryByStruct(QueryStructReq queryStructCmd, User user) throws Exception;

    void downloadByStruct(QueryStructReq queryStructReq, User user, HttpServletResponse response) throws Exception;

    QueryResultWithSchemaResp queryByStructWithAuth(QueryStructReq queryStructCmd, User user)
            throws Exception;

    QueryResultWithSchemaResp queryByMultiStruct(QueryMultiStructReq queryMultiStructCmd, User user) throws Exception;


    QueryResultWithSchemaResp queryDimValue(QueryDimValueReq queryDimValueReq, User user);

    Object queryByQueryStatement(QueryStatement queryStatement);

    List<ItemUseResp> getStatInfo(ItemUseReq itemUseCommend);

    <T> ExplainResp explain(ExplainSqlReq<T> explainSqlReq, User user) throws Exception;

    QueryStatement parseMetricReq(MetricReq metricReq) throws Exception;


}
