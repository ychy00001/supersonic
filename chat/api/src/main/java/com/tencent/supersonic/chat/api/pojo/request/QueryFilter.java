package com.tencent.supersonic.chat.api.pojo.request;

import com.tencent.supersonic.semantic.api.query.enums.FilterOperatorEnum;

import java.util.Objects;

import lombok.*;


@Data
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString(callSuper = true)
public class QueryFilter {

    private String bizName;

    private String name;

    @Builder.Default
    private FilterOperatorEnum operator = FilterOperatorEnum.EQUALS;

    private Object value;

    private Long elementID;

    private String alia;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        QueryFilter filter = (QueryFilter) o;
        return Objects.equals(bizName, filter.bizName) && Objects.equals(name, filter.name)
                && operator == filter.operator && Objects.equals(value, filter.value) && Objects.equals(
                elementID, filter.elementID);
    }

    @Override
    public int hashCode() {
        return Objects.hash(bizName, name, operator, value, elementID);
    }
}