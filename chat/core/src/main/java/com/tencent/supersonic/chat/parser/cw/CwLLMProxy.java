package com.tencent.supersonic.chat.parser.cw;

import com.alibaba.fastjson.JSON;
import com.tencent.supersonic.chat.api.component.SemanticInterpreter;
import com.tencent.supersonic.chat.api.pojo.ModelSchema;
import com.tencent.supersonic.chat.api.pojo.QueryContext;
import com.tencent.supersonic.chat.config.LLMParserConfig;
import com.tencent.supersonic.chat.parser.LLMProxy;
import com.tencent.supersonic.chat.parser.cw.param.LlmDslParam;
import com.tencent.supersonic.chat.parser.plugin.function.FunctionCallConfig;
import com.tencent.supersonic.chat.parser.plugin.function.FunctionReq;
import com.tencent.supersonic.chat.parser.plugin.function.FunctionResp;
import com.tencent.supersonic.chat.query.llm.s2sql.LLMReq;
import com.tencent.supersonic.chat.query.llm.s2sql.LLMResp;
import com.tencent.supersonic.chat.utils.ComponentFactory;
import com.tencent.supersonic.common.util.ContextUtils;
import com.tencent.supersonic.common.util.JsonUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.net.URL;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * CwLLMProxy sends requests to CW-based python service.
 */
@Slf4j
@Component
public class CwLLMProxy {

    private static final Logger keyPipelineLog = LoggerFactory.getLogger("keyPipeline");

    public boolean isSkip(QueryContext queryContext) {
        CwParserConfig llmParserConfig = ContextUtils.getBean(CwParserConfig.class);
        if (StringUtils.isEmpty(llmParserConfig.getUrl())) {
            log.warn("llmParserUrl is empty, skip :{}", CwLLMProxy.class.getName());
            return true;
        }
        return false;
    }

    public CwResp query2sql(CwReq cwReq) {
        CwParserConfig llmParserConfig = ContextUtils.getBean(CwParserConfig.class);
        String questUrl = llmParserConfig.getQueryToDslPath();
        long startTime = System.currentTimeMillis();
        log.info("requestLLM request, cwReq:{}", cwReq);
        RestTemplate restTemplate = ContextUtils.getBean(RestTemplate.class);
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<String> entity = new HttpEntity<>(JsonUtil.toString(cwReq.getRequestBody()), headers);
            ResponseEntity responseEntity = restTemplate.exchange(questUrl, HttpMethod.POST, entity,
                    Object.class);
            Object objectResponse = responseEntity.getBody();
            log.debug("cw llm parser objectResponse:{}", objectResponse);
            Map<String, Object> response = JsonUtil.objectToMap(objectResponse);
            CwResp cwResp;
            if (response.containsKey("generated_text")) {
                String dslJson = response.get("generated_text").toString();
                cwResp = JsonUtil.toObject(dslJson, CwResp.class);
                cwResp.setOriginQueryText(cwReq.getQueryText());
                cwResp.setJsonDsl(dslJson);
            } else {
                throw new RuntimeException("query result err , can't find generated_text");
            }
            log.info("requestLLM response,cost:{}, questUrl:{} \n entity:{} \n body:{}",
                    System.currentTimeMillis() - startTime, questUrl, entity, responseEntity.getBody());
            return cwResp;
        } catch (Exception e) {
            log.error("requestLLM error", e);
        }
        return null;
    }

    public FunctionResp requestFunction(FunctionReq functionReq) {
        FunctionCallConfig functionCallInfoConfig = ContextUtils.getBean(FunctionCallConfig.class);
        String url = functionCallInfoConfig.getUrl() + functionCallInfoConfig.getPluginSelectPath();
        HttpHeaders headers = new HttpHeaders();
        long startTime = System.currentTimeMillis();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> entity = new HttpEntity<>(JSON.toJSONString(functionReq), headers);
        URI requestUrl = UriComponentsBuilder.fromHttpUrl(url).build().encode().toUri();
        RestTemplate restTemplate = ContextUtils.getBean(RestTemplate.class);
        try {
            log.info("requestFunction functionReq:{}", JsonUtil.toString(functionReq));
            keyPipelineLog.info("requestFunction functionReq:{}", JsonUtil.toString(functionReq));
            ResponseEntity<FunctionResp> responseEntity = restTemplate.exchange(requestUrl, HttpMethod.POST, entity,
                    FunctionResp.class);
            log.info("requestFunction responseEntity:{},cost:{}", responseEntity,
                    System.currentTimeMillis() - startTime);
            keyPipelineLog.info("response:{}", responseEntity.getBody());
            return responseEntity.getBody();
        } catch (Exception e) {
            log.error("requestFunction error", e);
        }
        return null;
    }
}
