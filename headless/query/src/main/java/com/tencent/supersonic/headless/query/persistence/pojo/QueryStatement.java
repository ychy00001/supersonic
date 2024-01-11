package com.tencent.supersonic.headless.query.persistence.pojo;

import com.tencent.supersonic.headless.api.query.request.MetricReq;
import com.tencent.supersonic.headless.api.query.request.ParseSqlReq;
import com.tencent.supersonic.headless.api.query.request.QueryStructReq;
import lombok.Data;
import org.apache.commons.lang3.tuple.ImmutablePair;

import java.util.List;

@Data
public class QueryStatement {

    private List<Long> modelIds;
    private String sql = "";
    private String sourceId = "";
    private String errMsg = "";
    private Boolean ok;
    private QueryStructReq queryStructReq;
    private MetricReq metricReq;
    private ParseSqlReq parseSqlReq;
    private Integer status = 0;
    private Boolean isS2SQL = false;
    private List<ImmutablePair<String, String>> timeRanges;

    public boolean isOk() {
        this.ok = "".equals(errMsg) && !"".equals(sql);
        return ok;
    }

    public QueryStatement error(String msg) {
        this.setErrMsg(msg);
        return this;
    }
}
