package com.debug.pmp.server.shiro;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.debug.pmp.common.utils.Constant;
import com.debug.pmp.model.entity.SysMenu;
import com.debug.pmp.model.entity.SysUser;
import com.debug.pmp.server.service.SysMenuService;
import com.debug.pmp.server.service.SysUserService;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import io.netty.util.internal.StringUtil;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.authc.credential.CredentialsMatcher;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class UserRealm extends AuthorizingRealm {

    @Autowired
    private SysUserService sysUserService;

    @Autowired
    private SysMenuService sysMenuService;

    /**
     * 授权
     * @param principalCollection
     * @return
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {

        SysUser sysUser = (SysUser) principalCollection.getPrimaryPrincipal();

        List<String> permissions = Lists.newArrayList();

        if (sysUser.getUserId() == Constant.SUPER_ADMIN) {
            List<SysMenu> list = sysMenuService.list();
            if (list != null && !list.isEmpty()) {
                permissions = list.stream().map(SysMenu::getPerms).collect(Collectors.toList());
            }
        } else {
            permissions = sysMenuService.queryPermissionListByUserId(sysUser.getUserId());
        }

        HashSet<String> stringPermissions = Sets.newHashSet();
        permissions.stream().filter(permission -> !StringUtil.isNullOrEmpty(permission)).forEach(permission -> stringPermissions.addAll(Arrays.asList(StringUtils.split(permission.trim(), ","))));

        SimpleAuthorizationInfo simpleAuthorizationInfo = new SimpleAuthorizationInfo();
        simpleAuthorizationInfo.setStringPermissions(stringPermissions);

        return simpleAuthorizationInfo;
    }

    /**
     * 登陆认证
     * @param authenticationToken
     * @return
     * @throws AuthenticationException
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        String username = (String) authenticationToken.getPrincipal();
        SysUser findUser = sysUserService.getOne(new LambdaQueryWrapper<SysUser>().eq(SysUser::getUsername, username));
        if (null == findUser) {
            throw new UnknownAccountException("账户不存在");
        }

        if (0 == findUser.getStatus() ) {
            throw new DisabledAccountException("账户异常，请联系管理员");
        }

        SimpleAuthenticationInfo simpleAuthenticationInfo = new SimpleAuthenticationInfo(findUser, findUser.getPassword(), ByteSource.Util.bytes(findUser.getSalt()), getName());

        return simpleAuthenticationInfo;
    }

    /**
     * 密钥匹配器
     * @param credentialsMatcher
     */
    @Override
    public void setCredentialsMatcher(CredentialsMatcher credentialsMatcher) {
        HashedCredentialsMatcher hashedCredentialsMatcher = new HashedCredentialsMatcher();
        hashedCredentialsMatcher.setHashAlgorithmName(ShiroUtil.hashAlgorithmName);
        hashedCredentialsMatcher.setHashIterations(ShiroUtil.hashIterations);
        super.setCredentialsMatcher(hashedCredentialsMatcher);
    }
}
