package com.wevolution.domain;

import java.io.Serializable;

public class IdPhoto implements Serializable{

	/** 
	* @Fields serialVersionUID :  
	*/ 
	private static final long serialVersionUID = -7948828903599571767L;

	/**
     * 
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
     * 证件照
     */
    protected String idImageUrl;

    /**
     * 1：身份证正面；2：身份证反面；3：手持照；4：企业
     */
    protected Byte photoType;

    /**
     * 认证状态（1：未认证；2：认证中；3：认证成功；4：认证失败）
     */
    protected Byte identityStatus;

    /**
     * 上传时间
     */
    protected String createTime;

    /**
     * 更新时间
     */
    protected String updateTime;

    /**
     * id_photo.record_id
     */
    public Integer getRecordId() {
        return recordId;
    }

    /**
     * id_photo.record_id
     */
    public void setRecordId(Integer recordId) {
        this.recordId = recordId;
    }

    /**
     * id_photo.user_id
     */
    public String getUserId() {
        return userId;
    }

    /**
     * id_photo.user_id
     */
    public void setUserId(String userId) {
        this.userId = userId == null ? null : userId.trim();
    }

    /**
     * id_photo.auth_id
     */
    public String getAuthId() {
        return authId;
    }

    /**
     * id_photo.auth_id
     */
    public void setAuthId(String authId) {
        this.authId = authId == null ? null : authId.trim();
    }

    /**
     * id_photo.id_image_url
     */
    public String getIdImageUrl() {
        return idImageUrl;
    }

    /**
     * id_photo.id_image_url
     */
    public void setIdImageUrl(String idImageUrl) {
        this.idImageUrl = idImageUrl == null ? null : idImageUrl.trim();
    }

    /**
     * id_photo.photo_type
     */
    public Byte getPhotoType() {
        return photoType;
    }

    /**
     * id_photo.photo_type
     */
    public void setPhotoType(Byte photoType) {
        this.photoType = photoType;
    }

    /**
     * id_photo.identity_status
     */
    public Byte getIdentityStatus() {
        return identityStatus;
    }

    /**
     * id_photo.identity_status
     */
    public void setIdentityStatus(Byte identityStatus) {
        this.identityStatus = identityStatus;
    }

    /**
     * id_photo.create_time
     */
    public String getCreateTime() {
        return createTime;
    }

    /**
     * id_photo.create_time
     */
    public void setCreateTime(String createTime) {
        this.createTime = createTime == null ? null : createTime.trim();
    }

    /**
     * id_photo.update_time
     */
    public String getUpdateTime() {
        return updateTime;
    }

    /**
     * id_photo.update_time
     */
    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime == null ? null : updateTime.trim();
    }
}