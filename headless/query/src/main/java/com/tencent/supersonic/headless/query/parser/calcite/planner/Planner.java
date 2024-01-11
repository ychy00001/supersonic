package com.tencent.supersonic.headless.query.parser.calcite.planner;


import com.tencent.supersonic.headless.api.query.enums.AggOption;
import com.tencent.supersonic.headless.query.persistence.pojo.QueryStatement;
import com.tencent.supersonic.headless.query.parser.calcite.schema.HeadlessSchema;

public interface Planner {

    void explain(QueryStatement queryStatement, AggOption aggOption) throws Exception;

    void simpleExplain(QueryStatement queryStatement, AggOption aggOption) throws Exception;

    String getSql();

    String getSourceId();

    HeadlessSchema findBest();
}
