package com.tencent.supersonic.headless.query.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.alibaba.excel.util.FileUtils;
import com.google.common.collect.Lists;
import com.tencent.supersonic.auth.api.authentication.pojo.User;
import com.tencent.supersonic.common.pojo.DateConf;
import com.tencent.supersonic.common.util.DateUtils;
import com.tencent.supersonic.headless.api.model.pojo.DrillDownDimension;
import com.tencent.supersonic.headless.api.model.pojo.RelateDimension;
import com.tencent.supersonic.headless.api.model.response.DimSchemaResp;
import com.tencent.supersonic.headless.api.model.response.MetricSchemaResp;
import com.tencent.supersonic.headless.api.model.response.ModelSchemaResp;
import com.tencent.supersonic.headless.api.model.response.QueryResultWithSchemaResp;
import com.tencent.supersonic.headless.api.query.request.BatchDownloadReq;
import com.tencent.supersonic.headless.model.domain.ModelService;
import java.io.File;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;


class DownloadServiceImplTest {
    @Test
    void testBatchDownload() throws Exception {
        ModelService modelService = Mockito.mock(ModelService.class);
        QueryService queryService = Mockito.mock(QueryService.class);
        when(modelService.fetchModelSchema(any())).thenReturn(Lists.newArrayList(mockModelSchemaResp()));
        when(queryService.queryByStruct(any(), any())).thenReturn(mockQueryResult());
        DownloadServiceImpl downloadService = new DownloadServiceImpl(modelService, queryService);
        String fileName = String.format("%s_%s.xlsx", "supersonic", DateUtils.format(new Date(), DateUtils.FORMAT));
        File file = FileUtils.createTmpFile(fileName);
        downloadService.batchDownload(buildBatchDownloadReq(), User.getFakeUser(), file);
    }

    private ModelSchemaResp mockModelSchemaResp() {
        ModelSchemaResp modelSchemaResp = new ModelSchemaResp();
        modelSchemaResp.setId(1L);
        List<MetricSchemaResp> metricResps = Lists.newArrayList();
        metricResps.add(mockMetricPv());
        metricResps.add(mockMetricUv());
        modelSchemaResp.setMetrics(metricResps);
        List<DimSchemaResp> dimSchemaResps = Lists.newArrayList();
        dimSchemaResps.add(mockDimension(1L, "user_name", "用户名"));
        dimSchemaResps.add(mockDimension(2L, "department", "部门"));
        dimSchemaResps.add(mockDimension(3L, "page", "页面"));
        modelSchemaResp.setDimensions(dimSchemaResps);
        return modelSchemaResp;
    }

    private MetricSchemaResp mockMetric(Long id, String bizName, String name, List<Long> drillDownloadDimensions) {
        MetricSchemaResp metricResp = new MetricSchemaResp();
        metricResp.setId(id);
        metricResp.setBizName(bizName);
        metricResp.setName(name);
        RelateDimension relateDimension = new RelateDimension();
        relateDimension.setDrillDownDimensions(drillDownloadDimensions.stream()
                .map(DrillDownDimension::new).collect(Collectors.toList()));
        metricResp.setRelateDimension(relateDimension);
        return metricResp;
    }

    private DimSchemaResp mockDimension(Long id, String bizName, String name) {
        DimSchemaResp dimSchemaResp = new DimSchemaResp();
        dimSchemaResp.setId(id);
        dimSchemaResp.setBizName(bizName);
        dimSchemaResp.setName(name);
        return dimSchemaResp;
    }

    private MetricSchemaResp mockMetricPv() {
        return mockMetric(1L, "pv", "访问次数", Lists.newArrayList(1L, 2L));
    }

    private MetricSchemaResp mockMetricUv() {
        return mockMetric(2L, "uv", "访问用户数", Lists.newArrayList(2L));
    }

    private BatchDownloadReq buildBatchDownloadReq() {
        BatchDownloadReq batchDownloadReq = new BatchDownloadReq();
        batchDownloadReq.setMetricIds(Lists.newArrayList(1L));
        batchDownloadReq.setDateInfo(mockDataConf());
        return batchDownloadReq;
    }

    private DateConf mockDataConf() {
        DateConf dateConf = new DateConf();
        dateConf.setStartDate("2023-10-11");
        dateConf.setEndDate("2023-10-15");
        dateConf.setDateMode(DateConf.DateMode.BETWEEN);
        return dateConf;
    }

    private QueryResultWithSchemaResp mockQueryResult() {
        QueryResultWithSchemaResp queryResultWithSchemaResp = new QueryResultWithSchemaResp();
        List<Map<String, Object>> resultList = Lists.newArrayList();
        resultList.add(createMap("2023-10-11", "tom", "hr", "1"));
        resultList.add(createMap("2023-10-12", "alice", "sales", "2"));
        resultList.add(createMap("2023-10-13", "jack", "sales", "3"));
        resultList.add(createMap("2023-10-14", "luck", "market", "4"));
        resultList.add(createMap("2023-10-15", "tom", "hr", "5"));
        queryResultWithSchemaResp.setResultList(resultList);
        return queryResultWithSchemaResp;
    }

    private static Map<String, Object> createMap(String sysImpDate, String d1, String d2, String m1) {
        Map<String, Object> map = new HashMap<>();
        map.put("sys_imp_date", sysImpDate);
        map.put("user_name", d1);
        map.put("department", d2);
        map.put("pv", m1);
        return map;
    }

}
