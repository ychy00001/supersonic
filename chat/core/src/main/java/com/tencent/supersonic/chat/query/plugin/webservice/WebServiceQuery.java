package com.tencent.supersonic.chat.query.plugin.webservice;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.ImmutableMap;
import com.tencent.supersonic.auth.api.authentication.pojo.User;
import com.tencent.supersonic.chat.api.pojo.response.QueryResult;
import com.tencent.supersonic.chat.api.pojo.response.QueryState;
import com.tencent.supersonic.chat.plugin.Plugin;
import com.tencent.supersonic.chat.plugin.PluginParseResult;
import com.tencent.supersonic.chat.query.QueryManager;
import com.tencent.supersonic.chat.query.plugin.ParamOption;
import com.tencent.supersonic.chat.query.plugin.PluginSemanticQuery;
import com.tencent.supersonic.chat.query.plugin.WebBase;
import com.tencent.supersonic.common.pojo.Constants;
import com.tencent.supersonic.common.pojo.QueryColumn;
import com.tencent.supersonic.common.util.JsonUtil;
import com.tencent.supersonic.common.util.ContextUtils;

import java.net.URI;
import java.util.*;
import java.util.stream.Collectors;

import lombok.extern.slf4j.Slf4j;
import org.apache.calcite.sql.parser.SqlParseException;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;


@Slf4j
@Component
public class WebServiceQuery extends PluginSemanticQuery {

    public static String[] DOUBLE_ELEVEN_PROMPT_LIST = new String[]{
            "(( big '11'  and 'SALE' text caption  on the center of  poster)),Double 11 shopping extravaganza, platform wide promotion, stress on discounted goods, unique items specifics, elaborate and rich poster background, emphasize noticeable traits, should be sans English letters, no depiction of humans.,no human images,<lora:add-detail-xl:1>, (photoshoot realistic:1.5), 24mm, 4k, DSLR, high quality, 60 fps, ultra realistic"
    };
    public static String[] CHRISTMAS_PROMPT_LIST = new String[]{
            "TEST"
    };

    public static String QUERY_MODE = "WEB_SERVICE";

    private RestTemplate restTemplate;

    public WebServiceQuery() {
        QueryManager.register(this);
    }

    @Override
    public String getQueryMode() {
        return QUERY_MODE;
    }

    @Override
    public QueryResult execute(User user) throws SqlParseException {
        QueryResult queryResult = new QueryResult();
        queryResult.setQueryMode(QUERY_MODE);
        Map<String, Object> properties = parseInfo.getProperties();
        PluginParseResult pluginParseResult = JsonUtil.toObject(
                JsonUtil.toString(properties.get(Constants.CONTEXT)), PluginParseResult.class);
        WebServiceResponse webServiceResponse;
        if (pluginParseResult.getPlugin().getName().contains("海报")) {
            webServiceResponse = buildResponseWithPoster(pluginParseResult);
            queryResult.setResultType("IMAGE");
        } else if (pluginParseResult.getPlugin().getName().contains("写作")) {
            webServiceResponse = buildResponseWithWriter(pluginParseResult);
            queryResult.setResultType("TEXT");
        } else {
            webServiceResponse = buildResponse(pluginParseResult);
        }
        Object object = webServiceResponse.getResult();
        // in order to show webServiceQuery result int frontend conveniently,
        // webServiceResponse result format is consistent with queryByStruct result.
//        log.info("webServiceResponse result:{}", JsonUtil.toString(object));
        try {
            if(!queryResult.getResultType().equals("IMAGE")
                    && !queryResult.getResultType().equals("TEXT")){
                Map<String, Object> data = JsonUtil.toMap(JsonUtil.toString(object), String.class, Object.class);
                queryResult.setQueryResults(data.containsKey("resultList") ? (List<Map<String, Object>>) data.get("resultList") : null);
                queryResult.setQueryColumns(data.containsKey("resultList") ? (List<QueryColumn>) data.get("columns") : null);
            }
            queryResult.setResponse(webServiceResponse);
            queryResult.setQueryState(QueryState.SUCCESS);
        } catch (Exception e) {
            log.info("webServiceResponse result has an exception:{}", e.getMessage());
        }
        return queryResult;
    }

    /**
     * 海报接口处理
     */
    private WebServiceResponse buildResponseWithPoster(PluginParseResult pluginParseResult) {
        WebServiceResponse webServiceResponse = new WebServiceResponse();
        Plugin plugin = pluginParseResult.getPlugin();
        WebBase webBase = JsonUtil.toObject(plugin.getConfig(), WebBase.class);
        webServiceResponse.setWebBase(webBase);
        List<ParamOption> paramOptions = webBase.getParamOptions();
        Map<String, Object> params = constructPosterApiParam(pluginParseResult.getRequest().getQueryText());
        // 根据界面配置复制部分参数
        paramOptions.forEach(o -> {
            if (o.getParamType().equals(ParamOption.ParamType.CUSTOM)) {
                params.put(o.getKey(), o.getValue());
            }
        });
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> entity = new HttpEntity<>(JSON.toJSONString(params), headers);
        URI requestUrl = UriComponentsBuilder.fromHttpUrl(webBase.getUrl()).build().encode().toUri();
        ResponseEntity responseEntity = null;
        Object objectResponse = null;
        restTemplate = ContextUtils.getBean(RestTemplate.class);
        try {
            responseEntity = restTemplate.exchange(requestUrl, HttpMethod.POST, entity, Object.class);
            objectResponse = responseEntity.getBody();
            log.debug("objectResponse:{}", objectResponse);
            Map<String, Object> response = JsonUtil.objectToMap(objectResponse);
            if (response.containsKey("images")) {
                List<String> images = (List<String>) response.get("images");
                List<String> withPrefixImages = images.stream().map(item -> "data:image/png;base64," + item).collect(Collectors.toList());
                webServiceResponse.setResult(withPrefixImages);
            }
        } catch (Exception e) {
            log.error("Exception:{}", e.getMessage());
        }
        return webServiceResponse;
    }

    /**
     * 公文写作接口处理
     */
    private WebServiceResponse buildResponseWithWriter(PluginParseResult pluginParseResult) {
        WebServiceResponse webServiceResponse = new WebServiceResponse();
        Plugin plugin = pluginParseResult.getPlugin();
        WebBase webBase = JsonUtil.toObject(plugin.getConfig(), WebBase.class);
        webServiceResponse.setWebBase(webBase);
        List<ParamOption> paramOptions = webBase.getParamOptions();
        Map<String, Object> params = constructWriterApiParam(pluginParseResult.getRequest().getQueryText());
        // 根据界面配置复制部分参数
        paramOptions.forEach(o -> {
            if (o.getParamType().equals(ParamOption.ParamType.CUSTOM)) {
                params.put(o.getKey(), o.getValue());
            }
        });
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> entity = new HttpEntity<>(JSON.toJSONString(params), headers);
        URI requestUrl = UriComponentsBuilder.fromHttpUrl(webBase.getUrl()).build().encode().toUri();
        ResponseEntity responseEntity = null;
        Object objectResponse = null;
        restTemplate = ContextUtils.getBean(RestTemplate.class);
        try {
            responseEntity = restTemplate.exchange(requestUrl, HttpMethod.POST, entity, Object.class);
            objectResponse = responseEntity.getBody();
            log.debug("objectResponse:{}", objectResponse);
            Map<String, Object> response = JsonUtil.objectToMap(objectResponse);
            // TODO 获取结果字段名
            if (response.containsKey("xxxxx")) {
                String result = (String) response.get("xxx");
                webServiceResponse.setResult(result);
            }
        } catch (Exception e) {
            log.error("Exception:{}", e.getMessage());
        }
        return webServiceResponse;
    }

    /**
     * 获取写作接口参数
     */
    private Map<String, Object> constructWriterApiParam(String queryText) {
        String[] prompts = DOUBLE_ELEVEN_PROMPT_LIST;
        if (queryText.contains("圣诞")) {
            prompts = CHRISTMAS_PROMPT_LIST;
        } else if (queryText.contains("双十一")) {
            prompts = DOUBLE_ELEVEN_PROMPT_LIST;
        }
        Random r = new Random();
        int randIndex = prompts.length > 1 ? r.nextInt(prompts.length - 1) : 0;
        Map<String, Object> result = new HashMap<>();
        result.put("prompt", prompts[randIndex]);
        result.put("negative_prompt", "(with alphabets:1.5), (with hand:1.5), (with fingers:1.5), (with face:1.5), (with people:1.5),(with person:1.5),(with human:1.5), (distorted keyboard:1.5), bad X, negativeXL_D, unaestheticXL_Sky3.1,ac_neg1,, bad X, negativeXL_D, unaestheticXL_Sky3.1,ac_neg1,(with text:1.5)");
        Map<String, Object> overrideSettingItem = new HashMap<>();
        overrideSettingItem.put("sd_model_checkpoint", "dreamshaperXL10_alpha2Xl10.safetensors [0f1b80cfe8]");
        result.put("override_settings", overrideSettingItem);
        result.put("seed", -1);
        result.put("subseed", -1);
        result.put("sampler_index", "DPM++ 2M Karras");
        result.put("batch_size", 4);
        result.put("n_iter", 1);
        result.put("steps", 20);
        result.put("cfg_scale", 7);
        result.put("width", 768);
        result.put("height", 1024);
        result.put("hr_scale", 1);
        result.put("hr_upscaler", "Latent");
        result.put("denoising_strength", 0.7);
        return result;
    }

    /**
     * 构建海报生成查询参数
     */
    private Map<String, Object> constructPosterApiParam(String queryText) {
        String[] prompts = DOUBLE_ELEVEN_PROMPT_LIST;
        if (queryText.contains("圣诞")) {
            prompts = CHRISTMAS_PROMPT_LIST;
        } else if (queryText.contains("双十一")) {
            prompts = DOUBLE_ELEVEN_PROMPT_LIST;
        }
        Random r = new Random();
        int randIndex = prompts.length > 1 ? r.nextInt(prompts.length - 1) : 0;
        Map<String, Object> result = new HashMap<>();
        result.put("prompt", prompts[randIndex]);
        result.put("negative_prompt", "(with alphabets:1.5), (with hand:1.5), (with fingers:1.5), (with face:1.5), (with people:1.5),(with person:1.5),(with human:1.5), (distorted keyboard:1.5), bad X, negativeXL_D, unaestheticXL_Sky3.1,ac_neg1,, bad X, negativeXL_D, unaestheticXL_Sky3.1,ac_neg1,(with text:1.5)");
        Map<String, Object> overrideSettingItem = new HashMap<>();
        overrideSettingItem.put("sd_model_checkpoint", "dreamshaperXL10_alpha2Xl10.safetensors [0f1b80cfe8]");
        result.put("override_settings", overrideSettingItem);
        result.put("seed", -1);
        result.put("subseed", -1);
        result.put("sampler_index", "DPM++ 2M Karras");
        result.put("batch_size", 4);
        result.put("n_iter", 1);
        result.put("steps", 20);
        result.put("cfg_scale", 7);
        result.put("width", 768);
        result.put("height", 1024);
        result.put("hr_scale", 1);
        result.put("hr_upscaler", "Latent");
        result.put("denoising_strength", 0.7);
        return result;
    }

    protected WebServiceResponse buildResponse(PluginParseResult pluginParseResult) {
        WebServiceResponse webServiceResponse = new WebServiceResponse();
        Plugin plugin = pluginParseResult.getPlugin();
        WebBase webBase = JsonUtil.toObject(plugin.getConfig(), WebBase.class);
        webServiceResponse.setWebBase(webBase);
        List<ParamOption> paramOptions = webBase.getParamOptions();
        Map<String, Object> params = new HashMap<>();
        paramOptions.forEach(o -> params.put(o.getKey(), o.getValue()));
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> entity = new HttpEntity<>(JSON.toJSONString(params), headers);
        URI requestUrl = UriComponentsBuilder.fromHttpUrl(webBase.getUrl()).build().encode().toUri();
        ResponseEntity responseEntity = null;
        Object objectResponse = null;
        restTemplate = ContextUtils.getBean(RestTemplate.class);
        try {
            responseEntity = restTemplate.exchange(requestUrl, HttpMethod.POST, entity, Object.class);
            objectResponse = responseEntity.getBody();
            log.info("objectResponse:{}", objectResponse);
            Map<String, Object> response = JsonUtil.objectToMap(objectResponse);
            webServiceResponse.setResult(response);
        } catch (Exception e) {
            log.info("Exception:{}", e.getMessage());
        }
        return webServiceResponse;
    }

}
