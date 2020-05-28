package com.springboot_activiti.BusinessMapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

/**
 * @author FanJiangFeng
 * @version 1.0.0
 * @ClassName BusineMapper.java
 * @Description TODO
 * @createTime 2020年05月16日 14:14:00
 */
@Mapper
public interface BusineMapper {

    // todo 登录
    @Select(value = "select * from activiti_account where username=#{username} and password=#{password} and yxbz=1")
    List<Map> getAccountByPassword(@Param("username") String username, @Param("password") String password);
}
