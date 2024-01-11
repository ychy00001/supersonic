package com.tencent.supersonic.headless.model.domain.dataobject;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.util.Date;

@Data
@TableName("s2_dimension")
public class DimensionDO {

    @TableId(type = IdType.AUTO)
    private Long id;

    private Long modelId;

    private String name;

    private String bizName;

    private String description;

    private Integer status;

    private Integer sensitiveLevel;

    private String type;

    private Date createdAt;

    private String createdBy;

    private Date updatedAt;

    private String updatedBy;

    private String semanticType;

    private String alias;

    private String defaultValues;

    private String dimValueMaps;

    private String typeParams;

    private String expr;

    private String dataType;

    private int isTag;
}