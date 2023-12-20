package com.example.demo.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableWebMvc
public class WebMvcConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        //MVC 패턴을 사용하면 default 값으로 static경로를 잡아주지만 직접 설정할 수 도 있다.
        registry.addResourceHandler("/js/**").addResourceLocations("classpath:/static/js/");//.setCachePeriod(60 * 60 * 24 * 365);
        registry.addResourceHandler("/css/**").addResourceLocations("classpath:/static/css/");//.setCachePeriod(60 * 60 * 24 * 365);
        registry.addResourceHandler("/images/**").addResourceLocations("classpath:/static/images/");//.setCachePeriod(60 * 60 * 24 * 365);
        registry.addResourceHandler("/font/**").addResourceLocations("classpath:/static/font/");//.setCachePeriod(60 * 60 * 24 * 365);

    }
}
