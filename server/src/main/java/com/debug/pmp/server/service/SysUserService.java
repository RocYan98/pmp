package com.debug.pmp.server.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.debug.pmp.common.utils.PageUtil;
import com.debug.pmp.model.entity.SysUser;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 系统用户 服务类
 * </p>
 *
 * @author yqp
 * @since 2019-11-03
 */
public interface SysUserService extends IService<SysUser> {

    void updatePassword(Long userId, String oldPwd, String newPwd);

    PageUtil queryPage(Map<String, String> params);

    void saveUser(SysUser sysUser);

    void removeBatch(List userIdLIst);

    SysUser queryUser(Long userId);

    void updateUser(SysUser sysUser);

    void resetPwd(List<Long> userIdList);
}
