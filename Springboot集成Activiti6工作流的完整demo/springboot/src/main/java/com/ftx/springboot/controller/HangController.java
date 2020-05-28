package com.ftx.springboot.controller;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.repository.ProcessDefinitionQuery;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

/**
 * @author FanJiangFeng
 * @version 1.0.0
 * @ClassName HangController.java
 * @Description TODO
 * @createTime 2020年05月14日 14:24:00
 */
@Controller
@RequestMapping("/hang")
public class HangController {

    /**
     * todo 查询所有的流程定义
     */
    @RequestMapping("/list")
    public void list(){
        ProcessEngine defaultProcessEngine = ProcessEngines.getDefaultProcessEngine();
        RepositoryService repositoryService = defaultProcessEngine.getRepositoryService();
        ProcessDefinitionQuery processDefinitionQuery = repositoryService.createProcessDefinitionQuery();
        //得到流程定义列表
        List<ProcessDefinition> list = processDefinitionQuery.list();
        for (ProcessDefinition processDefinition:list){
            System.out.println("流程定义ID："+processDefinition.getId());
            System.out.println("流程定义名称："+processDefinition.getName());
            System.out.println("流程定义的Key："+processDefinition.getKey());
            System.out.println("流程定义的版本号："+processDefinition.getVersion());
            System.out.println("流程部署的ID："+processDefinition.getDeploymentId());
        }


    }


}
