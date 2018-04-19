package com.wevolution.domain;

public class Dictionary {
    /**
     * 
     */
    protected Integer id;

    /**
     * 编码
     */
    protected String code;

    /**
     * 名称
     */
    protected String name;

    /**
     * 状态
     */
    protected Byte status;

    /**
     * 主类型id
     */
    protected Integer typeId;

    /**
     * 序号
     */
    protected Integer sort;

    /**
     * dictionary.id
     */
    public Integer getId() {
        return id;
    }

    /**
     * dictionary.id
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * dictionary.code
     */
    public String getCode() {
        return code;
    }

    /**
     * dictionary.code
     */
    public void setCode(String code) {
        this.code = code == null ? null : code.trim();
    }

    /**
     * dictionary.name
     */
    public String getName() {
        return name;
    }

    /**
     * dictionary.name
     */
    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    /**
     * dictionary.status
     */
    public Byte getStatus() {
        return status;
    }

    /**
     * dictionary.status
     */
    public void setStatus(Byte status) {
        this.status = status;
    }

    /**
     * dictionary.type_id
     */
    public Integer getTypeId() {
        return typeId;
    }

    /**
     * dictionary.type_id
     */
    public void setTypeId(Integer typeId) {
        this.typeId = typeId;
    }

    /**
     * dictionary.sort
     */
    public Integer getSort() {
        return sort;
    }

    /**
     * dictionary.sort
     */
    public void setSort(Integer sort) {
        this.sort = sort;
    }
}