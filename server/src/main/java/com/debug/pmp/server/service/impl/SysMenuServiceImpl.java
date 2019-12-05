package com.debug.pmp.server.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.debug.pmp.common.utils.Constant;
import com.debug.pmp.model.entity.SysMenu;
import com.debug.pmp.model.entity.SysRoleMenu;
import com.debug.pmp.model.mapper.SysMenuMapper;
import com.debug.pmp.model.mapper.SysRoleMenuMapper;
import com.debug.pmp.server.service.SysMenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 菜单管理 服务实现类
 * </p>
 *
 * @author yqp
 * @since 2019-11-03
 */
@Service("sysMenuService")
public class SysMenuServiceImpl extends ServiceImpl<SysMenuMapper, SysMenu> implements SysMenuService {

    @Autowired
    private SysMenuMapper sysMenuMapper;

    @Autowired
    private SysRoleMenuMapper sysRoleMenuMapper;

    @Override
    public List<SysMenu> queryAll() {
        return sysMenuMapper.selectAll();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void remove(Long menuId) {
        removeById(menuId);
        sysRoleMenuMapper.delete(new LambdaQueryWrapper<SysRoleMenu>().eq(SysRoleMenu::getMenuId, menuId));
    }

    @Override
    public List<String> queryPermissionListByUserId(Long userId) {
        return sysMenuMapper.selectPermissions(userId);
    }

    @Override
    public List<SysMenu> queryMenuByUserId(Long userId) {

        if (userId == Constant.SUPER_ADMIN) {
            return getMenuList(this.list());
        }

        List<SysMenu> list = sysMenuMapper.selectMenuList(userId);

        return getMenuList(list);
    }

    public List<SysMenu> getMenuList(List<SysMenu> list) {
        List<SysMenu> parentList = list.stream().filter(sysMenu -> sysMenu.getParentId() == 0).collect(Collectors.toList());
        parentList.forEach(sysMenu -> sysMenu.setList(list.stream().filter(subMenu -> subMenu.getParentId() == sysMenu.getMenuId() && subMenu.getType() != 2).collect(Collectors.toList())));

        return parentList;
    }

}
