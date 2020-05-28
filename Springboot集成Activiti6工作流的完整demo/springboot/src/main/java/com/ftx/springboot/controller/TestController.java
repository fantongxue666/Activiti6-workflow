package com.ftx.springboot.controller;

import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.*;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.ftx.springboot.Cache.CacheManager;
import com.ftx.springboot.Filter.WorkFlowManager;
import com.ftx.springboot.Mapper.TestMapper;
import com.ftx.springboot.utils.EmailUtil;
import org.activiti.bpmn.model.BpmnModel;
import org.activiti.bpmn.model.FlowElement;
import org.activiti.bpmn.model.FlowNode;
import org.activiti.bpmn.model.SequenceFlow;
import org.activiti.engine.*;
import org.activiti.engine.history.HistoricActivityInstance;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.impl.RepositoryServiceImpl;
import org.activiti.engine.impl.persistence.entity.ExecutionEntity;
import org.activiti.engine.impl.persistence.entity.ProcessDefinitionEntity;
import org.activiti.engine.impl.persistence.entity.ProcessDefinitionEntityImpl;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.runtime.Execution;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.activiti.spring.ProcessEngineFactoryBean;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/test")
public class TestController {

	@Autowired
	TestMapper mapper;
	@Autowired
    EmailUtil emailUtil;
	
	@RequestMapping("/toLogin")
	public String one() {

        return "login";
	}

	/**
	 * todo 登录，记录session，跳转到待办项列表页
	 * @param username
	 * @param session
	 * @param model
	 * @return
	 */
	@Autowired
    CacheManager cacheManager;


	@PostMapping("/login")
	public String toIndex(String username,String password, HttpSession session, Model model){
        Map map=new HashMap();
	    if(cacheManager.get(username)!=null){
            map=(Map) cacheManager.get(username);

        }else{
            map = mapper.selectUserByAccountAndPassword(username, password);
        }


        if(map!=null){
            session.setAttribute("username",username);
            model.addAttribute("username",username);
            cacheManager.addCache(username,map);
            return "index";
        }else {
            model.addAttribute("msg","用户名或密码错误");
            return "login";
        }



	}

	@Autowired
    WorkFlowManager workFlowManager;

	@RequestMapping("/list")
	public String toList(HttpSession session,Model model){
        JSONObject jsonObject = workFlowManager.getDbList("process", (String) session.getAttribute("username"));
        List<Map> maps=new ArrayList<Map>();
        String listString=(String) jsonObject.get("data");
        List<Map> list = JSONObject.parseArray(listString, Map.class);
        for(Map map:list){

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
        return "list";
	}

	/**
	 * todo 处理待办
	 */
	@RequestMapping("/solute")
	@ResponseBody
	public Integer solute(String taskId,String id,boolean is,HttpSession session) throws Exception {
		if(is==true){
			//通过
            JSONObject jsonObject = workFlowManager.soluteTask(taskId, (String) session.getAttribute("username"));
            if("200".equals((String)jsonObject.get("status"))){
                return 0;
            }else{
                return 1;
            }

        }else if(is==false){
			//回退上一节点
            JSONObject jsonObject = workFlowManager.soluteTaskToPrev(taskId, (String) session.getAttribute("username"));
            if("200".equals((String)jsonObject.get("status"))){
                return 0;
            }else{
                return 1;
            }
        }


		return 0;
	}

    /**
     * todo 资源部审核通过之后发送邮箱通知审核结果
     */
    private void sendEmail(String emailAddress) throws Exception {
        int beginIndex=emailAddress.indexOf("@");
        int endIndex = emailAddress.indexOf(".");
        String suffix = emailAddress.substring(beginIndex + 1, endIndex);
        if("163".equals(suffix)){
            //发送网易163邮箱
            emailUtil.sendWY163Email(emailAddress);
        }else if("qq".equals(suffix)){
            //发送QQ邮箱
            emailUtil.sendQQEmail(emailAddress);
        }

    }


	/**
	 * @Description
	 * @Date 2020/5/7 17:20
	 * @return todo 提交请假单
	 * @Author FanJiangFeng
	 * @Version1.0
	 * @History
	 */
	@RequestMapping("/form")
	public String form() {
		return "form";
	}

	@RequestMapping("/submit")
	@ResponseBody
	public Integer submit(String name,String time,String reason,Model model){
		Map map=new HashMap();
		map.put("id", UUID.randomUUID().toString().replaceAll("-",""));
		map.put("name",name);
		map.put("time",time);
		map.put("reason",reason);
		map.put("status","业务部审批");
		int i = mapper.insertHoliday(map);

		Map<String,Object> variables =new HashMap<String, Object>();
        // variables.put("group1", "张三，李四，王五");${group1}组的成员
        //业务部成员
        List<Map> ywbList = mapper.selectUserByDeptId(1);
        String ywbTempStr="";
        for(int t=0;t<ywbList.size();t++){
            if(t==ywbList.size()-1){
                ywbTempStr+=ywbList.get(t).get("account");
            }else{
                ywbTempStr+=ywbList.get(t).get("account")+",";
            }
        }
        variables.put("group1",ywbTempStr);
        //技术部成员
        List<Map> jsbList = mapper.selectUserByDeptId(2);
        String jsbTempStr="";
        for(int t=0;t<jsbList.size();t++){
            if(t==jsbList.size()-1){
                jsbTempStr+=jsbList.get(t).get("account");
            }else{
                jsbTempStr+=jsbList.get(t).get("account")+",";
            }
        }
        variables.put("group2",jsbTempStr);
        //领导部成员
        List<Map> ldbList = mapper.selectUserByDeptId(3);
        String ldbTempStr="";
        for(int t=0;t<ldbList.size();t++){
            if(t==ldbList.size()-1){
                ldbTempStr+=ldbList.get(t).get("account");
            }else{
                ldbTempStr+=ldbList.get(t).get("account")+",";
            }
        }
        variables.put("group3",ldbTempStr);
        //资源部成员
        List<Map> zybList = mapper.selectUserByDeptId(4);
        String zybTempStr="";
        for(int t=0;t<zybList.size();t++){
            if(t==zybList.size()-1){
                zybTempStr+=zybList.get(t).get("account");
            }else{
                zybTempStr+=zybList.get(t).get("account")+",";
            }
        }
        variables.put("group4",zybTempStr);
		workFlowManager.deployProcessInstance("process",(String) map.get("id"),variables);
		if(i>0){
			return 1;
		}else{
			return 0;
		}
	}

	@RequestMapping("/logout")
	public String logout(HttpSession session){
		session.removeAttribute("username");
		return "login";
	}

	@RequestMapping("/logo")
	public String logo(){
		return "logo";
	}

	
	
	

}
