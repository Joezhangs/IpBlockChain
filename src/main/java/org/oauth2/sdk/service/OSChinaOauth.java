package org.oauth2.sdk.service;

import java.util.logging.Logger;

import org.oauth2.sdk.entity.OauthUser;

import com.alibaba.fastjson.JSONObject;

/**
 * OSChina第三方登录
 */
public class OSChinaOauth extends BaseOauth {

	protected static final Logger LOGGER = Logger.getLogger("OSChinaOauth");

	public OSChinaOauth(String name) {
		super(name);
	}

	/**
	 * 构造回调方法
	 */
	public String createAuthorizeUrl(String state) {
		StringBuilder sb = new StringBuilder("http://www.oschina.net/action/oauth2/authorize?");
		sb.append("response_type=code");
		sb.append("&client_id=" + getClientId());
		sb.append("&redirect_uri=" + getRedirectUri());
		sb.append("&state=" + state);
		return sb.toString();
	}

	/**
	 * 获取AccessToken
	 */
	protected JSONObject getAccessToken(String code) {
		StringBuilder sb = new StringBuilder("http://www.oschina.net/action/openapi/token?");
		sb.append("grant_type=authorization_code");
		sb.append("&dataType=json");
		sb.append("&client_id=" + getClientId());
		sb.append("&client_secret=" + getClientSecret());
		sb.append("&redirect_uri=" + getRedirectUri());
		sb.append("&code=" + code);
		String httpString = httpGet(sb.toString());
		LOGGER.info("getAccessToken" + httpString);
		// {"access_token":"****", "refresh_token":"****", "uid":****, "token_type":"****", 
		// "expires_in":****}
		return JSONObject.parseObject(httpString);
	}

	/**
	 * 获取用户信息
	 */
	protected OauthUser getOauthUser(String code) {
		String accessToken = getAccessToken(code).getString("access_token");

		StringBuilder sb = new StringBuilder("http://www.oschina.net/action/openapi/user?");
		sb.append("access_token=" + accessToken);
		sb.append("&dataType=json");
		String httpString = httpGet(sb.toString());
		LOGGER.info("getOauthUser" + httpString);

		// {"gender":"****","name":"****","location":"****","id":****, "avatar":"****", 
		// "email":"****","url":"****"}
		JSONObject json = JSONObject.parseObject(httpString);
		OauthUser user = new OauthUser();
		user.setAvatar(json.getString("avatar"));
		user.setOpenId(json.getString("id"));
		user.setNickname(json.getString("name"));
		user.setGender(json.getString("gender"));
		user.setSource(getName());
		return user;
	}
}