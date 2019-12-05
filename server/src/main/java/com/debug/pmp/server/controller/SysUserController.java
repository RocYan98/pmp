package com.debug.pmp.server.controller;

import com.debug.pmp.common.response.BaseResponse;
import com.debug.pmp.common.response.StatusCode;
import com.debug.pmp.common.utils.ValidatorUtil;
import com.debug.pmp.model.entity.SysUser;
import com.debug.pmp.server.annotation.LogAnnotation;
import com.debug.pmp.server.service.SysUserService;
import com.debug.pmp.server.shiro.ShiroUtil;
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
@RequestMapping("/sys/user")
public class SysUserController extends AbstractController {


    @Autowired
    private SysUserService sysUserService;

    @GetMapping("/info")
    public BaseResponse getInfo() {
        BaseResponse baseResponse = new BaseResponse(StatusCode.Success);
        HashMap<Object, Object> map = Maps.newHashMap();

        try {
            SysUser user = ShiroUtil.getUser();
            map.put("user", user);
            baseResponse.setData(map);
        } catch (Exception e) {
            return new BaseResponse(StatusCode.Fail);
        }

        return baseResponse;
    }

    @PostMapping("/password")
    @LogAnnotation("修改登陆密码")
    public BaseResponse updatePwd(@RequestParam("password") String oldPwd,
                                  @RequestParam("newPassword") String newPwd) {
        BaseResponse baseResponse = new BaseResponse(StatusCode.Success);
        if (StringUtil.isNullOrEmpty(oldPwd) || StringUtil.isNullOrEmpty(newPwd)) {
            return new BaseResponse(StatusCode.PasswordCanNotBlank);
        }

        try {
            SysUser user = ShiroUtil.getUser();
            String oldPassword = ShiroUtil.sha256(oldPwd, user.getSalt());
            if (!user.getPassword().equals(oldPassword)) {
                return new BaseResponse(StatusCode.OldPasswordNotMatch);
            }
            sysUserService.updatePassword(user.getUserId(), oldPassword, ShiroUtil.sha256(newPwd, user.getSalt()));
        } catch (Exception e) {
            errorLog(e);
            return new BaseResponse(StatusCode.UpdatePasswordFail);
        }

        return baseResponse;
    }

    @GetMapping("/list")
    @RequiresPermissions("sys:user:list")
    public BaseResponse list(@RequestParam Map<String, String> params) {
        BaseResponse response = new BaseResponse(StatusCode.Success);

        HashMap<Object, Object> map = Maps.newHashMap();
        try {
            map.put("page", sysUserService.queryPage(params));
            response.setData(map);
        } catch (Exception e) {
            errorLog(e);
            return new BaseResponse(StatusCode.Fail.getCode(), e.getMessage());
        }

        return response;
    }

    @PostMapping("/save")
    @RequiresPermissions("sys:user:save")
    @LogAnnotation("新增用户")
    public BaseResponse save(@RequestBody @Validated SysUser sysUser, BindingResult result) {
        BaseResponse baseResponse = new BaseResponse(StatusCode.Success);

        String res = ValidatorUtil.checkResult(result);
        if (!StringUtil.isNullOrEmpty(res)) {
            return new BaseResponse(StatusCode.InvalidParams.getCode(), res);
        }

        try {
            sysUserService.saveUser(sysUser);
        } catch (Exception e) {
            errorLog(e);
            return new BaseResponse(StatusCode.Fail.getCode(), e.getMessage());
        }

        return baseResponse;
    }

    @PostMapping("/delete")
    @RequiresPermissions("sys:user:delete")
    @LogAnnotation("删除用户")
    public BaseResponse delete(@RequestBody Long[] userIdList) {

        try {
            sysUserService.removeBatch(Arrays.asList(userIdList));
        } catch (Exception e) {
            errorLog(e);
            return new BaseResponse(StatusCode.Fail.getCode(), e.getMessage());
        }

        BaseResponse baseResponse = new BaseResponse(StatusCode.Success);
        return baseResponse;
    }

    @GetMapping("/info/{userId}")
    @RequiresPermissions("sys:user:info")
    public BaseResponse info(@PathVariable Long userId) {
        BaseResponse baseResponse = new BaseResponse(StatusCode.Success);

        try {
            HashMap<Object, Object> map = Maps.newHashMap();
            map.put("user", sysUserService.queryUser(userId));
            baseResponse.setData(map);
        } catch (Exception e) {
            errorLog(e);
            return new BaseResponse(StatusCode.Fail.getCode(), e.getMessage());
        }

        return baseResponse;
    }

    @PostMapping("/update")
    @RequiresPermissions("sys:user:update")
    @LogAnnotation("修改用户")
    public BaseResponse update(@RequestBody @Validated SysUser sysUser, BindingResult result) {
        BaseResponse baseResponse = new BaseResponse(StatusCode.Success);

        String res = ValidatorUtil.checkResult(result);
        if (!StringUtil.isNullOrEmpty(res)) {
            return new BaseResponse(StatusCode.InvalidParams.getCode(), res);
        }

        try {
            sysUserService.updateUser(sysUser);
        } catch (Exception e) {
            errorLog(e);
            return new BaseResponse(StatusCode.Fail.getCode(), e.getMessage());
        }

        return baseResponse;
    }

    @PostMapping("/psd/reset")
    @RequiresPermissions("sys:user:resetPsd")
    @LogAnnotation("重置用户密码")
    public BaseResponse reset(@RequestBody Long[] userId) {
        BaseResponse baseResponse = new BaseResponse(StatusCode.Success);

        try {
            sysUserService.resetPwd(Arrays.asList(userId));
        } catch (Exception e) {
            errorLog(e);
            return new BaseResponse(StatusCode.Fail.getCode(), e.getMessage());
        }

        return baseResponse;
    }
}
