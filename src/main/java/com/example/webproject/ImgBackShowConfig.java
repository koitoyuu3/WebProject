package com.example.webproject;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class ImgBackShowConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        //registry.addResourceHandler(相对路径)
        //addResourceLocations(绝对路径)
        //System.getProperty("user.dir") 获取当前项目的绝对路径
        //
        //registry.addResourceHandler("images/headImage/**").addResourceLocations("file:"+System.getProperty("user.dir")+"\\src\\main\\resources\\static\\images\\headImage\\");
        registry.addResourceHandler("uploads/**").addResourceLocations("file:"+System.getProperty("user.dir")+"\\src\\main\\resources\\static\\uploads\\");
    }

}
