package com.awc20.usercenter.service;
import com.awc20.usercenter.model.domain.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;


@SpringBootTest
class UserServiceTest {

    @Resource
    private UserService userService;

    @Test
    void testAddUser(){
        User user = new User();
        user.setUsername("alice");
        user.setUserAccount("alice");
        user.setAvatarUrl("https://image.baidu.com/search/detail?ct=503316480&z=0&tn=baiduimagedetail&ipn=d&cl=2&cm=1&sc=0&lm=-1&ie=utf8&pn=1&rn=1&di=7490230549689139201&ln=0&word=%E6%B4%9B%E7%90%AA%E5%B8%8C%E5%A4%B4%E5%83%8F&os=365809041,2109553635&cs=369252351,3837412165&objurl=http%3A%2F%2Fi0.hdslb.com%2Fbfs%2Fnew_dyn%2Fde9a4c786c63c30f72aa7502cdcfa618362386998.jpg%401192w_1192h.webp&bdtype=0&simid=369252351,3837412165&pi=0&adpicid=0&timingneed=&spn=0&is=0,0");
        user.setGender(0);
        user.setUserPassword("23321212");
        user.setPhone("12312312311");
        user.setEmail("alice@qq.com");


        boolean save = userService.save(user);
        Assertions.assertTrue(save);
        System.out.println(user.getId());
    }

    @Test
    void userRegister() {
        String userAccount="hanekawa";
        String userPassword="23321212";
        String checkPassword="23321212";
        String planetCode="1234";
        long result = userService.userRegister(userAccount, userPassword, checkPassword, planetCode);
        System.out.println(result);
        Assertions.assertTrue(result>0);
    }
}
