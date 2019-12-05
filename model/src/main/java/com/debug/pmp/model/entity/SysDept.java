package com.debug.pmp.model.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.experimental.Accessors;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

/**
 * <p>
 * 部门管理
 * </p>
 *
 * @author yqp
 * @since 2019-11-03
 */
@Data
@Accessors(chain = true)
@TableName("sys_dept")
public class SysDept implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "dept_id", type = IdType.AUTO)
    private Long deptId;

    /**
     * 上级部门ID，一级部门为0
     */
    @NotNull(message = "父级部门必填!")
    private Long parentId;

    /**
     * 部门名称
     */
    @NotBlank(message = "部门名称不能为空!")
    private String name;

    /***
     * 上级部门名称
     * 在表内不存在的字段
     */
    @TableField(exist=false)
    private String parentName;

    /**
     * 排序
     */
    private Integer orderNum;

    /**
     * 是否删除  -1：已删除  0：正常
     */
    @TableLogic
    private Integer delFlag;


    /**
     * ztree属性
     * 在表内不存在的字段
     */
    @TableField(exist=false)
    private Boolean open;

    /***
     * 在表内不存在的字段
     */
    @TableField(exist=false)
    private List<?> list;
}
