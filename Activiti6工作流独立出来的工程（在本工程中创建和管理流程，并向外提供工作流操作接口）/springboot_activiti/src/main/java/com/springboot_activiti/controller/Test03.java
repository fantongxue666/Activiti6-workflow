package com.springboot_activiti.controller;

//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.activiti.bpmn.converter.BpmnXMLConverter;
import org.activiti.bpmn.model.BpmnModel;
import org.activiti.editor.constants.ModelDataJsonConstants;
import org.activiti.editor.language.json.converter.BpmnJsonConverter;
import org.activiti.engine.ActivitiException;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.Model;
import org.activiti.engine.repository.ProcessDefinition;
import org.apache.batik.transcoder.TranscoderInput;
import org.apache.batik.transcoder.TranscoderOutput;
import org.apache.batik.transcoder.image.PNGTranscoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.List;

@RestController
public class Test03 implements ModelDataJsonConstants {
    protected static final Logger LOGGER = LoggerFactory.getLogger(Test03.class);
    ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
    @Autowired
    private RepositoryService repositoryService;
    @Autowired
    private ObjectMapper objectMapper;

    @RequestMapping("/model/test02")
    public void test02(Object obj,String modelId) throws Exception {
        Model modelData = repositoryService.getModel(modelId);
        ObjectNode modelNode = (ObjectNode) new ObjectMapper().readTree(repositoryService.getModelEditorSource(modelData.getId()));
        byte[] bpmnBytes = null;

        BpmnModel model = new BpmnJsonConverter().convertToBpmnModel(modelNode);
        bpmnBytes = new BpmnXMLConverter().convertToXML(model);

        String processName = modelData.getName() + ".bpmn";

        Deployment deployment = repositoryService.createDeployment()
                .name(modelData.getName()).addString(processName, new String(bpmnBytes,"UTF-8"))
                .deploy();

    }

    @RequestMapping("/model/ById")
    public void test(@RequestParam("id") String modelId, HttpServletResponse resp) throws JsonProcessingException, IOException {
        List<ProcessDefinition> list = processEngine.getRepositoryService().
                createProcessDefinitionQuery()
                .orderByProcessDefinitionVersion().asc()
                .list();

        String deploymentId = list.get(0).getDeploymentId();
        //获取图片资源名称
        List<String> list2 = processEngine.getRepositoryService().getDeploymentResourceNames(deploymentId);
        //定义图片资源的名称
        String resourceName = "";
        if(list2!=null && list2.size()>0){
            for(String name:list2){
                if(name.indexOf(".png")>=0){
                    resourceName = name;
                }
            }
        }


        //获取图片的输入流
        InputStream in = processEngine.getRepositoryService()//
                .getResourceAsStream(deploymentId, resourceName);
        InputStream fis = new BufferedInputStream(in);
        OutputStream toClient = new BufferedOutputStream(resp.getOutputStream());
        byte[] buffer = new byte[1024*8];
        int read=0;
        //如果没有数据了会返回-1；如果还有会返回数据的长度
        while ((read = fis.read(buffer))!=-1) {
            //读取多少输出多少
            toClient.write(buffer,0,read);
        }
        toClient.flush();
        toClient.close();
        fis.close();
    }

    /**
     * todo 画流程图保存请求
     * @param modelId
     * @param req
     * @param resp
     */
    @RequestMapping("/model/{modelId}/save")
    @ResponseStatus(HttpStatus.OK)
    public void saveModel(@PathVariable String modelId, HttpServletRequest req, HttpServletResponse resp) {
        try {
            Model model = this.repositoryService.getModel(modelId);
            ObjectNode modelJson = (ObjectNode)this.objectMapper.readTree(model.getMetaInfo());
            modelJson.put("name", req.getParameter("name"));
            modelJson.put("description",req.getParameter("description"));
            model.setMetaInfo(modelJson.toString());
            model.setName(req.getParameter("name"));
            this.repositoryService.saveModel(model);
            this.repositoryService.addModelEditorSource(model.getId(), (req.getParameter("json_xml")).getBytes("utf-8"));
            InputStream svgStream = new ByteArrayInputStream((req.getParameter("svg_xml")).getBytes("utf-8"));
            TranscoderInput input = new TranscoderInput(svgStream);
            PNGTranscoder transcoder = new PNGTranscoder();
            ByteArrayOutputStream outStream = new ByteArrayOutputStream();
            TranscoderOutput output = new TranscoderOutput(outStream);
            transcoder.transcode(input, output);
            byte[] result = outStream.toByteArray();
            System.out.println(new String(result,"utf-8"));
            this.repositoryService.addModelEditorSourceExtra(model.getId(), result);
            outStream.close();
        } catch (Exception var11) {
            LOGGER.error("Error saving model", var11);
            throw new ActivitiException("Error saving model", var11);
        }
    }
}

