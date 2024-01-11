package com.tencent.supersonic.headless.model.rest;

import com.tencent.supersonic.auth.api.authentication.pojo.User;
import com.tencent.supersonic.auth.api.authentication.utils.UserHolder;
import com.tencent.supersonic.headless.model.domain.CollectService;
import com.tencent.supersonic.headless.model.domain.dataobject.CollectDO;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/***
 * 创建收藏指标的逻辑
 */
@RestController
@RequestMapping("/api/semantic/collect")
public class CollectController {


    private CollectService collectService;

    public CollectController(CollectService collectService) {
        this.collectService = collectService;
    }

    @PostMapping("/createCollectionIndicators")
    public boolean createCollectionIndicators(@RequestBody CollectDO collectDO,
            HttpServletRequest request,
            HttpServletResponse response) {
        User user = UserHolder.findUser(request, response);
        return collectService.createCollectionIndicators(user, collectDO.getId());
    }

    @DeleteMapping("/deleteCollectionIndicators/{id}")
    public boolean deleteCollectionIndicators(@PathVariable Long id,
            HttpServletRequest request,
            HttpServletResponse response) {
        User user = UserHolder.findUser(request, response);
        return collectService.deleteCollectionIndicators(user, id);
    }

}
