package com.awc20.usercenter;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.DigestUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@SpringBootTest
class UserCenterApplicationTests {

    @Test
    void testDigest(){
        String s1 = DigestUtils.md5DigestAsHex("abcde".getBytes());

        System.out.println(s1);
    }


    @Test
    void testCheckAccount(){
        String userAccount="alsdj-fa_1231";
        String accountPattern = "^[a-zA-Z0-9_]+$";
        Matcher matcher = Pattern.compile(accountPattern).matcher(userAccount);
        Assertions.assertTrue(matcher.find());
    }

    @Test
    void contextLoads() {
        //不知道干啥用的
    }


}
