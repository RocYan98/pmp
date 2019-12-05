package com.debug.pmp.model.mapper;

import com.debug.pmp.model.entity.SysDept;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 部门管理 Mapper 接口
 * </p>
 *
 * @author yqp
 * @since 2019-11-03
 */
public interface SysDeptMapper extends BaseMapper<SysDept> {

    List<SysDept> selectAll(Map<String, Object> map);

}
