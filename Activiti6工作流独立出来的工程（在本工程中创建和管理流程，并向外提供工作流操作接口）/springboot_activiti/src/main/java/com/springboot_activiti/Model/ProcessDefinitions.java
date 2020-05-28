package com.springboot_activiti.Model;

/**
 * @author FanJiangFeng
 * @version 1.0.0
 * @ClassName ProcessDefinitions.java
 * @Description TODO
 * @createTime 2020年05月17日 12:35:00
 */
public class ProcessDefinitions {
    private String ID_;
    private String REV_;
    private String CATEGORY_;
    private String NAME_;
    private String KEY_;
    private String VERSION_;
    private String DEPLOYMENT_ID_;
    private String RESOURCE_NAME_;
    private String DGRM_RESOURCE_NAME_;
    private String DESCRIPTION_;
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

    public String getCATEGORY_() {
        return CATEGORY_;
    }

    public void setCATEGORY_(String CATEGORY_) {
        this.CATEGORY_ = CATEGORY_;
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

    public String getVERSION_() {
        return VERSION_;
    }

    public void setVERSION_(String VERSION_) {
        this.VERSION_ = VERSION_;
    }

    public String getDEPLOYMENT_ID_() {
        return DEPLOYMENT_ID_;
    }

    public void setDEPLOYMENT_ID_(String DEPLOYMENT_ID_) {
        this.DEPLOYMENT_ID_ = DEPLOYMENT_ID_;
    }

    public String getRESOURCE_NAME_() {
        return RESOURCE_NAME_;
    }

    public void setRESOURCE_NAME_(String RESOURCE_NAME_) {
        this.RESOURCE_NAME_ = RESOURCE_NAME_;
    }

    public String getDGRM_RESOURCE_NAME_() {
        return DGRM_RESOURCE_NAME_;
    }

    public void setDGRM_RESOURCE_NAME_(String DGRM_RESOURCE_NAME_) {
        this.DGRM_RESOURCE_NAME_ = DGRM_RESOURCE_NAME_;
    }

    public String getDESCRIPTION_() {
        return DESCRIPTION_;
    }

    public void setDESCRIPTION_(String DESCRIPTION_) {
        this.DESCRIPTION_ = DESCRIPTION_;
    }
}
