package com.springboot_activiti.BusinessController;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.springboot_activiti.BusinessService.WorkflowService;
import com.springboot_activiti.Model.ProcessExecutions;
import com.springboot_activiti.utils.ListModel_to_ListMap_util;
import org.activiti.bpmn.model.BpmnModel;
import org.activiti.bpmn.model.FlowNode;
import org.activiti.bpmn.model.SequenceFlow;
import org.activiti.engine.*;
import org.activiti.engine.history.HistoricActivityInstance;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.impl.persistence.entity.ProcessDefinitionEntity;
import org.activiti.engine.runtime.Execution;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author FanJiangFeng
 * @version 1.0.0
 * @ClassName NodeController.java
 * @Description TODO
 * @createTime 2020年05月17日 17:08:00
 */
@Controller
@RequestMapping("/node")
public class NodeController {

    @Autowired
    WorkflowService workflowService;


    /**
     * todo 根据流程定义KEY和任务负责人名称查询  组任务
     */
    @RequestMapping(value = "/groupList",produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public String groupList(String processDefinitionId,String assignmentName){
        List<Map> lists=new ArrayList<Map>();
        ProcessEngine defaultProcessEngine = ProcessEngines.getDefaultProcessEngine();
        TaskService taskService = defaultProcessEngine.getTaskService();
        String candidate_users=assignmentName;//指定某一个候选人
        //查询
        List<Task> list = taskService.createTaskQuery()
                .processDefinitionKey(processDefinitionId)
                .taskCandidateUser(candidate_users).list();//设置候选用户
        List<Map<String, Object>> maps = ListModel_to_ListMap_util.listConvert(list);
        JSONObject jsonObject=new JSONObject();
        String s = JSON.toJSON(maps).toString();
        if(list!=null){
            jsonObject.put("message","success");
            jsonObject.put("status","200");
            jsonObject.put("data", s);
        }else{
            jsonObject.put("message","error");
            jsonObject.put("status","500");
        }
        return jsonObject.toJSONString();

    }

    /**
     * todo 拾取组任务
     * @param taskId    任务ID
     * @param assignmentName    候选人
     */
    @RequestMapping(value = "/getTask",produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public String getTaskByTaskId(String taskId,String assignmentName){
        ProcessEngine defaultProcessEngine = ProcessEngines.getDefaultProcessEngine();
        TaskService taskService = defaultProcessEngine.getTaskService();
        String candidate_users=assignmentName;
        //第一个参数：任务id，第二个参数：候选人
        taskService.claim(taskId,candidate_users);
        JSONObject jsonObject=new JSONObject();
        jsonObject.put("message","组任务拾取成功");
        jsonObject.put("status","200");
        return jsonObject.toJSONString();
    }

    /**
     * todo 归还组任务
     * @param taskId    任务ID
     * @param assignmentName    候选人
     * @return
     */
    @RequestMapping(value = "/returnTask",produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public String returnTaskByTaskId(String taskId,String assignmentName){
        ProcessEngine defaultProcessEngine = ProcessEngines.getDefaultProcessEngine();
        TaskService taskService = defaultProcessEngine.getTaskService();
        String candidate_users=assignmentName;
        //如果设置为null，归还组任务，该任务没有负责人
        taskService.setAssignee(taskId,null);
        JSONObject jsonObject=new JSONObject();
        jsonObject.put("message","组任务归还成功");
        jsonObject.put("status","200");
        return jsonObject.toJSONString();
    }


    /**
     * todo 根据流程定义ID和任务负责人名称查询 我的待办列表
     * @param processDefinitionId 流程定义ID
     * @param assignmentName    任务负责人
     */
    @RequestMapping(value = "/dbList",produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public String dbList(String processDefinitionId,String assignmentName){
        List<Map> lists=new ArrayList<Map>();

        //根据用户名查询待办事项
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        TaskService taskService = processEngine.getTaskService();
        List<Task> list = taskService.createTaskQuery()
                .processDefinitionKey(processDefinitionId)
                .taskAssignee(assignmentName)
                .list();
        List<Map<String, Object>> maps = ListModel_to_ListMap_util.listConvert(list);
        JSONObject jsonObject=new JSONObject();
        String s = JSON.toJSON(maps).toString();
        if(list!=null){
            jsonObject.put("message","success");
            jsonObject.put("status","200");
            jsonObject.put("data", s);
        }else{
            jsonObject.put("message","error");
            jsonObject.put("status","500");
        }
        return jsonObject.toJSONString();
    }

    /**
     * todo 部署流程实例
     * @param processDefinitionKey 流程实例key
     * @param businessKey   业务编号
     * @param groupValue    任务组变量 启动流程实例的同时，设置流程变量，使用流程变量用来指定任务办理人组
     */
    @GetMapping(value = "/deployProcessExecution", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public String deployProcessExecution(@RequestParam String processDefinitionKey, @RequestParam String businessKey, @RequestParam String groupValue){
        Map<String,Object> newGroupValue = (Map<String,Object>)JSONObject.parse(groupValue);
        ProcessEngine defaultProcessEngine = ProcessEngines.getDefaultProcessEngine();
        RuntimeService runtimeService = defaultProcessEngine.getRuntimeService();
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey(processDefinitionKey, businessKey, newGroupValue);
        JSONObject jsonObject=new JSONObject();
        if(processInstance!=null){
            jsonObject.put("message","success");
            jsonObject.put("status","200");
            return jsonObject.toJSONString();
        }else{
            jsonObject.put("message","error");
            jsonObject.put("status","500");
            return jsonObject.toJSONString();
        }
    }

    /**
     * todo 处理待办 【通过 到下一节点审核】
     * @param taskId 任务ID
     * @param assignmentName    候选人
     */
    @RequestMapping(value = "/soluteToNext",produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public String soluteTask(String taskId,String assignmentName){
        ProcessEngine defaultProcessEngine = ProcessEngines.getDefaultProcessEngine();
        TaskService taskService = defaultProcessEngine.getTaskService();
        HistoryService historyService = defaultProcessEngine.getHistoryService();
        //通过
        taskService.complete(taskId);
        JSONObject jsonObject=new JSONObject();
        jsonObject.put("message","待办已处理，已到下一节点");
        jsonObject.put("status","200");
        return jsonObject.toJSONString();

    }

    /**
     * todo 处理待办 【回退 到上一节点审核】
     * @param taskId 任务ID
     * @param assignmentName    候选人
     */
    @RequestMapping(value = "/soluteToPrev",produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public String soluteTaskReturnOne(String taskId,String assignmentName){
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        TaskService taskService = processEngine.getTaskService();
        HistoryService historyService = processEngine.getHistoryService();
        RuntimeService runtimeService = processEngine.getRuntimeService();
        RepositoryService repositoryService = processEngine.getRepositoryService();
        //根据任务ID查到当前任务
        Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
        if(task==null) {
            throw new RuntimeException("流程未启动或已执行完成，无法撤回");
        }

        //根据流程实例ID查询到历史任务（时间顺序排列【包括开始和结束节点】）
        List<HistoricActivityInstance> list = historyService.createHistoricActivityInstanceQuery()
                .processInstanceId(task.getProcessInstanceId())
                .orderByHistoricActivityInstanceStartTime().asc().list();

        //###########################################
        //根据流程实例ID查询到历史任务（时间顺序排列【不包括开始和结束节点】）
        List<HistoricTaskInstance> htiList = historyService.createHistoricTaskInstanceQuery()
                .processInstanceId(task.getProcessInstanceId())
                .orderByTaskCreateTime()
                .asc()
                .list();

        //查询到当前用户提交的历史任务ID和历史任务
        String username=assignmentName;
        String myTaskId = null;
        HistoricTaskInstance myTask = null;
        for(HistoricTaskInstance hti : htiList) {
            if(username.equals(hti.getAssignee())) {
                myTaskId = hti.getId();
                myTask = hti;
                break;
            }
        }
        if(null==myTaskId) {
            throw new RuntimeException("该任务非当前用户提交，无法撤回");
        }
        //###########################################

        //拿到流程定义ID
        String processDefinitionId = myTask.getProcessDefinitionId();

        //根据流程定义ID得到该流程定义的实体（这个没用，懒得删了）
        ProcessDefinitionEntity processDefinitionEntity = (ProcessDefinitionEntity) repositoryService.createProcessDefinitionQuery()
                .processDefinitionId(processDefinitionId)
                .singleResult();

        //根据流程定义ID得到流程图
        BpmnModel bpmnModel = repositoryService.getBpmnModel(processDefinitionId);

        //得到当前节点的活动ID   【 _5 】
        String myActivityId = myTask.getTaskDefinitionKey();
        Integer value = Integer.valueOf(myActivityId.substring(1));
        int k=value-1;

        //得到回退的目标流程节点（上一节点）	_k 拼接成上一节点的活动ID【 -4 】
        FlowNode myFlowNode = (FlowNode) bpmnModel.getMainProcess().getFlowElement("_"+k);

        Execution execution = runtimeService.createExecutionQuery().executionId(task.getExecutionId()).singleResult();
        String activityId = execution.getActivityId();
        System.out.println("------->> activityId:" + activityId);
        FlowNode flowNode = (FlowNode) bpmnModel.getMainProcess().getFlowElement(activityId);

        //记录原活动方向
        List<SequenceFlow> oriSequenceFlows = new ArrayList<SequenceFlow>();
        oriSequenceFlows.addAll(flowNode.getOutgoingFlows());

        //清理活动方向
        flowNode.getOutgoingFlows().clear();

        //建立新方向
        List<SequenceFlow> newSequenceFlowList = new ArrayList<SequenceFlow>();
        SequenceFlow newSequenceFlow = new SequenceFlow();
        newSequenceFlow.setId("newSequenceFlowId");
        newSequenceFlow.setSourceFlowElement(flowNode);//设置原节点
        newSequenceFlow.setTargetFlowElement(myFlowNode);//设置目标节点
        newSequenceFlowList.add(newSequenceFlow);
        flowNode.setOutgoingFlows(newSequenceFlowList);
        //完成任务
        taskService.complete(task.getId());
        //恢复原方向
        flowNode.setOutgoingFlows(oriSequenceFlows);
        JSONObject jsonObject=new JSONObject();
        jsonObject.put("message","待办已处理，已回退到上一节点");
        jsonObject.put("status","200");
        return jsonObject.toJSONString();
    }

    /**
     * todo 根据业务编号得到流程实例的详细信息
     * @param businessKey
     */
    @RequestMapping(value = "/getExecutionBybusinessKey",produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public String getExecutionByBusinessKey(String businessKey){
        ProcessExecutions excutionsByBusinessKey = workflowService.getExcutionsByBusinessKey(businessKey);
        String returnJson = JSONObject.toJSONString(excutionsByBusinessKey);
        JSONObject jsonObject=new JSONObject();
        jsonObject.put("message","success");
        jsonObject.put("status","200");
        jsonObject.put("data",returnJson);
        return jsonObject.toJSONString();

    }

    /**
     * todo 根据流程实例ID得到流程实例的详细信息
     */
    @RequestMapping(value = "/getExecutionByProcessInstanceId",produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public String getExecutionsByProcessInstanceId(String processInstanceId){
        ProcessExecutions executionByProcessInstanceId = workflowService.getExecutionByProcessInstanceId(processInstanceId);
        JSONObject jsonObject=new JSONObject();
        if(executionByProcessInstanceId!=null){
            jsonObject.put("message","success");
            jsonObject.put("status","200");
            jsonObject.put("data",JSONObject.toJSONString(executionByProcessInstanceId));
        }else {
            jsonObject.put("message","error");
            jsonObject.put("status","500");
        }
        return jsonObject.toJSONString();
    }

}
