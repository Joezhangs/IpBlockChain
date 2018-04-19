package org.oauth2.sdk.utils.config;

/**
 * 配置实体类
 */
public class Xml {

	private String name;
	private String clientId;
	private String clientSecret;
	private String redirectUri;
	private String intro;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getClientId() {
		return clientId;
	}

	public void setClientId(String clientId) {
		this.clientId = clientId;
	}

	public String getClientSecret() {
		return clientSecret;
	}

	public void setClientSecret(String clientSecret) {
		this.clientSecret = clientSecret;
	}

	public String getRedirectUri() {
		return redirectUri;
	}

	public void setRedirectUri(String redirectUri) {
		this.redirectUri = redirectUri;
	}

	public String getIntro() {
		return intro;
	}

	public void setIntro(String intro) {
		this.intro = intro;
	}

	public String toString() {
		return "Xml [name=" + name + ", clientId=" + clientId
				+ ", clientSecret=" + clientSecret + ", redirectUri="
				+ redirectUri + ", intro=" + intro + "]";
	}
}