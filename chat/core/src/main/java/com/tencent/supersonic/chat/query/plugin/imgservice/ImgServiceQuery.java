package com.tencent.supersonic.chat.query.plugin.imgservice;

import com.alibaba.fastjson.JSON;
import com.tencent.supersonic.auth.api.authentication.pojo.User;
import com.tencent.supersonic.chat.api.pojo.request.QueryFilter;
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

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.util.*;
import java.util.stream.Collectors;

import com.tencent.supersonic.semantic.api.query.enums.FilterOperatorEnum;
import lombok.extern.slf4j.Slf4j;
import org.apache.calcite.sql.parser.SqlParseException;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.hadoop.util.hash.Hash;
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
public class ImgServiceQuery extends PluginSemanticQuery {

    public static String IMG_PROMPT_URL = "http://10.178.13.110:15501/chat";
    public static String IMG_PROMPT_TEMPLATE = "# Role:\n" +
            "你是AI绘画工具Stable Diffusion的提示词高手。\n" +
            "\n" +
            "## Background:\n" +
            "你擅长从用户输入的关键词或一句话内容中，丰富的生成一段图片描述，我会把你的创意直接使用AI绘画工具如stable diffusion进行AI绘画创作。\n" +
            "\n" +
            "## Goals:\n" +
            "1. 根据Background、Skills、Subject、Input、Constrains、Workflows等描述。请自由发挥你的想象力，不仅限上述信息，可以根据你的理解，设计一个充满创意的画面感很强的图片描述\n" +
            "\n" +
            "## Constrains:\n" +
            "1. 图片中不要有任何口号、标语、店名和文字\n" +
            "2. 图片描述的内容中必须基于用户的Input，另外需要增加详细的设计元素，例如背景、颜色、主要物体、布局、场景等；\n" +
            "3. 输出内容必须要包含Input中的内容\n" +
            "4. 输出的图片描述内容要简短，不超过30个tokens\n" +
            "5. 输出的描述可以是单词或短句，中间用逗号隔开\n" +
            "6. 不要生成关于情感、味觉、声音听觉相关描述、不要有职业、色情等涉及隐私的描述\n" +
            "7. 只需要生成与视觉相关的描述\n" +
            "8. 必须用英文回答\n" +
            "\n" +
            "## Skills:\n" +
            "你具备以下能力：\n" +
            "1. 根据指定的行业主题Subject，生成的图片描述需要包含画面主体、画面场景、构图方式、画面背景等描述。\n" +
            "2. 画面背景描述：设定周围的场景和辅助元素，比如天空的颜色、周围的物品、环境灯光、画面色调等视觉相关的描述，这一步是为了渲染画面氛围，凸显图片的主题。\n" +
            "3. 构图方式描述：主要用来调节画面的镜头和视角，比如强调景深，物体位置、黄金分割构图、中全景、景深等\n" +
            "4. 图片描述的画面要整体和谐，不能与给定的主题冲突\n" +
            "\n" +
            "## Workflows:\n" +
            "* Input：输入相关的关键字或短语内容\n" +
            "* Output：根据Input输入内容输出简短的图片描述，可以用关键字、短句来描述，不要超过30个tokens\n" +
            "\n" +
            "## Examples:\n" +
            "* Subject:餐饮\n" +
            "* Input: 早餐、杯子、牛奶和冰块，不要有人物描述\n" +
            "* Output:A cozy breakfast scene,transparent plastic cup full of brown milk tea with ice cubes, placed on a wooden table with a bright yellow background,no humans.\n" +
            "以上Examples仅做参考，不要重复输出相同内容\n" +
            "\n" +
            "## Subject:\n" +
            "\n" +
            "\n" +
            "## Input:\n" +
            "%s\n" +
            "## Output:";

    public static String QUERY_MODE = "IMG_SERVICE";
    // 过滤条件
    public static String QUERY_DIM_FILTER_STYLE = "PLUG#DIM#" + QUERY_MODE + "#STYLE";
    public static String QUERY_DIM_FILTER_STYLE_NAME = "风格";
    public static String[] QUERY_DIM_FILTER_STYLE_VAL = {"muou_realistic", "muou_illustration",
            "动漫(Anime)", "连环漫画(Comic Book)", "剪纸艺术(Kirigami)", "黑白照片(Monochrome)"};

    private RestTemplate restTemplate;

    public ImgServiceQuery() {
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
        String prompt = buildPromptWithRequest(pluginParseResult);
        Set<QueryFilter> dimensionFilters = parseInfo.getDimensionFilters();
        if (null == dimensionFilters) {
            dimensionFilters = new LinkedHashSet<>();
            parseInfo.setDimensionFilters(dimensionFilters);
        }
        ImgServiceResponse ImgServiceResponse = buildResponseWithPoster(pluginParseResult, prompt, dimensionFilters);
        queryResult.setResultType("IMAGE");
        try {
            queryResult.setResponse(ImgServiceResponse);
            queryResult.setQueryState(QueryState.SUCCESS);
        } catch (Exception e) {
            log.info("ImgServiceResponse result has an exception:{}", e.getMessage());
        }
        return queryResult;
    }

    /**
     * Prompt接口请求
     */
    private String buildPromptWithRequest(PluginParseResult pluginParseResult) {
        Plugin plugin = pluginParseResult.getPlugin();
        WebBase webBase = JsonUtil.toObject(plugin.getConfig(), WebBase.class);
        List<ParamOption> paramOptions = webBase.getParamOptions();
        Map<String, Object> params = constructPromptParam(pluginParseResult.getRequest().getQueryText());
        //TODO 根据界面配置复制部分参数 后续补充
//        paramOptions.forEach(o -> {
//            if (o.getParamType().equals(ParamOption.ParamType.CUSTOM)) {
//                params.put(o.getKey(), o.getValue());
//            }
//        });
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> entity = new HttpEntity<>(JSON.toJSONString(params), headers);
        URI requestUrl = UriComponentsBuilder.fromHttpUrl(IMG_PROMPT_URL).build().encode().toUri();
        ResponseEntity responseEntity = null;
        Object objectResponse = null;
        restTemplate = ContextUtils.getBean(RestTemplate.class);
        try {
            responseEntity = restTemplate.exchange(requestUrl, HttpMethod.POST, entity, Object.class);
            objectResponse = responseEntity.getBody();
            log.debug("ImgServiceQuery Generate Prompt Response:{}", objectResponse);
            Map<String, Object> response = JsonUtil.objectToMap(objectResponse);
            if (response.containsKey("generated_text")) {
                return String.valueOf(response.get("generated_text"));
            } else {
                throw new RuntimeException("获取prompt失败，请重试！");
            }
        } catch (Exception e) {
            log.error("Exception:{}", e.getMessage());
            throw new RuntimeException("请求IMG_PROMPT失败，请重试！");
        }
    }

    /**
     * 构建生成后续图片用到的Prompt的prompt参数
     */
    private Map<String, Object> constructPromptParam(String queryText) {
        InputStream inputStream = this.getClass().getResourceAsStream("/poster/init_img_prompt.json");
        String inputParamJson;
        try {
            inputParamJson = IOUtils.toString(inputStream);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        String promptContent = String.format(IMG_PROMPT_TEMPLATE, queryText);
        // 替换content内容
        inputParamJson = inputParamJson.replace("$$CONTENT$$", StringEscapeUtils.escapeJava(promptContent));
        return JsonUtil.toMap(inputParamJson, String.class, Object.class);
    }

    /**
     * 海报接口请求
     */
    private ImgServiceResponse buildResponseWithPoster(PluginParseResult pluginParseResult, String prompt, Set<QueryFilter> filters) {
        String requestStyle = "muou_illustration";
        ImgServiceResponse imgServiceResponse = new ImgServiceResponse();
        Plugin plugin = pluginParseResult.getPlugin();
        WebBase webBase = JsonUtil.toObject(plugin.getConfig(), WebBase.class);
        imgServiceResponse.setWebBase(webBase);
        List<ParamOption> paramOptions = webBase.getParamOptions();
        Iterator<ParamOption> paramIterator = paramOptions.iterator();
        // 预处理无用配置
        while (paramIterator.hasNext()) {
            ParamOption o = paramIterator.next();
            if (o.getKey().equals("default_style")) {
                requestStyle = String.valueOf(o.getValue());
                paramIterator.remove();
                break;
            }
        }
        // 处理过滤条件
        if (null != filters && filters.size() > 0) {
            for (QueryFilter filterItem : filters) {
                if (filterItem.getBizName().equals(QUERY_DIM_FILTER_STYLE)) {
                    requestStyle = String.valueOf(filterItem.getValue());
                }
            }
        } else if(null != filters){
            QueryFilter styleFilter = new QueryFilter();
            styleFilter.setValue(requestStyle);
            styleFilter.setElementID(-1L);
            styleFilter.setName(ImgServiceQuery.QUERY_DIM_FILTER_STYLE_NAME);
            styleFilter.setOperator(FilterOperatorEnum.EQUALS);
            styleFilter.setBizName(ImgServiceQuery.QUERY_DIM_FILTER_STYLE);
            filters.add(styleFilter);
        }
        Map<String, Object> params;
        try {
            params = constructPosterApiParam(prompt, requestStyle);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        // TODO 根据界面配置复制部分参数 目前只支持以及嵌套，后面优化为二级嵌套
        for (ParamOption o : paramOptions) {
            if (o.getParamType().equals(ParamOption.ParamType.CUSTOM)) {
                params.put(o.getKey(), o.getValue());
            }
        }
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
                if (withPrefixImages.size() % 2 == 1) {
                    withPrefixImages.remove(withPrefixImages.size() - 1);
                }
                imgServiceResponse.setResult(withPrefixImages);
            }
        } catch (Exception e) {
            log.error("Exception:{}", e.getMessage());
        }
        return imgServiceResponse;
    }


    /**
     * 获取海报接口参数
     */
    private Map<String, Object> constructPosterApiParam(String queryPrompt, String style) throws IOException {
        InputStream inputStream = this.getClass().getResourceAsStream("/poster/init_img_normal.json");
        String inputParamJson = IOUtils.toString(inputStream);
        inputParamJson = inputParamJson.replace("$$CONTENT$$", StringEscapeUtils.escapeJava(queryPrompt));
        inputParamJson = inputParamJson.replace("$$STYLE$$", style);
        return JsonUtil.toMap(inputParamJson, String.class, Object.class);
    }

    public static void main(String[] args) {
        String promptContent = String.format(IMG_PROMPT_TEMPLATE, "茄子 土豆 大辣椒");
        String inputParamJson = "{\"name\": \"$$CONTENT$$\"}";
        // 替换content内容
        inputParamJson = inputParamJson.replace("$$CONTENT$$", StringEscapeUtils.escapeJava(promptContent));
        Map map = JsonUtil.toMap(inputParamJson, String.class, Object.class);
        System.out.println(map.toString());
    }
}
