package com.debug.pmp.server.controller;

import com.debug.pmp.common.response.BaseResponse;
import com.debug.pmp.common.response.StatusCode;
import com.debug.pmp.common.utils.ValidatorUtil;
import com.debug.pmp.model.entity.SysRole;
import com.debug.pmp.server.annotation.LogAnnotation;
import com.debug.pmp.server.service.SysRoleService;
import com.google.common.collect.Maps;
import io.netty.util.internal.StringUtil;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/sys/role")
public class SysRoleController extends AbstractController {

    @Autowired
    private SysRoleService sysRoleService;

    @GetMapping("/list")
    @RequiresPermissions("sys:role:list")
    public BaseResponse list(@RequestParam Map<String, Object> params) {
        BaseResponse baseResponse = new BaseResponse(StatusCode.Success);

        try {
            HashMap<Object, Object> map = Maps.newHashMap();
            map.put("page", sysRoleService.queryPage(params));
            baseResponse.setData(map);
        } catch (Exception e) {
            errorLog(e);
            return new BaseResponse(StatusCode.Fail.getCode(), e.getMessage());
        }


        return baseResponse;
    }

    @PostMapping("/save")
    @RequiresPermissions("sys:role:save")
    @LogAnnotation("新增角色")
    public BaseResponse save(@RequestBody @Validated SysRole sysRole, BindingResult result) {
        BaseResponse baseResponse = new BaseResponse(StatusCode.Success);

        String res = ValidatorUtil.checkResult(result);
        if (!StringUtil.isNullOrEmpty(res)) {
            return new BaseResponse(StatusCode.InvalidParams.getCode(), res);
        }

        try {
            sysRoleService.saveRole(sysRole);
        } catch (Exception e) {
            errorLog(e);
            return new BaseResponse(StatusCode.Fail.getCode(), e.getMessage());
        }

        return baseResponse;
    }

    @GetMapping("/info/{roleId}")
    @RequiresPermissions("sys:role:info")
    public BaseResponse info(@PathVariable Long roleId) {
        BaseResponse baseResponse = new BaseResponse(StatusCode.Success);

        try {
            HashMap<Object, Object> map = Maps.newHashMap();
            map.put("role", sysRoleService.queryRoleDeptMenu(roleId));
            baseResponse.setData(map);
        } catch (Exception e) {
            errorLog(e);
            return new BaseResponse(StatusCode.Fail.getCode(), e.getMessage());
        }

        return baseResponse;
    }

    @PostMapping("/update")
    @RequiresPermissions("sys:role:update")
    @LogAnnotation("修改角色")
    public BaseResponse update(@RequestBody @Validated SysRole sysRole, BindingResult result) {
        BaseResponse baseResponse = new BaseResponse(StatusCode.Success);

        String res = ValidatorUtil.checkResult(result);
        if (!StringUtil.isNullOrEmpty(res)) {
            return new BaseResponse(StatusCode.InvalidParams.getCode(), res);
        }

        try {
            sysRoleService.updateRole(sysRole);
        } catch (Exception e) {
            errorLog(e);
            return new BaseResponse(StatusCode.Fail.getCode(), e.getMessage());
        }

        return baseResponse;
    }

    @PostMapping("/delete")
    @RequiresPermissions("sys:role:delete")
    @LogAnnotation("删除角色")
    public BaseResponse delete(@RequestBody Long[] roleIdList) {
        BaseResponse baseResponse = new BaseResponse(StatusCode.Success);

        try {
            sysRoleService.removeBatch(Arrays.asList(roleIdList));
        } catch (Exception e) {
            errorLog(e);
            return new BaseResponse(StatusCode.Fail.getCode(), e.getMessage());
        }

        return baseResponse;
    }

    @GetMapping("/select")
    public BaseResponse select() {
        BaseResponse baseResponse = new BaseResponse(StatusCode.Success);

        try {
            HashMap<Object, Object> map = Maps.newHashMap();
            map.put("list", sysRoleService.list());
            baseResponse.setData(map);
        } catch (Exception e) {
            errorLog(e);
            return new BaseResponse(StatusCode.Fail.getCode(), e.getMessage());
        }

        return baseResponse;
    }
}
