package com.tencent.supersonic.common.util.jsqlparser;

import net.sf.jsqlparser.JSQLParserException;
import org.junit.Assert;
import org.junit.jupiter.api.Test;

/**
 * SqlParserSelectHelper Test
 */
class SqlParserSelectFunctionHelperTest {

    @Test
    void hasAggregateFunction() throws JSQLParserException {

        String sql = "select 部门,sum (访问次数) from 超音数 where 数据日期 = '2023-08-08' "
                + "and 用户 =alice and 发布日期 ='11' group by 部门 limit 1";
        boolean hasAggregateFunction = SqlParserSelectFunctionHelper.hasAggregateFunction(sql);

        Assert.assertEquals(hasAggregateFunction, true);
        sql = "select 部门,count (访问次数) from 超音数 where 数据日期 = '2023-08-08' "
                + "and 用户 =alice and 发布日期 ='11' group by 部门 limit 1";
        hasAggregateFunction = SqlParserSelectFunctionHelper.hasAggregateFunction(sql);
        Assert.assertEquals(hasAggregateFunction, true);

        sql = "SELECT count(1) FROM s2 WHERE sys_imp_date = '2023-08-08' AND user_id = 'alice'"
                + " AND publish_date = '11' ORDER BY pv DESC LIMIT 1";
        hasAggregateFunction = SqlParserSelectFunctionHelper.hasAggregateFunction(sql);
        Assert.assertEquals(hasAggregateFunction, true);

        sql = "SELECT department, user_id, field_a FROM s2 WHERE sys_imp_date = '2023-08-08' "
                + "AND user_id = 'alice' AND publish_date = '11' ORDER BY pv DESC LIMIT 1";
        hasAggregateFunction = SqlParserSelectFunctionHelper.hasAggregateFunction(sql);
        Assert.assertEquals(hasAggregateFunction, false);

        sql = "SELECT department, user_id, field_a FROM s2 WHERE sys_imp_date = '2023-08-08'"
                + " AND user_id = 'alice' AND publish_date = '11'";
        hasAggregateFunction = SqlParserSelectFunctionHelper.hasAggregateFunction(sql);
        Assert.assertEquals(hasAggregateFunction, false);

        sql = "SELECT user_name, pv FROM t_34 WHERE sys_imp_date <= '2023-09-03' "
                + "AND sys_imp_date >= '2023-08-04' GROUP BY user_name ORDER BY sum(pv) DESC LIMIT 10";
        hasAggregateFunction = SqlParserSelectFunctionHelper.hasAggregateFunction(sql);
        Assert.assertEquals(hasAggregateFunction, true);
    }

    @Test
    void hasFunction() throws JSQLParserException {

        String sql = "select 部门,sum (访问次数) from 超音数 where 数据日期 = '2023-08-08' "
                + "and 用户 =alice and 发布日期 ='11' group by 部门 limit 1";
        boolean hasFunction = SqlParserSelectFunctionHelper.hasFunction(sql, "sum");

        Assert.assertEquals(hasFunction, true);
        sql = "select 部门,count (访问次数) from 超音数 where 数据日期 = '2023-08-08' "
                + "and 用户 =alice and 发布日期 ='11' group by 部门 limit 1";
        hasFunction = SqlParserSelectFunctionHelper.hasFunction(sql, "count");
        Assert.assertEquals(hasFunction, true);

        sql = "select 部门,count (*) from 超音数 where 数据日期 = '2023-08-08' "
                + "and 用户 =alice and 发布日期 ='11' group by 部门 limit 1";
        hasFunction = SqlParserSelectFunctionHelper.hasFunction(sql, "count");
        Assert.assertEquals(hasFunction, true);

        sql = "SELECT user_name, pv FROM t_34 WHERE sys_imp_date <= '2023-09-03' "
                + "AND sys_imp_date >= '2023-08-04' GROUP BY user_name ORDER BY sum(pv) DESC LIMIT 10";
        hasFunction = SqlParserSelectFunctionHelper.hasFunction(sql, "sum");
        Assert.assertEquals(hasFunction, false);

        sql = "select 部门,min (访问次数) from 超音数 where 数据日期 = '2023-08-08' "
                + "and 用户 =alice and 发布日期 ='11' group by 部门 limit 1";
        hasFunction = SqlParserSelectFunctionHelper.hasFunction(sql, "min");

        Assert.assertEquals(hasFunction, true);
    }
}
