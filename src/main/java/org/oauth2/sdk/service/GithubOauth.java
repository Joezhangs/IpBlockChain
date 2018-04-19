package org.oauth2.sdk.service;

import java.util.logging.Logger;

import org.oauth2.sdk.entity.OauthUser;
import org.oauth2.sdk.utils.StrUtil;

import com.alibaba.fastjson.JSONObject;

/**
 * Github第三方登录
 */
public class GithubOauth extends BaseOauth {
	
	protected static final Logger LOGGER = Logger.getLogger("GithubOauth");

	public GithubOauth(String name) {
		super(name);
	}

	/**
	 * 构造回调方法
	 */
	public String createAuthorizeUrl(String state) {
		StringBuilder sb = new StringBuilder("https://github.com/login/oauth/authorize?");
		sb.append("scope=user");
		sb.append("&client_id=" + getClientId());
		sb.append("&redirect_uri=" + getRedirectUri());
		sb.append("&state=" + state);
		return sb.toString();
	}
	
	/**
	 * 获取AccessToken
	 */
	protected JSONObject getAccessToken(String code) {
		StringBuilder sb = new StringBuilder("https://github.com/login/oauth/access_token?");
		sb.append("client_id=" + getClientId());
		sb.append("&client_secret=" + getClientSecret());
		sb.append("&code=" + code);
		String httpString = httpGet(sb.toString());
		LOGGER.info("httpString = " + httpString);
		// 返回不是JSON数据
		// access_token=****&scope=user&token_type=bearer
		return StrUtil.paramToJson(httpString);
	}

	/**
	 * 获取用户信息
	 */
	protected OauthUser getOauthUser(String code) {
		String accessToken = getAccessToken(code).getString("access_token");
		String url = "https://api.github.com/user?access_token=" + accessToken;
		String httpString = httpGet(url);
		LOGGER.info("getOauthUser = " + httpString);
		JSONObject json = JSONObject.parseObject(httpString);
		// {"login":"****","id":****,"avatar_url":"****","gravatar_id":"","url":"****",
		// "html_url":"****","followers_url":"****","following_url":"****","gists_url":"****",
		// "starred_url":"****","subscriptions_url":"****","organizations_url":"****",
		// "repos_url":"****","events_url":"****","received_events_url":"****","type":"User",
		// "site_admin":false,"name":null,"company":null,"blog":null,"location":null,"email":null,
		// "hireable":null,"bio":null,"public_repos":0,"public_gists":0,"followers":0,"following":0,
		// "created_at":"2016-08-15T08:57:55Z","updated_at":"2016-08-15T08:57:55Z",
		// "private_gists":0,"total_private_repos":0,"owned_private_repos":0,"disk_usage":0,
		// "collaborators":0,"plan":{"name":"free","space":976562499,"collaborators":0,"private_repos":0}}
		OauthUser user = new OauthUser();
		user.setAvatar(json.getString("avatar_url"));
		user.setOpenId(json.getString("id"));
		user.setNickname(json.getString("login"));
		// user.setGender(json.getString("gender"));
		user.setSource(getName());
		return user;
	}
}