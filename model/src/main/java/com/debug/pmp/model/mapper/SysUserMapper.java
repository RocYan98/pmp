package com.debug.pmp.model.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.debug.pmp.model.entity.SysUser;

import java.util.Set;

/**
 * <p>
 * 系统用户 Mapper 接口
 * </p>
 *
 * @author yqp
 * @since 2019-11-03
 */
public interface SysUserMapper extends BaseMapper<SysUser> {

    SysUser selectByUsername(String username);

    Set<Long> selectRoleDeptIdsByUserId(Long userId);
}
