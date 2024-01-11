package com.tencent.supersonic.headless.model.domain.repository;


import com.tencent.supersonic.headless.model.domain.dataobject.MetricDO;
import com.tencent.supersonic.headless.model.domain.dataobject.MetricQueryDefaultConfigDO;
import com.tencent.supersonic.headless.model.domain.pojo.MetricFilter;
import java.util.List;


public interface MetricRepository {

    Long createMetric(MetricDO metricDO);

    void createMetricBatch(List<MetricDO> metricDOS);

    void updateMetric(MetricDO metricDO);

    void batchUpdateStatus(List<MetricDO> metricDOS);

    MetricDO getMetricById(Long id);

    List<MetricDO> getMetric(MetricFilter metricFilter);

    void saveDefaultQueryConfig(MetricQueryDefaultConfigDO defaultConfigDO);

    void updateDefaultQueryConfig(MetricQueryDefaultConfigDO defaultConfigDO);

    MetricQueryDefaultConfigDO getDefaultQueryConfig(Long metricId, String userName);
}
