package com.springboot_activiti.service;

public interface ActivityConsumerService {

    boolean createDeployment();
    boolean startActivityDemo(String key);
    boolean getTaskList();
    boolean  complete(String taskId);
    boolean  deleteProcessInstance(String runId);
}
