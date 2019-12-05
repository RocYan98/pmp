package com.debug.pmp.server.controller;

import com.debug.pmp.common.response.BaseResponse;
import com.debug.pmp.common.response.StatusCode;
import com.debug.pmp.model.entity.SysDept;
import com.debug.pmp.server.annotation.LogAnnotation;
import com.debug.pmp.server.service.SysDeptService;
import com.google.common.collect.Maps;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping("/sys/dept")
public class SysDeptController extends AbstractController {

    @Autowired
    private SysDeptService sysDeptService;

    @GetMapping("/info")
    @RequiresPermissions("sys:dept:info")
    public BaseResponse info() {
        BaseResponse baseResponse = new BaseResponse(StatusCode.Success);
        Long deptId = 0L;
        HashMap<String, Object> resMap = Maps.newHashMap();

        resMap.put("deptId", deptId);

        baseResponse.setData(resMap);

        return baseResponse;
    }

    @GetMapping("/list")
    @RequiresPermissions("sys:dept:list")
    public List<SysDept> list() {
        return sysDeptService.queryAll();
    }

    @GetMapping("/select")
    public BaseResponse select() {
        BaseResponse baseResponse = new BaseResponse(StatusCode.Success);
        HashMap<String, Object> resMap = Maps.newHashMap();

        try {
            List<SysDept> deptList = sysDeptService.queryAll();
            resMap.put("deptList", deptList);
            baseResponse.setData(resMap);

        } catch (Exception e) {
            errorLog(e);
            return new BaseResponse(StatusCode.Fail.getCode(), e.getMessage());
        }


        return baseResponse;
    }

    @PostMapping(value = "/save", consumes = MediaType.APPLICATION_JSON_VALUE)
    @RequiresPermissions("sys:dept:save")
    @LogAnnotation("新增部门")
    public BaseResponse save(@RequestBody @Validated SysDept sysDept, BindingResult result) {
        BaseResponse baseResponse = new BaseResponse(StatusCode.Success);

        if (result.hasErrors()) {
            return new BaseResponse(StatusCode.InvalidParams);
        }

        try {
            sysDeptService.save(sysDept);
        } catch (Exception e) {
            errorLog(e);
            return new BaseResponse(StatusCode.Fail.getCode(), e.getMessage());
        }

        return baseResponse;
    }

    @GetMapping("/detail/{deptId}")
    public BaseResponse detail(@PathVariable Long deptId) {
        BaseResponse baseResponse = new BaseResponse(StatusCode.Success);

        try {
            SysDept dept = sysDeptService.getById(deptId);
            HashMap<Object, Object> map = Maps.newHashMap();
            map.put("dept", dept);
            baseResponse.setData(map);
        } catch (Exception e) {
            return new BaseResponse(StatusCode.Fail.getCode(), e.getMessage());
        }

        return baseResponse;
    }

    @PostMapping(value = "/update", consumes = MediaType.APPLICATION_JSON_VALUE)
    @RequiresPermissions("sys:dept:update")
    @LogAnnotation("修改部门")
    public BaseResponse update(@RequestBody @Validated SysDept sysDept, BindingResult result) {
        BaseResponse baseResponse = new BaseResponse(StatusCode.Success);

        if (result.hasErrors()) {
            return new BaseResponse(StatusCode.InvalidParams);
        }

        if (sysDept.getDeptId() == null || sysDept.getDeptId() <= 0) {
            return new BaseResponse(StatusCode.InvalidParams);
        }

        try {
            sysDeptService.updateById(sysDept);
        } catch (Exception e) {
            errorLog(e);
            return new BaseResponse(StatusCode.Fail.getCode(), e.getMessage());
        }

        return baseResponse;
    }

    @PostMapping("/delete")
    @RequiresPermissions("sys:dept:delete")
    @LogAnnotation("删除部门")
    public BaseResponse delete(@RequestParam Long deptId) {
        BaseResponse baseResponse = new BaseResponse(StatusCode.Success);

        if (deptId == null || deptId <= 0) {
            return new BaseResponse(StatusCode.InvalidParams);
        }

        try {
            List<SysDept> deptList = sysDeptService.querryByParentId(deptId);
            log.info("查找到的子部门:{}", deptList);

            if (deptList != null && !deptList.isEmpty()) {
                return new BaseResponse(StatusCode.DeptHasSubDeptCanNotBeDelete);
            }

            sysDeptService.removeById(deptId);
        } catch (Exception e) {
            errorLog(e);
            return new BaseResponse(StatusCode.Fail.getCode(), e.getMessage());
        }

        return baseResponse;
    }
}
