package com.awc20.usercenter.mapper.dto;

import lombok.Data;

import java.io.Serializable;
/**
 * 用户登录请求体
 * @author grey
 */
@Data
public class UserLoginDto implements Serializable {

    private static final long serialVersionUID = 3673381776787596362L;

    private String userAccount;
    private String userPassword;
}
