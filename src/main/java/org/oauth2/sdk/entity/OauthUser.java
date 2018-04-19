package org.oauth2.sdk.entity;

import java.io.Serializable;

/**
 * 第三方登录实体类
 */
public class OauthUser implements Serializable {

	private static final long serialVersionUID = 1L;

	private String openId; // 唯一ID
	private String nickname; // 昵称
	private String avatar; // 头像
	private String gender; // 性别
	private String source; // 处理器的名称

	public String getOpenId() {
		return openId;
	}

	public void setOpenId(String openId) {
		this.openId = openId;
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public String getAvatar() {
		return avatar;
	}

	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public String toString() {
		return "OauthUser [openId=" + openId + ", nickname=" + nickname
				+ ", avatar=" + avatar + ", gender=" + gender + ", source="
				+ source + "]";
	}
}