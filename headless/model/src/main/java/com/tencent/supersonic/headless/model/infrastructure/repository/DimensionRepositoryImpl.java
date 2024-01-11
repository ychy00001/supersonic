package com.tencent.supersonic.headless.model.infrastructure.repository;

import com.tencent.supersonic.headless.model.domain.dataobject.DimensionDO;
import com.tencent.supersonic.headless.model.domain.pojo.DimensionFilter;
import com.tencent.supersonic.headless.model.infrastructure.mapper.DimensionDOCustomMapper;
import com.tencent.supersonic.headless.model.infrastructure.mapper.DimensionDOMapper;
import com.tencent.supersonic.headless.model.domain.repository.DimensionRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class DimensionRepositoryImpl implements DimensionRepository {

    private DimensionDOMapper dimensionDOMapper;

    private DimensionDOCustomMapper dimensionDOCustomMapper;

    public DimensionRepositoryImpl(DimensionDOMapper dimensionDOMapper,
                                   DimensionDOCustomMapper dimensionDOCustomMapper) {
        this.dimensionDOMapper = dimensionDOMapper;
        this.dimensionDOCustomMapper = dimensionDOCustomMapper;
    }

    @Override
    public void createDimension(DimensionDO dimensionDO) {
        dimensionDOMapper.insert(dimensionDO);
    }

    @Override
    public void createDimensionBatch(List<DimensionDO> dimensionDOS) {
        dimensionDOCustomMapper.batchInsert(dimensionDOS);
    }

    @Override
    public void updateDimension(DimensionDO dimensionDO) {
        dimensionDOMapper.updateById(dimensionDO);
    }

    @Override
    public void batchUpdateStatus(List<DimensionDO> dimensionDOS) {
        dimensionDOCustomMapper.batchUpdateStatus(dimensionDOS);
    }

    @Override
    public DimensionDO getDimensionById(Long id) {
        return dimensionDOMapper.selectById(id);
    }

    @Override
    public List<DimensionDO> getDimension(DimensionFilter dimensionFilter) {
        return dimensionDOCustomMapper.query(dimensionFilter);
    }

}
