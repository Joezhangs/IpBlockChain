package com.wevolution.domain;

public class Region {
    /**
     * 
     */
    protected Integer id;

    /**
     * 名称（省、市、县）
     */
    protected String name;

    /**
     * parent_id
     */
    protected Integer parentId;

    /**
     * 名称（不含省、市、县）
     */
    protected String shortName;

    /**
     * 级别
     */
    protected Integer levelType;

    /**
     * 区号
     */
    protected String cityCode;

    /**
     * 邮政编码
     */
    protected Integer zipCode;

    /**
     * 合并名称
     */
    protected String mergerName;

    /**
     * 经度
     */
    protected Float longitude;

    /**
     * 纬度
     */
    protected Float latitude;

    /**
     * 拼音
     */
    protected String pinyin;

    /**
     * region.id
     */
    public Integer getId() {
        return id;
    }

    /**
     * region.id
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * region.name
     */
    public String getName() {
        return name;
    }

    /**
     * region.name
     */
    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    /**
     * region.parent_id
     */
    public Integer getParentId() {
        return parentId;
    }

    /**
     * region.parent_id
     */
    public void setParentId(Integer parentId) {
        this.parentId = parentId;
    }

    /**
     * region.short_name
     */
    public String getShortName() {
        return shortName;
    }

    /**
     * region.short_name
     */
    public void setShortName(String shortName) {
        this.shortName = shortName == null ? null : shortName.trim();
    }

    /**
     * region.level_type
     */
    public Integer getLevelType() {
        return levelType;
    }

    /**
     * region.level_type
     */
    public void setLevelType(Integer levelType) {
        this.levelType = levelType;
    }

    /**
     * region.city_code
     */
    public String getCityCode() {
        return cityCode;
    }

    /**
     * region.city_code
     */
    public void setCityCode(String cityCode) {
        this.cityCode = cityCode == null ? null : cityCode.trim();
    }

    /**
     * region.zip_code
     */
    public Integer getZipCode() {
        return zipCode;
    }

    /**
     * region.zip_code
     */
    public void setZipCode(Integer zipCode) {
        this.zipCode = zipCode;
    }

    /**
     * region.merger_name
     */
    public String getMergerName() {
        return mergerName;
    }

    /**
     * region.merger_name
     */
    public void setMergerName(String mergerName) {
        this.mergerName = mergerName == null ? null : mergerName.trim();
    }

    /**
     * region.longitude
     */
    public Float getLongitude() {
        return longitude;
    }

    /**
     * region.longitude
     */
    public void setLongitude(Float longitude) {
        this.longitude = longitude;
    }

    /**
     * region.latitude
     */
    public Float getLatitude() {
        return latitude;
    }

    /**
     * region.latitude
     */
    public void setLatitude(Float latitude) {
        this.latitude = latitude;
    }

    /**
     * region.pinyin
     */
    public String getPinyin() {
        return pinyin;
    }

    /**
     * region.pinyin
     */
    public void setPinyin(String pinyin) {
        this.pinyin = pinyin == null ? null : pinyin.trim();
    }
}