package com.springboot_activiti.Model;

import java.util.List;

/**
 * @author FanJiangFeng
 * @version 1.0.0
 * @ClassName TreeNode.java
 * @Description TODO
 * @createTime 2020年05月23日 17:25:00
 */
public class TreeNode {
    private Integer id;
    private String resourceName;
    private Integer parentid;
    private String checked;
    private String url;
    private List<TreeNode> treeNodeList;

    public Integer getParentid() {
        return parentid;
    }

    public void setParentid(Integer parentid) {
        this.parentid = parentid;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public List<TreeNode> getTreeNodeList() {
        return treeNodeList;
    }

    public void setTreeNodeList(List<TreeNode> treeNodeList) {
        this.treeNodeList = treeNodeList;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getResourceName() {
        return resourceName;
    }

    public void setResourceName(String resourceName) {
        this.resourceName = resourceName;
    }



    public String getChecked() {
        return checked;
    }

    public void setChecked(String checked) {
        this.checked = checked;
    }


}
