package com.wevolution.domain;

public class Authentication {
    /**
     * id
     */
    protected Integer recordId;

    /**
     * 用户id
     */
    protected String userId;

    /**
     * 认证id
     */
    protected String authId;

    /**
     * 真实名称（真实姓名、企业名）
     */
    protected String realName;

    /**
     * 证件类型（1：身份证号；2：社会信用代码认证；3：营业执照认证；4：非盈利组织认证；5：三证合一认证）
     */
    protected Byte idType;

    /**
     * 证件号
     */
    protected String idNumber;

    /**
     * 认证状态（1：未认证；2：认证中；3：认证成功；4：认证失败）
     */
    protected Byte identityStatus;

    /**
     * 认证方式（1：未认证；2：个人；3：企业）
     */
    protected Byte identifyType;

    /**
     * 认证提交时间
     */
    protected String submitTime;

    /**
     * 认证时间
     */
    protected String approvedTime;

    /**
     * 审核描述（失败原因）
     */
    protected String approvedDescription;

    /**
     * id类型（0：平台；1：微信；2：微博）
     */
    protected Integer accountType;

    /**
     * authentication.record_id
     */
    public Integer getRecordId() {
        return recordId;
    }

    /**
     * authentication.record_id
     */
    public void setRecordId(Integer recordId) {
        this.recordId = recordId;
    }

    /**
     * authentication.user_id
     */
    public String getUserId() {
        return userId;
    }

    /**
     * authentication.user_id
     */
    public void setUserId(String userId) {
        this.userId = userId == null ? null : userId.trim();
    }

    /**
     * authentication.auth_id
     */
    public String getAuthId() {
        return authId;
    }

    /**
     * authentication.auth_id
     */
    public void setAuthId(String authId) {
        this.authId = authId == null ? null : authId.trim();
    }

    /**
     * authentication.real_name
     */
    public String getRealName() {
        return realName;
    }

    /**
     * authentication.real_name
     */
    public void setRealName(String realName) {
        this.realName = realName == null ? null : realName.trim();
    }

    /**
     * authentication.id_type
     */
    public Byte getIdType() {
        return idType;
    }

    /**
     * authentication.id_type
     */
    public void setIdType(Byte idType) {
        this.idType = idType;
    }

    /**
     * authentication.id_number
     */
    public String getIdNumber() {
        return idNumber;
    }

    /**
     * authentication.id_number
     */
    public void setIdNumber(String idNumber) {
        this.idNumber = idNumber == null ? null : idNumber.trim();
    }

    /**
     * authentication.identity_status
     */
    public Byte getIdentityStatus() {
        return identityStatus;
    }

    /**
     * authentication.identity_status
     */
    public void setIdentityStatus(Byte identityStatus) {
        this.identityStatus = identityStatus;
    }

    /**
     * authentication.identify_type
     */
    public Byte getIdentifyType() {
        return identifyType;
    }

    /**
     * authentication.identify_type
     */
    public void setIdentifyType(Byte identifyType) {
        this.identifyType = identifyType;
    }

    /**
     * authentication.submit_time
     */
    public String getSubmitTime() {
        return submitTime;
    }

    /**
     * authentication.submit_time
     */
    public void setSubmitTime(String submitTime) {
        this.submitTime = submitTime == null ? null : submitTime.trim();
    }

    /**
     * authentication.approved_time
     */
    public String getApprovedTime() {
        return approvedTime;
    }

    /**
     * authentication.approved_time
     */
    public void setApprovedTime(String approvedTime) {
        this.approvedTime = approvedTime == null ? null : approvedTime.trim();
    }

    /**
     * authentication.approved_description
     */
    public String getApprovedDescription() {
        return approvedDescription;
    }

    /**
     * authentication.approved_description
     */
    public void setApprovedDescription(String approvedDescription) {
        this.approvedDescription = approvedDescription == null ? null : approvedDescription.trim();
    }

    /**
     * authentication.account_type
     */
    public Integer getAccountType() {
        return accountType;
    }

    /**
     * authentication.account_type
     */
    public void setAccountType(Integer accountType) {
        this.accountType = accountType;
    }
}