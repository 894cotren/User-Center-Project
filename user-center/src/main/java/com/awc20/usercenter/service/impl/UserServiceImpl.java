package com.awc20.usercenter.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.awc20.usercenter.model.domain.User;
import com.awc20.usercenter.service.UserService;
import com.awc20.usercenter.mapper.UserMapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import javax.annotation.Resource;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 用户服务实现类
 *
 * @author grey
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User>
    implements UserService{

    @Resource
    private UserMapper userMapper;

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
        String encryptPassword = DigestUtils.md5DigestAsHex(userPassword.getBytes());

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
}




