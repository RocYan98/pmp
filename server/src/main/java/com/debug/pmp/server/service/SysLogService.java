package com.debug.pmp.server.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.debug.pmp.common.utils.PageUtil;
import com.debug.pmp.model.entity.SysLog;

import java.util.Map;

/**
 * <p>
 * 系统日志 服务类
 * </p>
 *
 * @author yqp
 * @since 2019-11-03
 */
public interface SysLogService extends IService<SysLog> {

    PageUtil queryPage(Map params);

}
