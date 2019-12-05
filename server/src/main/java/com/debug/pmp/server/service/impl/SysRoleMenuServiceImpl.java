package com.debug.pmp.server.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.debug.pmp.model.entity.SysRole;
import com.debug.pmp.model.entity.SysRoleMenu;
import com.debug.pmp.model.mapper.SysRoleMenuMapper;
import com.debug.pmp.server.service.SysRoleMenuService;
import com.google.common.collect.Lists;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * <p>
 * 角色与菜单对应关系 服务实现类
 * </p>
 *
 * @author yqp
 * @since 2019-11-03
 */
@Service
public class SysRoleMenuServiceImpl extends ServiceImpl<SysRoleMenuMapper, SysRoleMenu> implements SysRoleMenuService {

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveOrUpdate(SysRole sysRole) {

        removeBatch(Arrays.asList(sysRole.getRoleId()));

        Long roleId = sysRole.getRoleId();
        ArrayList<SysRoleMenu> list = Lists.newArrayList();
        if (sysRole.getMenuIdList() != null && !sysRole.getMenuIdList().isEmpty()) {
            sysRole.getMenuIdList().stream().forEach(menuId -> list.add(new SysRoleMenu().setRoleId(roleId).setMenuId(menuId)));
            this.saveOrUpdateBatch(list);
        }
    }

    /**
     * 批量删除
     * @param roleIdList
     */
    @Override
    public void removeBatch(List roleIdList) {
        roleIdList.stream().forEach(roleId -> remove(new LambdaQueryWrapper<SysRoleMenu>().eq(SysRoleMenu::getRoleId, roleId)));
    }
}
