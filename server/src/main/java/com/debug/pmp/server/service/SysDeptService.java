package com.debug.pmp.server.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.debug.pmp.model.entity.SysDept;

import java.util.List;

/**
 * <p>
 * 部门管理 服务类
 * </p>
 *
 * @author yqp
 * @since 2019-11-03
 */
public interface SysDeptService extends IService<SysDept> {

    List<SysDept> queryAll();

    List<SysDept> querryByParentId(Long parentId);
}
