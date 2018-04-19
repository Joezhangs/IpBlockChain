package com.wevolution.domain;

import java.io.Serializable;

public class Users implements Serializable {
    /** 
	* @Fields serialVersionUID :  
	*/ 
	private static final long serialVersionUID = -417623026946813306L;

	/**
     * 记录id
     */
    protected Integer recordId;

    /**
     * 用户id
     */
    protected String userId;

    /**
     * 用户名
     */
    protected String userName;

    /**
     * 用户昵称
     */
    protected String userNickname;

    /**
     * 用户头像
     */
    protected String userImageUrl;

    /**
     * 电话
     */
    protected String phone;

    /**
     * 认证状态(1：未认证，2：审核中；3：认证成功；4：认证失败）
     */
    protected Byte identifyStatus;

    /**
     * 认证方式（1：未认证；2：个人认证；3：企业认证）
     */
    protected Byte identifyType;

    /**
     * 渠道标识（0：平台；1：微信；2：微博）
     */
    protected Byte verifyChannel;

    /**
     * 校验用户（授权id、手机号、用户名）
     */
    protected String verifyUser;

    /**
     * 校验凭据（密码、access_token）
     */
    protected String verifyCredential;

    /**
     * 企业logo
     */
    protected String companyLogoUrl;

    /**
     * 用户地址（扩展字段）
     */
    protected String userAddress;

    /**
     * 更新时间
     */
    protected String updateTime;

    /**
     * 创建时间
     */
    protected String createTime;

    /**
     * 是否可用（1：可用；0：不可用）
     */
    protected Boolean isUsable;

    /**
     * users.record_id
     */
    public Integer getRecordId() {
        return recordId;
    }

    /**
     * users.record_id
     */
    public void setRecordId(Integer recordId) {
        this.recordId = recordId;
    }

    /**
     * users.user_id
     */
    public String getUserId() {
        return userId;
    }

    /**
     * users.user_id
     */
    public void setUserId(String userId) {
        this.userId = userId == null ? null : userId.trim();
    }

    /**
     * users.user_name
     */
    public String getUserName() {
        return userName;
    }

    /**
     * users.user_name
     */
    public void setUserName(String userName) {
        this.userName = userName == null ? null : userName.trim();
    }

    /**
     * users.user_nickname
     */
    public String getUserNickname() {
        return userNickname;
    }

    /**
     * users.user_nickname
     */
    public void setUserNickname(String userNickname) {
        this.userNickname = userNickname == null ? null : userNickname.trim();
    }

    /**
     * users.user_image_url
     */
    public String getUserImageUrl() {
        return userImageUrl;
    }

    /**
     * users.user_image_url
     */
    public void setUserImageUrl(String userImageUrl) {
        this.userImageUrl = userImageUrl == null ? null : userImageUrl.trim();
    }

    /**
     * users.phone
     */
    public String getPhone() {
        return phone;
    }

    /**
     * users.phone
     */
    public void setPhone(String phone) {
        this.phone = phone == null ? null : phone.trim();
    }

    /**
     * users.identify_status
     */
    public Byte getIdentifyStatus() {
        return identifyStatus;
    }

    /**
     * users.identify_status
     */
    public void setIdentifyStatus(Byte identifyStatus) {
        this.identifyStatus = identifyStatus;
    }

    /**
     * users.identify_type
     */
    public Byte getIdentifyType() {
        return identifyType;
    }

    /**
     * users.identify_type
     */
    public void setIdentifyType(Byte identifyType) {
        this.identifyType = identifyType;
    }

    /**
     * users.verify_channel
     */
    public Byte getVerifyChannel() {
        return verifyChannel;
    }

    /**
     * users.verify_channel
     */
    public void setVerifyChannel(Byte verifyChannel) {
        this.verifyChannel = verifyChannel;
    }

    /**
     * users.verify_user
     */
    public String getVerifyUser() {
        return verifyUser;
    }

    /**
     * users.verify_user
     */
    public void setVerifyUser(String verifyUser) {
        this.verifyUser = verifyUser == null ? null : verifyUser.trim();
    }

    /**
     * users.verify_credential
     */
    public String getVerifyCredential() {
        return verifyCredential;
    }

    /**
     * users.verify_credential
     */
    public void setVerifyCredential(String verifyCredential) {
        this.verifyCredential = verifyCredential == null ? null : verifyCredential.trim();
    }

    /**
     * users.company_logo_url
     */
    public String getCompanyLogoUrl() {
        return companyLogoUrl;
    }

    /**
     * users.company_logo_url
     */
    public void setCompanyLogoUrl(String companyLogoUrl) {
        this.companyLogoUrl = companyLogoUrl == null ? null : companyLogoUrl.trim();
    }

    /**
     * users.user_address
     */
    public String getUserAddress() {
        return userAddress;
    }

    /**
     * users.user_address
     */
    public void setUserAddress(String userAddress) {
        this.userAddress = userAddress == null ? null : userAddress.trim();
    }

    /**
     * users.update_time
     */
    public String getUpdateTime() {
        return updateTime;
    }

    /**
     * users.update_time
     */
    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime == null ? null : updateTime.trim();
    }

    /**
     * users.create_time
     */
    public String getCreateTime() {
        return createTime;
    }

    /**
     * users.create_time
     */
    public void setCreateTime(String createTime) {
        this.createTime = createTime == null ? null : createTime.trim();
    }

    /**
     * users.is_usable
     */
    public Boolean getIsUsable() {
        return isUsable;
    }

    /**
     * users.is_usable
     */
    public void setIsUsable(Boolean isUsable) {
        this.isUsable = isUsable;
    }
}