package com.debug.pmp.server.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.debug.pmp.common.utils.Constant;
import com.debug.pmp.model.entity.SysDept;
import com.debug.pmp.model.mapper.SysDeptMapper;
import com.debug.pmp.model.mapper.SysUserMapper;
import com.debug.pmp.server.service.SysDeptService;
import com.debug.pmp.server.shiro.ShiroUtil;
import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * <p>
 * 部门管理 服务实现类
 * </p>
 *
 * @author yqp
 * @since 2019-11-03
 */
@Service("sysDeptService")
public class SysDeptServiceImpl extends ServiceImpl<SysDeptMapper, SysDept> implements SysDeptService {

    @Autowired
    private SysDeptMapper sysDeptMapper;

    @Autowired
    private SysUserMapper sysUserMapper;

    @Override
    public List<SysDept> queryAll() {
        HashMap<String, Object> map = Maps.newHashMap();

        Set<Long> deptIds = Sets.newHashSet();
        if (ShiroUtil.getUserId() != Constant.SUPER_ADMIN) {
            Set<Long> roleDeptIds = sysUserMapper.selectRoleDeptIdsByUserId(ShiroUtil.getUserId());
            deptIds.addAll(roleDeptIds);

            Long deptId = ShiroUtil.getUser().getDeptId();
            deptIds.add(deptId);
            List<Long> parentIds = getSubDeptIdList(deptId);

            deptIds.addAll(parentIds);

            map.put("deptIdList", Joiner.on(",").join(deptIds));
        }

        return sysDeptMapper.selectAll(map);
    }

    @Override
    public List<SysDept> querryByParentId(Long parentId) {
        List<SysDept> deptList = sysDeptMapper.selectList(new LambdaQueryWrapper<SysDept>().eq(SysDept::getParentId, parentId));
        return deptList;
    }

    public List<Long> getSubDeptIdList(Long deptId) {
        List<Long> deptIdList = Lists.newLinkedList();

        //第一级部门Id列表
        List<Long> subIdList = querryByParentId(deptId).stream().map(SysDept::getDeptId).collect(Collectors.toList());
        getDeptTreeList(subIdList, deptIdList);

        return deptIdList;
    }

    /**
     * 递归
     *
     * @param subIdList  第一级部门数据Id列表
     * @param deptIdList 每次递归时循环存储的结果数据Id列表
     */
    private void getDeptTreeList(List<Long> subIdList, List<Long> deptIdList) {
        List<Long> list;
        for (Long subId : subIdList) {
            list = querryByParentId(subId).stream().map(SysDept::getDeptId).collect(Collectors.toList());
            if (list != null && !list.isEmpty()) {
                //调用递归之处
                getDeptTreeList(list, deptIdList);
            }

            //执行到这里时，就表示当前递归结束
            deptIdList.add(subId);
        }
    }


}
