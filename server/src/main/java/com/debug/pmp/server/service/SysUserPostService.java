package com.debug.pmp.server.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.debug.pmp.model.entity.SysUser;
import com.debug.pmp.model.entity.SysUserPost;

import java.util.List;

/**
 * <p>
 * 用户与岗位关联表 服务类
 * </p>
 *
 * @author yqp
 * @since 2019-11-03
 */
public interface SysUserPostService extends IService<SysUserPost> {

    String querryPostNameByUserId(Long userId);

    void saveOrUpdate(SysUser sysUser);

    void removeBatch(List userIdList);

}
