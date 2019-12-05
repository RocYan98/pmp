package com.debug.pmp.server.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.debug.pmp.common.utils.PageUtil;
import com.debug.pmp.model.entity.SysPost;

import java.util.Map;

/**
 * <p>
 * 岗位信息表 服务类
 * </p>
 *
 * @author yqp
 * @since 2019-11-03
 */
public interface SysPostService extends IService<SysPost> {

    PageUtil queryPage(Map<String, Object> params);

    void savePost(SysPost sysPost);

    void updatePost(SysPost sysPost);

    void removeBatch(Long[] ids);
}
