package com.debug.pmp.server.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.debug.pmp.common.utils.PageUtil;
import com.debug.pmp.model.entity.SysRole;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 角色 服务类
 * </p>
 *
 * @author yqp
 * @since 2019-11-03
 */
public interface SysRoleService extends IService<SysRole> {

    PageUtil queryPage(Map params);

    void saveRole(SysRole sysRole);

    SysRole queryRoleDeptMenu(Long roleId);

    void updateRole(SysRole sysRole);

    void removeBatch(List roleIdList);
}
