package com.springboot_activiti.BusinessService;

import com.github.pagehelper.PageInfo;
import com.springboot_activiti.Model.ProcessDefinitions;
import com.springboot_activiti.Model.ProcessExecutions;
import com.springboot_activiti.Model.ProcessModel;

import java.util.List;

/**
 * @author FanJiangFeng
 * @version 1.0.0
 * @ClassName WorkflowService.java
 * @Description TODO
 * @createTime 2020年05月16日 16:30:00
 */
public interface WorkflowService {

    PageInfo<ProcessDefinitions> getAllProcessDefinitions(Integer pn, String processDefifitionId);
    PageInfo<ProcessModel> getModelList(Integer pn,String modelId);
    int delModelByModelId(String id);
    List<ProcessExecutions> getProcessExecutionsByProcessDefinitionId(String id);
    List<ProcessExecutions> getAllExcutions();
    PageInfo<ProcessExecutions> getAllExcutions1(Integer pn,String processDefinitionId);
    ProcessExecutions getExcutionsByBusinessKey(String businessKey);
    ProcessExecutions getExecutionByProcessInstanceId(String processInstanceId);


}
