package com.awc20.usercenter.service.impl;
import java.util.Date;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.awc20.usercenter.model.domain.User;
import com.awc20.usercenter.service.UserService;
import com.awc20.usercenter.mapper.UserMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 用户服务实现类
 *
 * @author grey
 */

@Slf4j
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User>
    implements UserService{

    @Resource
    private UserMapper userMapper;
    /**
     * 盐值，密码加密
     */
    private static final String SALT="grey";

    /**
     *     用户登录态key(session里的key)
     */
    private static final String USER_LOGIN_STATE="userLoginState";
    /**
     * 用户注册
     * @param userAccount  用户账户
     * @param userPassword  用户密码
     * @param checkPassword 用户校验密码（二次校验）
     * @return 返回新用户id   或者 -1验证码报错（暂时）
     */
    @Override
    public long userRegister(String userAccount, String userPassword, String checkPassword) {
        //1.进行校验
        //非空
        if(StringUtils.isAnyBlank(userAccount,userPassword,checkPassword)){
            //todo 修改为自定义异常
            return -1;
        }
        //账户长度必须大于4位
        if (userAccount.length()<4){
            return -1;
        }
        //账户密码不能小于8位
        if(userPassword.length()<8 || checkPassword.length()<8){
            return -1;
        }

        //账号字符校验
        String accountPattern = "^[a-zA-Z0-9_]+$";
        Matcher matcher = Pattern.compile(accountPattern).matcher(userAccount);
        if (!matcher.find()){
            return -1;
        }
        //密码和校验密码一样
        if (!userPassword.equals(checkPassword)){
            return -1;
        }

        //账户不能重复
        QueryWrapper<User> queryWrapper=new QueryWrapper<>();
        queryWrapper.eq("userAccount",userAccount);
        long count = userMapper.selectCount(queryWrapper);
        if(count>0){
            return -1;
        }

        //2.密码加密
        String encryptPassword = DigestUtils.md5DigestAsHex((SALT+userPassword).getBytes());

        //3.封装对象，插入数据
        User user = new User();
        user.setUserAccount(userAccount);
        user.setUserPassword(encryptPassword);
        int insertFlag = userMapper.insert(user);
        if (insertFlag==0){
            return -1;
        }
        return user.getId();
    }

    @Override
    public User userLogin(String userAccount, String userPassword, HttpServletRequest request) {
        //1. 校验用户账户密码是否合法
        //非空
        if(StringUtils.isAnyBlank(userAccount,userPassword)){
            //todo 修改为自定义异常
            return null;
        }
        //账户长度不小于4
        if(userAccount.length()<4){
            return null;
        }

        //密码不小于8
        if(userPassword.length()<8){
            return null;
        }
        //账户不包含特殊字符
        String accountPattern = "^[a-zA-Z0-9_]+$";
        Matcher matcher = Pattern.compile(accountPattern).matcher(userAccount);
        if (!matcher.find()){
            return null;
        }
        //2. 查询用户，校验密码
        //密码加密
        String encryptPassword = DigestUtils.md5DigestAsHex((SALT+userPassword).getBytes());
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("userAccount",userAccount);
        queryWrapper.eq("userPassword",encryptPassword);
        User user = userMapper.selectOne(queryWrapper);
        if (user==null) {
            //用户不存在或者密码错误
            log.info("user login failed ,userAccount cannot match userPassword");
            return null;
        }

        //3.用户信息(脱敏)
        User safetyUser = new User();
        safetyUser.setId(user.getId());
        safetyUser.setUsername(user.getUsername());
        safetyUser.setUserAccount(user.getUserAccount());
        safetyUser.setAvatarUrl(user.getAvatarUrl());
        safetyUser.setGender(user.getGender());
        safetyUser.setPhone(user.getPhone());
        safetyUser.setEmail(user.getEmail());
        safetyUser.setCreateTime(user.getCreateTime());

        //4. 登录成功，记录用户登录态,返回用户脱敏信息
        request.getSession().setAttribute(USER_LOGIN_STATE,safetyUser);
        return safetyUser;
    }
}




