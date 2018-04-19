package org.oauth2.sdk.service;

import java.util.logging.Logger;

import org.oauth2.sdk.entity.OauthUser;
import org.oauth2.sdk.utils.StrUtil;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

/**
 * QQ第三方登录
 */
public class QQOauth extends BaseOauth {

	protected static final Logger LOGGER = Logger.getLogger("QQOauth");

	public QQOauth(String name) {
		super(name);
	}

	/**
	 * 构造回调方法
	 */
	public String createAuthorizeUrl(String state) {
		StringBuilder sb = new StringBuilder("https://graph.qq.com/oauth2.0/authorize?");
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
		StringBuilder sb = new StringBuilder("https://graph.qq.com/oauth2.0/token?");
		sb.append("grant_type=authorization_code");
		sb.append("&code=" + code);
		sb.append("&client_id=" + getClientId());
		sb.append("&client_secret=" + getClientSecret());
		sb.append("&redirect_uri=" + getRedirectUri());

		String httpString = httpGet(sb.toString());
		LOGGER.info("getAccessToken = " + httpString);
		// 返回的不是JSON格式数据
		// access_token=C0F236D739A7E70147BFA7AD24CC4253&expires_in=7776000&
		// refresh_token=A77DD75ECF242D9057FAB34F4F938F1A
		return StrUtil.paramToJson(httpString);
	}

	/**
	 * 获取openId
	 */
	protected JSONObject getOpenId(String accessToken, String code) {
		StringBuilder sb = new StringBuilder("https://graph.qq.com/oauth2.0/me?");
		sb.append("access_token=" + accessToken);
		String httpString = httpGet(sb.toString());
		LOGGER.info("getOpenId = " + httpString);
		// 返回的不是JSON格式数据
		// callback({"client_id":"****","openid":"****"});
		return StrUtil.funToJson(httpString);
	}

	/**
	 * 获取用户信息
	 */
	protected OauthUser getOauthUser(String code) {
		String accessToken = getAccessToken(code).getString("access_token");
		String openId = getOpenId(accessToken, code).getString("openid");

		StringBuilder sb = new StringBuilder("https://graph.qq.com/user/get_user_info?");
		sb.append("access_token=" + accessToken);
		sb.append("&oauth_consumer_key=" + getClientId());
		sb.append("&openid=" + openId);
		sb.append("&format=format");

		String httpString = httpGet(sb.toString());
		LOGGER.info("getOauthUser = " + httpString);
		// { "ret": ****, "msg": "", "is_lost":****, "nickname": "****",
		// "gender": "****", "province": "", "city": "", "year": "****",
		// "figureurl": "****", "figureurl_1": "****", "figureurl_2": "****",
		// "figureurl_qq_1": "****", "figureurl_qq_2": "****", "is_yellow_vip": "****", 
		// "vip": "****", "yellow_vip_level": "****", "level": "****", "is_yellow_year_vip": "****" }
		JSONObject json = JSON.parseObject(httpString);
		OauthUser user = new OauthUser();
		user.setAvatar(json.getString("figureurl_2"));
		user.setNickname(json.getString("nickname"));
		user.setOpenId(openId);
		user.setGender(json.getString("gender"));
		user.setSource(getName());
		return user;
	}
}