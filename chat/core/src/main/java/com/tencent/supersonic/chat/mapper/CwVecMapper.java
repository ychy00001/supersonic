//package com.tencent.supersonic.chat.mapper;
//
//import com.hankcs.hanlp.seg.common.Term;
//import com.tencent.supersonic.chat.api.component.SchemaMapper;
//import com.tencent.supersonic.chat.api.pojo.*;
//import com.tencent.supersonic.chat.service.SemanticService;
//import com.tencent.supersonic.common.pojo.enums.DictWordType;
//import com.tencent.supersonic.common.util.ContextUtils;
//import com.tencent.supersonic.knowledge.dictionary.MapResult;
//import com.tencent.supersonic.knowledge.dictionary.builder.BaseWordBuilder;
//import com.tencent.supersonic.knowledge.dictionary.builder.WordBuilderFactory;
//import com.tencent.supersonic.knowledge.utils.NatureHelper;
//import lombok.extern.slf4j.Slf4j;
//import org.apache.commons.collections.CollectionUtils;
//import org.apache.commons.lang3.StringUtils;
//import org.springframework.beans.BeanUtils;
//
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Map;
//import java.util.Objects;
//import java.util.stream.Collectors;
//
///**
// * 通过向量化索引读取mapper信息
// */
//@Slf4j
//public class CwVecMapper implements SchemaMapper {
//
//    @Override
//    public void map(QueryContext queryContext) {
//        // 帮我查询前年每个季度的平均销售价是多少？
//        // 读取LLM信息解析维度指标等内容
//        String queryText = queryContext.getRequest().getQueryText();
//
//        // 根据向量相似度匹配维度指标获取对应的bizName
//
//        // 构建SchemaElementMatches
//        convertItemToSchemaMapInfo();
//    }
//
//    /**
//     * 转化大模型返回的元素为SchemaElementMatch对象
//     */
//    private void convertItemToSchemaMapInfo(List<MapResult> mapResults, SchemaMapInfo schemaMap, List<Term> terms) {
//        if (CollectionUtils.isEmpty(mapResults)) {
//            return;
//        }
//
//        Map<String, Long> wordNatureToFrequency = terms.stream().collect(
//                Collectors.toMap(entry -> entry.getWord() + entry.getNature(),
//                        term -> Long.valueOf(term.getFrequency()), (value1, value2) -> value2));
//
//        for (MapResult mapResult : mapResults) {
//            for (String nature : mapResult.getNatures()) {
//                Long modelId = NatureHelper.getModelId(nature);
//                if (Objects.isNull(modelId)) {
//                    continue;
//                }
//                SchemaElementType elementType = NatureHelper.convertToElementType(nature);
//                if (Objects.isNull(elementType)) {
//                    continue;
//                }
//
//                SemanticService schemaService = ContextUtils.getBean(SemanticService.class);
//                ModelSchema modelSchema = schemaService.getModelSchema(modelId);
//
//                BaseWordBuilder baseWordBuilder = WordBuilderFactory.get(DictWordType.getNatureType(nature));
//                Long elementID = baseWordBuilder.getElementID(nature);
//                Long frequency = wordNatureToFrequency.get(mapResult.getName() + nature);
//
//                SchemaElement elementDb = modelSchema.getElement(elementType, elementID);
//                if (Objects.isNull(elementDb)) {
//                    log.info("element is null, elementType:{},elementID:{}", elementType, elementID);
//                    continue;
//                }
//                SchemaElement element = new SchemaElement();
//                BeanUtils.copyProperties(elementDb, element);
//                element.setAlias(getAlias(elementDb));
//                if (element.getType().equals(SchemaElementType.VALUE)) {
//                    element.setName(mapResult.getName());
//                }
//                SchemaElementMatch schemaElementMatch = SchemaElementMatch.builder()
//                        .element(element)
//                        .frequency(frequency)
//                        .word(mapResult.getName())
//                        .similarity(mapResult.getSimilarity())
//                        .detectWord(mapResult.getDetectWord())
//                        .build();
//
//                Map<Long, List<SchemaElementMatch>> modelElementMatches = schemaMap.getModelElementMatches();
//                List<SchemaElementMatch> schemaElementMatches = modelElementMatches.putIfAbsent(modelId,
//                        new ArrayList<>());
//                if (schemaElementMatches == null) {
//                    schemaElementMatches = modelElementMatches.get(modelId);
//                }
//                schemaElementMatches.add(schemaElementMatch);
//            }
//        }
//    }
//
//    /**
//     * TODO 获取别名 没太看懂意义在哪里
//     */
//    public List<String> getAlias(SchemaElement element) {
//        // 不是值类型 直接获取别名列表
//        if (!SchemaElementType.VALUE.equals(element.getType())) {
//            return element.getAlias();
//        }
//        // 是值类型并且名称不为空并且存在别名，则返回包含元素名称的别名
//        if (CollectionUtils.isNotEmpty(element.getAlias()) && StringUtils.isNotEmpty(element.getName())) {
//            return element.getAlias().stream()
//                    .filter(aliasItem -> aliasItem.contains(element.getName()))
//                    .collect(Collectors.toList());
//        }
//        return element.getAlias();
//    }
//}
