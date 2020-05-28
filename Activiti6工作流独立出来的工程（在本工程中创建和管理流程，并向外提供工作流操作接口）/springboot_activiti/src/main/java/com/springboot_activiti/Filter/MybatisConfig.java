package com.springboot_activiti.Filter;

import com.github.pagehelper.PageHelper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Properties;


/**
 * @author FanJiangFeng
 * @version 1.0.0
 * @ClassName MybatisConfig.java
 * @Description TODO
 * @createTime 2020年05月19日 16:43:00
 */
@Configuration
public class MybatisConfig {
    @Bean
    public PageHelper pageHelper(){
        PageHelper pageHelper=new PageHelper();
        Properties properties=new Properties();
        //把这个设置为true，会带RowBounds第一个参数offset当成PageNum使用
        properties.setProperty("offsetAsPageNum","true");
        //设置为true时，使用RowBounds分页会进行count查询
        properties.setProperty("rowBoundsWithCount","true");
        properties.setProperty("reasonable","true");
        pageHelper.setProperties(properties);
        return pageHelper;
    }

}
