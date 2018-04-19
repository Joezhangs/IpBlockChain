package org.oauth2.sdk;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;

import org.oauth2.sdk.entity.OauthUser;
import org.oauth2.sdk.service.BaseOauth;
import org.oauth2.sdk.service.GithubOauth;
import org.oauth2.sdk.service.OSChinaOauth;
import org.oauth2.sdk.service.QQOauth;
import org.oauth2.sdk.service.WechatOauth;
import org.oauth2.sdk.service.WeiboOauth;
import org.oauth2.sdk.utils.Const;
import org.oauth2.sdk.utils.StrUtil;

/**
 * 服务类
 */
public class OauthHelper {
	
	protected static final Logger LOGGER = Logger.getLogger("OauthHelper");
	
	private static final Map<String, BaseOauth> oauths = new HashMap<String, BaseOauth>();

	/**
	 * 创建
	 */
	public static BaseOauth createOauth(String name) {
		BaseOauth connector = oauths.get(name);
		if (connector == null) {
			if (Const.CD0001.equals(name)) {
				connector = new QQOauth(name);
			} else if (Const.CD0002.equals(name)) {
				connector = new WechatOauth(name);
			} else if (Const.CD0003.equals(name)) {
				connector = new WeiboOauth(name);
			} else if (Const.CD0004.equals(name)) {
				connector = new OSChinaOauth(name);
			} else if (Const.CD0005.equals(name)) {
				connector = new GithubOauth(name);
			} else {
				throw new IllegalArgumentException("无法识别的类型");
			}

			if (connector != null) {
				oauths.put(name, connector);
			}
		}
		return connector;
	}

	/**
	 * 处理器
	 * 
	 * @param name 第三方登录名称，例如qq
	 * @param request
	 * @return
	 */
	public static String processer(String name, HttpServletRequest request) {
		BaseOauth op = createOauth(name);
		// https://graph.qq.com/oauth2.0/authorize?response_type=code&client_id=101294356&
		// redirect_uri=http://localhost:8080/domicool_v2/pubOauth2/index/callback/qq.html&
		// state=3c1fe247400d47538fa7972c4120b7f1
		String state = StrUtil.get32UUID();
		request.getSession().setAttribute("oauth_state", state);
		return op.getAuthorizeUrl(state);
	}

	/**
	 * 处理回调方法
	 */
	public static OauthUser callback(String name, HttpServletRequest request) {
		String sessionState = (String) request.getSession().getAttribute("oauth_state");
		String state = request.getParameter("state");
		LOGGER.info("sessionState = " + sessionState);
		LOGGER.info("state = " + state);
		if (!sessionState.equals(state)) { 
			LOGGER.info("state 验证失败");
			return null;
		}

		String code = request.getParameter("code");
		if (null == code || "".equals(code.trim())) {
			LOGGER.info("code 为空");
			return null;
		}

		BaseOauth op = createOauth(name);
		OauthUser ouser = op.getUser(code);
		LOGGER.info("ouser = " + ouser);
		return ouser;
	}
}