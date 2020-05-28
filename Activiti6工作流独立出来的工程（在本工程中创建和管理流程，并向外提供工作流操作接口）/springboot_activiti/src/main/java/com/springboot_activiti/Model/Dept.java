package com.springboot_activiti.Model;

/**
 * @author FanJiangFeng
 * @version 1.0.0
 * @ClassName Dept.java
 * @Description TODO
 * @createTime 2020年05月23日 20:46:00
 */
public class Dept {
    private Integer id;
    private String deptno;
    private String deptname;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDeptno() {
        return deptno;
    }

    public void setDeptno(String deptno) {
        this.deptno = deptno;
    }

    public String getDeptname() {
        return deptname;
    }

    public void setDeptname(String deptname) {
        this.deptname = deptname;
    }
}
