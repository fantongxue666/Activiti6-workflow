package com.springboot_activiti.Model;

import java.util.Date;

/**
 * @author FanJiangFeng
 * @version 1.0.0
 * @ClassName ProcessModel.java
 * @Description TODO
 * @createTime 2020年05月16日 17:47:00
 */
public class ProcessModel {
    private String ID_;
    private String REV_;
    private String NAME_;
    private String KEY_;
    private Date CREATE_TIME_;
    private String VERSION_;

    public String getID_() {
        return ID_;
    }

    public void setID_(String ID_) {
        this.ID_ = ID_;
    }

    public String getREV_() {
        return REV_;
    }

    public void setREV_(String REV_) {
        this.REV_ = REV_;
    }

    public String getNAME_() {
        return NAME_;
    }

    public void setNAME_(String NAME_) {
        this.NAME_ = NAME_;
    }

    public String getKEY_() {
        return KEY_;
    }

    public void setKEY_(String KEY_) {
        this.KEY_ = KEY_;
    }

    public Date getCREATE_TIME_() {
        return CREATE_TIME_;
    }

    public void setCREATE_TIME_(Date CREATE_TIME_) {
        this.CREATE_TIME_ = CREATE_TIME_;
    }

    public String getVERSION_() {
        return VERSION_;
    }

    public void setVERSION_(String VERSION_) {
        this.VERSION_ = VERSION_;
    }
}
