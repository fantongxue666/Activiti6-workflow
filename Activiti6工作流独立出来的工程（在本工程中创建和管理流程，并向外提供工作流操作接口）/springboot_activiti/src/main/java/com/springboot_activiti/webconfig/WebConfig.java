package com.springboot_activiti.webconfig;


import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public  class WebConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry){
        registry.addResourceHandler("/html/**").addResourceLocations("classpath:/html/");
        registry.addResourceHandler("/diagram-viewer/**").addResourceLocations("classpath:/html/");
        registry.addResourceHandler("/editor-app/**").addResourceLocations("classpath:/html/");
        registry.addResourceHandler("/js/**").addResourceLocations("classpath:/js/");
        registry.addResourceHandler("/css/**").addResourceLocations("classpath:/css/");
        registry.addResourceHandler("/static/**").addResourceLocations("classpath:/static/");
    }


}
