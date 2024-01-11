package com.tencent.supersonic.headless.query.parser;

import com.tencent.supersonic.headless.api.query.enums.AggOption;
import com.tencent.supersonic.headless.model.domain.Catalog;
import com.tencent.supersonic.headless.query.persistence.pojo.QueryStatement;

public interface SqlParser {

    QueryStatement explain(QueryStatement queryStatement, AggOption aggOption, Catalog catalog) throws Exception;

    QueryStatement simpleExplain(QueryStatement queryStatement, AggOption isAgg, Catalog catalog) throws Exception;
}
