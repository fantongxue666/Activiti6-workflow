package com.springboot_activiti.controller;

import org.apache.commons.io.IOUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.InputStream;

@RestController
public class Test01Controller {

    @RequestMapping("/editor/stencilset")
    public String test01(){
        InputStream stream=this.getClass().getClassLoader().getResourceAsStream("stencilset.json");
        try{
            return IOUtils.toString(stream,"utf-8");
        }catch (Exception e){

        }
        return null;
    }
}
