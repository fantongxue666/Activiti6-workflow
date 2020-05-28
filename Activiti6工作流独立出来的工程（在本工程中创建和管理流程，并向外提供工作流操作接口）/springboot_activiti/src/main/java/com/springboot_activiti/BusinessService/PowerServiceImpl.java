package com.springboot_activiti.BusinessService;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.springboot_activiti.BusinessMapper.PowerMapper;
import com.springboot_activiti.Model.Dept;
import com.springboot_activiti.Model.TreeNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author FanJiangFeng
 * @version 1.0.0
 * @ClassName PowerServiceImpl.java
 * @Description TODO
 * @createTime 2020年05月23日 17:30:00
 */
@Service
public class PowerServiceImpl implements PowerService {

    @Autowired
    PowerMapper powerMapper;

    @Override
    public List<TreeNode> getTreeList(String account) {
        List<TreeNode> treeList = powerMapper.getTreeList(account);
        List<TreeNode> tempList=new ArrayList<>();
        if(treeList!=null&&treeList.size()>0){
            for(TreeNode treeNode:treeList){
                if(treeNode.getParentid()==0){
                    tempList.add(treeNode);
                    //递归绑定子节点
                    bindChildren(treeNode,treeList);
                }
            }
        }
        return tempList;
    }

    @Override
    public PageInfo<Dept> getDeptList(Integer pn) {
        if(pn==null){
            pn=1;
        }
        PageHelper.startPage(pn,10);
        List<Dept> deptList = powerMapper.getDeptList();
        PageInfo<Dept> pageInfo=new PageInfo<Dept>(deptList);
        return pageInfo;

    }

    @Override
    public int insertDept(Dept dept) {
        return powerMapper.insertDept(dept);
    }

    @Override
    public int delDeptById(Integer id) {
        return powerMapper.delDeptById(id);
    }

    @Override
    public PageInfo<Map> getUserList(Integer pn) {
        if(pn==null){
            pn=1;
        }
        PageHelper.startPage(pn,10);
        List<Map> userList = powerMapper.getUserList();
        PageInfo<Map> pageInfo=new PageInfo<Map>(userList);
        return pageInfo;
    }

    @Override
    public int startUser(Integer id) {
        return powerMapper.startUser(id);
    }

    @Override
    public int endUser(Integer id) {
        return powerMapper.endUser(id);
    }

    @Override
    public List<TreeNode> getCheckedTree(String deptno) {
        List<TreeNode> powerList = powerMapper.powerList();
        List<Map> selectPowerListByDeptno = powerMapper.selectPowerListByDeptno(deptno);
        if(powerList!=null&&powerList.size()>0){
            for(TreeNode treeNode:powerList){
                if(selectPowerListByDeptno!=null&&selectPowerListByDeptno.size()>0){
                    for(Map map:selectPowerListByDeptno){
                        if(map.get("powerid").equals(treeNode.getId())){
                            treeNode.setChecked("checked");
                        }
                    }
                }
            }
        }
        List<TreeNode> tempList=new ArrayList<>();
        if(powerList!=null&&powerList.size()>0){
            for(TreeNode treeNode:powerList){
                if(treeNode.getParentid()==0){
                    tempList.add(treeNode);
                    bindChildren(treeNode,powerList);
                }
            }
        }
    return tempList;
    }

    @Override
    public int deletePower(String deptno) {
        return powerMapper.deletePower(deptno);
    }

    @Override
    public int savePower(Integer intId, String deptno) {
        return powerMapper.savePower(intId,deptno);
    }


    private void bindChildren(TreeNode treeNode, List<TreeNode> treeList) {
        for(TreeNode listChildren:treeList){
            if(treeNode.getId()==listChildren.getParentid()){
                List<TreeNode> treeNodeList = treeNode.getTreeNodeList();
                if(treeNodeList==null){
                    List<TreeNode> tempList=new ArrayList<>();
                    tempList.add(listChildren);
                    treeNode.setTreeNodeList(tempList);
                }else {
                    treeNodeList.add(listChildren);
                }
                bindChildren(listChildren,treeList);
            }
        }
    }
}
