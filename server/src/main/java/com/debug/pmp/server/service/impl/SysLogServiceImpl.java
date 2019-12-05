package com.debug.pmp.server.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.debug.pmp.common.utils.PageUtil;
import com.debug.pmp.common.utils.QueryUtil;
import com.debug.pmp.model.entity.SysLog;
import com.debug.pmp.model.mapper.SysLogMapper;
import com.debug.pmp.server.service.SysLogService;
import io.netty.util.internal.StringUtil;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * <p>
 * 系统日志 服务实现类
 * </p>
 *
 * @author yqp
 * @since 2019-11-03
 */
@Service
public class SysLogServiceImpl extends ServiceImpl<SysLogMapper, SysLog> implements SysLogService {

    @Override
    public PageUtil queryPage(Map params) {
        String search = params.get("key") == null ? "" : params.get("key").toString().trim();

        IPage queryPage = new QueryUtil().getQueryPage(params);
        IPage page = page(queryPage, new LambdaQueryWrapper<SysLog>()
                .like(!StringUtil.isNullOrEmpty(search), SysLog::getUsername, search)
                .or(!StringUtil.isNullOrEmpty(search))
                .like(!StringUtil.isNullOrEmpty(search), SysLog::getOperation, search));

        return new PageUtil(page);
    }
}
