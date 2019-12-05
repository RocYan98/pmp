package com.debug.pmp.server.controller;


import com.debug.pmp.common.response.BaseResponse;
import com.debug.pmp.common.response.StatusCode;
import com.debug.pmp.server.service.SysLogService;
import com.google.common.collect.Maps;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/sys/log")
public class SysLogController extends AbstractController {

    @Autowired
    private SysLogService sysLogService;

    @GetMapping("/list")
    @RequiresPermissions("sys:log:list")
    public BaseResponse list(@RequestParam Map params) {
        BaseResponse baseResponse = new BaseResponse(StatusCode.Success);

        try {
            HashMap<Object, Object> map = Maps.newHashMap();
            map.put("page", sysLogService.queryPage(params));
            baseResponse.setData(map);
        } catch (Exception e) {
            errorLog(e);
            return new BaseResponse(StatusCode.Fail.getCode(), e.getMessage());
        }

        return baseResponse;
    }

    @PostMapping("/truncate")
    public BaseResponse truncate() {
        BaseResponse baseResponse = new BaseResponse(StatusCode.Success);

        return baseResponse;
    }
}
