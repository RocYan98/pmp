package com.debug.pmp.server.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.debug.pmp.model.entity.SysMenu;

import java.util.List;

/**
 * <p>
 * 菜单管理 服务类
 * </p>
 *
 * @author yqp
 * @since 2019-11-03
 */
public interface SysMenuService extends IService<SysMenu> {

    List<SysMenu> queryAll();

    void remove(Long menuId);

    List<String> queryPermissionListByUserId(Long userId);

    List<SysMenu> queryMenuByUserId(Long userId);
}
