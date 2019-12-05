package com.debug.pmp.server.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.debug.pmp.model.entity.SysUser;
import com.debug.pmp.model.entity.SysUserPost;
import com.debug.pmp.model.mapper.SysUserPostMapper;
import com.debug.pmp.server.service.SysUserPostService;
import com.google.common.base.Joiner;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * <p>
 * 用户与岗位关联表 服务实现类
 * </p>
 *
 * @author yqp
 * @since 2019-11-03
 */
@Service
public class SysUserPostServiceImpl extends ServiceImpl<SysUserPostMapper, SysUserPost> implements SysUserPostService {

    @Override
    public String querryPostNameByUserId(Long userId) {

        List<SysUserPost> list = baseMapper.selectByUserId(userId);

        String result = "";
        if (list != null && !list.isEmpty()) {
            Set<String> set = list.stream().map(SysUserPost::getPostName).collect(Collectors.toSet());
            result = Joiner.on(",").join(set);
        }

        return result;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveOrUpdate(SysUser sysUser) {

        removeBatch(Arrays.asList(sysUser.getUserId()));

        List<Long> postIdList = sysUser.getPostIdList();

        if (postIdList != null && !postIdList.isEmpty()) {
            List<SysUserPost> list = postIdList.stream().map(id -> new SysUserPost().setPostId(id).setUserId(sysUser.getUserId())).collect(Collectors.toList());
            saveOrUpdateBatch(list);
        }
    }

    @Override
    public void removeBatch(List userIdList) {
        userIdList.stream().forEach(userId -> this.remove(new LambdaQueryWrapper<SysUserPost>().eq(SysUserPost::getUserId, userId)));
    }
}
