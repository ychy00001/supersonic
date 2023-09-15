package com.tencent.supersonic.chat.corrector;

import com.tencent.supersonic.chat.api.component.SemanticCorrector;
import com.tencent.supersonic.chat.api.pojo.SchemaElement;
import com.tencent.supersonic.chat.api.pojo.SemanticSchema;
import com.tencent.supersonic.common.util.ContextUtils;
import com.tencent.supersonic.knowledge.service.SchemaService;
import com.tencent.supersonic.semantic.api.model.enums.TimeDimensionEnum;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;

@Slf4j
public abstract class BaseSemanticCorrector implements SemanticCorrector {
    public static final String DATE_FIELD = "数据日期";

    protected Map<String, String> getFieldToBizName(Long modelId) {

        SemanticSchema semanticSchema = ContextUtils.getBean(SchemaService.class).getSemanticSchema();

        List<SchemaElement> dbAllFields = new ArrayList<>();
        dbAllFields.addAll(semanticSchema.getMetrics());
        dbAllFields.addAll(semanticSchema.getDimensions());

        // add Alias
        List<SchemaElement> aliaFields = new ArrayList<>();
        dbAllFields.stream()
                .filter(item -> item.getAlias() != null && item.getAlias().size() > 0)
                .forEach(item -> {
                    List<String> alias = item.getAlias();
                    for (String alia : alias) {
                        SchemaElement aliaElement = new SchemaElement();
                        BeanUtils.copyProperties(item, aliaElement);
                        aliaElement.setName(alia);
                        aliaFields.add(aliaElement);
                    }
                });
        dbAllFields.addAll(aliaFields);

        Map<String, String> result = dbAllFields.stream()
                .filter(entry -> entry.getModel().equals(modelId))
                .collect(Collectors.toMap(SchemaElement::getName, a -> a.getBizName(), (k1, k2) -> k1));
        result.put(DATE_FIELD, TimeDimensionEnum.DAY.getName());
        return result;
    }

}
