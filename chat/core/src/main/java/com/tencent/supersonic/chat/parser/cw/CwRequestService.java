package com.tencent.supersonic.chat.parser.cw;

import com.tencent.supersonic.chat.agent.AgentToolType;
import com.tencent.supersonic.chat.agent.NL2SQLTool;
import com.tencent.supersonic.chat.api.component.SemanticInterpreter;
import com.tencent.supersonic.chat.api.pojo.*;
import com.tencent.supersonic.chat.api.pojo.request.QueryReq;
import com.tencent.supersonic.chat.config.LLMParserConfig;
import com.tencent.supersonic.chat.config.OptimizationConfig;
import com.tencent.supersonic.chat.parser.SatisfactionChecker;
import com.tencent.supersonic.chat.parser.cw.param.LlmDslParam;
import com.tencent.supersonic.chat.parser.sql.llm.LLMSqlParser;
import com.tencent.supersonic.chat.parser.sql.llm.ModelResolver;
import com.tencent.supersonic.chat.parser.sql.llm.S2SqlDateHelper;
import com.tencent.supersonic.chat.service.AgentService;
import com.tencent.supersonic.chat.utils.ComponentFactory;
import com.tencent.supersonic.common.pojo.ModelCluster;
import com.tencent.supersonic.common.pojo.enums.DataFormatTypeEnum;
import com.tencent.supersonic.common.util.DateUtils;
import com.tencent.supersonic.headless.api.model.pojo.SchemaItem;
import com.tencent.supersonic.headless.api.model.response.ModelSchemaResp;
import com.tencent.supersonic.knowledge.service.SchemaService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
public class CwRequestService {

    protected SemanticInterpreter semanticInterpreter = ComponentFactory.getSemanticLayer();
    @Autowired
    private LLMParserConfig llmParserConfig;
    @Autowired
    private AgentService agentService;
    @Autowired
    private SchemaService schemaService;
    @Autowired
    private OptimizationConfig optimizationConfig;

    public boolean isSkip(QueryContext queryCtx) {
        if (ComponentFactory.getLLMProxy().isSkip(queryCtx)) {
            return true;
        }
        if (SatisfactionChecker.isSkip(queryCtx)) {
            log.info("skip {}, queryText:{}", LLMSqlParser.class, queryCtx.getRequest().getQueryText());
            return true;
        }
        return false;
    }

    public ModelCluster getModelCluster(QueryContext queryCtx, ChatContext chatCtx, Integer agentId) {
        Set<Long> distinctModelIds = agentService.getModelIds(agentId, AgentToolType.NL2SQL_CW);
        if (agentService.containsAllModel(distinctModelIds)) {
            distinctModelIds = new HashSet<>();
        }
        ModelResolver modelResolver = ComponentFactory.getModelResolver();
        String modelCluster = modelResolver.resolve(queryCtx, chatCtx, distinctModelIds);
        log.info("resolve modelId:{},llmParser Models:{}", modelCluster, distinctModelIds);
        return ModelCluster.build(modelCluster);
    }

    public NL2SQLTool getParserTool(QueryReq request, Set<Long> modelIdSet) {
        List<NL2SQLTool> commonAgentTools = agentService.getParserTools(request.getAgentId(),
                AgentToolType.NL2SQL_CW);
        Optional<NL2SQLTool> llmParserTool = commonAgentTools.stream()
                .filter(tool -> {
                    List<Long> modelIds = tool.getModelIds();
                    if (agentService.containsAllModel(new HashSet<>(modelIds))) {
                        return true;
                    }
                    for (Long modelId : modelIdSet) {
                        if (modelIds.contains(modelId)) {
                            return true;
                        }
                    }
                    return false;
                })
                .findFirst();
        return llmParserTool.orElse(null);
    }

    public CwReq getLlmReq(QueryContext queryCtx, SemanticSchema semanticSchema,
            ModelCluster modelCluster, List<CwReq.ElementValue> linkingValues) {

        Map<Long, String> modelIdToName = semanticSchema.getModelIdToName();
        Long firstModelId = modelCluster.getFirstModel();
        String queryText = queryCtx.getRequest().getQueryText();

        CwReq cwReq = new CwReq();
        cwReq.setQueryText(queryText);

        CwReq.CwSchema cwSchema = new CwReq.CwSchema();
        cwSchema.setModelName(modelIdToName.get(firstModelId));
        cwSchema.setDomainName(modelIdToName.get(firstModelId));
        List<String> fieldNameList = getFieldNameList(queryCtx, modelCluster, llmParserConfig);
        cwSchema.setFieldNameList(fieldNameList);
        cwReq.setSchema(cwSchema);

        List<CwReq.ElementValue> linking = new ArrayList<>();
        if (optimizationConfig.isUseLinkingValueSwitch()) {
            linking.addAll(linkingValues);
        }
        cwReq.setLinking(linking);

        String currentDate = S2SqlDateHelper.getReferenceDate(firstModelId);
        if (StringUtils.isEmpty(currentDate)) {
            currentDate = DateUtils.getBeforeDate(0);
        }
        cwReq.setCurrentDate(currentDate);

        // construct real llm request
        ModelSchema schemaInfo = getModelSchema(firstModelId);
        LlmDslParam requestBody = getCwRequestParam(cwReq, schemaInfo);
        cwReq.setRequestBody(requestBody);

        return cwReq;
    }

    public CwResp requestLLM(CwReq llmReq) {
        return ComponentFactory.getCwProxy().query2sql(llmReq);
    }

    protected List<String> getFieldNameList(QueryContext queryCtx, ModelCluster modelCluster,
            LLMParserConfig llmParserConfig) {

        Set<String> results = getTopNFieldNames(modelCluster, llmParserConfig);

        Set<String> fieldNameList = getMatchedFieldNames(queryCtx, modelCluster);

        results.addAll(fieldNameList);
        return new ArrayList<>(results);
    }

    private String getPriorExts(Set<Long> modelIds, List<String> fieldNameList) {
        StringBuilder extraInfoSb = new StringBuilder();
        List<ModelSchemaResp> modelSchemaResps = semanticInterpreter.fetchModelSchema(
                new ArrayList<>(modelIds), true);
        if (!CollectionUtils.isEmpty(modelSchemaResps)) {

            ModelSchemaResp modelSchemaResp = modelSchemaResps.get(0);
            Map<String, String> fieldNameToDataFormatType = modelSchemaResp.getMetrics()
                    .stream().filter(metricSchemaResp -> Objects.nonNull(metricSchemaResp.getDataFormatType()))
                    .flatMap(metricSchemaResp -> {
                        Set<Pair<String, String>> result = new HashSet<>();
                        String dataFormatType = metricSchemaResp.getDataFormatType();
                        result.add(Pair.of(metricSchemaResp.getName(), dataFormatType));
                        List<String> aliasList = SchemaItem.getAliasList(metricSchemaResp.getAlias());
                        if (!CollectionUtils.isEmpty(aliasList)) {
                            for (String alias : aliasList) {
                                result.add(Pair.of(alias, dataFormatType));
                            }
                        }
                        return result.stream();
                    })
                    .collect(Collectors.toMap(a -> a.getLeft(), a -> a.getRight(), (k1, k2) -> k1));

            for (String fieldName : fieldNameList) {
                String dataFormatType = fieldNameToDataFormatType.get(fieldName);
                if (DataFormatTypeEnum.DECIMAL.getName().equalsIgnoreCase(dataFormatType)
                        || DataFormatTypeEnum.PERCENT.getName().equalsIgnoreCase(dataFormatType)) {
                    String format = String.format("%s的计量单位是%s", fieldName, "小数; ");
                    extraInfoSb.append(format);
                }
            }
        }
        return extraInfoSb.toString();
    }

    protected List<CwReq.ElementValue> getValueList(QueryContext queryCtx, ModelCluster modelCluster) {
        Map<Long, String> itemIdToName = getItemIdToName(modelCluster);

        List<SchemaElementMatch> matchedElements = queryCtx.getModelClusterMapInfo()
                .getMatchedElements(modelCluster.getKey());
        if (CollectionUtils.isEmpty(matchedElements)) {
            return new ArrayList<>();
        }
        Set<CwReq.ElementValue> valueMatches = matchedElements
                .stream()
                .filter(elementMatch -> !elementMatch.isInherited())
                .filter(schemaElementMatch -> {
                    SchemaElementType type = schemaElementMatch.getElement().getType();
                    return SchemaElementType.VALUE.equals(type) || SchemaElementType.ID.equals(type);
                })
                .map(elementMatch -> {
                    CwReq.ElementValue elementValue = new CwReq.ElementValue();
                    elementValue.setFieldName(itemIdToName.get(elementMatch.getElement().getId()));
                    elementValue.setFieldValue(elementMatch.getWord());
                    return elementValue;
                }).collect(Collectors.toSet());
        return new ArrayList<>(valueMatches);
    }

    protected Map<Long, String> getItemIdToName(ModelCluster modelCluster) {
        SemanticSchema semanticSchema = schemaService.getSemanticSchema();
        return semanticSchema.getDimensions(modelCluster.getModelIds()).stream()
                .collect(Collectors.toMap(SchemaElement::getId, SchemaElement::getName, (value1, value2) -> value2));
    }

    private Set<String> getTopNFieldNames(ModelCluster modelCluster, LLMParserConfig llmParserConfig) {
        SemanticSchema semanticSchema = schemaService.getSemanticSchema();
        Set<String> results = semanticSchema.getDimensions(modelCluster.getModelIds()).stream()
                .sorted(Comparator.comparing(SchemaElement::getUseCnt).reversed())
                .limit(llmParserConfig.getDimensionTopN())
                .map(entry -> entry.getName())
                .collect(Collectors.toSet());

        Set<String> metrics = semanticSchema.getMetrics(modelCluster.getModelIds()).stream()
                .sorted(Comparator.comparing(SchemaElement::getUseCnt).reversed())
                .limit(llmParserConfig.getMetricTopN())
                .map(entry -> entry.getName())
                .collect(Collectors.toSet());

        results.addAll(metrics);
        return results;
    }

    protected Set<String> getMatchedFieldNames(QueryContext queryCtx, ModelCluster modelCluster) {
        Map<Long, String> itemIdToName = getItemIdToName(modelCluster);
        List<SchemaElementMatch> matchedElements = queryCtx.getModelClusterMapInfo()
                .getMatchedElements(modelCluster.getKey());
        if (CollectionUtils.isEmpty(matchedElements)) {
            return new HashSet<>();
        }
        Set<String> fieldNameList = matchedElements.stream()
                .filter(schemaElementMatch -> {
                    SchemaElementType elementType = schemaElementMatch.getElement().getType();
                    return SchemaElementType.METRIC.equals(elementType)
                            || SchemaElementType.DIMENSION.equals(elementType)
                            || SchemaElementType.VALUE.equals(elementType);
                })
                .map(schemaElementMatch -> {
                    SchemaElement element = schemaElementMatch.getElement();
                    SchemaElementType elementType = element.getType();
                    if (SchemaElementType.VALUE.equals(elementType)) {
                        return itemIdToName.get(element.getId());
                    }
                    return schemaElementMatch.getWord();
                })
                .collect(Collectors.toSet());
        return fieldNameList;
    }

    private ModelSchema getModelSchema(Long modelId) {
        SemanticInterpreter semanticInterpreter = ComponentFactory.getSemanticLayer();
        List<ModelSchema> modelSchema = semanticInterpreter.getModelSchema(Collections.singletonList(modelId));
        return modelSchema.get(0);
    }

    private LlmDslParam getCwRequestParam(CwReq cwReq, ModelSchema schemaInfo) {
        String queryText = cwReq.getQueryText();
        LlmDslParam requestParam = new LlmDslParam();
        List<LlmDslParam.MessageItem> messageItemList = new ArrayList<>();
        messageItemList.add(new LlmDslParam.MessageItem("system", "You are an expert in SQL and data analysis and can extract keywords and schema info from user input and output it in the following JSON format\n" +
                "If the user's question is not related to data analysis, directly reply with [UNKNOWN].\n" +
                "{ \n" +
                "    \"Entity\": [], \n" +
                "    \"Dimension\": [], \n" +
                "    \"Filters\": {\n" +
                "        \"Key\":\"Value\",\n" +
                "        \"Key\":\"Value\",\n" +
                "        \"Key\":\"Value\",\n" +
                "        \"Key\":\"Value\"\n" +
                "    }, \n" +
                "    \"Metrics\": [],\n" +
                "    \"Operator\":\"\",\n" +
                "    \"Groupby\": []\n" +
                "} \n" +
                "\n" +
                "Let's work this out in a step by step way to be sure we have the right answer.\n" +
                "Only output JSON FORMAT."));
        String userFormat = "Return the 'Question' result based on the following database schema information:\n" +
                "\n" +
                "### Database Schema\n" +
                "|column|name|\n" +
                "|---|---|\n" +
                "%s" +
                "\n" +
                "%s" +
                "\n" +
                "### GroupBy Notes\n" +
                "如果Groupby包含日期信息，根据日期信息返回日期的实际单位和其他Groupby信息，示例：\n" +
                "- 月环比---->'Groupby': ['日期按月', ...]\n" +
                "- 按周统计---->'Groupby': ['日期按周', ...]\n" +
                "- 月环比---->'Groupby': ['日期按月', ...]\n" +
                "- 基于年---->'Groupby': ['日期按年', ...]\n" +
                "\n" +
                "### Filters Notes\n" +
                "When querying the database, ensure that the \"Value\" in the \"Filters\" section supports fuzzy descriptions, such as \"包含\" \"不包含\" \"以_开头\" or \"以_结尾\" \n" +
                "For example: \n" +
                "- If querying for table schema containing \"BBB\" within \"AAA,\" use: 'Filters': {'AAA': '包含BBB'} \n" +
                "- If querying for table schema not containing \"BBB\" within \"AAA,\" use: 'Filters': {'AAA': '不包含BBB'} \n" +
                "- If querying for table schema ending with \"BBB\" within \"AAA,\" use: 'Filters': {'AAA': '以BBB结尾'} \n" +
                "- If querying for table schema not ending with \"BBB\" within \"AAA,\" use: 'Filters': {'AAA': '不以BBB'} \n" +
                "- If querying for table schema starting with \"BBB\" within \"AAA,\" use: 'Filters': {'AAA': '以BBB开头'} \n" +
                "\n" +
                "Your response should be based on the provided database schema and should accurately retrieve the required information. Pay close attention to the \"Filters\" and \"GroupBy\" sections, and only return JSON in the result, excluding any other content. \n" +
                "\n" +
                "### Question\n" +
                "%s";
        StringBuilder schemaInfoStr = new StringBuilder();
        StringBuilder dimValueStr = new StringBuilder();
        if (null != schemaInfo) {
            String schemaItemFormat = "|%s|%s|\n";
            for (SchemaElement item : schemaInfo.getDimensions()) {
                schemaInfoStr.append(String.format(schemaItemFormat, item.getBizName(), item.getName()));
            }
            for (SchemaElement item : schemaInfo.getMetrics()) {
                schemaInfoStr.append(String.format(schemaItemFormat, item.getBizName(), item.getName()));
            }
        }
        if (cwReq.getLinking().size() > 0) {
            dimValueStr.append("### Dimension Value\n" +
                    "|dim|value|\n" +
                    "|---|---|\n");
            String schemaItemFormat = "|%s|%s|\n";
            for (CwReq.ElementValue item : cwReq.getLinking()) {
                dimValueStr.append(String.format(schemaItemFormat, item.getFieldName(), item.getFieldValue()));
            }
        }
        messageItemList.add(new LlmDslParam.MessageItem("user", String.format(userFormat, schemaInfoStr.toString(), dimValueStr.toString(), queryText)));
        requestParam.setMessages(messageItemList);
        requestParam.setParameters(new LlmDslParam.Parameter());
        log.info("cw dsl param:{}", requestParam);
        return requestParam;
    }

}
