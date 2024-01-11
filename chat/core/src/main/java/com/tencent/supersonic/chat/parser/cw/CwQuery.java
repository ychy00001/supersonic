package com.tencent.supersonic.chat.parser.cw;

import com.tencent.supersonic.auth.api.authentication.pojo.User;
import com.tencent.supersonic.chat.api.component.SemanticInterpreter;
import com.tencent.supersonic.chat.api.pojo.response.EntityInfo;
import com.tencent.supersonic.chat.api.pojo.response.QueryResult;
import com.tencent.supersonic.chat.api.pojo.response.QueryState;
import com.tencent.supersonic.chat.api.pojo.response.SqlInfo;
import com.tencent.supersonic.chat.query.QueryManager;
import com.tencent.supersonic.chat.query.llm.LLMSemanticQuery;
import com.tencent.supersonic.chat.service.SemanticService;
import com.tencent.supersonic.chat.utils.ComponentFactory;
import com.tencent.supersonic.chat.utils.QueryReqBuilder;
import com.tencent.supersonic.common.pojo.Constants;
import com.tencent.supersonic.common.pojo.QueryColumn;
import com.tencent.supersonic.common.util.ContextUtils;
import com.tencent.supersonic.common.util.JsonUtil;
import com.tencent.supersonic.headless.api.model.response.QueryResultWithSchemaResp;
import com.tencent.supersonic.headless.api.query.request.QuerySqlReq;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Slf4j
@Component
public class CwQuery extends LLMSemanticQuery {

    public static final String QUERY_MODE = "CW_QUERY";
    protected SemanticInterpreter semanticLayer = ComponentFactory.getSemanticLayer();

    public CwQuery() {
        QueryManager.register(this);
    }

    @Override
    public String getQueryMode() {
        return QUERY_MODE;
    }

    @Override
    public QueryResult execute(User user) {
        String json = JsonUtil.toString(parseInfo.getProperties().get(Constants.CONTEXT));
        CwParseResult cwParseResult = JsonUtil.toObject(json, CwParseResult.class);
        CwResp cwResp = cwParseResult.getCwResp();

        long startTime = System.currentTimeMillis();
        QuerySqlReq querySqlReq = QueryReqBuilder.buildPureSqlReq(cwResp.getCorrectorSql(), parseInfo.getModel().getModelIds(), cwResp.getSqlSourceId());
        QueryResultWithSchemaResp queryResp = semanticLayer.queryBySql(querySqlReq, user);

        log.info("queryByDsl cost:{},querySql:{}", System.currentTimeMillis() - startTime, cwResp.getCorrectorSql());

        QueryResult queryResult = new QueryResult();
        if (Objects.nonNull(queryResp)) {
            queryResult.setQueryAuthorization(queryResp.getQueryAuthorization());
        }
        String resultQql = queryResp == null ? null : queryResp.getSql();
        List<Map<String, Object>> resultList = queryResp == null ? new ArrayList<>() : queryResp.getResultList();
        List<QueryColumn> columns = queryResp == null ? new ArrayList<>() : queryResp.getColumns();
        queryResult.setQuerySql(resultQql);
        queryResult.setQueryResults(resultList);
        queryResult.setQueryColumns(columns);
        queryResult.setQueryMode(QUERY_MODE);
        queryResult.setQueryState(QueryState.SUCCESS);

        // add model info
        EntityInfo entityInfo = ContextUtils.getBean(SemanticService.class).getEntityInfo(parseInfo, user);
        queryResult.setEntityInfo(entityInfo);
        parseInfo.setProperties(null);
        return queryResult;
    }

    @Override
    public void initS2Sql(User user) {
        SqlInfo sqlInfo = parseInfo.getSqlInfo();
        sqlInfo.setCorrectS2SQL(sqlInfo.getS2SQL());
    }
}
