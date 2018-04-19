package org.oauth2.sdk.service;

import java.util.logging.Logger;

import org.oauth2.sdk.entity.OauthUser;

import com.alibaba.fastjson.JSONObject;

/**
 * 微信第三方登录
 */
public class WechatOauth extends BaseOauth {
	
	protected static final Logger LOGGER = Logger.getLogger("WechatOauth");

	public WechatOauth(String name) {
		super(name);
	}

	/**
	 * 构造回调方法
	 */
	public String createAuthorizeUrl(String state) {
		StringBuilder sb = new StringBuilder("https://open.weixin.qq.com/connect/qrconnect?");
		sb.append("response_type=code");
		sb.append("&scope=snsapi_login");
		sb.append("&appid=" + getClientId());
		sb.append("&redirect_uri=" + getRedirectUri());
		sb.append("&state=" + state);
		sb.append("#wechat_redirect");
		return sb.toString();
	}

	/**
	 * 获取AccessToken
	 */
	protected JSONObject getAccessToken(String code) {
		StringBuilder sb = new StringBuilder("https://api.weixin.qq.com/sns/oauth2/access_token?");
		sb.append("grant_type=authorization_code");
		sb.append("&appid=" + getClientId());
		sb.append("&secret=" + getClientSecret());
		sb.append("&code=" + code);
		String httpString = httpGet(sb.toString());
		LOGGER.info("getAccessToken = " + httpString);
		// { "access_token":"****", "expires_in":****, "refresh_token":"****", 
		// "openid":"****", "scope":"****", "unionid": "****" }
		return JSONObject.parseObject(httpString);
	}

	/**
	 * 获取用户信息
	 */
	protected OauthUser getOauthUser(String code) {
		JSONObject tokenJson = getAccessToken(code);
		String accessToken = tokenJson.getString("access_token");
		String openId = tokenJson.getString("openid");
		
		StringBuilder sb = new StringBuilder("https://api.weixin.qq.com/sns/userinfo?");
		sb.append("access_token=" + accessToken);
		sb.append("&openid=" + openId);
		String httpString = httpGet(sb.toString());
		LOGGER.info("getOauthUser = " + httpString);
		
		// { "openid":"****", "nickname":"****", "sex":****,"province":"****", "city":"****",
		// "country":"****", "headimgurl":"****", "privilege":[ "PRIVILEGE1", "****" ], "unionid":"****" }
		OauthUser user = new OauthUser();
		JSONObject json = JSONObject.parseObject(httpString);
		user.setAvatar(json.getString("headimgurl"));
		user.setNickname(json.getString("nickname"));
		user.setOpenId(openId);
		int sex = json.getIntValue("sex");
		user.setGender(sex == 1 ? "male" : "female");
		user.setSource(getName());
		return user;
	}
}