package com.tencent.supersonic.headless.model.infrastructure.repository;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.tencent.supersonic.headless.model.infrastructure.mapper.DomainDOMapper;
import com.tencent.supersonic.headless.model.domain.dataobject.DomainDO;
import com.tencent.supersonic.headless.model.domain.repository.DomainRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;


@Component
@Slf4j
public class DomainRepositoryImpl implements DomainRepository {

    private DomainDOMapper domainDOMapper;

    public DomainRepositoryImpl(DomainDOMapper domainDOMapper) {
        this.domainDOMapper = domainDOMapper;
    }

    @Override
    public void createDomain(DomainDO metaDomainDO) {
        domainDOMapper.insert(metaDomainDO);
    }

    @Override
    public void updateDomain(DomainDO metaDomainDO) {
        domainDOMapper.updateById(metaDomainDO);
    }

    @Override
    public void deleteDomain(Long id) {
        domainDOMapper.deleteById(id);
    }

    @Override
    public List<DomainDO> getDomainList() {
        return domainDOMapper.selectList(Wrappers.emptyWrapper());
    }

    @Override
    public DomainDO getDomainById(Long id) {
        return domainDOMapper.selectById(id);
    }

}
