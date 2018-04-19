package com.wevolution.domain;

import java.io.Serializable;

public class WorksSample implements Serializable{
    /** 
	* @Fields serialVersionUID :  
	*/ 
	private static final long serialVersionUID = 5629603373538591502L;

	/**
     * 记录id
     */
    protected Integer recordId;

    /**
     * 作品id
     */
    protected String worksId;

    /**
     * 作品名
     */
    protected String worksName;

    /**
     * 用户id
     */
    protected String userId;

    /**
     * 作品URL
     */
    protected String worksUrl;

    /**
     * 创建时间
     */
    protected String createdTime;

    /**
     * 更新时间
     */
    protected String updateTime;

    /**
     * 审核通过时间
     */
    protected String approvedTime;

    /**
     * works_sample.record_id
     */
    public Integer getRecordId() {
        return recordId;
    }

    /**
     * works_sample.record_id
     */
    public void setRecordId(Integer recordId) {
        this.recordId = recordId;
    }

    /**
     * works_sample.works_id
     */
    public String getWorksId() {
        return worksId;
    }

    /**
     * works_sample.works_id
     */
    public void setWorksId(String worksId) {
        this.worksId = worksId == null ? null : worksId.trim();
    }

    /**
     * works_sample.works_name
     */
    public String getWorksName() {
        return worksName;
    }

    /**
     * works_sample.works_name
     */
    public void setWorksName(String worksName) {
        this.worksName = worksName == null ? null : worksName.trim();
    }

    /**
     * works_sample.user_id
     */
    public String getUserId() {
        return userId;
    }

    /**
     * works_sample.user_id
     */
    public void setUserId(String userId) {
        this.userId = userId == null ? null : userId.trim();
    }

    /**
     * works_sample.works_url
     */
    public String getWorksUrl() {
        return worksUrl;
    }

    /**
     * works_sample.works_url
     */
    public void setWorksUrl(String worksUrl) {
        this.worksUrl = worksUrl == null ? null : worksUrl.trim();
    }

    /**
     * works_sample.created_time
     */
    public String getCreatedTime() {
        return createdTime;
    }

    /**
     * works_sample.created_time
     */
    public void setCreatedTime(String createdTime) {
        this.createdTime = createdTime == null ? null : createdTime.trim();
    }

    /**
     * works_sample.update_time
     */
    public String getUpdateTime() {
        return updateTime;
    }

    /**
     * works_sample.update_time
     */
    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime == null ? null : updateTime.trim();
    }

    /**
     * works_sample.approved_time
     */
    public String getApprovedTime() {
        return approvedTime;
    }

    /**
     * works_sample.approved_time
     */
    public void setApprovedTime(String approvedTime) {
        this.approvedTime = approvedTime == null ? null : approvedTime.trim();
    }
}