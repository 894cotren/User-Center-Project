package com.awc20.usercenter.controller;

import com.awc20.usercenter.common.ErrorCodeEnum;
import com.awc20.usercenter.common.Result;
import com.awc20.usercenter.constant.UserConstant;
import com.awc20.usercenter.mapper.dto.UserLoginDto;
import com.awc20.usercenter.mapper.dto.UserRegisterDto;
import com.awc20.usercenter.model.domain.User;
import com.awc20.usercenter.service.UserService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 用户接口
 * @author grey
 */
@RestController
@RequestMapping("/user")
@CrossOrigin(allowCredentials = "true" , originPatterns = "*" , allowedHeaders = "*")
public class UserController {

    @Resource
    private UserService userService;

    @PostMapping("/register")
    public Result<Long> userRegister(@RequestBody UserRegisterDto userRegisterDto){
        String userAccount = userRegisterDto.getUserAccount();
        String userPassword = userRegisterDto.getUserPassword();
        String checkPassword = userRegisterDto.getCheckPassword();
        String planetCode = userRegisterDto.getPlanetCode();
        if (StringUtils.isAnyBlank(userAccount, userPassword, checkPassword,planetCode)){
            return Result.fail(ErrorCodeEnum.PARAMS_ERROR);
        }
        long count = userService.userRegister(userAccount, userPassword, checkPassword, planetCode);
        return Result.success(count);
    }


    @PostMapping("/login")
    public Result<User> userLogin(@RequestBody UserLoginDto userLoginDto, HttpServletRequest request){
        String userAccount = userLoginDto.getUserAccount();
        String userPassword = userLoginDto.getUserPassword();
        if (StringUtils.isAnyBlank(userAccount, userPassword)){
            return Result.fail(ErrorCodeEnum.PARAMS_ERROR);
        }
        User user = userService.userLogin(userAccount, userPassword, request);
        return Result.success(user);
    }

    @PostMapping("/logout")
    public Result<Integer> userLogout(HttpServletRequest request){
        if (request==null){
            return Result.fail(ErrorCodeEnum.PARAMS_ERROR);
        }
        int count = userService.userLogout(request);
        return Result.success(count);
    }


    @GetMapping("/search")
    public Result<List<User>> searchUser(String username,HttpServletRequest request){
        //管理员鉴权
        if (!isAdmin(request)){
            return Result.fail(ErrorCodeEnum.NOT_AUTH);
        }
        QueryWrapper<User> queryWrapper=new QueryWrapper<>();
        if (StringUtils.isNotBlank(username)){
            queryWrapper.like("username",username);
        }
        //用户数据脱敏

        List<User> userList = userService.list(queryWrapper);
        List<User> users = userList.stream().map(user -> userService.getSafetyUser(user)).collect(Collectors.toList());
        return Result.success(users);
    }

    @PostMapping("/delete")
    public Result<Boolean> deleteUser(@RequestBody Long id,HttpServletRequest request){
        //管理员鉴权
        if (!isAdmin(request)){
            return Result.fail(ErrorCodeEnum.NOT_AUTH);
        }
        //id非空验证
        if (id==null || id<0){
            return Result.fail(ErrorCodeEnum.PARAMS_ERROR);
        }
        boolean flag = userService.removeById(id);
        return Result.success(flag);
    }

    @GetMapping("/current")
    public Result<User> getCurrentUser(HttpServletRequest request){
        Object userObj = request.getSession().getAttribute(UserConstant.USER_LOGIN_STATE);
        User currentUser=(User)userObj;
        if (currentUser==null){
            //如果为空说明当前用户未登录
            return Result.fail(ErrorCodeEnum.NOT_LOGIN);
        }
        //我们需要再去数据库查询一下用户，因为当前用户是从session里获取出来的，可能有些东西还没有更新
        Long userId = currentUser.getId();
        User user = userService.getById(userId);
        //再返回脱敏后的用户数据
        User safetyUser = userService.getSafetyUser(user);
        return Result.success(safetyUser);
    }
    /**
     * 通过本地session里获取到当前用户进行鉴权
     * @param request 请求体
     * @return 是管理员返回true
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
