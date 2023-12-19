package com.example.webproject.config;

import com.example.webproject.LoginInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class InterceptorConfig implements WebMvcConfigurer {
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 添加自定义拦截器，并指定要拦截的URL
        InterceptorRegistration ir = registry.addInterceptor(new LoginInterceptor());
        ir.excludePathPatterns("/login");
        ir.excludePathPatterns("/register");
        ir.excludePathPatterns("/loginBackground.jpg");
        ir.addPathPatterns("/**");
    }
}
