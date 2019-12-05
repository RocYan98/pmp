package com.debug.pmp.server.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.debug.pmp.model.entity.SysUser;
import com.debug.pmp.model.entity.SysUserRole;

import java.util.List;

/**
 * <p>
 * 用户与角色对应关系 服务类
 * </p>
 *
 * @author yqp
 * @since 2019-11-03
 */
public interface SysUserRoleService extends IService<SysUserRole> {

    void saveOrUpdate(SysUser sysUser);

    void removeBatch(List userIdList);
}
