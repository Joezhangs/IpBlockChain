package com.wevolution.domain;

public class CopyrightAudit {
    /**
     * id
     */
    protected Integer recordId;

    /**
     * 作品名
     */
    protected String worksName;

    /**
     * 作品id
     */
    protected String worksId;

    /**
     * 用户id
     */
    protected String userId;

    /**
     * 更新时间
     */
    protected String updateTime;

    /**
     * 
     */
    protected String updateFlag;

    /**
     * 
     */
    protected String reqNo;

    /**
     * 块id
     */
    protected Integer blockId;

    /**
     * 创建时间
     */
    protected String createdTime;

    /**
     * 作者署名
     */
    protected String authorSing;

    /**
     * 作者
     */
    protected String author;

    /**
     * 审核状态（1：录入；2：共识成功；3：共识失败；4：共识超时；5：审核成功；6审核失败）
     */
    protected Byte approvedStatus;

    /**
     * 审核描述（失败原因）
     */
    protected String approvedDescription;

    /**
     * copyright_audit.record_id
     */
    public Integer getRecordId() {
        return recordId;
    }

    /**
     * copyright_audit.record_id
     */
    public void setRecordId(Integer recordId) {
        this.recordId = recordId;
    }

    /**
     * copyright_audit.works_name
     */
    public String getWorksName() {
        return worksName;
    }

    /**
     * copyright_audit.works_name
     */
    public void setWorksName(String worksName) {
        this.worksName = worksName == null ? null : worksName.trim();
    }

    /**
     * copyright_audit.works_id
     */
    public String getWorksId() {
        return worksId;
    }

    /**
     * copyright_audit.works_id
     */
    public void setWorksId(String worksId) {
        this.worksId = worksId == null ? null : worksId.trim();
    }

    /**
     * copyright_audit.user_id
     */
    public String getUserId() {
        return userId;
    }

    /**
     * copyright_audit.user_id
     */
    public void setUserId(String userId) {
        this.userId = userId == null ? null : userId.trim();
    }

    /**
     * copyright_audit.update_time
     */
    public String getUpdateTime() {
        return updateTime;
    }

    /**
     * copyright_audit.update_time
     */
    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime == null ? null : updateTime.trim();
    }

    /**
     * copyright_audit.update_flag
     */
    public String getUpdateFlag() {
        return updateFlag;
    }

    /**
     * copyright_audit.update_flag
     */
    public void setUpdateFlag(String updateFlag) {
        this.updateFlag = updateFlag == null ? null : updateFlag.trim();
    }

    /**
     * copyright_audit.req_no
     */
    public String getReqNo() {
        return reqNo;
    }

    /**
     * copyright_audit.req_no
     */
    public void setReqNo(String reqNo) {
        this.reqNo = reqNo == null ? null : reqNo.trim();
    }

    /**
     * copyright_audit.block_id
     */
    public Integer getBlockId() {
        return blockId;
    }

    /**
     * copyright_audit.block_id
     */
    public void setBlockId(Integer blockId) {
        this.blockId = blockId;
    }

    /**
     * copyright_audit.created_time
     */
    public String getCreatedTime() {
        return createdTime;
    }

    /**
     * copyright_audit.created_time
     */
    public void setCreatedTime(String createdTime) {
        this.createdTime = createdTime == null ? null : createdTime.trim();
    }

    /**
     * copyright_audit.author_sing
     */
    public String getAuthorSing() {
        return authorSing;
    }

    /**
     * copyright_audit.author_sing
     */
    public void setAuthorSing(String authorSing) {
        this.authorSing = authorSing == null ? null : authorSing.trim();
    }

    /**
     * copyright_audit.author
     */
    public String getAuthor() {
        return author;
    }

    /**
     * copyright_audit.author
     */
    public void setAuthor(String author) {
        this.author = author == null ? null : author.trim();
    }

    /**
     * copyright_audit.approved_status
     */
    public Byte getApprovedStatus() {
        return approvedStatus;
    }

    /**
     * copyright_audit.approved_status
     */
    public void setApprovedStatus(Byte approvedStatus) {
        this.approvedStatus = approvedStatus;
    }

    /**
     * copyright_audit.approved_description
     */
    public String getApprovedDescription() {
        return approvedDescription;
    }

    /**
     * copyright_audit.approved_description
     */
    public void setApprovedDescription(String approvedDescription) {
        this.approvedDescription = approvedDescription == null ? null : approvedDescription.trim();
    }
}