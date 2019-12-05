package com.debug.pmp.server.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.debug.pmp.common.response.BaseResponse;
import com.debug.pmp.common.response.StatusCode;
import com.debug.pmp.common.utils.Constant;
import com.debug.pmp.model.entity.SysMenu;
import com.debug.pmp.server.annotation.LogAnnotation;
import com.debug.pmp.server.service.SysMenuService;
import com.debug.pmp.server.shiro.ShiroUtil;
import com.google.common.collect.Maps;
import io.netty.util.internal.StringUtil;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping("/sys/menu")
public class SysMenuController extends AbstractController {

    @Autowired
    private SysMenuService sysMenuService;

    @GetMapping("/list")
    @RequiresPermissions("sys:menu:list")
    public List<SysMenu> list() {
        return sysMenuService.queryAll();
    }

    @GetMapping("/select")
    public BaseResponse select() {
        BaseResponse baseResponse = new BaseResponse(StatusCode.Success);

        try {
            List<SysMenu> list = sysMenuService.list(new LambdaQueryWrapper<SysMenu>().ne(SysMenu::getType, 2));
            SysMenu root = new SysMenu();
            root.setMenuId(Constant.TOP_MENU_ID);
            root.setName(Constant.TOP_MENU_NAME);
            root.setParentId(-1L);
            root.setOpen(true);
            list.add(root);


            HashMap<Object, Object> map = Maps.newHashMap();
            map.put("menuList", list);
            baseResponse.setData(map);
        } catch (Exception e) {
            errorLog(e);
            return new BaseResponse(StatusCode.Fail.getCode(), e.getMessage());
        }


        return baseResponse;
    }

    @PostMapping("/save")
    @RequiresPermissions("sys:menu:save")
    public BaseResponse save(@RequestBody SysMenu sysMenu) {
        BaseResponse baseResponse = new BaseResponse(StatusCode.Success);

        String result = validateForm(sysMenu);

        if (!StringUtil.isNullOrEmpty(result)) {
            return new BaseResponse(StatusCode.Fail.getCode(), result);
        }

        try {
            sysMenuService.save(sysMenu);
        } catch (Exception e) {

        }


        return baseResponse;
    }

    @GetMapping("/info/{menuId}")
    @RequiresPermissions("sys:menu:info")
    public BaseResponse info(@PathVariable Long menuId) {
        BaseResponse baseResponse = new BaseResponse(StatusCode.Success);

        if (menuId == null || menuId <= 0) {
            return new BaseResponse(StatusCode.InvalidParams);
        }

        try {
            HashMap<Object, Object> map = Maps.newHashMap();
            map.put("menu", sysMenuService.getById(menuId));
            baseResponse.setData(map);
        } catch (Exception e) {
            errorLog(e);
            return new BaseResponse(StatusCode.Fail.getCode(), e.getMessage());
        }

        return baseResponse;
    }

    @PostMapping("/update")
    @RequiresPermissions("sys:menu:update")
    @LogAnnotation("修改菜单")
    public BaseResponse update(@RequestBody SysMenu sysMenu) {
        BaseResponse baseResponse = new BaseResponse(StatusCode.Success);

        String result = validateForm(sysMenu);

        if (!StringUtil.isNullOrEmpty(result)) {
            return new BaseResponse(StatusCode.Fail.getCode(), result);
        }

        try {
            sysMenuService.updateById(sysMenu);
        } catch (Exception e) {
            errorLog(e);
            return new BaseResponse(StatusCode.Fail.getCode(), e.getMessage());
        }

        return baseResponse;
    }

    @PostMapping("/delete")
    @RequiresPermissions("sys:menu:delete")
    @LogAnnotation("删除菜单")
    public BaseResponse delete(@RequestParam Long menuId) {
        BaseResponse baseResponse = new BaseResponse(StatusCode.Success);

        try {
            SysMenu entity = sysMenuService.getById(menuId);

            if (entity == null) {
                return new BaseResponse(StatusCode.InvalidParams);
            }

            List<SysMenu> list = sysMenuService.list(new LambdaQueryWrapper<SysMenu>().eq(SysMenu::getParentId, menuId));
            if (list != null && !list.isEmpty()) {
                return new BaseResponse(StatusCode.MenuHasSubMenuListCanNotDelete);
            }

            sysMenuService.remove(menuId);
        } catch (Exception e) {
            errorLog(e);
            return new BaseResponse(StatusCode.Fail.getCode(), e.getMessage());
        }


        return baseResponse;
    }

    @GetMapping("/nav")
    public BaseResponse nav() {
        BaseResponse baseResponse = new BaseResponse(StatusCode.Success);

        try {
            HashMap<Object, Object> map = Maps.newHashMap();
            map.put("menuList", sysMenuService.queryMenuByUserId(ShiroUtil.getUserId()));
            baseResponse.setData(map);
        } catch (Exception e) {
            errorLog(e);
            return new BaseResponse(StatusCode.Fail.getCode(), e.getMessage());
        }

        return baseResponse;
    }

    //验证参数是否正确
    private String validateForm(SysMenu menu) {
        if (StringUtils.isBlank(menu.getName())) {
            return "菜单名称不能为空";
        }
        if (menu.getParentId() == null) {
            return "上级菜单不能为空";
        }

        //菜单
        if (menu.getType() == Constant.MenuType.MENU.getValue()) {
            if (StringUtils.isBlank(menu.getUrl())) {
                return "菜单链接url不能为空";
            }
        }

        //上级菜单类型
        int parentType = -1;

        if (menu.getParentId() != 0) {
            SysMenu parentMenu = sysMenuService.getById(menu.getParentId());
            parentType = ObjectUtils.isNull(parentMenu) ? -1 : parentMenu.getType();
        }

        //目录、菜单
        if (menu.getType() == Constant.MenuType.CATALOG.getValue() || menu.getType() == Constant.MenuType.MENU.getValue()) {
            if (parentType != Constant.MenuType.CATALOG.getValue()) {
                return "上级菜单只能为目录类型";
            }
            return "";
        }

        //按钮
        if (menu.getType() == Constant.MenuType.BUTTON.getValue()) {
            if (parentType != Constant.MenuType.MENU.getValue()) {
                return "上级菜单只能为菜单类型";
            }
            return "";
        }

        return "";
    }
}
