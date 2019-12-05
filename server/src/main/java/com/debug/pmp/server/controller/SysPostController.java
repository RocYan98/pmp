package com.debug.pmp.server.controller;

import com.debug.pmp.common.response.BaseResponse;
import com.debug.pmp.common.response.StatusCode;
import com.debug.pmp.common.utils.PageUtil;
import com.debug.pmp.model.entity.SysPost;
import com.debug.pmp.server.annotation.LogAnnotation;
import com.debug.pmp.server.service.SysPostService;
import com.google.common.collect.Maps;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/sys/post")
public class SysPostController extends AbstractController {

    @Autowired
    private SysPostService sysPostService;

    /**
     * 分也列表模糊查询
     *
     * @return
     */
    @GetMapping("/list")
    @RequiresPermissions("sys:post:list")
    public BaseResponse list(@RequestParam Map<String, Object> params) {
        BaseResponse baseResponse = new BaseResponse(StatusCode.Success);
        HashMap<String, Object> map = Maps.newHashMap();

        try {
            PageUtil pageUtil = sysPostService.queryPage(params);
            map.put("page", pageUtil);
            baseResponse.setData(map);
        } catch (Exception e) {
            errorLog(e);
            System.out.println(e);
            return new BaseResponse(StatusCode.Fail.getCode(), e.getMessage());
        }

        return baseResponse;
    }

    @PostMapping(value = "/save", consumes = MediaType.APPLICATION_JSON_VALUE)
    @RequiresPermissions("sys:post:save")
    @LogAnnotation("新增岗位")
    public BaseResponse save(@RequestBody @Validated SysPost sysPost, BindingResult result) {
        BaseResponse baseResponse = new BaseResponse(StatusCode.Success, "保存成功!");


        if (result != null && result.hasErrors()) {
            StringBuilder sb = new StringBuilder();
            result.getAllErrors().stream().forEach(error -> sb.append(error.getDefaultMessage()).append("<br>"));
            return new BaseResponse(StatusCode.InvalidParams.getCode(), sb.toString());
        }

        try {
            sysPostService.savePost(sysPost);
        } catch (RuntimeException e) {
            errorLog(e);
            return new BaseResponse(StatusCode.PostCodeHasExist);
        }

        return baseResponse;
    }

    @GetMapping("/info/{postId}")
    @RequiresPermissions("sys:post:info")
    public BaseResponse info(@PathVariable Long postId) {
        BaseResponse baseResponse = new BaseResponse(StatusCode.Success);

        if (postId == null || postId <= 0) {
            return new BaseResponse(StatusCode.InvalidParams);
        }

        HashMap<String, Object> resMap = Maps.newHashMap();
        try {
            resMap.put("post", sysPostService.getById(postId));
            baseResponse.setData(resMap);
        } catch (Exception e) {
            errorLog(e);
            return new BaseResponse(StatusCode.Fail.getCode(), e.getMessage());
        }

        return baseResponse;
    }

    @PostMapping(value = "/update", consumes = MediaType.APPLICATION_JSON_VALUE)
    @RequiresPermissions("sys:post:update")
    @LogAnnotation("修改岗位")
    public BaseResponse update(@RequestBody @Validated SysPost sysPost, BindingResult result) {
        BaseResponse baseResponse = new BaseResponse(StatusCode.Success);

        if (result.hasErrors()) {
            return new BaseResponse(StatusCode.InvalidParams);
        }

        if (sysPost.getPostId() == null || sysPost.getPostId() <= 0) {
            return new BaseResponse(StatusCode.InvalidParams);
        }

        try {
            sysPostService.updatePost(sysPost);
        } catch (Exception e) {
            errorLog(e);
            return new BaseResponse(StatusCode.Fail.getCode(), e.getMessage());
        }

        return baseResponse;
    }

    @PostMapping(value = "/delete", consumes = MediaType.APPLICATION_JSON_VALUE)
    @RequiresPermissions("sys:post:delete")
    @LogAnnotation("删除岗位")
    public BaseResponse delete(@RequestBody Long[] ids) {
        BaseResponse baseResponse = new BaseResponse(StatusCode.Success);

        try {
            sysPostService.removeBatch(ids);
            log.info("删除的岗位id:{}", Arrays.toString(ids));
        } catch (Exception e) {
            errorLog(e);
            return new BaseResponse(StatusCode.Fail.getCode(), e.getMessage());
        }

        return baseResponse;
    }

    @GetMapping("/select")
    public BaseResponse select() {
        BaseResponse Response = new BaseResponse(StatusCode.Success);

        try {
            HashMap<Object, Object> map = Maps.newHashMap();
            map.put("list", sysPostService.list());
            Response.setData(map);
        } catch (Exception e) {
            errorLog(e);
            return new BaseResponse(StatusCode.Fail.getCode(), e.getMessage());
        }

        return Response;
    }
}
