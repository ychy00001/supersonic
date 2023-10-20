package com.tencent.supersonic.semantic.query.utils;

import com.tencent.supersonic.semantic.api.model.enums.TimeDimensionEnum;
import com.tencent.supersonic.semantic.api.query.request.QueryStructReq;
import com.tencent.supersonic.common.pojo.Aggregator;

import java.util.List;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import static com.tencent.supersonic.common.pojo.Constants.JOIN_UNDERLINE;

@Component
@Slf4j
public class SqlGenerateUtils {

    public static String getUnionSelect(QueryStructReq queryStructCmd) {
        StringBuilder sb = new StringBuilder();
        int locate = 0;
        for (String group : queryStructCmd.getGroups()) {
            if (group.contains(JOIN_UNDERLINE)) {
                group = group.split(JOIN_UNDERLINE)[1];
            }
            if (!TimeDimensionEnum.getNameList().contains(group)) {
                locate++;
                sb.append(group).append(" as ").append("name").append(locate).append(",");
            } else {
                sb.append(group).append(",");
            }
        }
        locate = 0;
        for (Aggregator agg : queryStructCmd.getAggregators()) {
            locate++;
            sb.append(agg.getColumn()).append(" as ").append("value").append(locate).append(",");
        }
        String selectSql = sb.substring(0, sb.length() - 1);
        log.info("union select sql {}", selectSql);
        return selectSql;
    }

    public String getLimit(QueryStructReq queryStructCmd) {
        if (queryStructCmd.getLimit() > 0) {
            return " limit " + queryStructCmd.getLimit();
        }
        return "";
    }

    public String getSelect(QueryStructReq queryStructCmd) {
        String aggStr = queryStructCmd.getAggregators().stream().map(this::getSelectField)
                .collect(Collectors.joining(","));
        return CollectionUtils.isEmpty(queryStructCmd.getGroups()) ? aggStr
                : String.join(",", queryStructCmd.getGroups()) + "," + aggStr;
    }

    public String getSelectField(final Aggregator agg) {
        if (CollectionUtils.isEmpty(agg.getArgs())) {
            return agg.getFunc() + " ( " + agg.getColumn() + " ) AS " + agg.getColumn() + " ";
        }
        return agg.getFunc() + " ( " + agg.getArgs().stream().map(arg ->
                arg.equals(agg.getColumn()) ? arg : (StringUtils.isNumeric(arg) ? arg : ("'" + arg + "'"))
        ).collect(Collectors.joining(",")) + " ) AS " + agg.getColumn() + " ";
    }

    public String getGroupBy(QueryStructReq queryStructCmd) {
        List<String> groups = queryStructCmd.getGroups();
        if (CollectionUtils.isEmpty(groups)) {
            return "";
        }
        StringBuilder sb = new StringBuilder("group by ");
        for(String group : groups){
            if(group.contains(" AS ")){
                group = group.split(" AS ")[1];
            }
            sb.append(group).append(", ");
        }
        return sb.delete(sb.length()-2, sb.length()).toString();
    }

    public String getOrderBy(QueryStructReq queryStructCmd) {
        if (CollectionUtils.isEmpty(queryStructCmd.getOrders())) {
            return "";
        }
        return "order by " + queryStructCmd.getOrders().stream()
                .map(order -> " " + order.getColumn() + " " + order.getDirection() + " ")
                .collect(Collectors.joining(","));
    }


}
