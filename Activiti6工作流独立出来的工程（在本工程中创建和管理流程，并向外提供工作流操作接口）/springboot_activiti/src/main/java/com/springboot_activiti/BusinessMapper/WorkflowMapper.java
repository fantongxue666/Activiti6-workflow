package com.springboot_activiti.BusinessMapper;

import com.springboot_activiti.Model.ProcessDefinitions;
import com.springboot_activiti.Model.ProcessExecutions;
import com.springboot_activiti.Model.ProcessModel;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @author FanJiangFeng
 * @version 1.0.0
 * @ClassName WorkflowMapper.java
 * @Description TODO
 * @createTime 2020年05月16日 16:28:00
 */
@Mapper
public interface WorkflowMapper {

    //todo 查询已部署的流程定义信息
    @Select({"<script>",
            "select * from ACT_RE_PROCDEF",
            "where 1=1",
            "<when test='_parameter!=null'>",
            "AND ID_ = #{_parameter}",
            "</when>",
            "</script>"})
    List<ProcessDefinitions> getAllProcessDefinitions(String processDefifitionId);

    //todo 查询全部的模型信息Model
    @Select({"<script>",
            "select * from ACT_RE_MODEL",
            "where 1=1",
            "<when test='_parameter!=null'>",
            "AND ID_ = #{_parameter}",
            "</when>",
            "</script>"})
    List<ProcessModel> getModelList(String modelId);

    //todo 根据模型ID删除模型
    @Delete(value = "delete from ACT_RE_MODEL where ID_=#{id}")
    int delModelByModelId(String id);

    //todo 查询流程定义下的流程实例
    @Select(value = "select * from ACT_RU_EXECUTION where PROC_DEF_ID_ =#{id}")
    List<ProcessExecutions> getProcessExecutionsByProcessDefinitionId(String id);

    //todo 查询所有的流程实例
    @Select({"<script>",
            "select * from ACT_RU_EXECUTION",
            "where 1=1",
            "<when test='_parameter!=null'>",
            "AND PROC_DEF_ID_ = #{_parameter}",
            "</when>",
            "</script>"})
    List<ProcessExecutions> getAllExcutions1(String processDefinitionId);

    @Select(value = "select * from ACT_RU_EXECUTION")
    List<ProcessExecutions> getAllExcutions();

    //todo 根据业务编号得到流程实例信息
    @Select(value = "select * from ACT_RU_EXECUTION where BUSINESS_KEY_=#{businessKey}")
    ProcessExecutions getExcutionsByBusinessKey(String businessKey);

    //todo 根据流程实例ID得到流程实例的详细信息
    @Select(value = "select * from ACT_RU_EXECUTION where ID_ = #{processInstanceId}")
    ProcessExecutions getExecutionByProcessInstanceId(String processInstanceId);
}
