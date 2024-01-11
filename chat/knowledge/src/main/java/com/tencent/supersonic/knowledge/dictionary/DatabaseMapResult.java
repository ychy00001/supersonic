package com.tencent.supersonic.knowledge.dictionary;

import com.google.common.base.Objects;
import com.tencent.supersonic.chat.api.pojo.SchemaElement;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class DatabaseMapResult extends MapResult {

    private SchemaElement schemaElement;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        DatabaseMapResult that = (DatabaseMapResult) o;
        return Objects.equal(name, that.name) && Objects.equal(schemaElement, that.schemaElement);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(name, schemaElement);
    }
}