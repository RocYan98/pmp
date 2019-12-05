package com.debug.pmp.server.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.debug.pmp.common.utils.PageUtil;
import com.debug.pmp.common.utils.QueryUtil;
import com.debug.pmp.model.entity.SysRole;
import com.debug.pmp.model.entity.SysRoleDept;
import com.debug.pmp.model.entity.SysRoleMenu;
import com.debug.pmp.model.mapper.SysRoleMapper;
import com.debug.pmp.server.service.SysRoleDeptService;
import com.debug.pmp.server.service.SysRoleMenuService;
import com.debug.pmp.server.service.SysRoleService;
import com.google.common.collect.Lists;
import io.netty.util.internal.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 角色 服务实现类
 * </p>
 *
 * @author yqp
 * @since 2019-11-03
 */
@Service("sysRoleService")
public class SysRoleServiceImpl extends ServiceImpl<SysRoleMapper, SysRole> implements SysRoleService {


    @Autowired
    private SysRoleDeptService sysRoleDeptService;

    @Autowired
    private SysRoleMenuService sysRoleMenuService;

    /**
     * 模糊查询页面
     * @param params
     * @return
     */
    @Override
    public PageUtil queryPage(Map params) {
        String search = params.get("search") == null ? "" : (String) params.get("search");
        IPage page = new QueryUtil().getQueryPage(params);

        IPage reqPage = this.page(page, new LambdaQueryWrapper<SysRole>().like(!StringUtil.isNullOrEmpty(search), SysRole::getRoleName, search.trim()));

        return new PageUtil(reqPage);
    }


    /**
     * 保存角色
     * @param sysRole
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveRole(SysRole sysRole) {
        this.save(sysRole);
        sysRoleDeptService.saveOrUpdate(sysRole);
        sysRoleMenuService.saveOrUpdate(sysRole);
    }

    /**
     * 查找角色-部门-菜单
     * @param roleId
     * @return
     */
    @Override
    public SysRole queryRoleDeptMenu(Long roleId) {
        SysRole sysRole = getById(roleId);
        ArrayList<Long> deptIdList = Lists.newArrayList();
        sysRoleDeptService.list(new LambdaQueryWrapper<SysRoleDept>().eq(SysRoleDept::getRoleId, roleId)).stream().forEach(entity -> deptIdList.add(entity.getDeptId()));

        ArrayList<Long> menuIdList = Lists.newArrayList();
        sysRoleMenuService.list(new LambdaQueryWrapper<SysRoleMenu>().eq(SysRoleMenu::getRoleId, roleId)).stream().forEach(entity -> menuIdList.add(entity.getMenuId()));

        sysRole.setDeptIdList(deptIdList);
        sysRole.setMenuIdList(menuIdList);

        return sysRole;
    }

    /**
     * 更新角色
     * @param sysRole
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateRole(SysRole sysRole) {
        updateById(sysRole);

        sysRoleDeptService.saveOrUpdate(sysRole);
        sysRoleMenuService.saveOrUpdate(sysRole);
    }

    /**
     * 批量删除
     * @param roleIdList
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void removeBatch(List roleIdList) {
        removeByIds(roleIdList);

        sysRoleDeptService.removeBatch(roleIdList);
        sysRoleMenuService.removeBatch(roleIdList);
    }
}
