package com.tencent.supersonic.headless.model.infrastructure.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.tencent.supersonic.headless.model.domain.dataobject.CollectDO;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 收藏项表 Mapper 接口
 * </p>
 *
 * @author yannsu
 * @since 2023-11-09 03:49:33
 */
@Mapper
public interface CollectMapper extends BaseMapper<CollectDO> {

}
