package com.tencent.supersonic.chat.api.pojo;

import com.google.common.base.Objects;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SchemaElement implements Serializable {

    private Long model;
    private Long id;
    private String name;
    private String bizName;
    private Long useCnt;
    private SchemaElementType type;
    /**
     * 用于字段可能本身包含函数的一些维度指标
     * 示例 YEAR(buy_time) 购买时间为2021年
     */
    private String dataType;

    private List<String> alias;
    private List<SchemaValueMap> schemaValueMaps;
    private List<RelatedSchemaElement> relatedSchemaElements;

    private String defaultAgg;

    private double order;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        SchemaElement schemaElement = (SchemaElement) o;
        return Objects.equal(model, schemaElement.model) && Objects.equal(id,
                schemaElement.id) && Objects.equal(name, schemaElement.name)
                && Objects.equal(bizName, schemaElement.bizName)
                && Objects.equal(type, schemaElement.type);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(model, id, name, bizName, type);
    }

}
