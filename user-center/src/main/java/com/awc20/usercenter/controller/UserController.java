package com.awc20.usercenter.controller;

import com.awc20.usercenter.constant.UserConstant;
import com.awc20.usercenter.mapper.UserMapper;
import com.awc20.usercenter.mapper.dto.UserLoginDto;
import com.awc20.usercenter.mapper.dto.UserRegisterDto;
import com.awc20.usercenter.model.domain.User;
import com.awc20.usercenter.service.UserService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 用户接口
 * @author grey
 */
@RestController
@RequestMapping("/user")
public class UserController {

    @Resource
    private UserService userService;

    @PostMapping("/register")
    public Long userRegister(@RequestBody UserRegisterDto userRegisterDto){
        String userAccount = userRegisterDto.getUserAccount();
        String userPassword = userRegisterDto.getUserPassword();
        String checkPassword = userRegisterDto.getCheckPassword();
        if (StringUtils.isAnyBlank(userAccount, userPassword, checkPassword)){
            return null;
        }
        return userService.userRegister(userAccount, userPassword, checkPassword);
    }


    @PostMapping("/login")
    public User userLogin(@RequestBody UserLoginDto userLoginDto, HttpServletRequest request){
        String userAccount = userLoginDto.getUserAccount();
        String userPassword = userLoginDto.getUserPassword();
        if (StringUtils.isAnyBlank(userAccount, userPassword)){
            return null;
        }
        return userService.userLogin(userAccount, userPassword,request);
    }


    @GetMapping("/search")
    public List<User> searchUser(String username,HttpServletRequest request){
        //管理员鉴权
        if (!isAdmin(request)){
            return new ArrayList<>();
        }
        QueryWrapper<User> queryWrapper=new QueryWrapper<>();
        if (StringUtils.isNotBlank(username)){
            queryWrapper.like("username",username);
        }
        //用户数据脱敏

        List<User> userList = userService.list(queryWrapper);
        return userList.stream().map(user -> userService.getSafetyUser(user)).collect(Collectors.toList());
    }

    @PostMapping("/delete")
    public boolean deleteUser(@RequestBody Long id,HttpServletRequest request){
        //管理员鉴权
        if (!isAdmin(request)){
            return false;
        }
        //id非空验证
        if (id==null || id<0){
            return false;
        }
        return userService.removeById(id);
    }

    /**
     * 通过本地session里获取到当前用户进行鉴权
     * @param request 请求体
     * @return
     */
    private boolean isAdmin(HttpServletRequest request){
        //鉴权  只有管理员能查询
        Object userObj = request.getSession().getAttribute(UserConstant.USER_LOGIN_STATE);
        User curUser=(User)userObj;
        if (curUser==null||!curUser.getUserRole().equals(UserConstant.ADMIN_ROLE)){
            return false;
        }
        return true;
    }


}
