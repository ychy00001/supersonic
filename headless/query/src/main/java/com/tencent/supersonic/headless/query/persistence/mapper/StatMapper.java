package com.tencent.supersonic.headless.query.persistence.mapper;


import com.tencent.supersonic.headless.api.model.pojo.QueryStat;
import com.tencent.supersonic.headless.api.query.request.ItemUseReq;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface StatMapper {

    Boolean createRecord(QueryStat queryStatInfo);

    List<QueryStat> getStatInfo(ItemUseReq itemUseCommend);
}
