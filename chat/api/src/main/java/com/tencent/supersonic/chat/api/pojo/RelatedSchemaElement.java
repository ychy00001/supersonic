package com.tencent.supersonic.chat.api.pojo;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RelatedSchemaElement {

    private Long dimensionId;

    private boolean isNecessary;

}
