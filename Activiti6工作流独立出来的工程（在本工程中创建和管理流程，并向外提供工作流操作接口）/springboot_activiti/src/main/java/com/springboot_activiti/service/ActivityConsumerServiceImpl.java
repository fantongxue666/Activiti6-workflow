package com.springboot_activiti.service;


import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.repository.DeploymentBuilder;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("activityService")
public class ActivityConsumerServiceImpl implements ActivityConsumerService {


    @Autowired
    private RuntimeService runtimeService;
    @Autowired
    private TaskService taskService;

    @Autowired
    private RepositoryService repositoryService;

    /**
     * 注册一个流程
     * @return
     */
    @Override
    public boolean createDeployment() {
        DeploymentBuilder builder=repositoryService.createDeployment();
        builder.addClasspathResource("test01.bpmn");
        builder.deploy();
        return true;
    }

    /**
     * 查询待办
     * @return
     */
    public boolean getTaskList(){
        //获取待办的一些信息，这里可以传入需要查询的用户，
        //我这里查询的所有待办
        List<Task> tasks = taskService.createTaskQuery().list();
        for (Task t:tasks) {
            System.out.println(t.getCreateTime());
            System.out.println(t.getId());
            System.out.println(t.getName());
            System.out.println(t.getProcessInstanceId());
            System.out.println(t.getTaskDefinitionKey());
            System.out.println(t.getParentTaskId());
        }
        return true;
    }


    /**
     * 根据流程key开启一个流程
     * @param key
     * @return
     */
    @Override
    public boolean startActivityDemo(String key) {
        ProcessInstance test01 = runtimeService.startProcessInstanceByKey(key);
        String id = test01.getId();
        System.out.println("流程id="+id);
          //
    /*    String assignee = "abc";*/
        return true;
    }

    /**
     * 根据任务id提交任务
     * @param taskId
     * @return
     */
    @Override
    public boolean  complete(String taskId){
        taskService.complete(taskId);
        return true;
    }

    /**
     * 根据流程id直接结束流程
     * @param runId
     * @return
     */
   @Override
   public boolean  deleteProcessInstance(String runId){
       runtimeService.deleteProcessInstance(runId,"结束");
       return true;
   }


}
