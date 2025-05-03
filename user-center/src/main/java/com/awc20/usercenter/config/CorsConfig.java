package com.awc20.usercenter.config;

import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.Arrays;
import java.util.List;


/**
 * 暂时不使用这个解决跨域，本来想优化一下下面那个可放行的url的
 * @author grey
 */
//@Configuration
public class CorsConfig implements WebMvcConfigurer {


    // 静态跨域白名单（可动态修改）
    private static final List<String> ALLOWED_ORIGINS = Arrays.asList(
            "http://localhost",
            "http://129.28.51.111",
            "http://129.28.51.111:3000",
            "http://129.28.51.111:80"
    );

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")  // 所有路径
                .allowedOrigins(ALLOWED_ORIGINS.toArray(new String[0]))  // 允许的源
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")  // 允许的方法
                .allowedHeaders("*")  // 允许的请求头
                .allowCredentials(true)  // 允许携带凭证(cookie等)
                .maxAge(3600);  // 预检请求的缓存时间(秒)
    }
}
