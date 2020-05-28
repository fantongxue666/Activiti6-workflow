package com.springboot_activiti.BusinessController;

import com.github.pagehelper.PageInfo;
import com.springboot_activiti.BusinessService.PowerService;
import com.springboot_activiti.Model.Dept;
import com.springboot_activiti.Model.TreeNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author FanJiangFeng
 * @version 1.0.0
 * @ClassName PowerController.java
 * @Description TODO
 * @createTime 2020年05月23日 17:23:00
 */
@Controller
@RequestMapping("/power")
public class PowerController {

    @Autowired
    PowerService powerService;

    /**
     * todo 权限树
     * @param session
     * @return
     */
    @RequestMapping("/tree")
    @ResponseBody
    public List<TreeNode> getTree(HttpSession session){
        List<TreeNode> treeList = powerService.getTreeList((String) session.getAttribute("account"));
        return treeList;
    }

    /**
     * todo 得到角色对应的权限（多选框加上勾勾）
     */
    @RequestMapping("/checkedTree")
    @ResponseBody
    public List<TreeNode> checkedTree(String deptno){
        List<TreeNode> checkedTree = powerService.getCheckedTree(deptno);
        return checkedTree;
    }

    /**
     * todo 点击保存授权，接收授权时勾选的权限对应id，并保存授权
     */
    @RequestMapping("/savePower")
    @ResponseBody
    public Integer savePower(String tempArr, String deptno){//参数：页面的权限对应id（html页面拼接成了“12,5,7”格式的字符串），部门编号
        int j=0;
        //把拼接成了“12,5,7”格式的字符串转为数组的格式
        String[] arr=tempArr.split(",");
        //创建一个list集合，对arr数组进行遍历，把数组中的内容全部放到list集合里
        List<String> ids=new ArrayList<String>(arr.length);
        for(String s:arr){
            ids.add(s);
        }
        //保存之前删除之前的所有权限
        powerService.deletePower(deptno);
        //对权限对应id的集合进行遍历，然后进行插入
        for(String id:ids){
            Integer intId=Integer.valueOf(id);
            //在tb_dept_power表插入数据，给角色保存权限
            int i = powerService.savePower(intId, deptno);
            j+=1;
        }
        return 1;

    }

    /**
     * todo 部门列表
     */
    @RequestMapping("/deptList")
    public String deptList(Integer pn, Model model){
        PageInfo<Dept> deptList = powerService.getDeptList(pn);
        model.addAttribute("list",deptList);
        return "back/deptList";
    }

    /**
     * todo 新增部门
     */
    @RequestMapping("/insertDept")
    @ResponseBody
    public Integer insertDept(Dept dept){
        int i = powerService.insertDept(dept);
        return i;
    }

    /**
     * todo 删除部门
     */
    @RequestMapping("/delDeptById")
    @ResponseBody
    public int delDeptById(Integer id){
       return powerService.delDeptById(id);
    }

    /**
     * todo 用户列表
     */
    @RequestMapping("/userList")
    public String userList(Integer pn,Model model){
        PageInfo<Map> userList = powerService.getUserList(pn);
        model.addAttribute("list",userList);
        return "back/userList";
    }

    /**
     * todo 启用用户
     */
    @RequestMapping("/startUser")
    @ResponseBody
    public int startUser(Integer id){
        return powerService.startUser(id);
    }
    /**
     * todo 禁用用户
     */
    @RequestMapping("/endUser")
    @ResponseBody
    public int endUser(Integer id){
        return powerService.endUser(id);
    }
}
