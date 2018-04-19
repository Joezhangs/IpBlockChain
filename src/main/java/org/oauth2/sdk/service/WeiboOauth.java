package org.oauth2.sdk.service;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import org.oauth2.sdk.entity.OauthUser;

import com.alibaba.fastjson.JSONObject;

/**
 * 新浪微博第三方登录
 */
public class WeiboOauth extends BaseOauth {
	
	protected static final Logger LOGGER = Logger.getLogger("WeiboOauth");

	static Map<String, String> genders = new HashMap<String, String>();
	static {
		genders.put("m", "male");
		genders.put("f", "female");
		genders.put("n", "unkown");
	}

	public WeiboOauth(String name) {
		super(name);
	}

	/**
	 * 构造回调方法
	 */
	public String createAuthorizeUrl(String state) {
		StringBuilder sb = new StringBuilder("https://api.weibo.com/oauth2/authorize?");
		sb.append("scope=email");
		sb.append("&client_id=" + getClientId());
		sb.append("&redirect_uri=" + getRedirectUri());
		sb.append("&state=" + state);
		return sb.toString();
	}
	
	/**
	 * 获取AccessToken
	 */
	protected JSONObject getAccessToken(String code) {
		String url = "https://api.weibo.com/oauth2/access_token";
		StringBuilder sb = new StringBuilder();
		sb.append("grant_type=authorization_code");
		sb.append("&client_id=" + getClientId());
		sb.append("&client_secret=" + getClientSecret());
		sb.append("&redirect_uri=" + getRedirectUri());
		sb.append("&code=" + code);
		String httpString = httpPost(url, sb.toString());
		LOGGER.info("getOauthUser = " + httpString);
		return JSONObject.parseObject(httpString);
	}

	/**
	 * 获取用户信息
	 */
	protected OauthUser getOauthUser(String code) {
		JSONObject json = getAccessToken(code);
		String accessToken = json.getString("access_token");
		String uid = json.getString("uid");
		
		StringBuilder sb = new StringBuilder("https://api.weibo.com/2/users/show.json?");
		sb.append("access_token=" + accessToken);
		sb.append("&uid=" + uid);
		String httpString = httpGet(sb.toString());
		LOGGER.info("getOauthUser = " + httpString);
		json = JSONObject.parseObject(httpString);

		OauthUser user = new OauthUser();
		user.setAvatar(json.getString("avatar_large"));
		user.setNickname(json.getString("screen_name"));
		user.setOpenId(json.getString("id"));
		user.setGender(genders.get(json.getString("gender")));
		user.setSource(getName());
		return user;
	}
}