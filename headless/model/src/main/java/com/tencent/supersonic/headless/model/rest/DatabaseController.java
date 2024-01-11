package com.tencent.supersonic.headless.model.rest;

import com.tencent.supersonic.auth.api.authentication.pojo.User;
import com.tencent.supersonic.auth.api.authentication.utils.UserHolder;
import com.tencent.supersonic.headless.api.model.request.DatabaseReq;
import com.tencent.supersonic.headless.api.model.request.SqlExecuteReq;
import com.tencent.supersonic.headless.api.model.response.DatabaseResp;
import com.tencent.supersonic.headless.api.model.response.QueryResultWithSchemaResp;
import com.tencent.supersonic.headless.model.domain.DatabaseService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.DeleteMapping;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@RestController
@RequestMapping("/api/semantic/database")
public class DatabaseController {


    private DatabaseService databaseService;

    public DatabaseController(DatabaseService databaseService) {
        this.databaseService = databaseService;
    }

    @PostMapping("/testConnect")
    public boolean testConnect(@RequestBody DatabaseReq databaseReq,
            HttpServletRequest request,
            HttpServletResponse response) {
        User user = UserHolder.findUser(request, response);
        return databaseService.testConnect(databaseReq, user);
    }

    @PostMapping("/createOrUpdateDatabase")
    public DatabaseResp createOrUpdateDatabase(@RequestBody DatabaseReq databaseReq,
            HttpServletRequest request,
            HttpServletResponse response) {
        User user = UserHolder.findUser(request, response);
        return databaseService.createOrUpdateDatabase(databaseReq, user);
    }

    @GetMapping("/{id}")
    public DatabaseResp getDatabase(@PathVariable("id") Long id) {
        return databaseService.getDatabase(id);
    }

    @GetMapping("/getDatabaseList")
    public List<DatabaseResp> getDatabaseList(HttpServletRequest request,
                                             HttpServletResponse response) {
        User user = UserHolder.findUser(request, response);
        return databaseService.getDatabaseList(user);
    }

    @DeleteMapping("/{id}")
    public boolean deleteDatabase(@PathVariable("id") Long id) {
        databaseService.deleteDatabase(id);
        return true;
    }

    @PostMapping("/executeSql")
    public QueryResultWithSchemaResp executeSql(@RequestBody SqlExecuteReq sqlExecuteReq,
                                                HttpServletRequest request,
                                                HttpServletResponse response) {
        User user = UserHolder.findUser(request, response);
        return databaseService.executeSql(sqlExecuteReq.getSql(), sqlExecuteReq.getId(), user);
    }

    @RequestMapping("/getDbNames/{id}")
    public QueryResultWithSchemaResp getDbNames(@PathVariable("id") Long id) {
        return databaseService.getDbNames(id);
    }

    @RequestMapping("/getTables/{id}/{db}")
    public QueryResultWithSchemaResp getTables(@PathVariable("id") Long id,
            @PathVariable("db") String db) {
        return databaseService.getTables(id, db);
    }

    @RequestMapping("/getColumns/{id}/{db}/{table}")
    public QueryResultWithSchemaResp getColumns(@PathVariable("id") Long id,
            @PathVariable("db") String db,
            @PathVariable("table") String table) {
        return databaseService.getColumns(id, db, table);
    }

}
