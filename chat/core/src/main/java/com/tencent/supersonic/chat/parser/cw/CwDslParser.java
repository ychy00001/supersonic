package com.tencent.supersonic.chat.parser.cw;

import com.tencent.supersonic.chat.agent.NL2SQLTool;
import com.tencent.supersonic.chat.api.component.SemanticParser;
import com.tencent.supersonic.chat.api.pojo.*;
import com.tencent.supersonic.chat.api.pojo.request.QueryReq;
import com.tencent.supersonic.chat.parser.sql.llm.LLMSqlParser;
import com.tencent.supersonic.chat.service.SemanticService;
import com.tencent.supersonic.common.pojo.*;
import com.tencent.supersonic.common.util.ContextUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.util.*;

@Slf4j
public class CwDslParser implements SemanticParser {

    @Override
    public void parse(QueryContext queryCtx, ChatContext chatCtx) {

        QueryReq request = queryCtx.getRequest();
        CwRequestService requestService = ContextUtils.getBean(CwRequestService.class);
        SemanticService semanticService = ContextUtils.getBean(SemanticService.class);
        //1.determine whether to skip this parser.
        if (requestService.isSkip(queryCtx)) {
            return;
        }
        try {
            //2.get modelId from queryCtx and chatCtx.
            ModelCluster modelCluster = requestService.getModelCluster(queryCtx, chatCtx, request.getAgentId());
            if (StringUtils.isBlank(modelCluster.getKey())) {
                return;
            }
            //3.get agent tool and determine whether to skip this parser.
            NL2SQLTool commonAgentTool = requestService.getParserTool(request, modelCluster.getModelIds());
            if (Objects.isNull(commonAgentTool)) {
                log.info("no tool in this agent, skip {}", LLMSqlParser.class);
                return;
            }
            //4.construct a request, call the API for the large model, and retrieve the results.
            List<CwReq.ElementValue> linkingValues = requestService.getValueList(queryCtx, modelCluster);
            SemanticSchema semanticSchema = semanticService.getSemanticSchema();

            CwReq llmReq = requestService.getLlmReq(queryCtx, semanticSchema, modelCluster, linkingValues);
            CwResp llmResp = requestService.requestLLM(llmReq);

            if (Objects.isNull(llmResp)) {
                return;
            }
            //5. build parserInfo
            modelCluster.buildName(semanticSchema.getModelIdToName());
            CwResponseService responseService = ContextUtils.getBean(CwResponseService.class);
            // 构造一个独立的CwLLM解析器返回的全部信息
            CwParseResult parseResult = CwParseResult.builder()
                    .modelCluster(modelCluster)
                    .request(request)
                    .cwReq(llmReq)
                    .cwResp(llmResp)
                    .commonAgentTool(commonAgentTool)
                    .linkingValues(linkingValues)
                    .build();

            responseService.addParseInfo(queryCtx, parseResult, llmResp);
        } catch (Exception e) {
            log.error("parse", e);
        }
    }
}
