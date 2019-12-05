package com.debug.pmp.server.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.debug.pmp.common.response.StatusCode;
import com.debug.pmp.common.utils.PageUtil;
import com.debug.pmp.common.utils.QueryUtil;
import com.debug.pmp.model.entity.SysPost;
import com.debug.pmp.model.mapper.SysPostMapper;
import com.debug.pmp.server.service.SysPostService;
import io.netty.util.internal.StringUtil;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Map;

/**
 * <p>
 * 岗位信息表 服务实现类
 * </p>
 *
 * @author yqp
 * @since 2019-11-03
 */
@Service("sysPostService")
public class SysPostServiceImpl extends ServiceImpl<SysPostMapper, SysPost> implements SysPostService {

    @Override
    public PageUtil queryPage(Map<String, Object> params) {
        String search = (params.get("search") == null) ? "" : params.get("search").toString();

        IPage<SysPost> page = new QueryUtil<SysPost>().getPage(params);
        LambdaQueryWrapper<SysPost> queryWrapper = new QueryWrapper<SysPost>().lambda()
                .like(!StringUtil.isNullOrEmpty(search), SysPost::getPostId, search.trim())
                .or(!StringUtil.isNullOrEmpty(search))
                .like(!StringUtil.isNullOrEmpty(search), SysPost::getPostName, search.trim());

        IPage<SysPost> resPage = this.page(page, queryWrapper);
        return new PageUtil(resPage);

    }

    @Override
    public void savePost(SysPost sysPost) {

        if (this.getOne(new LambdaQueryWrapper<SysPost>().eq(SysPost::getPostCode, sysPost.getPostCode())) != null) {
            throw new RuntimeException(StatusCode.PostCodeHasExist.getMsg());
        }

        this.save(sysPost);
    }

    @Override
    public void updatePost(SysPost sysPost) {
        SysPost oldPost = this.getById(sysPost.getPostId());
        if (oldPost != null && !oldPost.getPostCode().equals(sysPost.getPostCode())) {
            if (this.getOne(new LambdaQueryWrapper<SysPost>().eq(SysPost::getPostCode, sysPost.getPostCode())) != null) {
                throw new RuntimeException(StatusCode.PostCodeHasExist.getMsg());
            }
        }

        this.updateById(sysPost);
    }

    @Override
    public void removeBatch(Long[] ids) {
        this.removeByIds(Arrays.asList(ids));
    }
}
