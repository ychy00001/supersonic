package com.tencent.supersonic.chat.parser.cw;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class VecFilterBO {
    private VecFilterTypeEnum item_type;

    public enum VecFilterTypeEnum {
        METRIC,
        DIMENSION
    }
}
