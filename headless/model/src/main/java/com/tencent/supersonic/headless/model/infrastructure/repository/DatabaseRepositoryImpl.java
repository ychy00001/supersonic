package com.tencent.supersonic.headless.model.infrastructure.repository;

import com.tencent.supersonic.headless.model.domain.dataobject.DatabaseDO;
import com.tencent.supersonic.headless.model.infrastructure.mapper.DatabaseDOMapper;
import com.tencent.supersonic.headless.model.domain.dataobject.DatabaseDOExample;
import com.tencent.supersonic.headless.model.domain.repository.DatabaseRepository;
import java.util.List;
import org.springframework.stereotype.Component;


@Component
public class DatabaseRepositoryImpl implements DatabaseRepository {


    private DatabaseDOMapper databaseDOMapper;


    public DatabaseRepositoryImpl(DatabaseDOMapper databaseDOMapper) {
        this.databaseDOMapper = databaseDOMapper;
    }

    @Override
    public void createDatabase(DatabaseDO databaseDO) {
        databaseDOMapper.insert(databaseDO);
    }

    @Override
    public void updateDatabase(DatabaseDO databaseDO) {
        databaseDOMapper.updateByPrimaryKeyWithBLOBs(databaseDO);
    }

    @Override
    public DatabaseDO getDatabase(Long id) {
        return databaseDOMapper.selectByPrimaryKey(id);
    }

    @Override
    public List<DatabaseDO> getDatabaseList() {
        return databaseDOMapper.selectByExampleWithBLOBs(new DatabaseDOExample());
    }

    @Override
    public void deleteDatabase(Long id) {
        databaseDOMapper.deleteByPrimaryKey(id);
    }

}
