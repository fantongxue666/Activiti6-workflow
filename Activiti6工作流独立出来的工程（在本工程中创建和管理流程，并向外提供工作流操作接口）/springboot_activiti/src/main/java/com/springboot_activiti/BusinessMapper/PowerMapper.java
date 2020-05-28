package com.springboot_activiti.BusinessMapper;

import com.springboot_activiti.Model.Dept;
import com.springboot_activiti.Model.TreeNode;
import org.apache.ibatis.annotations.*;

import java.util.List;
import java.util.Map;

/**
 * @author FanJiangFeng
 * @version 1.0.0
 * @ClassName PowerMapper.java
 * @Description TODO
 * @createTime 2020年05月23日 17:24:00
 */
@Mapper
public interface PowerMapper {

    //todo 根据登录用户的账号查到对应权限下的Tree
    @Select(value = "select * from activiti_power where id in(select powerid from activiti_dept_power where deptno=(select deptno from activiti_account where username=#{account}))")
    List<TreeNode> getTreeList(String account);

    //todo 查询所有的权限表数据
    @Select(value = "select * from activiti_power")
    List<TreeNode> powerList();

    //todo 查询角色拥有的权限
    @Select(value = "select powerid from activiti_dept_power where deptno=#{deptno}")
    List<Map> selectPowerListByDeptno(String deptno);

    //todo 保存授权之前删除所有权限
    @Delete(value = "delete from activiti_dept_power where deptno=#{deptno}")
    int deletePower(String deptno);

    //todo 保存授权
    @Insert(value = "insert into activiti_dept_power(deptno,powerid) values (#{deptno},#{intId})")
    int savePower(@Param("intId") Integer intId,@Param("deptno") String deptno);

    //todo 查询部门列表
    @Select(value = "select * from activiti_dept")
    List<Dept> getDeptList();

    //todo 新增部门
    @Insert(value = "insert into activiti_dept(deptno,deptname) values(#{deptno},#{deptname})")
    int insertDept(Dept dept);

    //todo 删除部门
    @Delete(value = "delete from activiti_dept where id=#{id}")
    int delDeptById(Integer id);

    //todo 查询用户列表
    @Select(value = "select * from activiti_account")
    List<Map> getUserList();

    //todo 启用和禁用用户
    @Update(value = "update activiti_account set yxbz=1 where id=#{id}")
    int startUser(Integer id);
    @Update(value = "update activiti_account set yxbz=0 where id=#{id}")
    int endUser(Integer id);
}
