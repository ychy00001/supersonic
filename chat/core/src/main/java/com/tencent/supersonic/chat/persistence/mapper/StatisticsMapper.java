package com.tencent.supersonic.chat.persistence.mapper;

import com.tencent.supersonic.chat.persistence.dataobject.StatisticsDO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface StatisticsMapper {
    boolean batchSaveStatistics(@Param("list") List<StatisticsDO> list);
}
