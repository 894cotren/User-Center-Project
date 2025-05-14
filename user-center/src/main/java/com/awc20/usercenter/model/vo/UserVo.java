package com.awc20.usercenter.model.vo;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 用户表VO
 *  传输脱敏后数据
 * @author grey
 */
@TableName(value ="user")
@Data
public class UserVo implements Serializable {
    /**
     * 主键
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 昵称
     */
    private String username;

    /**
     * 账号
     */
    private String userAccount;

    /**
     * 用户头像
     */
    private String avatarUrl;

    /**
     * 性别
     */
    private Integer gender;


    /**
     * 电话
     */
    private String phone;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 账号状态 ： 0-正常
     */
    private Integer userStatus;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 用户角色  0-普通用户  1-管理员
     */
    private Integer userRole;

    /**
     * 星球编号
     */
    private String planetCode;


    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}
