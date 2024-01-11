package com.tencent.supersonic.headless.query.parser.calcite;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.tencent.supersonic.common.pojo.ModelRela;
import com.tencent.supersonic.common.pojo.enums.FilterOperatorEnum;
import com.tencent.supersonic.headless.api.model.yaml.DataModelYamlTpl;
import com.tencent.supersonic.headless.api.model.yaml.DimensionTimeTypeParamsTpl;
import com.tencent.supersonic.headless.api.model.yaml.DimensionYamlTpl;
import com.tencent.supersonic.headless.api.model.yaml.IdentifyYamlTpl;
import com.tencent.supersonic.headless.api.model.yaml.MeasureYamlTpl;
import com.tencent.supersonic.headless.api.model.yaml.MetricTypeParamsYamlTpl;
import com.tencent.supersonic.headless.api.model.yaml.MetricYamlTpl;
import com.tencent.supersonic.headless.model.domain.Catalog;
import com.tencent.supersonic.headless.query.parser.calcite.s2sql.Constants;
import com.tencent.supersonic.headless.query.parser.calcite.s2sql.DataSource;
import com.tencent.supersonic.headless.query.parser.calcite.s2sql.DataType;
import com.tencent.supersonic.headless.query.parser.calcite.s2sql.Dimension;
import com.tencent.supersonic.headless.query.parser.calcite.s2sql.DimensionTimeTypeParams;
import com.tencent.supersonic.headless.query.parser.calcite.s2sql.Identify;
import com.tencent.supersonic.headless.query.parser.calcite.s2sql.JoinRelation;
import com.tencent.supersonic.headless.query.parser.calcite.s2sql.Materialization.TimePartType;
import com.tencent.supersonic.headless.query.parser.calcite.s2sql.Measure;
import com.tencent.supersonic.headless.query.parser.calcite.s2sql.Metric;
import com.tencent.supersonic.headless.query.parser.calcite.s2sql.MetricTypeParams;
import com.tencent.supersonic.headless.query.parser.calcite.s2sql.HeadlessModel;
import com.tencent.supersonic.headless.query.parser.calcite.schema.HeadlessSchema;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.tuple.Triple;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

@Slf4j
@Service
public class HeadlessSchemaManager {

    @Autowired
    private LoadingCache<String, HeadlessModel> loadingCache;
    private final Catalog catalog;

    public HeadlessSchemaManager(Catalog catalog) {
        this.catalog = catalog;
    }

    public HeadlessModel reload(String rootPath) {
        HeadlessModel headlessModel = new HeadlessModel();
        headlessModel.setRootPath(rootPath);
        Set<Long> modelIds = Arrays.stream(rootPath.split(",")).map(s -> Long.parseLong(s.trim()))
                .collect(Collectors.toSet());
        if (modelIds.isEmpty()) {
            log.error("get modelIds empty {}", rootPath);
            return headlessModel;
        }
        Map<String, List<DimensionYamlTpl>> dimensionYamlTpls = new HashMap<>();
        List<DataModelYamlTpl> dataModelYamlTpls = new ArrayList<>();
        List<MetricYamlTpl> metricYamlTpls = new ArrayList<>();
        Map<Long, String> modelIdName = new HashMap<>();
        catalog.getModelYamlTplByModelIds(modelIds, dimensionYamlTpls, dataModelYamlTpls, metricYamlTpls, modelIdName);
        List<ModelRela> modelRelas = catalog.getModelRela(new ArrayList<>(modelIds));
        if (!CollectionUtils.isEmpty(modelRelas)) {
            headlessModel.setJoinRelations(getJoinRelation(modelRelas, modelIdName));
        }
        if (!dataModelYamlTpls.isEmpty()) {
            Map<String, DataSource> dataSourceMap = dataModelYamlTpls.stream().map(d -> getDatasource(d))
                    .collect(Collectors.toMap(DataSource::getName, item -> item, (k1, k2) -> k1));
            headlessModel.setDatasourceMap(dataSourceMap);
        }
        if (!dimensionYamlTpls.isEmpty()) {
            Map<String, List<Dimension>> dimensionMap = new HashMap<>();
            for (Map.Entry<String, List<DimensionYamlTpl>> entry : dimensionYamlTpls.entrySet()) {
                dimensionMap.put(entry.getKey(), getDimensions(entry.getValue()));
            }
            headlessModel.setDimensionMap(dimensionMap);
        }
        if (!metricYamlTpls.isEmpty()) {
            headlessModel.setMetrics(getMetrics(metricYamlTpls));
        }
        return headlessModel;
    }

    //private Map<String, SemanticSchema> semanticSchemaMap = new HashMap<>();
    public HeadlessModel get(String rootPath) throws Exception {
        rootPath = formatKey(rootPath);
        HeadlessModel schema = loadingCache.get(rootPath);
        if (schema == null) {
            return null;
        }
        return schema;
    }

    public static List<Metric> getMetrics(final List<MetricYamlTpl> t) {
        return getMetricsByMetricYamlTpl(t);
    }

    public static List<Dimension> getDimensions(final List<DimensionYamlTpl> t) {
        return getDimension(t);
    }

    public static DataSource getDatasource(final DataModelYamlTpl d) {
        DataSource datasource = DataSource.builder().sourceId(d.getSourceId()).sqlQuery(d.getSqlQuery())
                .name(d.getName()).tableQuery(d.getTableQuery()).identifiers(getIdentify(d.getIdentifiers()))
                .measures(getMeasures(d.getMeasures())).dimensions(getDimensions(d.getDimensions())).build();
        datasource.setAggTime(getDataSourceAggTime(datasource.getDimensions()));
        if (Objects.nonNull(d.getModelSourceTypeEnum())) {
            datasource.setTimePartType(TimePartType.of(d.getModelSourceTypeEnum().name()));
        }
        return datasource;
    }

    private static String getDataSourceAggTime(List<Dimension> dimensions) {
        Optional<Dimension> timeDimension = dimensions.stream()
                .filter(d -> Constants.DIMENSION_TYPE_TIME.equalsIgnoreCase(d.getType())).findFirst();
        if (timeDimension.isPresent() && Objects.nonNull(timeDimension.get().getDimensionTimeTypeParams())) {
            return timeDimension.get().getDimensionTimeTypeParams().getTimeGranularity();
        }
        return Constants.DIMENSION_TYPE_TIME_GRANULARITY_NONE;
    }

    private static List<Metric> getMetricsByMetricYamlTpl(List<MetricYamlTpl> metricYamlTpls) {
        List<Metric> metrics = new ArrayList<>();
        for (MetricYamlTpl metricYamlTpl : metricYamlTpls) {
            Metric metric = new Metric();
            metric.setMetricTypeParams(getMetricTypeParams(metricYamlTpl.getTypeParams()));
            metric.setOwners(metricYamlTpl.getOwners());
            metric.setType(metricYamlTpl.getType());
            metric.setName(metricYamlTpl.getName());
            metrics.add(metric);
        }
        return metrics;
    }

    private static MetricTypeParams getMetricTypeParams(MetricTypeParamsYamlTpl metricTypeParamsYamlTpl) {
        MetricTypeParams metricTypeParams = new MetricTypeParams();
        metricTypeParams.setExpr(metricTypeParamsYamlTpl.getExpr());
        metricTypeParams.setMeasures(getMeasures(metricTypeParamsYamlTpl.getMeasures()));
        return metricTypeParams;
    }

    private static List<Measure> getMeasures(List<MeasureYamlTpl> measureYamlTpls) {
        List<Measure> measures = new ArrayList<>();
        for (MeasureYamlTpl measureYamlTpl : measureYamlTpls) {
            Measure measure = new Measure();
            measure.setCreateMetric(measureYamlTpl.getCreateMetric());
            measure.setExpr(measureYamlTpl.getExpr());
            measure.setAgg(measureYamlTpl.getAgg());
            measure.setName(measureYamlTpl.getName());
            measure.setAlias(measureYamlTpl.getAlias());
            measure.setConstraint(measureYamlTpl.getConstraint());
            measures.add(measure);
        }
        return measures;
    }

    private static List<Dimension> getDimension(List<DimensionYamlTpl> dimensionYamlTpls) {
        List<Dimension> dimensions = new ArrayList<>();
        for (DimensionYamlTpl dimensionYamlTpl : dimensionYamlTpls) {
            Dimension dimension = Dimension.builder().build();
            dimension.setType(dimensionYamlTpl.getType());
            dimension.setExpr(dimensionYamlTpl.getExpr());
            dimension.setName(dimensionYamlTpl.getName());
            dimension.setOwners(dimensionYamlTpl.getOwners());
            if (Objects.nonNull(dimensionYamlTpl.getDataType())) {
                dimension.setDataType(DataType.of(dimensionYamlTpl.getDataType().getType()));
            }
            if (Objects.isNull(dimension.getDataType())) {
                dimension.setDataType(DataType.UNKNOWN);
            }
            dimension.setDimensionTimeTypeParams(getDimensionTimeTypeParams(dimensionYamlTpl.getTypeParams()));
            dimensions.add(dimension);
        }
        return dimensions;
    }

    private static DimensionTimeTypeParams getDimensionTimeTypeParams(
            DimensionTimeTypeParamsTpl dimensionTimeTypeParamsTpl) {
        DimensionTimeTypeParams dimensionTimeTypeParams = new DimensionTimeTypeParams();
        if (dimensionTimeTypeParamsTpl != null) {
            dimensionTimeTypeParams.setTimeGranularity(dimensionTimeTypeParamsTpl.getTimeGranularity());
            dimensionTimeTypeParams.setIsPrimary(dimensionTimeTypeParamsTpl.getIsPrimary());
        }
        return dimensionTimeTypeParams;
    }

    private static List<Identify> getIdentify(List<IdentifyYamlTpl> identifyYamlTpls) {
        List<Identify> identifies = new ArrayList<>();
        for (IdentifyYamlTpl identifyYamlTpl : identifyYamlTpls) {
            Identify identify = new Identify();
            identify.setType(identifyYamlTpl.getType());
            identify.setName(identifyYamlTpl.getName());
            identifies.add(identify);
        }
        return identifies;
    }

    private static List<JoinRelation> getJoinRelation(List<ModelRela> modelRelas, Map<Long, String> modelIdName) {
        List<JoinRelation> joinRelations = new ArrayList<>();
        modelRelas.stream().forEach(r -> {
            if (modelIdName.containsKey(r.getFromModelId()) && modelIdName.containsKey(r.getToModelId())) {
                JoinRelation joinRelation = JoinRelation.builder().left(modelIdName.get(r.getFromModelId()))
                        .right(modelIdName.get(r.getToModelId())).joinType(r.getJoinType()).build();
                List<Triple<String, String, String>> conditions = new ArrayList<>();
                r.getJoinConditions().stream().forEach(rr -> {
                    if (FilterOperatorEnum.isValueCompare(rr.getOperator())) {
                        conditions.add(Triple.of(rr.getLeftField(), rr.getOperator().getValue(), rr.getRightField()));
                    }
                });
                joinRelation.setJoinCondition(conditions);
                joinRelations.add(joinRelation);
            }
        });
        return joinRelations;
    }

    public static void update(HeadlessSchema schema, List<Metric> metric) throws Exception {
        if (schema != null) {
            updateMetric(metric, schema.getMetrics());
        }
    }

    public static void update(HeadlessSchema schema, DataSource datasourceYamlTpl) throws Exception {
        if (schema != null) {
            String dataSourceName = datasourceYamlTpl.getName();
            Optional<Entry<String, DataSource>> datasourceYamlTplMap = schema.getDatasource().entrySet().stream()
                    .filter(t -> t.getKey().equalsIgnoreCase(dataSourceName)).findFirst();
            if (datasourceYamlTplMap.isPresent()) {
                datasourceYamlTplMap.get().setValue(datasourceYamlTpl);
            } else {
                schema.getDatasource().put(dataSourceName, datasourceYamlTpl);
            }
        }
    }

    public static void update(HeadlessSchema schema, String datasourceBizName, List<Dimension> dimensionYamlTpls)
            throws Exception {
        if (schema != null) {
            Optional<Map.Entry<String, List<Dimension>>> datasourceYamlTplMap = schema.getDimension().entrySet()
                    .stream().filter(t -> t.getKey().equalsIgnoreCase(datasourceBizName)).findFirst();
            if (datasourceYamlTplMap.isPresent()) {
                updateDimension(dimensionYamlTpls, datasourceYamlTplMap.get().getValue());
            } else {
                List<Dimension> dimensions = new ArrayList<>();
                updateDimension(dimensionYamlTpls, dimensions);
                schema.getDimension().put(datasourceBizName, dimensions);
            }
        }
    }

    private static void updateDimension(List<Dimension> dimensionYamlTpls, List<Dimension> dimensions) {
        if (CollectionUtils.isEmpty(dimensionYamlTpls)) {
            return;
        }
        Set<String> toAdd = dimensionYamlTpls.stream().map(m -> m.getName()).collect(Collectors.toSet());
        Iterator<Dimension> iterator = dimensions.iterator();
        while (iterator.hasNext()) {
            Dimension cur = iterator.next();
            if (toAdd.contains(cur.getName())) {
                iterator.remove();
            }
        }
        dimensions.addAll(dimensionYamlTpls);
    }

    private static void updateMetric(List<Metric> metricYamlTpls, List<Metric> metrics) {
        if (CollectionUtils.isEmpty(metricYamlTpls)) {
            return;
        }
        Set<String> toAdd = metricYamlTpls.stream().map(m -> m.getName()).collect(Collectors.toSet());
        Iterator<Metric> iterator = metrics.iterator();
        while (iterator.hasNext()) {
            Metric cur = iterator.next();
            if (toAdd.contains(cur.getName())) {
                iterator.remove();
            }
        }
        metrics.addAll(metricYamlTpls);
    }

    public static String formatKey(String key) {
        key = key.trim();
        if (key.startsWith("/")) {
            key = key.substring(1);
        }
        if (key.endsWith("/")) {
            key = key.substring(0, key.length() - 1);
        }
        return key;
    }

    @Configuration
    @EnableCaching
    public class GuavaCacheConfig {

        @Value("${parser.cache.saveMinute:1}")
        private Integer saveMinutes = 1;
        @Value("${parser.cache.maximumSize:1000}")
        private Integer maximumSize = 1000;

        @Bean
        public LoadingCache<String, HeadlessModel> getCache() {
            LoadingCache<String, HeadlessModel> cache
                    = CacheBuilder.newBuilder()
                    .expireAfterWrite(saveMinutes, TimeUnit.MINUTES)
                    .initialCapacity(10)
                    .maximumSize(maximumSize).build(
                            new CacheLoader<String, HeadlessModel>() {
                                @Override
                                public HeadlessModel load(String key) {
                                    log.info("load SemanticSchema [{}]", key);
                                    return HeadlessSchemaManager.this.reload(key);
                                }
                            }
                    );
            return cache;
        }
    }

}
