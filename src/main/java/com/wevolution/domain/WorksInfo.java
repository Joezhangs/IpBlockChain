package com.wevolution.domain;

import java.io.Serializable;

public class WorksInfo implements Serializable{
    /** 
	* @Fields serialVersionUID :  
	*/ 
	private static final long serialVersionUID = -2907608906269832601L;

	/**
     * 记录id
     */
    protected Integer recordId;

    /**
     * 作品id
     */
    protected String worksId;

    /**
     * 用户id
     */
    protected String userId;

    /**
     * 作品名称
     */
    protected String worksName;

    /**
     * 作品类别id
     */
    protected String worksType;

    /**
     * 作品类别说明
     */
    protected String worksExplain;

    /**
     * 作品创作性质
     */
    protected String worksNature;

    /**
     * 作品性质说明
     */
    protected String worksNatureExplain;

    /**
     * 创作完成时间
     */
    protected String accomplishTime;

    /**
     * 创作省份
     */
    protected String province;

    /**
     * 创作城市
     */
    protected String city;

    /**
     * 创作区域
     */
    protected String area;

    /**
     * 发表状态（1：已发表；2：未发表）
     */
    protected Byte publishStatus;

    /**
     * 首次发表日期
     */
    protected String firstPublishStatus;

    /**
     * 发表地点
     */
    protected String publishAddress;

    /**
     * 作者名称
     */
    protected String author;

    /**
     * 作者署名
     */
    protected String authorSing;

    /**
     * 权利获取方式
     */
    protected String accessRightsType;

    /**
     * 权利获取方式说明
     */
    protected String accessRightsTypeExplain;

    /**
     * 权利归属方式
     */
    protected String rightsAffiliationType;

    /**
     * 权利归属方式说明
     */
    protected String rightsAffiliationTypeExplain;

    /**
     * 权利拥有状况
     */
    protected String ownRightStatus;

    /**
     * 权利拥有状况说明
     */
    protected String ownRightStatusExplain;

    /**
     * 著作权姓名
     */
    protected String ownerName;

    /**
     * 著作权署名情况
     */
    protected String ownerSign;

    /**
     * 类别
     */
    protected String ownerType;

    /**
     * 证件类型
     */
    protected String ownerIdType;

    /**
     * 证件号码
     */
    protected String ownerIdNumber;

    /**
     * 国籍
     */
    protected String ownerNationality;

    /**
     * 省份
     */
    protected String ownerProvince;

    /**
     * 城市
     */
    protected String ownerCity;

    /**
     * 电子介质
     */
    protected Integer electronicMedium;

    /**
     * 创建时间
     */
    protected String createdTime;

    /**
     * 更新时间
     */
    protected String updatedTime;

    /**
     * 作品登记进度（1：作品信息；2：作者及著作权）
     */
    protected Integer progress;

    /**
     * works_info.record_id
     */
    public Integer getRecordId() {
        return recordId;
    }

    /**
     * works_info.record_id
     */
    public void setRecordId(Integer recordId) {
        this.recordId = recordId;
    }

    /**
     * works_info.works_id
     */
    public String getWorksId() {
        return worksId;
    }

    /**
     * works_info.works_id
     */
    public void setWorksId(String worksId) {
        this.worksId = worksId == null ? null : worksId.trim();
    }

    /**
     * works_info.user_id
     */
    public String getUserId() {
        return userId;
    }

    /**
     * works_info.user_id
     */
    public void setUserId(String userId) {
        this.userId = userId;
    }

    /**
     * works_info.works_name
     */
    public String getWorksName() {
        return worksName;
    }

    /**
     * works_info.works_name
     */
    public void setWorksName(String worksName) {
        this.worksName = worksName == null ? null : worksName.trim();
    }

    /**
     * works_info.works_type
     */
    public String getWorksType() {
        return worksType;
    }

    /**
     * works_info.works_type
     */
    public void setWorksType(String worksType) {
        this.worksType = worksType == null ? null : worksType.trim();
    }

    /**
     * works_info.works_explain
     */
    public String getWorksExplain() {
        return worksExplain;
    }

    /**
     * works_info.works_explain
     */
    public void setWorksExplain(String worksExplain) {
        this.worksExplain = worksExplain == null ? null : worksExplain.trim();
    }

    /**
     * works_info.works_nature
     */
    public String getWorksNature() {
        return worksNature;
    }

    /**
     * works_info.works_nature
     */
    public void setWorksNature(String worksNature) {
        this.worksNature = worksNature == null ? null : worksNature.trim();
    }

    /**
     * works_info.works_nature_explain
     */
    public String getWorksNatureExplain() {
        return worksNatureExplain;
    }

    /**
     * works_info.works_nature_explain
     */
    public void setWorksNatureExplain(String worksNatureExplain) {
        this.worksNatureExplain = worksNatureExplain == null ? null : worksNatureExplain.trim();
    }

    /**
     * works_info.accomplish_time
     */
    public String getAccomplishTime() {
        return accomplishTime;
    }

    /**
     * works_info.accomplish_time
     */
    public void setAccomplishTime(String accomplishTime) {
        this.accomplishTime = accomplishTime == null ? null : accomplishTime.trim();
    }

    /**
     * works_info.province
     */
    public String getProvince() {
        return province;
    }

    /**
     * works_info.province
     */
    public void setProvince(String province) {
        this.province = province == null ? null : province.trim();
    }

    /**
     * works_info.city
     */
    public String getCity() {
        return city;
    }

    /**
     * works_info.city
     */
    public void setCity(String city) {
        this.city = city == null ? null : city.trim();
    }

    /**
     * works_info.area
     */
    public String getArea() {
        return area;
    }

    /**
     * works_info.area
     */
    public void setArea(String area) {
        this.area = area == null ? null : area.trim();
    }

    /**
     * works_info.publish_status
     */
    public Byte getPublishStatus() {
        return publishStatus;
    }

    /**
     * works_info.publish_status
     */
    public void setPublishStatus(Byte publishStatus) {
        this.publishStatus = publishStatus;
    }

    /**
     * works_info.first_publish_status
     */
    public String getFirstPublishStatus() {
        return firstPublishStatus;
    }

    /**
     * works_info.first_publish_status
     */
    public void setFirstPublishStatus(String firstPublishStatus) {
        this.firstPublishStatus = firstPublishStatus == null ? null : firstPublishStatus.trim();
    }

    /**
     * works_info.publish_address
     */
    public String getPublishAddress() {
        return publishAddress;
    }

    /**
     * works_info.publish_address
     */
    public void setPublishAddress(String publishAddress) {
        this.publishAddress = publishAddress == null ? null : publishAddress.trim();
    }

    /**
     * works_info.author
     */
    public String getAuthor() {
        return author;
    }

    /**
     * works_info.author
     */
    public void setAuthor(String author) {
        this.author = author == null ? null : author.trim();
    }

    /**
     * works_info.author_sing
     */
    public String getAuthorSing() {
        return authorSing;
    }

    /**
     * works_info.author_sing
     */
    public void setAuthorSing(String authorSing) {
        this.authorSing = authorSing == null ? null : authorSing.trim();
    }

    /**
     * works_info.access_rights_type
     */
    public String getAccessRightsType() {
        return accessRightsType;
    }

    /**
     * works_info.access_rights_type
     */
    public void setAccessRightsType(String accessRightsType) {
        this.accessRightsType = accessRightsType == null ? null : accessRightsType.trim();
    }

    /**
     * works_info.access_rights_type_explain
     */
    public String getAccessRightsTypeExplain() {
        return accessRightsTypeExplain;
    }

    /**
     * works_info.access_rights_type_explain
     */
    public void setAccessRightsTypeExplain(String accessRightsTypeExplain) {
        this.accessRightsTypeExplain = accessRightsTypeExplain == null ? null : accessRightsTypeExplain.trim();
    }

    /**
     * works_info.rights_affiliation_type
     */
    public String getRightsAffiliationType() {
        return rightsAffiliationType;
    }

    /**
     * works_info.rights_affiliation_type
     */
    public void setRightsAffiliationType(String rightsAffiliationType) {
        this.rightsAffiliationType = rightsAffiliationType == null ? null : rightsAffiliationType.trim();
    }

    /**
     * works_info.rights_affiliation_type_explain
     */
    public String getRightsAffiliationTypeExplain() {
        return rightsAffiliationTypeExplain;
    }

    /**
     * works_info.rights_affiliation_type_explain
     */
    public void setRightsAffiliationTypeExplain(String rightsAffiliationTypeExplain) {
        this.rightsAffiliationTypeExplain = rightsAffiliationTypeExplain == null ? null : rightsAffiliationTypeExplain.trim();
    }

    /**
     * works_info.own_right_status
     */
    public String getOwnRightStatus() {
        return ownRightStatus;
    }

    /**
     * works_info.own_right_status
     */
    public void setOwnRightStatus(String ownRightStatus) {
        this.ownRightStatus = ownRightStatus == null ? null : ownRightStatus.trim();
    }

    /**
     * works_info.own_right_status_explain
     */
    public String getOwnRightStatusExplain() {
        return ownRightStatusExplain;
    }

    /**
     * works_info.own_right_status_explain
     */
    public void setOwnRightStatusExplain(String ownRightStatusExplain) {
        this.ownRightStatusExplain = ownRightStatusExplain == null ? null : ownRightStatusExplain.trim();
    }

    /**
     * works_info.owner_name
     */
    public String getOwnerName() {
        return ownerName;
    }

    /**
     * works_info.owner_name
     */
    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName == null ? null : ownerName.trim();
    }

    /**
     * works_info.owner_sign
     */
    public String getOwnerSign() {
        return ownerSign;
    }

    /**
     * works_info.owner_sign
     */
    public void setOwnerSign(String ownerSign) {
        this.ownerSign = ownerSign == null ? null : ownerSign.trim();
    }

    /**
     * works_info.owner_type
     */
    public String getOwnerType() {
        return ownerType;
    }

    /**
     * works_info.owner_type
     */
    public void setOwnerType(String ownerType) {
        this.ownerType = ownerType == null ? null : ownerType.trim();
    }

    /**
     * works_info.owner_id_type
     */
    public String getOwnerIdType() {
        return ownerIdType;
    }

    /**
     * works_info.owner_id_type
     */
    public void setOwnerIdType(String ownerIdType) {
        this.ownerIdType = ownerIdType == null ? null : ownerIdType.trim();
    }

    /**
     * works_info.owner_id_number
     */
    public String getOwnerIdNumber() {
        return ownerIdNumber;
    }

    /**
     * works_info.owner_id_number
     */
    public void setOwnerIdNumber(String ownerIdNumber) {
        this.ownerIdNumber = ownerIdNumber == null ? null : ownerIdNumber.trim();
    }

    /**
     * works_info.owner_nationality
     */
    public String getOwnerNationality() {
        return ownerNationality;
    }

    /**
     * works_info.owner_nationality
     */
    public void setOwnerNationality(String ownerNationality) {
        this.ownerNationality = ownerNationality == null ? null : ownerNationality.trim();
    }

    /**
     * works_info.owner_province
     */
    public String getOwnerProvince() {
        return ownerProvince;
    }

    /**
     * works_info.owner_province
     */
    public void setOwnerProvince(String ownerProvince) {
        this.ownerProvince = ownerProvince == null ? null : ownerProvince.trim();
    }

    /**
     * works_info.owner_city
     */
    public String getOwnerCity() {
        return ownerCity;
    }

    /**
     * works_info.owner_city
     */
    public void setOwnerCity(String ownerCity) {
        this.ownerCity = ownerCity == null ? null : ownerCity.trim();
    }

    /**
     * works_info.electronic_medium
     */
    public Integer getElectronicMedium() {
        return electronicMedium;
    }

    /**
     * works_info.electronic_medium
     */
    public void setElectronicMedium(Integer electronicMedium) {
        this.electronicMedium = electronicMedium;
    }

    /**
     * works_info.created_time
     */
    public String getCreatedTime() {
        return createdTime;
    }

    /**
     * works_info.created_time
     */
    public void setCreatedTime(String createdTime) {
        this.createdTime = createdTime == null ? null : createdTime.trim();
    }

    /**
     * works_info.updated_time
     */
    public String getUpdatedTime() {
        return updatedTime;
    }

    /**
     * works_info.updated_time
     */
    public void setUpdatedTime(String updatedTime) {
        this.updatedTime = updatedTime == null ? null : updatedTime.trim();
    }

    /**
     * works_info.progress
     */
    public Integer getProgress() {
        return progress;
    }

    /**
     * works_info.progress
     */
    public void setProgress(Integer progress) {
        this.progress = progress;
    }

}