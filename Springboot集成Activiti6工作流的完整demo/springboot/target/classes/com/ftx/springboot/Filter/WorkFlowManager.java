package com.ftx.springboot.Filter;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

/**
 * @author FanJiangFeng
 * @version 1.0.0
 * @ClassName WorkFlowManager.java
 * @Description TODO
 * @createTime 2020年05月19日 13:43:00
 */
@Component
public class WorkFlowManager {

    private static final String urls="http://tiger2.cn:8085/node";


    /**
     * 提交一个新流程
     * @param processDefinitionKey 流程定义KEY
     * @param businessKey 业务编号
     * @param variables 任务负责人初始化
     * @return
     */
    public JSONObject deployProcessInstance(String processDefinitionKey, String businessKey, Map<String,Object> variables){
        String json = JSONObject.toJSONString(variables);
        RestTemplate restTemplate = new RestTemplate();
        String url = urls+"/deployProcessExecution?processDefinitionKey="+processDefinitionKey+"&businessKey="+businessKey+"&groupValue={json}";
        HttpHeaders headers = new HttpHeaders();
        MediaType type = MediaType.parseMediaType("application/json; charset=UTF-8");
        headers.setContentType(type);
        ResponseEntity<String> response = restTemplate.getForEntity( url, String.class,json);
        String responseData = response.getBody();
        JSONObject jsonObject= JSON.parseObject(responseData);
        return jsonObject;
    }

    //查询组任务
    public JSONObject getGroupList(String processName,String assignment){
        RestTemplate restTemplate=new RestTemplate();
        String url =urls +"/groupList?processDefinitionId="+processName+"&assignmentName="+assignment;
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        ResponseEntity<String> response = restTemplate.getForEntity( url, String.class );
        String responseData = response.getBody();
        JSONObject jsonObject= JSON.parseObject(responseData);
        return jsonObject;
    }

    //拾取组任务
    public JSONObject getGroupTask(String taskId,String assignmentName){
        RestTemplate restTemplate=new RestTemplate();
        String url =urls +"/getTask?taskId="+taskId+"&assignmentName="+assignmentName;
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        ResponseEntity<String> response = restTemplate.getForEntity( url, String.class );
        String responseData = response.getBody();
        JSONObject jsonObject= JSON.parseObject(responseData);
        return jsonObject;
    }

    //归还组任务
    public JSONObject returnGroupTask(String taskId,String assignmentName){
        RestTemplate restTemplate=new RestTemplate();
        String url =urls +"/returnTask?taskId="+taskId+"&assignmentName="+assignmentName;
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        ResponseEntity<String> response = restTemplate.getForEntity( url, String.class );
        String responseData = response.getBody();
        JSONObject jsonObject= JSON.parseObject(responseData);
        return jsonObject;
    }


    //根据流程实例ID得到实例(包括业务编号)
    public JSONObject getInstanceByInstanceId(String processInstanceId){
        RestTemplate restTemplate=new RestTemplate();
        HttpHeaders headers1 = new HttpHeaders();
        headers1.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        ResponseEntity<String> response1 = restTemplate.getForEntity( urls+"/getExecutionByProcessInstanceId?processInstanceId="+processInstanceId, String.class );
        String responseData1 = response1.getBody();
        JSONObject jsonObject1 = JSONObject.parseObject(responseData1);
        return jsonObject1;
    }

    //根据流程定义名称和负责人查询待办
    public JSONObject getDbList(String processDefinitionId,String assignmentName){
        RestTemplate restTemplate=new RestTemplate();
        String url = urls+"/dbList?processDefinitionId="+processDefinitionId+"&assignmentName="+assignmentName;
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        ResponseEntity<String> response = restTemplate.getForEntity( url, String.class );
        String responseData = response.getBody();
        JSONObject jsonObject = JSONObject.parseObject(responseData);
        return jsonObject;
    }

    //处理待办  通过
    public JSONObject soluteTask(String taskId,String assignmentName){
        RestTemplate restTemplate=new RestTemplate();
        String url = urls+"/soluteToNext?taskId="+taskId+"&assignmentName="+assignmentName;
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        ResponseEntity<String> response = restTemplate.getForEntity( url, String.class );
        String responseData = response.getBody();
        JSONObject jsonObject = JSONObject.parseObject(responseData);
        return jsonObject;
    }

    //处理待办  回退上节点
    public JSONObject soluteTaskToPrev(String taskId,String assignmentName){
        RestTemplate restTemplate=new RestTemplate();
        String url = urls+"/soluteToPrev?taskId="+taskId+"&assignmentName="+assignmentName;
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        ResponseEntity<String> response = restTemplate.getForEntity( url, String.class );
        String responseData = response.getBody();
        JSONObject jsonObject = JSONObject.parseObject(responseData);
        return jsonObject;
    }
}
