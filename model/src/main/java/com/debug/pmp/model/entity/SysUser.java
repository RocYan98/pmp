package com.debug.pmp.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.experimental.Accessors;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>
 * 系统用户
 * </p>
 *
 * @author yqp
 * @since 2019-11-03
 */
@Data
@Accessors(chain = true)
@TableName("sys_user")
public class SysUser implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "user_id", type = IdType.AUTO)
    private Long userId;

    /**
     * 姓名
     */
    @NotBlank(message = "用户名不能为空")
    private String name;

    /**
     * 用户名
     */
    private String username;

    /**
     * 密码
     */
    private String password;

    /**
     * 盐
     */
    private String salt;

    /**
     * 邮箱
     */
    @NotBlank(message = "邮箱不能为空!")
    private String email;

    /**
     * 手机号
     */
    @NotBlank(message = "手机号不能为空!")
    private String mobile;

    /**
     * 状态  0：禁用   1：正常
     */
    private Integer status;

    /***
     * @TableField：字段属性不为数据库表字段，但又是必须使用的
     */
    @TableField(exist = false)
    private List<Long> roleIdList;

    /**
     * 部门ID
     */
    @NotNull(message="部门Id不能为空!")
    private Long deptId;

    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime createTime;

    /***
     * 在表内不存在的字段
     */
    @TableField(exist=false)
    private String deptName;

    /***
     * 在表内不存在的字段
     */
    @TableField(exist=false)
    private List<Long> postIdList;

    /***
     * 在表内不存在的字段
     */
    @TableField(exist=false)
    private String postName;

}
