package com.springboot_activiti.BusinessController;

import com.github.pagehelper.PageInfo;
import com.springboot_activiti.BusinessService.WorkflowService;
import com.springboot_activiti.Model.ProcessDefinitions;
import com.springboot_activiti.Model.ProcessExecutions;
import com.springboot_activiti.Model.ProcessModel;
import org.activiti.engine.*;
import org.activiti.engine.identity.Group;
import org.activiti.engine.identity.User;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * @author FanJiangFeng
 * @version 1.0.0
 * @ClassName WorkflowController.java
 * @Description TODO
 * @createTime 2020年05月16日 15:50:00
 */
@Controller
@RequestMapping("/workflow")
public class WorkflowController {

    @Autowired
    WorkflowService workflowService;

    /**
     * todo 流程定义信息查询
     */

    @RequestMapping("/getProcessDefinitions")
    public String getProcessDefinitions(Model model,Integer pn,String processDefifitionId){
        if("".equals(processDefifitionId)){
            processDefifitionId=null;
        }
        PageInfo<ProcessDefinitions> allProcessDefinitions = workflowService.getAllProcessDefinitions(pn,processDefifitionId);
        //判断流程定义是否为挂起状态
        List<ProcessDefinitions> list = allProcessDefinitions.getList();
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        RepositoryService repositoryService = processEngine.getRepositoryService();
        for (ProcessDefinitions pro:list){
            ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery().processDefinitionId(pro.getID_()).singleResult();
            boolean suspended = processDefinition.isSuspended();
            if(suspended){//暂停状态
                pro.setStatus("已暂停");
            }else{
                pro.setStatus("已激活");
            }

        }

        model.addAttribute("list",allProcessDefinitions);
        return "back/processDefinition";

    }

    /**
     * todo 根据流程定义查询所有的流程实例
     */
    @RequestMapping("/getProcessExcutionByProcessDefinitionId")
    @ResponseBody
    public List<ProcessExecutions> getProcessExcutionByProcessDefinitionId(String id,Model model){
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        RuntimeService runtimeService = processEngine.getRuntimeService();
        List<ProcessExecutions> list = workflowService.getProcessExecutionsByProcessDefinitionId(id);
        ProcessInstance holiday=null;
        for(ProcessExecutions executions:list){
             holiday = runtimeService.createProcessInstanceQuery().processInstanceId(executions.getPROC_INST_ID_()).singleResult();
            boolean suspended = holiday.isSuspended();
            if(suspended){
                executions.setStatus("已死亡");
            }else{
                executions.setStatus("存活中---");
            }

        }

        return list;
    }


    /**
     * todo 挂起与激活流程定义
     */
    @RequestMapping("/active")
    @ResponseBody
    public Integer active(String id){
        ProcessEngine defaultProcessEngine = ProcessEngines.getDefaultProcessEngine();
        RepositoryService repositoryService = defaultProcessEngine.getRepositoryService();
        ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery().processDefinitionId(id).singleResult();
        boolean suspended = processDefinition.isSuspended();
        if(suspended){
            //激活流程定义
            repositoryService.activateProcessDefinitionById(id,true,null);
        }else{
            //挂起流程定义（暂停）
            repositoryService.suspendProcessDefinitionById(id,true,null);
        }
        return 1;

    }


    /**
     * todo 模型列表查询
     */
    @RequestMapping("/getModelList")
    public String getModelList(Integer pn,Model model,String modelId){
        if("".equals(modelId)){
            modelId=null;
        }
        PageInfo<ProcessModel> modelList = workflowService.getModelList(pn,modelId);
        model.addAttribute("list",modelList);
        return "back/modelList";
    }

    /**
     * todo 根据流程定义ID删除已部署成功的流程定义
     */
    @RequestMapping("/delProcessDefinitionById")
    @ResponseBody
    public Integer delProcessDefinitionById(String id){
        //1，创建processEngine对象
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        //2，得到RepositoryService对象
        RepositoryService repositoryService = processEngine.getRepositoryService();
        //3，执行删除流程定义，参数代表流程部署的id
        repositoryService.deleteDeployment(id,true);
        return 1;
    }

    /**
     * todo 根据模型ID删除模型
     */
    @RequestMapping("/delModelByModelId")
    @ResponseBody
    public Integer delModelByModelId(String id){
        return workflowService.delModelByModelId(id);
    }

    /**
     * todo 得到所有的流程实例
     */
    @RequestMapping("/getAllExecutions")
    @ResponseBody
    public List<ProcessExecutions> getAllExecutions(){
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        RuntimeService runtimeService = processEngine.getRuntimeService();
        List<ProcessExecutions> list = workflowService.getAllExcutions();
        ProcessInstance holiday=null;
        for(ProcessExecutions executions:list){
            holiday = runtimeService.createProcessInstanceQuery().processInstanceId(executions.getPROC_INST_ID_()).singleResult();
            boolean suspended = holiday.isSuspended();
            if(suspended){
                executions.setStatus("已死亡");
            }else{
                executions.setStatus("存活中---");
            }

        }

        return list;
    }

    /**
     * todo 流程实例管理列表
     */
    @RequestMapping("/processList")
    public String processList(Integer pn,String processDefinitionId,Model model){
        if("".equals(processDefinitionId)){
            processDefinitionId=null;
        }
        PageInfo<ProcessExecutions> allExcutions = workflowService.getAllExcutions1(pn, processDefinitionId);
        List<ProcessExecutions> list = allExcutions.getList();
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        RuntimeService runtimeService = processEngine.getRuntimeService();
        for (ProcessExecutions executions:list){

            ProcessInstance holiday = runtimeService.createProcessInstanceQuery().processInstanceId(executions.getPROC_INST_ID_()).singleResult();
            boolean suspended = holiday.isSuspended();
            if(suspended){
                executions.setStatus("已死亡");
            }else{
                executions.setStatus("存活中---");
            }
        }
        model.addAttribute("list",allExcutions);
        return "back/executionList";
    }

    /**
     * todo 单个流程实例的挂起与激活
     */
    @RequestMapping("/processExcutionActive")
    @ResponseBody
    public Integer processExcutionActive(String id){
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        RuntimeService runtimeService = processEngine.getRuntimeService();
        ProcessInstance holiday = runtimeService.createProcessInstanceQuery().processInstanceId(id).singleResult();
        boolean suspended = holiday.isSuspended();
        if(suspended){
            runtimeService.activateProcessInstanceById(holiday.getId());
        }else{
            runtimeService.suspendProcessInstanceById(holiday.getId());
        }
        return 1;

    }

    /**
     * todo 查询用户组信息
     */
    @RequestMapping("/getGroupList")
    @ResponseBody
    public List<Group> getGroupList(){
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        IdentityService identityService = processEngine.getIdentityService();
        List<Group> list = identityService.createGroupQuery().list();
        return list;
    }

    /**
     * todo 查询用户信息
     */
    @RequestMapping("/getUserList")
    @ResponseBody
    public List<User> getUserList(){
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        IdentityService identityService = processEngine.getIdentityService();
        List<User> list = identityService.createUserQuery().list();
        return list;
    }






}
