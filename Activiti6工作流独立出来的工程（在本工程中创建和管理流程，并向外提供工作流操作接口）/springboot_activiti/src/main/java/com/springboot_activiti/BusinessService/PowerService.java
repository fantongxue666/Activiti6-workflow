package com.springboot_activiti.BusinessService;

import com.github.pagehelper.PageInfo;
import com.springboot_activiti.Model.Dept;
import com.springboot_activiti.Model.TreeNode;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * @author FanJiangFeng
 * @version 1.0.0
 * @ClassName PowerService.java
 * @Description TODO
 * @createTime 2020年05月23日 17:30:00
 */
public interface PowerService {

    List<TreeNode> getTreeList(String account);
    PageInfo<Dept> getDeptList(Integer pn);
    int insertDept(Dept dept);
    int delDeptById(Integer id);
    PageInfo<Map> getUserList(Integer pn);
    int startUser(Integer id);
    int endUser(Integer id);
    List<TreeNode> getCheckedTree(String deptno);
    int deletePower(String deptno);
    int savePower(@Param("intId") Integer intId, @Param("deptno") String deptno);
}
