package com.springboot_activiti.BusinessService;

import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * @author FanJiangFeng
 * @version 1.0.0
 * @ClassName BusineService.java
 * @Description TODO
 * @createTime 2020年05月16日 14:17:00
 */

public interface BusineService {

    List<Map> getAccountByPassword(@Param("username") String username, @Param("password") String password);

}
