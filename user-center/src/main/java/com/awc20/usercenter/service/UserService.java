package com.awc20.usercenter.service;

import com.awc20.usercenter.model.domain.User;
import com.baomidou.mybatisplus.extension.service.IService;

/**
* @author grey
* @description 针对表【user(用户表)】的数据库操作Service
* @createDate 2025-04-20 19:48:49
*/
public interface UserService extends IService<User> {
    /**
     * 用户注册
     * @param userAccount  用户账户
     * @param userPassword  用户密码
     * @param checkPassword 用户校验密码（二次校验）
     * @return  新用户ID
     */
    long userRegister(String userAccount, String userPassword, String checkPassword);
}
