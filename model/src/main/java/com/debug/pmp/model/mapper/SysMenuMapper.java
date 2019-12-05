package com.debug.pmp.model.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.debug.pmp.model.entity.SysMenu;

import java.util.List;

/**
 * <p>
 * 菜单管理 Mapper 接口
 * </p>
 *
 * @author yqp
 * @since 2019-11-03
 */
public interface SysMenuMapper extends BaseMapper<SysMenu> {

    List<SysMenu> selectAll();

    List<String> selectPermissions(Long userId);

    List<SysMenu> selectMenuList(Long userId);
}
