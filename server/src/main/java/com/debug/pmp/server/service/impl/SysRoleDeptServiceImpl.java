package com.debug.pmp.server.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.debug.pmp.model.entity.SysRole;
import com.debug.pmp.model.entity.SysRoleDept;
import com.debug.pmp.model.mapper.SysRoleDeptMapper;
import com.debug.pmp.server.service.SysRoleDeptService;
import com.google.common.collect.Lists;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * <p>
 * 角色与部门对应关系 服务实现类
 * </p>
 *
 * @author yqp
 * @since 2019-11-03
 */
@Service
public class SysRoleDeptServiceImpl extends ServiceImpl<SysRoleDeptMapper, SysRoleDept> implements SysRoleDeptService {

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveOrUpdate(SysRole sysRole) {

        removeBatch(Arrays.asList(sysRole.getRoleId()));

        Long roleId = sysRole.getRoleId();
        ArrayList<SysRoleDept> list = Lists.newArrayList();
        if (sysRole.getDeptIdList() != null && !sysRole.getDeptIdList().isEmpty()) {
            sysRole.getDeptIdList().stream().forEach(deptId -> list.add(new SysRoleDept().setRoleId(roleId).setDeptId(deptId)));
            this.saveOrUpdateBatch(list);
        }
    }

    /**
     * 批量删除
     * @param roleIdList
     */
    @Override
    public void removeBatch(List roleIdList) {
        roleIdList.stream().forEach(roleId -> remove(new LambdaQueryWrapper<SysRoleDept>().eq(SysRoleDept::getRoleId, roleId)));
    }
}
