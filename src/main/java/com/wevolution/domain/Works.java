package com.wevolution.domain;

import java.io.Serializable;

public class Works implements Serializable{
    /** 
	* @Fields serialVersionUID :  
	*/ 
	private static final long serialVersionUID = 5597983116442169313L;

	/**
     * 记录id
     */
    protected Integer recordId;

    /**
     * 用户id
     */
    protected String userId;

    /**
     * 作品id
     */
    protected String worksId;

    /**
     * 作品名
     */
    protected String worksName;

    /**
     * 作品署名
     */
    protected String worksSign;

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
     * 审核状态（1：录入；2：共识成功；3：共识失败；4：共识超时；5：审核成功；6审核失败）
     */
    protected Byte approvedStatus;

    /**
     * 审核通过时间
     */
    protected String approvedTime;

    /**
     * 审核描述（失败原因）
     */
    protected String approvedDescription;

    /**
     * 作品登记进度（0：作品文件；1：作品信息；2：作者及著作权；3：样品上传；//4：短信确认）
     */
    protected Integer progress;

    /**
     * works.record_id
     */
    public Integer getRecordId() {
        return recordId;
    }

    /**
     * works.record_id
     */
    public void setRecordId(Integer recordId) {
        this.recordId = recordId;
    }

    /**
     * works.user_id
     */
    public String getUserId() {
        return userId;
    }

    /**
     * works.user_id
     */
    public void setUserId(String userId) {
        this.userId = userId == null ? null : userId.trim();
    }

    /**
     * works.works_id
     */
    public String getWorksId() {
        return worksId;
    }

    /**
     * works.works_id
     */
    public void setWorksId(String worksId) {
        this.worksId = worksId == null ? null : worksId.trim();
    }

    /**
     * works.works_name
     */
    public String getWorksName() {
        return worksName;
    }

    /**
     * works.works_name
     */
    public void setWorksName(String worksName) {
        this.worksName = worksName == null ? null : worksName.trim();
    }

    /**
     * works.works_sign
     */
    public String getWorksSign() {
        return worksSign;
    }

    /**
     * works.works_sign
     */
    public void setWorksSign(String worksSign) {
        this.worksSign = worksSign == null ? null : worksSign.trim();
    }

    /**
     * works.works_url
     */
    public String getWorksUrl() {
        return worksUrl;
    }

    /**
     * works.works_url
     */
    public void setWorksUrl(String worksUrl) {
        this.worksUrl = worksUrl == null ? null : worksUrl.trim();
    }

    /**
     * works.created_time
     */
    public String getCreatedTime() {
        return createdTime;
    }

    /**
     * works.created_time
     */
    public void setCreatedTime(String createdTime) {
        this.createdTime = createdTime == null ? null : createdTime.trim();
    }

    /**
     * works.update_time
     */
    public String getUpdateTime() {
        return updateTime;
    }

    /**
     * works.update_time
     */
    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime == null ? null : updateTime.trim();
    }

    /**
     * works.approved_status
     */
    public Byte getApprovedStatus() {
        return approvedStatus;
    }

    /**
     * works.approved_status
     */
    public void setApprovedStatus(Byte approvedStatus) {
        this.approvedStatus = approvedStatus;
    }

    /**
     * works.approved_time
     */
    public String getApprovedTime() {
        return approvedTime;
    }

    /**
     * works.approved_time
     */
    public void setApprovedTime(String approvedTime) {
        this.approvedTime = approvedTime == null ? null : approvedTime.trim();
    }

    /**
     * works.approved_description
     */
    public String getApprovedDescription() {
        return approvedDescription;
    }

    /**
     * works.approved_description
     */
    public void setApprovedDescription(String approvedDescription) {
        this.approvedDescription = approvedDescription == null ? null : approvedDescription.trim();
    }

    /**
     * works.progress
     */
    public Integer getProgress() {
        return progress;
    }

    /**
     * works.progress
     */
    public void setProgress(Integer progress) {
        this.progress = progress;
    }
}