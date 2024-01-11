package com.tencent.supersonic.headless.query.parser.calcite.planner;


import com.tencent.supersonic.headless.api.query.enums.AggOption;
import com.tencent.supersonic.headless.api.query.request.MetricReq;
import com.tencent.supersonic.headless.query.parser.calcite.sql.render.SimpleSqlRender;
import com.tencent.supersonic.headless.query.persistence.pojo.QueryStatement;
import com.tencent.supersonic.headless.query.parser.calcite.s2sql.Constants;
import com.tencent.supersonic.headless.query.parser.calcite.s2sql.DataSource;
import com.tencent.supersonic.headless.query.parser.calcite.schema.SchemaBuilder;
import com.tencent.supersonic.headless.query.parser.calcite.schema.HeadlessSchema;
import com.tencent.supersonic.headless.query.parser.calcite.sql.Renderer;
import com.tencent.supersonic.headless.query.parser.calcite.sql.TableView;
import com.tencent.supersonic.headless.query.parser.calcite.sql.node.DataSourceNode;
import com.tencent.supersonic.headless.query.parser.calcite.sql.node.SemanticNode;
import com.tencent.supersonic.headless.query.parser.calcite.sql.render.FilterRender;
import com.tencent.supersonic.headless.query.parser.calcite.sql.render.OutputRender;
import com.tencent.supersonic.headless.query.parser.calcite.sql.render.SourceRender;
import org.apache.calcite.sql.SqlNode;
import org.apache.calcite.sql.validate.SqlValidatorScope;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.Objects;
import java.util.Stack;

public class AggPlanner implements Planner {

    private MetricReq metricReq;
    private HeadlessSchema schema;
    private SqlValidatorScope scope;
    private Stack<TableView> dataSets = new Stack<>();
    private SqlNode parserNode;
    private String sourceId;
    private boolean isAgg = false;
    private AggOption aggOption = AggOption.DEFAULT;

    public AggPlanner(HeadlessSchema schema) {
        this.schema = schema;
    }

    public void parse() throws Exception {
        // find the match Datasource
        scope = SchemaBuilder.getScope(schema);
        List<DataSource> datasource = getMatchDataSource(scope);
        if (datasource == null || datasource.isEmpty()) {
            throw new Exception("datasource not found");
        }
        isAgg = getAgg(datasource.get(0));
        sourceId = String.valueOf(datasource.get(0).getSourceId());

        // build  level by level
        LinkedList<Renderer> builders = new LinkedList<>();
        builders.add(new SourceRender());
        builders.add(new FilterRender());
        builders.add(new OutputRender());
        ListIterator<Renderer> it = builders.listIterator();
        int i = 0;
        Renderer previous = null;
        while (it.hasNext()) {
            Renderer renderer = it.next();
            if (previous != null) {
                previous.render(metricReq, datasource, scope, schema, !isAgg);
                renderer.setTable(previous.builderAs(DataSourceNode.getNames(datasource) + "_" + String.valueOf(i)));
                i++;
            }
            previous = renderer;
        }
        builders.getLast().render(metricReq, datasource, scope, schema, !isAgg);
        parserNode = builders.getLast().builder();


    }

    public void simpleParse() throws Exception {
        // find the match Datasource
        scope = SchemaBuilder.getScope(schema);
        // 此处根据metricCommand中的dimensions和metrics对应的字符串找到合适的datasource
        List<DataSource> datasource = getMatchDataSourceNew(scope);
        if (datasource == null || datasource.isEmpty()) {
            throw new Exception("datasource not found");
        }
        isAgg = getAgg(datasource.get(0));
        sourceId = String.valueOf(datasource.get(0).getSourceId());

        // build  level by level
        LinkedList<Renderer> builders = new LinkedList<>();
        builders.add(new SimpleSqlRender());
        ListIterator<Renderer> it = builders.listIterator();
        int i = 0;
        Renderer previous = null;
        while (it.hasNext()) {
            Renderer renderer = it.next();
            if (previous != null) {
                previous.render(metricReq, datasource, scope, schema, !isAgg);
                renderer.setTable(previous.builderAs(DataSourceNode.getNames(datasource) + "_" + String.valueOf(i)));
                i++;
            }
            previous = renderer;
        }
        builders.getLast().render(metricReq, datasource, scope, schema, !isAgg);
        parserNode = builders.getLast().builder();
    }

    private List<DataSource> getMatchDataSource(SqlValidatorScope scope) throws Exception {
        return DataSourceNode.getMatchDataSources(scope, schema, metricReq);
    }

    private List<DataSource> getMatchDataSourceNew(SqlValidatorScope scope) throws Exception {
        return DataSourceNode.getMatchDataSourcesNew(scope, schema, metricReq);
    }

    private boolean getAgg(DataSource dataSource) {
        if (!AggOption.DEFAULT.equals(aggOption)) {
            return AggOption.isAgg(aggOption);
        }
        // default by dataSource time aggregation
        if (Objects.nonNull(dataSource.getAggTime()) && !dataSource.getAggTime().equalsIgnoreCase(
                Constants.DIMENSION_TYPE_TIME_GRANULARITY_NONE)) {
            if (!metricReq.isNativeQuery()) {
                return true;
            }
        }
        return isAgg;
    }

    @Override
    public void explain(QueryStatement queryStatement, AggOption aggOption) throws Exception {
        this.metricReq = queryStatement.getMetricReq();
        if (metricReq.getMetrics() == null) {
            metricReq.setMetrics(new ArrayList<>());
        }
        if (metricReq.getDimensions() == null) {
            metricReq.setDimensions(new ArrayList<>());
        }
        if (metricReq.getLimit() == null) {
            metricReq.setLimit(0L);
        }
        this.aggOption = aggOption;
        // build a parse Node
        parse();
        // optimizer
    }

    @Override
    public void simpleExplain(QueryStatement queryStatement, AggOption aggOption) throws Exception {
        this.metricReq = queryStatement.getMetricReq();
        if (metricReq.getMetrics() == null) {
            metricReq.setMetrics(new ArrayList<>());
        }
        if (metricReq.getDimensions() == null) {
            metricReq.setDimensions(new ArrayList<>());
        }
        if (metricReq.getLimit() == null) {
            metricReq.setLimit(0L);
        }
        this.aggOption = aggOption;
        // build a parse Node
        simpleParse();
        // optimizer
    }

    @Override
    public String getSql() {
        return SemanticNode.getSql(parserNode);
    }

    @Override
    public String getSourceId() {
        return sourceId;
    }

    @Override
    public HeadlessSchema findBest() {
        return schema;
    }
}
