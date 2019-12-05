package com.debug.pmp.server.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.debug.pmp.common.utils.Constant;
import com.debug.pmp.common.utils.PageUtil;
import com.debug.pmp.common.utils.QueryUtil;
import com.debug.pmp.model.entity.SysDept;
import com.debug.pmp.model.entity.SysUser;
import com.debug.pmp.model.entity.SysUserPost;
import com.debug.pmp.model.entity.SysUserRole;
import com.debug.pmp.model.mapper.SysUserMapper;
import com.debug.pmp.server.service.SysDeptService;
import com.debug.pmp.server.service.SysUserPostService;
import com.debug.pmp.server.service.SysUserRoleService;
import com.debug.pmp.server.service.SysUserService;
import com.debug.pmp.server.shiro.ShiroUtil;
import io.netty.util.internal.StringUtil;
import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * <p>
 * 系统用户 服务实现类
 * </p>
 *
 * @author yqp
 * @since 2019-11-03
 */
@Service("sysUserService")
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUser> implements SysUserService {

    @Autowired
    private SysUserPostService sysUserPostService;

    @Autowired
    private SysUserRoleService sysUserRoleService;

    @Autowired
    private SysDeptService sysDeptService;

    @Override
    public void updatePassword(Long userId, String oldPwd, String newPwd) {
        SysUser sysUser = new SysUser();
        sysUser.setPassword(newPwd);
        update(sysUser, new QueryWrapper<SysUser>().lambda().eq(SysUser::getUserId, userId).eq(SysUser::getPassword, oldPwd));
    }

    @Override
    public PageUtil queryPage(Map<String, String> params) {
        String search = params.get("username") == null ? "" : params.get("username").trim();

        IPage<SysUser> queryPage = new QueryUtil().getQueryPage(params);
        IPage<SysUser> resPage = this.page(queryPage, new LambdaQueryWrapper<SysUser>()
                .like(!StringUtil.isNullOrEmpty(search), SysUser::getName, search)
                .or(!StringUtil.isNullOrEmpty(search))
                .like(!StringUtil.isNullOrEmpty(search), SysUser::getUsername, search));

        //设置用户部门名称、岗位名称
        for (SysUser sysUser :
                resPage.getRecords()) {
            SysDept dept = sysDeptService.getById(sysUser.getDeptId());
            sysUser.setDeptName(dept == null || dept.getName().isEmpty() ? "" : dept.getName());

            String postName = sysUserPostService.querryPostNameByUserId(sysUser.getUserId());
            sysUser.setPostName(postName);
        }

        return new PageUtil(resPage);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveUser(SysUser sysUser) {

        if (getOne(new LambdaQueryWrapper<SysUser>().eq(SysUser::getUsername, sysUser.getUsername())) != null) {
            throw new RuntimeException("用户名已存在");
        }

        String salt = RandomStringUtils.randomAlphanumeric(20);
        sysUser.setSalt(salt);
        sysUser.setPassword(ShiroUtil.sha256(sysUser.getPassword(), salt));

        saveOrUpdate(sysUser);

        sysUserPostService.saveOrUpdate(sysUser);
        sysUserRoleService.saveOrUpdate(sysUser);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void removeBatch(List userIdLIst) {

        removeByIds(userIdLIst);

        sysUserRoleService.removeBatch(userIdLIst);
        sysUserPostService.removeBatch(userIdLIst);
    }

    @Override
    public SysUser queryUser(Long userId) {
        SysUser sysUser = this.getById(userId);
        sysUser.setPostName(sysUserPostService.querryPostNameByUserId(userId));
        sysUser.setPostIdList(sysUserPostService.list(new LambdaQueryWrapper<SysUserPost>().eq(SysUserPost::getUserId, userId)).stream().map(SysUserPost::getPostId).collect(Collectors.toList()));
        sysUser.setRoleIdList(sysUserRoleService.list(new LambdaQueryWrapper<SysUserRole>().eq(SysUserRole::getUserId, userId)).stream().map(SysUserRole::getRoleId).collect(Collectors.toList()));

        return sysUser;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateUser(SysUser sysUser) {
        SysUser oldUser = getById(sysUser.getUserId());

        if (oldUser == null) {
            throw new RuntimeException("用户不存在");
        }

        if (!StringUtils.equals(oldUser.getUsername(), sysUser.getUsername())) {
            if (getOne(new LambdaQueryWrapper<SysUser>().eq(SysUser::getUsername, sysUser.getUsername())) != null) {
                throw new RuntimeException("修改后的用户名已经存在");
            }
        }

        if (!StringUtil.isNullOrEmpty(sysUser.getPassword())) {
            String pwd = ShiroUtil.sha256(sysUser.getPassword(), oldUser.getSalt());
            sysUser.setPassword(pwd);
        }

        updateById(sysUser);
        sysUserPostService.saveOrUpdate(sysUser);
        sysUserRoleService.saveOrUpdate(sysUser);
    }

    @Override
    public void resetPwd(List<Long> userIdList) {
        for (Long userId : userIdList) {
            String salt = RandomStringUtils.randomAlphanumeric(20);
            updateById(new SysUser().setUserId(userId).setSalt(salt).setPassword(ShiroUtil.sha256(Constant.DefaultPassword, salt)));
        }
    }
}