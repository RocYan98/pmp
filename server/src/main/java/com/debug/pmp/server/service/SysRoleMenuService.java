package com.debug.pmp.server.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.debug.pmp.model.entity.SysRole;
import com.debug.pmp.model.entity.SysRoleMenu;

import java.util.List;

/**
 * <p>
 * 角色与菜单对应关系 服务类
 * </p>
 *
 * @author yqp
 * @since 2019-11-03
 */
public interface SysRoleMenuService extends IService<SysRoleMenu> {

    void saveOrUpdate(SysRole sysRole);

    void removeBatch(List roleIdList);

}
