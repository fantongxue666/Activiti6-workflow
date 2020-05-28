package com.springboot_activiti.BusinessController;

import com.springboot_activiti.BusinessService.BusineService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Map;

/**
 * @author FanJiangFeng
 * @version 1.0.0
 * @ClassName ActivitiController.java
 * @Description TODO
 * @createTime 2020年05月16日 10:46:00
 */
@Controller
@RequestMapping("/business")
public class BusineController {

    @Autowired
    BusineService busineService;

    @GetMapping("/login")
    public String login(){
        return "login";
    }

    //后台主页
    @RequestMapping("/index")
    public String index(){
        return "back/index";
    }

    //流程定义管理列表
//    @RequestMapping("/list")
//    public String list(){
//        return "back/processDefinition";
//    }
    //编辑示例
    @RequestMapping("/toEdit")
    public String toEdit(){
        return "back/house_edit";
    }
    //楼盘状态示例
    @RequestMapping("/loupan")
    public String loupan(){
        return "back/loupanchart";
    }
    //欢迎页
    @RequestMapping("/welcome")
    public String welcome(){
        return "back/introduce";
    }
    //仪表盘
    @RequestMapping("/yibiaopan")
    public String ybp(){
        return "back/yibiaopan";
    }

    /**
     * todo 退出系统
     */
    @RequestMapping("/logout")
    public String logout(HttpSession session){
        session.removeAttribute("account");
        return "forward:/business/login";
    }

    /**
     * todo 取登录名称
     */
    @RequestMapping("/getSession")
    @ResponseBody
    public String getSession(HttpSession session){
        String name=(String) session.getAttribute("account");
        return name;
    }

    /**
     * todo 登录请求
     */
    @PostMapping("/loginSubmit")
    public String loginSubmit(String username, String password, HttpSession session, Model model){
        List<Map> accountByPassword = busineService.getAccountByPassword(username, password);
        if(accountByPassword.size()>0){
            //用户存在
            session.setAttribute("account",username);
            model.addAttribute("account",username);
            return "redirect:/business/index";
        }else{
            //用户不存在
            model.addAttribute("msg","用户名或密码错误");
            return "login";
        }
    }


}
