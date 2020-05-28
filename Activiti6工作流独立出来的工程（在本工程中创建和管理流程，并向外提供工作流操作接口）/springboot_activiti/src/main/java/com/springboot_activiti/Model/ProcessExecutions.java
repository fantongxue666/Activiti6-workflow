package com.springboot_activiti.Model;

import java.util.Date;

/**
 * @author FanJiangFeng
 * @version 1.0.0
 * @ClassName ProcessExecutions.java
 * @Description TODO
 * @createTime 2020年05月17日 13:25:00
 */
public class ProcessExecutions {
    private String ID_;
    private String REV_;
    private String PROC_INST_ID_;
    private String BUSINESS_KEY_;
    private String PARENT_ID_;
    private String PROC_DEF_ID_;
    private String ACT_ID_;
    private String IS_ACTIVE_;
    private Date START_TIME_;
    private String status;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

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

    public String getPROC_INST_ID_() {
        return PROC_INST_ID_;
    }

    public void setPROC_INST_ID_(String PROC_INST_ID_) {
        this.PROC_INST_ID_ = PROC_INST_ID_;
    }

    public String getBUSINESS_KEY_() {
        return BUSINESS_KEY_;
    }

    public void setBUSINESS_KEY_(String BUSINESS_KEY_) {
        this.BUSINESS_KEY_ = BUSINESS_KEY_;
    }

    public String getPARENT_ID_() {
        return PARENT_ID_;
    }

    public void setPARENT_ID_(String PARENT_ID_) {
        this.PARENT_ID_ = PARENT_ID_;
    }

    public String getPROC_DEF_ID_() {
        return PROC_DEF_ID_;
    }

    public void setPROC_DEF_ID_(String PROC_DEF_ID_) {
        this.PROC_DEF_ID_ = PROC_DEF_ID_;
    }

    public String getACT_ID_() {
        return ACT_ID_;
    }

    public void setACT_ID_(String ACT_ID_) {
        this.ACT_ID_ = ACT_ID_;
    }

    public String getIS_ACTIVE_() {
        return IS_ACTIVE_;
    }

    public void setIS_ACTIVE_(String IS_ACTIVE_) {
        this.IS_ACTIVE_ = IS_ACTIVE_;
    }

    public Date getSTART_TIME_() {
        return START_TIME_;
    }

    public void setSTART_TIME_(Date START_TIME_) {
        this.START_TIME_ = START_TIME_;
    }
}
