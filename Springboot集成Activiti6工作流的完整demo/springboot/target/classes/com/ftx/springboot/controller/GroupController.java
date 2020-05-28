package com.ftx.springboot.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.ftx.springboot.Filter.WorkFlowManager;
import com.ftx.springboot.Mapper.TestMapper;
import com.sun.org.apache.xpath.internal.operations.Mod;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.TaskService;
import org.activiti.engine.task.Task;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * @author FanJiangFeng
 * @version 1.0.0
 * @ClassName GroupController.java
 * @Description TODO
 * @createTime 2020年05月14日 10:43:00
 */
@Controller
@RequestMapping("/group")
public class GroupController {

    @Autowired
    TestMapper mapper;
    @Autowired
    WorkFlowManager workFlowManager;

    /**
     * todo 查询组任务
     */
    @RequestMapping("/findSelfWork")
    public String findSelfWork(HttpSession session, Model model){
        List<Map> maps=new ArrayList<Map>();
        JSONObject jsonObject = workFlowManager.getGroupList("process", (String) session.getAttribute("username"));

        String list1=(String) jsonObject.get("data");
        List<Map> list=JSONObject.parseArray(list1,Map.class);
        for (Map map:list){
            String processInstanceId=(String) map.get("processInstanceId");
            JSONObject jsonObject1 = workFlowManager.getInstanceByInstanceId(processInstanceId);
            Object data = jsonObject1.get("data");
            if(data!=null){
                String processInstanceString=(String) jsonObject1.get("data");
                Map map1 = JSON.parseObject(processInstanceString, Map.class);
                //业务编号
                String businessKey=(String) map1.get("bUSINESS_KEY_");
                Map map2 = mapper.selectHolidayById(businessKey);
                map2.put("taskId",(String)map.get("id"));
                maps.add(map2);
            }
        }
        model.addAttribute("list",maps);
        return "group";
    }

    /**
     * todo 拾取组任务
     */
    @RequestMapping("/getTask")
    @ResponseBody
    public Integer getTask(String taskId,HttpSession session){
        JSONObject username = workFlowManager.getGroupTask(taskId, (String) session.getAttribute("username"));
        String status=(String) username.get("status");
        if("200".equals(status)){
            return 0;
        }else{
            return 1;
        }


    }

    /**
     * todo 归还组任务
     */
    @RequestMapping("/returnTask")
    @ResponseBody
    public Integer returnTask(String taskId,HttpSession session){

        JSONObject jsonObject = workFlowManager.returnGroupTask(taskId, (String) session.getAttribute("username"));
        String status=(String) jsonObject.get("status");
        if("200".equals(status)){
            return 0;
        }else{
            return 1;
        }

    }



}
