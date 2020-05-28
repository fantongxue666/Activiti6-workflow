package com.springboot_activiti.BusinessService;

import com.springboot_activiti.BusinessMapper.BusineMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @author FanJiangFeng
 * @version 1.0.0
 * @ClassName BusineServiceImpl.java
 * @Description TODO
 * @createTime 2020年05月16日 14:17:00
 */
@Service
public class BusineServiceImpl implements BusineService {

    @Autowired
    BusineMapper busineMapper;

    @Override
    public List<Map> getAccountByPassword(String username, String password) {
        return busineMapper.getAccountByPassword(username,password);
    }
}
