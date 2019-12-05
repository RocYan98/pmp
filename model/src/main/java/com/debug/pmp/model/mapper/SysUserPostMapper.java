package com.debug.pmp.model.mapper;

import com.debug.pmp.model.entity.SysUserPost;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
 * <p>
 * 用户与岗位关联表 Mapper 接口
 * </p>
 *
 * @author yqp
 * @since 2019-11-03
 */
public interface SysUserPostMapper extends BaseMapper<SysUserPost> {

    List<SysUserPost> selectByUserId(Long userId);

}
