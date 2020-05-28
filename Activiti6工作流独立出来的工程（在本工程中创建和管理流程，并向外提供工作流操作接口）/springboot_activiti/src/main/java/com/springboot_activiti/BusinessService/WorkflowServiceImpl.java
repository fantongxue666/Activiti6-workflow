package com.springboot_activiti.BusinessService;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.springboot_activiti.BusinessMapper.WorkflowMapper;
import com.springboot_activiti.Model.ProcessDefinitions;
import com.springboot_activiti.Model.ProcessExecutions;
import com.springboot_activiti.Model.ProcessModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author FanJiangFeng
 * @version 1.0.0
 * @ClassName WorkflowServiceImpl.java
 * @Description TODO
 * @createTime 2020年05月16日 16:30:00
 */
@Service
public class WorkflowServiceImpl implements WorkflowService {

    @Autowired
    WorkflowMapper workflowMapper;

    @Override
    public PageInfo<ProcessDefinitions> getAllProcessDefinitions(Integer pn, String processDefifitionId) {
        if(pn==null){
            pn=1;
        }
        PageHelper.startPage(pn,10);
        List<ProcessDefinitions> list = workflowMapper.getAllProcessDefinitions(processDefifitionId);
        PageInfo<ProcessDefinitions> pageInfo=new PageInfo<ProcessDefinitions>(list);
        return pageInfo;
    }

    @Override
    public PageInfo<ProcessModel> getModelList(Integer pn,String modelId) {
        if(pn==null){
            pn=1;
        }
        PageHelper.startPage(pn,10);
        List<ProcessModel> modelList = workflowMapper.getModelList(modelId);
        PageInfo<ProcessModel> pageInfo=new PageInfo<ProcessModel>(modelList);
        return pageInfo;
    }

    @Override
    public int delModelByModelId(String id) {
        return workflowMapper.delModelByModelId(id);
    }

    @Override
    public List<ProcessExecutions> getProcessExecutionsByProcessDefinitionId(String id) {
        return workflowMapper.getProcessExecutionsByProcessDefinitionId(id);
    }

    @Override
    public List<ProcessExecutions> getAllExcutions() {
        return workflowMapper.getAllExcutions();
    }

    @Override
    public PageInfo<ProcessExecutions> getAllExcutions1(Integer pn,String processDefinitionId) {
        if(pn==null){
            pn=1;
        }
        PageHelper.startPage(pn,10);
        List<ProcessExecutions> allExcutions = workflowMapper.getAllExcutions1(processDefinitionId);
        PageInfo<ProcessExecutions> pageInfo=new PageInfo<ProcessExecutions>(allExcutions);
        return pageInfo;
    }

    @Override
    public ProcessExecutions getExcutionsByBusinessKey(String businessKey) {
        return getExcutionsByBusinessKey(businessKey);
    }

    @Override
    public ProcessExecutions getExecutionByProcessInstanceId(String processInstanceId) {
        return workflowMapper.getExecutionByProcessInstanceId(processInstanceId);
    }


}
