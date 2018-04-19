package org.oauth2.sdk.service;

import org.oauth2.sdk.entity.OauthUser;
import org.oauth2.sdk.utils.AjaxUtil;
import org.oauth2.sdk.utils.StrUtil;
import org.oauth2.sdk.utils.config.ReaderXml;

import com.alibaba.fastjson.JSONObject;

/**
 * 第三方登录基类 
 * 第一步，构建跳转的URL，跳转后用户登录成功，返回到回调地址，并带上code 
 * 第二步，通过code，获取access_token 
 * 第三步，通过 access_token 获取用户的open_id 
 * 第四步，通过 open_id 获取用户信息
 */
public abstract class BaseOauth {

	private String clientId;
	private String clientSecret;
	private String name;
	private String redirectUri;

	public BaseOauth(String name) {
		this.clientId = ReaderXml.getClientId(name);
		this.clientSecret = ReaderXml.getClientSecret(name);
		this.name = name;
		this.redirectUri = StrUtil.URLEncoder(ReaderXml.getRedirectUri(name));
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

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getRedirectUri() {
		return redirectUri;
	}

	public String getAuthorizeUrl(String state) {
		return createAuthorizeUrl(state);
	}

	protected String httpGet(String url) {
		return AjaxUtil.get(url);
	}
	
	protected String httpPost(String url, String params) {
		return AjaxUtil.post(url, params);
	}

	public abstract String createAuthorizeUrl(String state);
	
	protected abstract JSONObject getAccessToken(String code);

	protected abstract OauthUser getOauthUser(String code);

	public OauthUser getUser(String code) {
		return getOauthUser(code);
	}
}