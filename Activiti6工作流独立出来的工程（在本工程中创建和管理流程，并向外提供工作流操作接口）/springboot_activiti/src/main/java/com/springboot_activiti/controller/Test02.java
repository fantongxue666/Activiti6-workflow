package com.springboot_activiti.controller;

//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//


import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.activiti.editor.constants.ModelDataJsonConstants;
import org.activiti.engine.ActivitiException;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.repository.Model;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Test02 implements ModelDataJsonConstants {
    protected static final Logger LOGGER = LoggerFactory.getLogger(Test02.class);
    @Autowired
    private RepositoryService repositoryService;
    @Autowired
    private ObjectMapper objectMapper;

    public Test02() {
    }

    /**
     * todo 流程图查看的请求
     * @param modelId
     * @return
     */
    @RequestMapping(
            value = {"/model/{modelId}/json"},
            method = {RequestMethod.GET},
            produces = {"application/json"}
    )
    public ObjectNode getEditorJson(@PathVariable String modelId) {
        ObjectNode modelNode = null;
        Model model = this.repositoryService.getModel(modelId);
        if (model != null) {
            try {
                if (StringUtils.isNotEmpty(model.getMetaInfo())) {
                    modelNode = (ObjectNode)this.objectMapper.readTree(model.getMetaInfo());
                } else {
                    modelNode = this.objectMapper.createObjectNode();
                    modelNode.put("name", model.getName());
                }

                modelNode.put("modelId", model.getId());
                ObjectNode editorJsonNode = (ObjectNode)this.objectMapper.readTree(new String(this.repositoryService.getModelEditorSource(model.getId()), "utf-8"));
                modelNode.put("model", editorJsonNode);
            } catch (Exception var5) {
                LOGGER.error("Error creating model JSON", var5);
                throw new ActivitiException("Error creating model JSON", var5);
            }
        }

        return modelNode;
    }
}

