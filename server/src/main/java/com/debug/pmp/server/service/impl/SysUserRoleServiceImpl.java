package com.debug.pmp.server.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.debug.pmp.model.entity.SysUser;
import com.debug.pmp.model.entity.SysUserRole;
import com.debug.pmp.model.mapper.SysUserRoleMapper;
import com.debug.pmp.server.service.SysUserRoleService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 用户与角色对应关系 服务实现类
 * </p>
 *
 * @author yqp
 * @since 2019-11-03
 */
@Service
public class SysUserRoleServiceImpl extends ServiceImpl<SysUserRoleMapper, SysUserRole> implements SysUserRoleService {

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveOrUpdate(SysUser sysUser) {

        removeBatch(Arrays.asList(sysUser.getUserId()));

        List<Long> roleIdList = sysUser.getRoleIdList();

        if (roleIdList != null && !roleIdList.isEmpty()) {
            List<SysUserRole> list = roleIdList.stream().map(id -> new SysUserRole().setRoleId(id).setUserId(sysUser.getUserId())).collect(Collectors.toList());
            saveOrUpdateBatch(list);
        }
    }

    @Override
    public void removeBatch(List userIdList) {
        userIdList.stream().forEach(userId -> this.remove(new LambdaQueryWrapper<SysUserRole>().eq(SysUserRole::getUserId, userId)));
    }
}
