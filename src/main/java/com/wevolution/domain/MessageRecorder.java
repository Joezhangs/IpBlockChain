package com.wevolution.domain;

public class MessageRecorder {
    /**
     * 记录id
     */
    protected Integer recordId;

    /**
     * 电话
     */
    protected String phone;

    /**
     * 验证码
     */
    protected String smsCode;

    /**
     * 用途
     */
    protected String smsUsage;

    /**
     * 短信状态（1：发送成功；2：发送失败；3：验证码已使用）
     */
    protected Integer smsStatus;

    /**
     * 发送时间
     */
    protected String sendtime;

    /**
     * 操作ip
     */
    protected String ipAddress;

    /**
     * message_recorder.record_id
     */
    public Integer getRecordId() {
        return recordId;
    }

    /**
     * message_recorder.record_id
     */
    public void setRecordId(Integer recordId) {
        this.recordId = recordId;
    }

    /**
     * message_recorder.phone
     */
    public String getPhone() {
        return phone;
    }

    /**
     * message_recorder.phone
     */
    public void setPhone(String phone) {
        this.phone = phone == null ? null : phone.trim();
    }

    /**
     * message_recorder.sms_code
     */
    public String getSmsCode() {
        return smsCode;
    }

    /**
     * message_recorder.sms_code
     */
    public void setSmsCode(String smsCode) {
        this.smsCode = smsCode == null ? null : smsCode.trim();
    }

    /**
     * message_recorder.sms_usage
     */
    public String getSmsUsage() {
        return smsUsage;
    }

    /**
     * message_recorder.sms_usage
     */
    public void setSmsUsage(String smsUsage) {
        this.smsUsage = smsUsage == null ? null : smsUsage.trim();
    }

    /**
     * message_recorder.sms_status
     */
    public Integer getSmsStatus() {
        return smsStatus;
    }

    /**
     * message_recorder.sms_status
     */
    public void setSmsStatus(Integer smsStatus) {
        this.smsStatus = smsStatus;
    }

    /**
     * message_recorder.sendtime
     */
    public String getSendtime() {
        return sendtime;
    }

    /**
     * message_recorder.sendtime
     */
    public void setSendtime(String sendtime) {
        this.sendtime = sendtime == null ? null : sendtime.trim();
    }

    /**
     * message_recorder.ip_address
     */
    public String getIpAddress() {
        return ipAddress;
    }

    /**
     * message_recorder.ip_address
     */
    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress == null ? null : ipAddress.trim();
    }
}