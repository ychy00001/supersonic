package com.tencent.supersonic.chat.parser.plugin;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.tencent.supersonic.chat.api.component.SemanticParser;
import com.tencent.supersonic.chat.api.component.SemanticQuery;
import com.tencent.supersonic.chat.api.pojo.ChatContext;
import com.tencent.supersonic.chat.api.pojo.QueryContext;
import com.tencent.supersonic.chat.api.pojo.SchemaElementMatch;
import com.tencent.supersonic.chat.api.pojo.SchemaElementType;
import com.tencent.supersonic.chat.api.pojo.SemanticParseInfo;
import com.tencent.supersonic.chat.api.pojo.request.QueryFilter;
import com.tencent.supersonic.chat.api.pojo.request.QueryReq;
import com.tencent.supersonic.chat.plugin.Plugin;
import com.tencent.supersonic.chat.plugin.PluginManager;
import com.tencent.supersonic.chat.plugin.PluginParseResult;
import com.tencent.supersonic.chat.plugin.PluginRecallResult;
import com.tencent.supersonic.chat.query.QueryManager;
import com.tencent.supersonic.chat.query.plugin.ParamOption;
import com.tencent.supersonic.chat.query.plugin.PluginSemanticQuery;
import com.tencent.supersonic.chat.query.plugin.WebBase;
import com.tencent.supersonic.chat.query.plugin.imgservice.ImgServiceQuery;
import com.tencent.supersonic.common.pojo.Constants;
import com.tencent.supersonic.common.pojo.ModelCluster;
import com.tencent.supersonic.common.pojo.enums.FilterOperatorEnum;
import com.tencent.supersonic.common.util.JsonUtil;
import org.springframework.util.CollectionUtils;

import java.util.*;


/**
 * PluginParser defines the basic process and common methods for recalling plugins.
 */
public abstract class PluginParser implements SemanticParser {

    @Override
    public void parse(QueryContext queryContext, ChatContext chatContext) {
        for (SemanticQuery semanticQuery : queryContext.getCandidateQueries()) {
            if (queryContext.getRequest().getQueryText().length() <= semanticQuery.getParseInfo().getScore()
                    && (QueryManager.getPluginQueryModes().contains(semanticQuery.getQueryMode()))) {
                return;
            }
        }
        if (!checkPreCondition(queryContext)) {
            return;
        }
        PluginRecallResult pluginRecallResult = recallPlugin(queryContext);
        if (pluginRecallResult == null) {
            return;
        }
        buildQuery(queryContext, pluginRecallResult);
    }

    public abstract boolean checkPreCondition(QueryContext queryContext);

    public abstract PluginRecallResult recallPlugin(QueryContext queryContext);

    public void buildQuery(QueryContext queryContext, PluginRecallResult pluginRecallResult) {
        Plugin plugin = pluginRecallResult.getPlugin();
        Set<Long> modelIds = pluginRecallResult.getModelIds();
        if (plugin.isContainsAllModel()) {
            modelIds = Sets.newHashSet(-1L);
        }
        for (Long modelId : modelIds) {
            PluginSemanticQuery pluginQuery = QueryManager.createPluginQuery(plugin.getType());
            SemanticParseInfo semanticParseInfo = buildSemanticParseInfo(modelId, plugin,
                    queryContext.getRequest(),
                    queryContext.getModelClusterMapInfo().getMatchedElements(modelId),
                    pluginRecallResult.getDistance());
            semanticParseInfo.setQueryMode(pluginQuery.getQueryMode());
            semanticParseInfo.setScore(pluginRecallResult.getScore());
            //TODO queryMode如果是图片的话增加筛选条件，后续看这个条件怎么优化，可以在前端配置
            if (pluginQuery.getQueryMode().equals(ImgServiceQuery.QUERY_MODE)) {
                String requestStyle = ImgServiceQuery.QUERY_DIM_FILTER_STYLE_VAL[0];
                WebBase webBase = JsonUtil.toObject(plugin.getConfig(), WebBase.class);
                List<ParamOption> paramOptions = webBase.getParamOptions();
                // 预处理无用配置
                for (ParamOption o : paramOptions) {
                    if (o.getKey().equals("default_style")) {
                        requestStyle = String.valueOf(o.getValue());
                        break;
                    }
                }
                Set<QueryFilter> dimensionFilters = new LinkedHashSet<>();
                QueryFilter styleFilter = new QueryFilter();
                styleFilter.setValue(requestStyle);
                styleFilter.setElementID(-1L);
                styleFilter.setName(ImgServiceQuery.QUERY_DIM_FILTER_STYLE_NAME);
                styleFilter.setOperator(FilterOperatorEnum.EQUALS);
                styleFilter.setBizName(ImgServiceQuery.QUERY_DIM_FILTER_STYLE);
                dimensionFilters.add(styleFilter);
                semanticParseInfo.setDimensionFilters(dimensionFilters);
            }
            pluginQuery.setParseInfo(semanticParseInfo);
            queryContext.getCandidateQueries().add(pluginQuery);
        }
    }

    protected List<Plugin> getPluginList(QueryContext queryContext) {
        return PluginManager.getPluginAgentCanSupport(queryContext.getRequest().getAgentId());
    }

    protected SemanticParseInfo buildSemanticParseInfo(Long modelId, Plugin plugin, QueryReq queryReq,
                                                       List<SchemaElementMatch> schemaElementMatches, double distance) {
        if (modelId == null && !CollectionUtils.isEmpty(plugin.getModelList())) {
            modelId = plugin.getModelList().get(0);
        }
        if (schemaElementMatches == null) {
            schemaElementMatches = Lists.newArrayList();
        }
        SemanticParseInfo semanticParseInfo = new SemanticParseInfo();
        semanticParseInfo.setElementMatches(schemaElementMatches);
        semanticParseInfo.setModel(ModelCluster.build(Sets.newHashSet(modelId)));
        Map<String, Object> properties = new HashMap<>();
        PluginParseResult pluginParseResult = new PluginParseResult();
        pluginParseResult.setPlugin(plugin);
        pluginParseResult.setRequest(queryReq);
        pluginParseResult.setDistance(distance);
        properties.put(Constants.CONTEXT, pluginParseResult);
        properties.put("type", "plugin");
        properties.put("name", plugin.getName());
        semanticParseInfo.setProperties(properties);
        semanticParseInfo.setScore(distance);
        fillSemanticParseInfo(semanticParseInfo);
        return semanticParseInfo;
    }

    private void fillSemanticParseInfo(SemanticParseInfo semanticParseInfo) {
        List<SchemaElementMatch> schemaElementMatches = semanticParseInfo.getElementMatches();
        if (CollectionUtils.isEmpty(schemaElementMatches)) {
            return;
        }
        schemaElementMatches.stream().filter(schemaElementMatch ->
                SchemaElementType.VALUE.equals(schemaElementMatch.getElement().getType())
                        || SchemaElementType.ID.equals(schemaElementMatch.getElement().getType()))
                .forEach(schemaElementMatch -> {
                    QueryFilter queryFilter = new QueryFilter();
                    queryFilter.setValue(schemaElementMatch.getWord());
                    queryFilter.setElementID(schemaElementMatch.getElement().getId());
                    queryFilter.setName(schemaElementMatch.getElement().getName());
                    queryFilter.setOperator(FilterOperatorEnum.EQUALS);
                    queryFilter.setBizName(schemaElementMatch.getElement().getBizName());
                    semanticParseInfo.getDimensionFilters().add(queryFilter);
                });
    }
}
