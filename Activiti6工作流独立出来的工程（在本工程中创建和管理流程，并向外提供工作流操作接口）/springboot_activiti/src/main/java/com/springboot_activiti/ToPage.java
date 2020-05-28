package com.springboot_activiti;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/detail")
public class ToPage {


    @RequestMapping("/modeler")
    public String test02(String modelId){
        System.out.println(123);
        return "redirect:/html/modeler.html?modelId="+modelId;
    }

}
