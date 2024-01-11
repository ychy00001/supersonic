package com.tencent.supersonic.chat.processor.parse;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import com.tencent.supersonic.chat.api.pojo.ChatContext;
import com.tencent.supersonic.chat.api.pojo.QueryContext;
import com.tencent.supersonic.chat.api.pojo.request.PageQueryInfoReq;
import com.tencent.supersonic.chat.api.pojo.response.ParseResp;
import com.tencent.supersonic.chat.api.pojo.response.QueryResp;
import com.tencent.supersonic.chat.api.pojo.response.SimilarQueryRecallResp;
import com.tencent.supersonic.chat.persistence.dataobject.ChatQueryDO;
import com.tencent.supersonic.chat.persistence.repository.ChatQueryRepository;
import com.tencent.supersonic.chat.utils.SimilarQueryManager;
import com.tencent.supersonic.common.util.ContextUtils;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

/**
 * MetricRecommendProcessor fills recommended query based on embedding similarity.
 */
@Slf4j
public class QueryRecommendProcessor implements ParseResultProcessor {

    @Override
    public void process(ParseResp parseResp, QueryContext queryContext, ChatContext chatContext) {
        CompletableFuture.runAsync(() -> doProcess(parseResp, queryContext));
    }

    @SneakyThrows
    private void doProcess(ParseResp parseResp, QueryContext queryContext) {
        Long queryId = parseResp.getQueryId();
        List<SimilarQueryRecallResp> solvedQueries = getSimilarQueries(queryContext.getRequest().getQueryText(),
                queryContext.getRequest().getAgentId());
        ChatQueryDO chatQueryDO = getChatQuery(queryId);
        chatQueryDO.setSimilarQueries(JSONObject.toJSONString(solvedQueries));
        updateChatQuery(chatQueryDO);
    }

    public List<SimilarQueryRecallResp> getSimilarQueries(String queryText, Integer agentId) {
        //1. recall solved query by queryText
        SimilarQueryManager solvedQueryManager = ContextUtils.getBean(SimilarQueryManager.class);
        List<SimilarQueryRecallResp> similarQueries = solvedQueryManager.recallSimilarQuery(queryText, agentId);
        if (CollectionUtils.isEmpty(similarQueries)) {
            return Lists.newArrayList();
        }
        //2. remove low score query
        List<Long> queryIds = similarQueries.stream()
                .map(SimilarQueryRecallResp::getQueryId).collect(Collectors.toList());
        int lowScoreThreshold = 3;
        List<QueryResp> queryResps = getChatQuery(queryIds);
        if (CollectionUtils.isEmpty(queryResps)) {
            return Lists.newArrayList();
        }
        Set<Long> lowScoreQueryIds = queryResps.stream().filter(queryResp ->
                        queryResp.getScore() != null && queryResp.getScore() <= lowScoreThreshold)
                .map(QueryResp::getQuestionId).collect(Collectors.toSet());
        return similarQueries.stream().filter(solvedQueryRecallResp ->
                        !lowScoreQueryIds.contains(solvedQueryRecallResp.getQueryId()))
                .collect(Collectors.toList());
    }

    private ChatQueryDO getChatQuery(Long queryId) {
        ChatQueryRepository chatQueryRepository = ContextUtils.getBean(ChatQueryRepository.class);
        return chatQueryRepository.getChatQueryDO(queryId);
    }

    private List<QueryResp> getChatQuery(List<Long> queryIds) {
        ChatQueryRepository chatQueryRepository = ContextUtils.getBean(ChatQueryRepository.class);
        PageQueryInfoReq pageQueryInfoReq = new PageQueryInfoReq();
        pageQueryInfoReq.setIds(queryIds);
        pageQueryInfoReq.setPageSize(100);
        pageQueryInfoReq.setCurrent(1);
        PageInfo<QueryResp> queryRespPageInfo = chatQueryRepository.getChatQuery(pageQueryInfoReq, null);
        return queryRespPageInfo.getList();
    }

    private void updateChatQuery(ChatQueryDO chatQueryDO) {
        ChatQueryRepository chatQueryRepository = ContextUtils.getBean(ChatQueryRepository.class);
        chatQueryRepository.updateChatQuery(chatQueryDO);
    }

}
