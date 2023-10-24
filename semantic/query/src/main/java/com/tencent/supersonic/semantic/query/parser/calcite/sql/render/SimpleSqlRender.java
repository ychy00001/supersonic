package com.tencent.supersonic.semantic.query.parser.calcite.sql.render;


import com.tencent.supersonic.semantic.api.query.request.MetricReq;
import com.tencent.supersonic.semantic.query.parser.calcite.s2ql.Constants;
import com.tencent.supersonic.semantic.query.parser.calcite.s2ql.DataSource;
import com.tencent.supersonic.semantic.query.parser.calcite.s2ql.Dimension;
import com.tencent.supersonic.semantic.query.parser.calcite.s2ql.Identify;
import com.tencent.supersonic.semantic.query.parser.calcite.s2ql.Measure;
import com.tencent.supersonic.semantic.query.parser.calcite.s2ql.Metric;
import com.tencent.supersonic.semantic.query.parser.calcite.schema.SemanticSchema;
import com.tencent.supersonic.semantic.query.parser.calcite.sql.Renderer;
import com.tencent.supersonic.semantic.query.parser.calcite.sql.TableView;
import com.tencent.supersonic.semantic.query.parser.calcite.sql.node.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.calcite.sql.SqlNode;
import org.apache.calcite.sql.validate.SqlValidatorScope;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
public class SimpleSqlRender extends Renderer {

    public static TableView renderOne(String alias, List<String> fieldWheres,
                                      List<String> reqMetrics, List<String> reqDimensions, String queryWhere, DataSource datasource,
                                      SqlValidatorScope scope, SemanticSchema schema, boolean nonAgg) throws Exception {

        TableView dataSet = new TableView();
        TableView output = new TableView();
        List<String> queryMetrics = new ArrayList<>(reqMetrics);
        List<String> queryDimensions = new ArrayList<>(reqDimensions);
        List<String> fieldWhere = new ArrayList<>(fieldWheres);
        if (null != queryWhere && !queryWhere.isEmpty()) {
            SqlNode whereNode = FilterNode.parse(queryWhere, scope);
            output.getFilter().add(whereNode);
        }
        if (!fieldWhere.isEmpty()) {
            Set<String> dimensions = new HashSet<>();
            Set<String> metrics = new HashSet<>();
            whereDimMetric(fieldWhere, queryMetrics, queryDimensions, datasource, schema, dimensions, metrics);
            queryMetrics.addAll(metrics);
            queryMetrics = uniqList(queryMetrics);
            queryDimensions.addAll(dimensions);
            queryDimensions = uniqList(queryDimensions);
            mergeWhere(fieldWhere, dataSet, output, queryMetrics, queryDimensions, datasource, scope, schema, nonAgg);
        }

        for (String metric : queryMetrics) {
            MetricNode metricNode = buildMetricNode(metric, datasource, scope, schema, nonAgg, alias);
            if (!metricNode.getAggNode().isEmpty()) {
                metricNode.getAggNode().entrySet().stream().forEach(m -> {
                            try {
                                output.getMeasure().add(SemanticNode.parse(metricNode.getMetric().getName(), scope));
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                );
            }
            if (metricNode.getNonAggNode() != null) {
                metricNode.getNonAggNode().entrySet().stream().forEach(m -> {
                            try {
                                dataSet.getMeasure().add(SemanticNode.parse(metricNode.getMetric().getName(), scope));
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                );
//                metricNode.getNonAggNode().entrySet().stream().forEach(m -> dataSet.getMeasure().add(m.getValue()));
            }
            if (metricNode.getMeasureFilter() != null) {
                metricNode.getMeasureFilter().entrySet().stream().forEach(m -> {
                            try {
                                dataSet.getFilter().add(SemanticNode.parse(metricNode.getMetric().getName(), scope));
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                );
//                metricNode.getMeasureFilter().entrySet().stream().forEach(m -> dataSet.getFilter().add(m.getValue()));
            }
        }
        // 因为要移除已经匹配的维度元素，则不能使用原始引用数据直接匹配，因为引用数据在缓存中，删除后缓存会造成下次查询异常
        List<Dimension> dimensionList = new ArrayList<>(schema.getDimension().get(datasource.getName()));
        for (String dimension : queryDimensions) {
            if (dimension.contains(Constants.DIMENSION_IDENTIFY) && queryDimensions.contains(
                    dimension.split(Constants.DIMENSION_IDENTIFY)[1])) {
                continue;
            }
            // 添加维度信息到Select中，注释了之前维度添加至GroupBy的流程
            Iterator<Dimension> dimIterator = dimensionList.iterator();
            while (dimIterator.hasNext()) {
                Dimension dim = dimIterator.next();
                if (!dimension.contains(dim.getName())) {
                    continue;
                }
                dataSet.getMeasure().add(DimensionNode.build(dim, scope));
                output.getMeasure().add(DimensionNode.buildName(dim, scope));
                dimIterator.remove();
            }

//            buildDimension(dimension.contains(Constants.DIMENSION_IDENTIFY) ? dimension : "",
//                    dimension.contains(Constants.DIMENSION_IDENTIFY) ? dimension.split(Constants.DIMENSION_IDENTIFY)[1]
//                            : dimension, datasource, schema, nonAgg, dataSet, output, scope);
        }
        SqlNode tableNode = DataSourceNode.build(datasource, scope);
        dataSet.setTable(tableNode);
        output.setTable(SemanticNode.buildAs(
                Constants.DATASOURCE_TABLE_OUT_PREFIX + datasource.getName() + "_" + UUID.randomUUID().toString()
                        .substring(32), dataSet.build()));
        return output;
    }

    private static void buildDimension(String alias, String dimension, DataSource datasource, SemanticSchema schema,
                                       boolean nonAgg, TableView dataSet, TableView output, SqlValidatorScope scope) throws Exception {
        List<Dimension> dimensionList = schema.getDimension().get(datasource.getName());
        boolean isAdd = false;
        if (!CollectionUtils.isEmpty(dimensionList)) {
            for (Dimension dim : dimensionList) {
                if (!dim.getName().equalsIgnoreCase(dimension)) {
                    continue;
                }
                dataSet.getMeasure().add(DimensionNode.build(dim, scope));
                if (nonAgg) {
                    //dataSet.getMeasure().addAll(DimensionNode.expand(dim, scope));
                    output.getMeasure().add(DimensionNode.buildName(dim, scope));
                    isAdd = true;
                    continue;
                }

                if ("".equals(alias)) {
                    output.getDimension().add(DimensionNode.buildName(dim, scope));
                } else {
                    output.getDimension().add(DimensionNode.buildNameAs(alias, dim, scope));
                }
                isAdd = true;
                break;
            }
        }
        if (!isAdd) {
            Optional<Identify> identify = datasource.getIdentifiers().stream()
                    .filter(i -> i.getName().equalsIgnoreCase(dimension)).findFirst();
            if (identify.isPresent()) {
                if (nonAgg) {
                    dataSet.getMeasure().add(SemanticNode.parse(identify.get().getName(), scope));
                    output.getMeasure().add(SemanticNode.parse(identify.get().getName(), scope));
                } else {
                    dataSet.getMeasure().add(SemanticNode.parse(identify.get().getName(), scope));
                    output.getDimension().add(SemanticNode.parse(identify.get().getName(), scope));
                }
                isAdd = true;
            }
        }
        if (isAdd) {
            return;
        }
        Optional<Dimension> dimensionOptional = getDimensionByName(dimension, datasource);
        if (dimensionOptional.isPresent()) {
            if (nonAgg) {
                dataSet.getMeasure().add(DimensionNode.build(dimensionOptional.get(), scope));
                output.getMeasure().add(DimensionNode.buildName(dimensionOptional.get(), scope));
                return;
            }
            dataSet.getMeasure().add(DimensionNode.build(dimensionOptional.get(), scope));
            output.getDimension().add(DimensionNode.buildName(dimensionOptional.get(), scope));
        }
    }

    private static boolean isWhereHasMetric(List<String> fields, DataSource datasource) {
        Long metricNum = datasource.getMeasures().stream().filter(m -> fields.contains(m.getName().toLowerCase()))
                .count();
        Long measureNum = datasource.getMeasures().stream().filter(m -> fields.contains(m.getName().toLowerCase()))
                .count();
        return metricNum > 0 || measureNum > 0;
    }

    private static List<SqlNode> getWhereMeasure(List<String> fields, List<String> queryMetrics,
                                                 List<String> queryDimensions, DataSource datasource, SqlValidatorScope scope, SemanticSchema schema,
                                                 boolean nonAgg) throws Exception {
        Iterator<String> iterator = fields.iterator();
        List<SqlNode> whereNode = new ArrayList<>();
        // 维度指标的过滤条件如何已经存在，则移除where条件 该逻辑在生成sql中不适用，可以不走该逻辑
        while (iterator.hasNext()) {
            String cur = iterator.next();
            if (queryDimensions.contains(cur) || queryMetrics.contains(cur)) {
                iterator.remove();
            }
        }
        for (String where : fields) {
            List<Dimension> dimensionList = schema.getDimension().get(datasource.getName());
            boolean isAdd = false;
            if (!CollectionUtils.isEmpty(dimensionList)) {
                for (Dimension dim : dimensionList) {
                    if (!dim.getName().equalsIgnoreCase(where)) {
                        continue;
                    }
                    if (nonAgg) {
                        whereNode.addAll(DimensionNode.expand(dim, scope));
                        isAdd = true;
                        continue;
                    }
                    whereNode.addAll(DimensionNode.expand(dim, scope));
                    isAdd = true;
                }
            }
            Optional<Identify> identify = getIdentifyByName(where, datasource);
            if (identify.isPresent()) {
                whereNode.add(IdentifyNode.build(identify.get(), scope));
                isAdd = true;
            }
            if (isAdd) {
                continue;
            }
            Optional<Dimension> dimensionOptional = getDimensionByName(where, datasource);
            if (dimensionOptional.isPresent()) {
                if (nonAgg) {
                    whereNode.add(DimensionNode.build(dimensionOptional.get(), scope));
                    continue;
                }
                whereNode.add(DimensionNode.build(dimensionOptional.get(), scope));
            }
        }
        return whereNode;
    }

    private static void mergeWhere(List<String> fields, TableView dataSet, TableView outputSet,
                                   List<String> queryMetrics,
                                   List<String> queryDimensions, DataSource datasource, SqlValidatorScope scope, SemanticSchema schema,
                                   boolean nonAgg) throws Exception {
        List<SqlNode> whereNode = getWhereMeasure(fields, queryMetrics, queryDimensions, datasource, scope, schema,
                nonAgg);
        dataSet.getMeasure().addAll(whereNode);
    }

    public static void whereDimMetric(List<String> fields, List<String> queryMetrics,
                                      List<String> queryDimensions, DataSource datasource, SemanticSchema schema, Set<String> dimensions,
                                      Set<String> metrics) {
        for (String field : fields) {
            if (queryDimensions.contains(field) || queryMetrics.contains(field)) {
                continue;
            }
            String filterField = field;
            if (field.contains(Constants.DIMENSION_IDENTIFY)) {
                filterField = field.split(Constants.DIMENSION_IDENTIFY)[1];
            }
            addField(filterField, field, datasource, schema, dimensions, metrics);
        }
    }

    private static void addField(String field, String oriField, DataSource datasource, SemanticSchema schema,
                                 Set<String> dimensions,
                                 Set<String> metrics) {
        Optional<Dimension> dimension = datasource.getDimensions().stream()
                .filter(d -> d.getName().equalsIgnoreCase(field)).findFirst();
        if (dimension.isPresent()) {
            dimensions.add(oriField);
            return;
        }
        Optional<Identify> identify = datasource.getIdentifiers().stream()
                .filter(i -> i.getName().equalsIgnoreCase(field)).findFirst();
        if (identify.isPresent()) {
            dimensions.add(oriField);
            return;
        }
        if (schema.getDimension().containsKey(datasource.getName())) {
            Optional<Dimension> dataSourceDim = schema.getDimension().get(datasource.getName()).stream()
                    .filter(d -> d.getName().equalsIgnoreCase(field)).findFirst();
            if (dataSourceDim.isPresent()) {
                dimensions.add(oriField);
                return;
            }
        }
        Optional<Measure> metric = datasource.getMeasures()
                .stream().filter(m -> m.getName().equalsIgnoreCase(field)).findFirst();
        if (metric.isPresent()) {
            metrics.add(oriField);
            return;
        }
        Optional<Metric> datasourceMetric = schema.getMetrics()
                .stream().filter(m -> m.getName().equalsIgnoreCase(field)).findFirst();
        if (datasourceMetric.isPresent()) {
            Set<String> measures = datasourceMetric.get().getMetricTypeParams().getMeasures().stream()
                    .map(m -> m.getName()).collect(
                            Collectors.toSet());
            if (datasource.getMeasures().stream().map(m -> m.getName()).collect(Collectors.toSet())
                    .containsAll(measures)) {
                metrics.add(oriField);
                return;
            }
        }
    }

    public static boolean isDimension(String name, DataSource datasource, SemanticSchema schema) {
        Optional<Dimension> dimension = datasource.getDimensions().stream()
                .filter(d -> d.getName().equalsIgnoreCase(name)).findFirst();
        if (dimension.isPresent()) {
            return true;
        }
        Optional<Identify> identify = datasource.getIdentifiers().stream()
                .filter(i -> i.getName().equalsIgnoreCase(name)).findFirst();
        if (identify.isPresent()) {
            return true;
        }
        if (schema.getDimension().containsKey(datasource.getName())) {
            Optional<Dimension> dataSourceDim = schema.getDimension().get(datasource.getName()).stream()
                    .filter(d -> d.getName().equalsIgnoreCase(name)).findFirst();
            if (dataSourceDim.isPresent()) {
                return true;
            }
        }
        return false;
    }

    private static void expandWhere(MetricReq metricCommand, TableView tableView, SqlValidatorScope scope)
            throws Exception {
        if (metricCommand.getWhere() != null && !metricCommand.getWhere().isEmpty()) {
            SqlNode sqlNode = SemanticNode.parse(metricCommand.getWhere(), scope);
            Set<String> fieldWhere = new HashSet<>();
            FilterNode.getFilterField(sqlNode, fieldWhere);
            //super.tableView.getFilter().add(sqlNode);
            tableView.getFilter().add(sqlNode);
        }
    }

    public void render(MetricReq metricCommand, List<DataSource> dataSources, SqlValidatorScope scope,
                       SemanticSchema schema, boolean nonAgg) throws Exception {
        String queryWhere = metricCommand.getWhere();
        Set<String> whereFields = new HashSet<>();
        List<String> fieldWhere = new ArrayList<>();
        if (queryWhere != null && !queryWhere.isEmpty()) {
            SqlNode sqlNode = SemanticNode.parse(queryWhere, scope);
            FilterNode.getFilterField(sqlNode, whereFields);
            fieldWhere = whereFields.stream().collect(Collectors.toList());
        }
        if (dataSources.size() == 1) {
            DataSource dataSource = dataSources.get(0);
            super.tableView = renderOne("", fieldWhere, metricCommand.getMetrics(),
                    metricCommand.getDimensions(),
                    metricCommand.getWhere(), dataSource, scope, schema, nonAgg);
            return;
        }
        JoinRender joinRender = new JoinRender();
        joinRender.render(metricCommand, dataSources, scope, schema, nonAgg);
        super.tableView = joinRender.getTableView();
    }


}
