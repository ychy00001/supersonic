package com.tencent.supersonic.chat.utils;

import com.github.pagehelper.PageInfo;
import com.tencent.supersonic.chat.api.component.SemanticInterpreter;
import com.tencent.supersonic.chat.api.pojo.ModelSchema;
import com.tencent.supersonic.chat.api.pojo.SchemaElement;
import com.tencent.supersonic.chat.api.pojo.request.KnowledgeAdvancedConfig;
import com.tencent.supersonic.chat.api.pojo.request.KnowledgeInfoReq;
import com.tencent.supersonic.chat.api.pojo.response.ChatConfigRichResp;
import com.tencent.supersonic.chat.api.pojo.response.ChatDefaultRichConfigResp;
import com.tencent.supersonic.chat.config.DefaultMetric;
import com.tencent.supersonic.chat.config.Dim4Dict;
import com.tencent.supersonic.chat.persistence.dataobject.DimValueDO;
import com.tencent.supersonic.chat.service.ConfigService;
import com.tencent.supersonic.knowledge.dictionary.DictUpdateMode;
import com.tencent.supersonic.knowledge.dictionary.DimValue2DictCommand;
import com.tencent.supersonic.headless.api.model.request.PageDimensionReq;
import com.tencent.supersonic.headless.api.model.response.DimensionResp;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import static com.tencent.supersonic.common.pojo.Constants.DAY;
import static com.tencent.supersonic.common.pojo.Constants.UNDERLINE;

@Component
public class DictMetaHelper {

    @Autowired
    private ConfigService configService;
    @Value("${model.internal.metric.suffix:internal_cnt}")
    private String internalMetricNameSuffix;
    @Value("${model.internal.day.number:2}")
    private Integer internalMetricDays;
    private SemanticInterpreter semanticInterpreter = ComponentFactory.getSemanticLayer();

    public List<DimValueDO> generateDimValueInfo(DimValue2DictCommand dimValue2DictCommend) {
        List<DimValueDO> dimValueDOList = new ArrayList<>();
        DictUpdateMode updateMode = dimValue2DictCommend.getUpdateMode();
        Set<Long> modelIds = new HashSet<>();
        switch (updateMode) {
            case OFFLINE_MODEL:
                modelIds.addAll(dimValue2DictCommend.getModelIds());
                dimValueDOList = generateDimValueInfoByModel(modelIds);
                break;
            case OFFLINE_FULL:
                List<ModelSchema> modelSchemaDescList = semanticInterpreter.getModelSchema();
                if (CollectionUtils.isEmpty(modelSchemaDescList)) {
                    break;
                }

                Map<Long, ModelSchema> modelIdAndDescPair = modelSchemaDescList.stream()
                        .collect(Collectors.toMap(a -> a.getModel().getId(), schema -> schema, (k1, k2) -> k1));
                if (!CollectionUtils.isEmpty(modelIdAndDescPair)) {
                    modelIds.addAll(modelIdAndDescPair.keySet());
                    dimValueDOList = generateDimValueInfoByModel(modelIds);
                    break;
                }
                break;
            case REALTIME_ADD:
                dimValueDOList = generateDimValueInfoByModelAndDim(dimValue2DictCommend.getModelAndDimPair());
                break;
            case NOT_SUPPORT:
                throw new RuntimeException("illegal parameter for updateMode");
            default:
                break;
        }

        return dimValueDOList;
    }

    private List<DimValueDO> generateDimValueInfoByModelAndDim(Map<Long, List<Long>> modelAndDimMap) {
        List<DimValueDO> dimValueDOList = new ArrayList<>();
        if (CollectionUtils.isEmpty(modelAndDimMap)) {
            return dimValueDOList;
        }

        List<ModelSchema> modelSchemaDescList = semanticInterpreter.getModelSchema();
        if (CollectionUtils.isEmpty(modelSchemaDescList)) {
            return dimValueDOList;
        }
        Map<Long, ModelSchema> modelIdAndDescPair = modelSchemaDescList.stream()
                .collect(Collectors.toMap(a -> a.getModel().getId(), a -> a, (k1, k2) -> k1));

        for (Long modelId : modelAndDimMap.keySet()) {
            if (!modelIdAndDescPair.containsKey(modelId)) {
                continue;
            }
            Map<Long, SchemaElement> dimIdAndDescPairAll;
            dimIdAndDescPairAll = modelIdAndDescPair.get(modelId).getDimensions().stream()
                    .collect(Collectors.toMap(SchemaElement::getId, dimSchemaDesc -> dimSchemaDesc, (k1, k2) -> k1));
            List<Long> dimIdReq = modelAndDimMap.get(modelId);
            Map<Long, SchemaElement> dimIdAndDescPairReq = new HashMap<>();
            for (Long dimId : dimIdReq) {
                if (dimIdAndDescPairAll.containsKey(dimId)) {
                    dimIdAndDescPairReq.put(dimId, dimIdAndDescPairAll.get(dimId));
                }
            }
            fillDimValueDOList(dimValueDOList, modelId, dimIdAndDescPairReq);
        }

        return dimValueDOList;
    }

    private List<DimValueDO> generateDimValueInfoByModel(Set<Long> modelIds) {
        List<DimValueDO> dimValueDOList = new ArrayList<>();
        List<ModelSchema> modelSchemaDescList = semanticInterpreter.getModelSchema(new ArrayList<>(modelIds));
        if (CollectionUtils.isEmpty(modelSchemaDescList)) {
            return dimValueDOList;
        }

        modelSchemaDescList.forEach(modelSchemaDesc -> {
            Map<Long, SchemaElement> dimIdAndDescPair = modelSchemaDesc.getDimensions().stream()
                    .collect(Collectors.toMap(SchemaElement::getId, dimSchemaDesc -> dimSchemaDesc, (k1, k2) -> k1));
            fillDimValueDOList(dimValueDOList, modelSchemaDesc.getModel().getId(), dimIdAndDescPair);

        });

        return dimValueDOList;
    }

    private void fillDimValueDOList(List<DimValueDO> dimValueDOList, Long modelId,
                                    Map<Long, SchemaElement> dimIdAndDescPair) {
        ChatConfigRichResp chaConfigRichDesc = configService.getConfigRichInfo(modelId);
        if (Objects.nonNull(chaConfigRichDesc) && Objects.nonNull(chaConfigRichDesc.getChatAggRichConfig())) {

            ChatDefaultRichConfigResp chatDefaultConfig =
                    chaConfigRichDesc.getChatAggRichConfig().getChatDefaultConfig();

            List<KnowledgeInfoReq> knowledgeAggInfo =
                    chaConfigRichDesc.getChatAggRichConfig().getKnowledgeInfos();

            List<KnowledgeInfoReq> knowledgeDetailInfo =
                    chaConfigRichDesc.getChatDetailRichConfig().getKnowledgeInfos();

            fillKnowledgeDimValue(knowledgeDetailInfo, chatDefaultConfig, dimValueDOList, dimIdAndDescPair, modelId);
            fillKnowledgeDimValue(knowledgeAggInfo, chatDefaultConfig, dimValueDOList, dimIdAndDescPair, modelId);


        }
    }

    private void fillKnowledgeDimValue(List<KnowledgeInfoReq> knowledgeInfos,
                                       ChatDefaultRichConfigResp chatDefaultConfig,
                                       List<DimValueDO> dimValueDOList,
                                       Map<Long, SchemaElement> dimIdAndDescPair, Long modelId) {
        Map<Long, DimensionResp> dimIdAndRespPair = queryDimensionRespByModelId(
                new ArrayList<>(Arrays.asList(modelId)));
        if (!CollectionUtils.isEmpty(knowledgeInfos)) {
            List<Dim4Dict> dimensions = new ArrayList<>();
            List<DefaultMetric> defaultMetricDescList = new ArrayList<>();
            knowledgeInfos.stream()
                    .filter(knowledgeInfo -> knowledgeInfo.getSearchEnable()
                            && !CollectionUtils.isEmpty(dimIdAndDescPair)
                            && dimIdAndDescPair.containsKey(knowledgeInfo.getItemId()))
                    .forEach(knowledgeInfo -> {
                        SchemaElement dimensionDesc = dimIdAndDescPair.get(knowledgeInfo.getItemId());

                        Long dimId = dimensionDesc.getId();
                        String internalMetricName = "";
                        if (Objects.nonNull(dimId)) {
                            String datasourceBizName = queryDataSourceByDimId(dimId);
                            internalMetricName = datasourceBizName + UNDERLINE + internalMetricNameSuffix;
                        }

                        if (Objects.isNull(chatDefaultConfig)) {
                            defaultMetricDescList.add(new DefaultMetric(internalMetricName,
                                    internalMetricDays, DAY));
                        } else {
                            String metric = internalMetricName;
                            if (!CollectionUtils.isEmpty(chatDefaultConfig.getMetrics())) {
                                metric = chatDefaultConfig.getMetrics().get(0).getBizName();
                            }
                            defaultMetricDescList.add(new DefaultMetric(metric,
                                    chatDefaultConfig.getUnit(), chatDefaultConfig.getPeriod()));

                        }

                        String bizName = dimensionDesc.getBizName();
                        Dim4Dict dim4Dict = new Dim4Dict();
                        dim4Dict.setDimId(knowledgeInfo.getItemId());
                        dim4Dict.setBizName(bizName);
                        if (Objects.nonNull(knowledgeInfo.getKnowledgeAdvancedConfig())) {
                            KnowledgeAdvancedConfig knowledgeAdvancedConfig
                                    = knowledgeInfo.getKnowledgeAdvancedConfig();
                            BeanUtils.copyProperties(knowledgeAdvancedConfig, dim4Dict);

                            if (Objects.nonNull(dimIdAndRespPair)
                                    && dimIdAndRespPair.containsKey(dim4Dict.getDimId())) {
                                String datasourceFilterSql = dimIdAndRespPair.get(
                                        dim4Dict.getDimId()).getModelFilterSql();
                                if (StringUtils.isNotEmpty(datasourceFilterSql)) {
                                    dim4Dict.getRuleList().add(datasourceFilterSql);
                                }

                            }
                        }
                        dimensions.add(dim4Dict);

                    });

            if (!CollectionUtils.isEmpty(dimensions)) {
                DimValueDO dimValueDO = new DimValueDO()
                        .setModelId(modelId)
                        .setDefaultMetricIds(defaultMetricDescList)
                        .setDimensions(dimensions);
                dimValueDOList.add(dimValueDO);
            }
        }
    }

    private Map<Long, DimensionResp> queryDimensionRespByModelId(List<Long> modelIds) {
        Map<Long, DimensionResp> dimIdAndRespPair = new HashMap<>();
        PageDimensionReq pageDimensionCmd = new PageDimensionReq();
        pageDimensionCmd.setModelIds(modelIds);
        PageInfo<DimensionResp> dimensionPage = semanticInterpreter.getDimensionPage(pageDimensionCmd);
        if (Objects.nonNull(dimensionPage) && !CollectionUtils.isEmpty(dimensionPage.getList())) {
            List<DimensionResp> dimList = dimensionPage.getList();
            dimIdAndRespPair = dimList.stream().collect(Collectors.toMap(DimensionResp::getId, v -> v, (v1, v2) -> v2));
        }
        return dimIdAndRespPair;
    }

    private String queryDataSourceByDimId(Long id) {
        PageDimensionReq pageDimensionCmd = new PageDimensionReq();
        pageDimensionCmd.setId(id.toString());
        PageInfo<DimensionResp> dimensionPage = semanticInterpreter.getDimensionPage(pageDimensionCmd);
        if (Objects.nonNull(dimensionPage) && !CollectionUtils.isEmpty(dimensionPage.getList())) {
            List<DimensionResp> list = dimensionPage.getList();
            return list.get(0).getModelBizName();
        }
        return "";
    }
}
