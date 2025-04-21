package com.awc20.usercenter.mapper.dto;

import lombok.Data;

import java.io.Serializable;
/**
 * z注册接口接受参数
 * @author grey
 */
@Data
public class UserRegisterDto implements Serializable {
    private static final long serialVersionUID = -5890428572216428423L;

    private String userAccount;
    private String userPassword;
    private String checkPassword;

}
