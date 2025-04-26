package com.awc20.usercenter.service.impl;

import com.awc20.usercenter.common.ErrorCodeEnum;
import com.awc20.usercenter.constant.UserConstant;
import com.awc20.usercenter.exception.BusinessException;
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
     * 用户注册
     *
     * @param userAccount   用户账户
     * @param userPassword  用户密码
     * @param checkPassword 用户校验密码（二次校验）
     * @param planetCode
     * @return 返回新用户id   或者 -1验证码报错（暂时）
     */
    @Override
    public long userRegister(String userAccount, String userPassword, String checkPassword, String planetCode) {
        //1.进行校验
        //非空
        if(StringUtils.isAnyBlank(userAccount,userPassword,checkPassword,planetCode)){
            throw new BusinessException(ErrorCodeEnum.PARAMS_ERROR,"参数不能为空");
        }
        //账户长度必须大于4位
        if (userAccount.length()<4){
            throw new BusinessException(ErrorCodeEnum.PARAMS_ERROR,"账户长度必须大于4位");
        }
        //账户密码不能小于8位
        if(userPassword.length()<8 || checkPassword.length()<8){
            throw new BusinessException(ErrorCodeEnum.PARAMS_ERROR,"账户密码不能小于8位");
        }

        //账号字符校验
        String accountPattern = "^[a-zA-Z0-9_]+$";
        Matcher matcher = Pattern.compile(accountPattern).matcher(userAccount);
        if (!matcher.find()){
            throw new BusinessException(ErrorCodeEnum.PARAMS_ERROR,"账号不能包含特殊符号");
        }
        //密码和校验密码一样
        if (!userPassword.equals(checkPassword)){
            throw new BusinessException(ErrorCodeEnum.PARAMS_ERROR,"两次密码不一致");
        }

        //校验星球编号不能大于5
        if (planetCode.length()>5){
            throw new BusinessException(ErrorCodeEnum.PARAMS_ERROR,"星球编号不能大于5");
        }

        //账户不能重复
        QueryWrapper<User> queryWrapper=new QueryWrapper<>();
        queryWrapper.eq("userAccount",userAccount);
        long count = userMapper.selectCount(queryWrapper);
        if(count>0){
            throw new BusinessException(ErrorCodeEnum.PARAMS_ERROR,"账户已存在");
        }

        //星球编号不能重复
        queryWrapper=new QueryWrapper<>();
        queryWrapper.eq("planetCode",planetCode);
        count = userMapper.selectCount(queryWrapper);
        if(count>0){
            throw new BusinessException(ErrorCodeEnum.PARAMS_ERROR,"星球编号已存在");
        }

        //2.密码加密
        String encryptPassword = DigestUtils.md5DigestAsHex((SALT+userPassword).getBytes());

        //3.封装对象，插入数据
        User user = new User();
        user.setUserAccount(userAccount);
        user.setUserPassword(encryptPassword);
        user.setPlanetCode(planetCode);
        int insertFlag = userMapper.insert(user);
        if (insertFlag==0){
            throw new BusinessException(ErrorCodeEnum.PARAMS_ERROR,"用户注册时新增失败");
        }
        return user.getId();
    }

    @Override
    public User userLogin(String userAccount, String userPassword, HttpServletRequest request) {
        //1. 校验用户账户密码是否合法
        //非空
        if(StringUtils.isAnyBlank(userAccount,userPassword)){
            throw new BusinessException(ErrorCodeEnum.PARAMS_ERROR,"参数不能为空");
        }
        //账户长度不小于4
        if(userAccount.length()<4){
            throw new BusinessException(ErrorCodeEnum.PARAMS_ERROR,"账户长度不小于4");
        }

        //密码不小于8
        if(userPassword.length()<8){
            throw new BusinessException(ErrorCodeEnum.PARAMS_ERROR,"密码长度不小于8");
        }
        //账户不包含特殊字符
        String accountPattern = "^[a-zA-Z0-9_]+$";
        Matcher matcher = Pattern.compile(accountPattern).matcher(userAccount);
        if (!matcher.find()){
            throw new BusinessException(ErrorCodeEnum.PARAMS_ERROR,"账户不能包含特殊字符");
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
            throw new BusinessException(ErrorCodeEnum.PARAMS_ERROR,"用户不存在或者密码错误");
        }

        //3.用户信息(脱敏)
        User safetyUser = getSafetyUser(user);
        //4. 登录成功，记录用户登录态,返回用户脱敏信息
        request.getSession().setAttribute(UserConstant.USER_LOGIN_STATE,safetyUser);
        return safetyUser;
    }

    @Override
    public User getSafetyUser(User originUser){
        //如果为空，直接返回空
        if (originUser==null){
            return null;
        }

        User safetyUser = new User();
        safetyUser.setId(originUser.getId());
        safetyUser.setUsername(originUser.getUsername());
        safetyUser.setUserAccount(originUser.getUserAccount());
        safetyUser.setAvatarUrl(originUser.getAvatarUrl());
        safetyUser.setGender(originUser.getGender());
        safetyUser.setPhone(originUser.getPhone());
        safetyUser.setEmail(originUser.getEmail());
        safetyUser.setPlanetCode(originUser.getPlanetCode());
        safetyUser.setUserRole(originUser.getUserRole());
        safetyUser.setCreateTime(originUser.getCreateTime());
        return safetyUser;
    }

    /**
     * 用户注销
     * @param request
     * @return  不明白为什么鱼皮要返回1
     */
    @Override
    public int userLogout(HttpServletRequest request) {
        request.getSession().removeAttribute(UserConstant.USER_LOGIN_STATE);
        return 1;
    }
}




