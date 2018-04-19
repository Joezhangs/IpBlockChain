package com.wevolution.controller;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.oauth2.sdk.OauthHelper;
import org.oauth2.sdk.entity.OauthUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.code.kaptcha.impl.DefaultKaptcha;
import com.wevolution.common.utils.DateUtil;
import com.wevolution.common.utils.FileUtil;
import com.wevolution.common.utils.ResponseUtil;
import com.wevolution.common.utils.SmsUtil;
import com.wevolution.common.utils.StringUtil;
import com.wevolution.domain.MessageRecorder;
import com.wevolution.domain.Users;
import com.wevolution.service.MessageRecorderService;
import com.wevolution.service.UsersService;

import aws.s3.AWSS3Client;

@Controller
@RequestMapping("/auth")
public class AuthorityController {
	private static Logger logger = LoggerFactory.getLogger(AuthorityController.class);
	@Resource
	private UsersService userservice;
	@Resource
	private MessageRecorderService messageRecorderService;

	@Value("${web.upload-path}")
	private String path;
	
	@Autowired
	private DefaultKaptcha defaultKaptcha;

	@RequestMapping("/loginPage")
	public String loginPage(HttpServletRequest request){
		Users user = (Users) request.getSession().getAttribute("user");
		if(user!=null)//已登录用户跳转到首页
			return "redirect:/";
		return "login";
	}

	@RequestMapping("/loginPage_en")
	public String loginPage_en(HttpServletRequest request){
		Users user = (Users) request.getSession().getAttribute("user");
		if(user!=null)//已登录用户跳转到首页
			return "redirect:/";
		return "login_en";
	}
	/**
	 * 
	* @Title: callback 
	* @Description: 第三方登录回调 
	* @param request
	* @param params
	* @return
	* @throws Exception 
	*
	 */
	@RequestMapping(value = "/callback/{params}")
	public String callback(HttpServletRequest request,@PathVariable("params") String params) throws Exception {
		OauthUser ouser = OauthHelper.callback(params, request);
		if (ouser == null) {
			return "login";
		}
		Byte channel = null;
		if("wechat".equals(params)){
			channel=1;
		}else if("weibo".equals(params)){
			channel=2;
		}
		Users user = userservice.getUserByVerify(ouser.getOpenId(), channel);
		Users retUser = new Users();
		if(user!=null){
			retUser = userservice.getUserById(user.getUserId());
		}else{
			user = new Users();
			user.setUserName(ouser.getNickname());
			user.setVerifyUser(ouser.getOpenId());
			user.setVerifyChannel(channel);
			/*user.setIdentifyStatus((byte) 1);
			user.setIdentifyType((byte) 1);*/
			//user.setUserImageUrl(ouser.getAvatar());
			retUser = userservice.signUpByThird(user);
			//保存头像，防止用户修改第三方头像而丢失头像
//			String avatarPath = path+retUser.getUserId()+"/avatar/";//"/用户id/avatar
			String avatarPath = "userimage/"+retUser.getUserId()+"/avatar/";//"/用户id/avatar
			String fileName =String.valueOf(System.currentTimeMillis())+".png";
			//FileUtil.downLoadFromUrl(ouser.getAvatar(), fileName, avatarPath);
			InputStream is = FileUtil.getStreamFromUrl(ouser.getAvatar());
			String url = AWSS3Client.uploadToS3(is, avatarPath+fileName);
			retUser.setUserImageUrl(url);
			userservice.updateUserById(retUser);
		}
		request.getSession().setAttribute("user", retUser);
		return "redirect:/";
	}
	/**
	 * 
	* @Title: login 
	* @Description: 登录
	* @param request
	* @param nameOrphone 用户名或手机号
	* @param loginType 登录方式（0：密码登录；01：动态密码登录；1：微信登录；2：微博登录）
	* @param credential
	* @return String 
	* @throws
	 */
	@RequestMapping(value="/login",method=RequestMethod.POST)
	@ResponseBody
	public String login(HttpServletRequest request, @RequestParam(value ="nameOrPhone",required= false)String nameOrPhone,@RequestParam("loginType")String loginType,@RequestParam(value = "credential",required= false)String credential){
		HttpSession session = request.getSession();
		Users users = null;
		switch (loginType) {
		case "01":// 动态密码登录
			users = userservice.findByUserParam(nameOrPhone, null);
			if (users != null) {
				String smsCode = (String) session.getAttribute("smsCode");
				if (smsCode == null) {
					return ResponseUtil.getResponseJson(204, "验证码超时", null);
				} else {
					String phone = (String) session.getAttribute("phone");
					if (phone != null && phone.equals(nameOrPhone)) {
						if (!smsCode.equals(credential)) {
							return ResponseUtil.getResponseJson(204, "验证码错误", null);
						} else {
							Integer msgId = (Integer) session.getAttribute(nameOrPhone);
							MessageRecorder recorder = new MessageRecorder();
							recorder.setRecordId(msgId);
							recorder.setSmsStatus(3);
							messageRecorderService.updateMsg(recorder);
							session.removeAttribute(nameOrPhone);
							
							session.removeAttribute("smsCode");
							session.removeAttribute("phone");
							session.setAttribute("user", users);
							return ResponseUtil.getResponseJson(0, "登录成功", users);
						}
					} else {
						return ResponseUtil.getResponseJson(204, "登录手机号与接收短信手机号不匹配", null);
					}
				}
			} else {
				return ResponseUtil.getResponseJson(204, "该手机号尚未注册", null);
			}
		case "1":// 微信登录 
			//return ResponseUtil.getResponseJson(0, "wx", weiXinOAuthApi.authorize());
			String url = OauthHelper.processer("wechat", request);
			return ResponseUtil.getResponseJson(0, "wx", url);
		case "2":// 微博登录 TODO
			return ResponseUtil.getResponseJson(0, "wb", OauthHelper.processer("weibo", request));
		default:// 密码登录
			if (StringUtil.isEmpty(credential)) {
				return ResponseUtil.getResponseJson(204, "登录账号或密码错误", null);
			} else {
				users = userservice.selectByNameOrPhoneSys(nameOrPhone);
				if (users != null) {
					users = userservice.findByUserParam(nameOrPhone, credential);
					if (users != null) {
						session.setAttribute("user", users);
						return ResponseUtil.getResponseJson(0, "登录成功", users);
					} else {
						return ResponseUtil.getResponseJson(204, "密码错误", null);
					}
				} else {
					return ResponseUtil.getResponseJson(204, "登录账号错误", null);
				}
				
			}
		}
	}
	@RequestMapping("/kaptcha")
	public String kaptcha(){
		//TODO
		return "kaptcha_test";
	}
	/**
	 * 
	* @Title: defaultKaptcha 
	* @Description: 图形验证码生成
	* @param httpServletRequest
	* @param httpServletResponse
	* @throws Exception 
	* void 
	* @throws
	 */
	@RequestMapping("/defaultKaptcha")
	public void defaultKaptcha(HttpServletRequest request,HttpServletResponse response) throws Exception{
		byte[] captchaChallengeAsJpeg = null;
		ByteArrayOutputStream jpegOutputStream = new ByteArrayOutputStream();
		try {
			// 生产验证码字符串并保存到session中
			String createText = defaultKaptcha.createText();
			request.getSession().setAttribute("vrifyCode", createText);
			logger.info(createText);
			// 使用生产的验证码字符串返回一个BufferedImage对象并转为byte写入到byte数组中
			BufferedImage challenge = defaultKaptcha.createImage(createText);
			ImageIO.write(challenge, "jpg", jpegOutputStream);
		} catch (IllegalArgumentException e) {
			response.sendError(HttpServletResponse.SC_NOT_FOUND);
			return;
		}
		// 定义response输出类型为image/jpeg类型，使用response输出流输出图片的byte数组
		captchaChallengeAsJpeg = jpegOutputStream.toByteArray();
		response.setHeader("Cache-Control", "no-store");
		response.setHeader("Pragma", "no-cache");
		response.setDateHeader("Expires", 0);
		response.setContentType("image/jpeg");
		ServletOutputStream responseOutputStream = response.getOutputStream();
		responseOutputStream.write(captchaChallengeAsJpeg);
		responseOutputStream.flush();
		responseOutputStream.close();
	}
	/**
	 * 
	* @Title: imgvrifyControllerDefaultKaptcha 
	* @Description: 图形验证码校验
	* @param request
	* @param vrifyCode 验证码
	* @return 
	* String 
	* @throws
	 */
	@RequestMapping(value="/imgvrify",method=RequestMethod.POST)
	@ResponseBody
	public String imgvrify(HttpServletRequest request,@RequestParam("vrifyCode")String vrifyCode){
		HttpSession session = request.getSession();
		String captchaId = (String) session.getAttribute("vrifyCode");
		logger.info("Session  vrifyCode " + captchaId + " form vrifyCode " + vrifyCode);
		//session.removeAttribute("vrifyCode");
		if (captchaId != null && captchaId.equals(vrifyCode)) 
			return ResponseUtil.getResponseJson(0, "验证码正确", null);
		 else
			return ResponseUtil.getResponseJson(204, "验证码错误", null);
	}
	/**
	 * 
	* @Title: sengSmsCode 
	* @Description: 发送手机验证码
	* @param request
	* @param phone 手机号
	* @param type 0:登录；1：注册
	* @return 
	* ResponseEntity<?> 
	* @throws
	 */
	@RequestMapping(value="/sendSmsCode",method=RequestMethod.POST)
	@ResponseBody
	public String sendSmsCode(HttpServletRequest request,@RequestParam("phone")String phone,@RequestParam(value="type",required=false)String type){
		String smsCode = StringUtil.createRandomVcode();
		HttpSession session = request.getSession(true);
		MessageRecorder recorder = new MessageRecorder();
		if("0".equals(type)){
			recorder.setSmsUsage("登录");
			Users users = userservice.findByUserParam(phone, null);
			if(users==null){
				return ResponseUtil.getResponseJson(204, "该手机号尚未注册", null);
			}
		}
		if("1".equals(type)){
			recorder.setSmsUsage("注册");
			Users users = userservice.findByUserParam(phone, null);
			if(users!=null){
				return ResponseUtil.getResponseJson(204, "该手机号已被注册", null);
			}
		}
		try {
			if(StringUtil.checkMobile(phone)){
				recorder.setSmsCode(smsCode);
				recorder.setSendtime(DateUtil.getCurrentDateString(DateUtil.DATETIMEPATTERN24H));
				recorder.setPhone(phone);
				//TODO 开发阶段暂停短信发送 
				String retCode = SmsUtil.sendPT(new StringBuffer(phone) , new StringBuffer("【我们信息科技有限公司】您的验证码是："+smsCode+"。请不要把验证码泄漏给其他人。"));
				String[] status = retCode.split(",");
				if("0".equals(status[0])){
					recorder.setSmsStatus(1);
					MessageRecorder insertMsg = messageRecorderService.insertMsg(recorder);
					session.setAttribute("smsCode", smsCode);
					session.setAttribute("phone", phone);
					session.setAttribute(phone, insertMsg.getRecordId());
					session.setMaxInactiveInterval(10*60);//十分钟有效
					logger.info("phone:"+phone+",smsCode"+smsCode+"，发送成功");
					return ResponseUtil.getResponseJson(0, "验证码发送成功", null);
				}else{
					recorder.setSmsStatus(2);
					messageRecorderService.insertMsg(recorder);
					logger.info("phone:"+phone+",smsCode:"+smsCode+"，发送失败");
					return ResponseUtil.getResponseJson(204, "验证码发送失败", null);
				}
			}else{
				recorder = null;
				return ResponseUtil.getResponseJson(204, "手机号有误", null);
			}
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseUtil.getResponseJson(204, "验证码发送失败", null);
		}
	}
	/**
	 * 
	* @Title: smsVrify 
	* @Description: 手机验证码校验
	* @param request
	* @param smsCode
	* @return String 
	* @throws
	 */
	@RequestMapping(value="/smsVrify",method=RequestMethod.POST)
	@ResponseBody
	public String smsVrify(HttpServletRequest request,@RequestParam("smsCode")String smsCode){
		HttpSession session = request.getSession();
		String sSmsCode = (String) session.getAttribute("smsCode");
		if(sSmsCode==null){
			return ResponseUtil.getResponseJson(204, "验证码超时", null);
		}else{
			//session.removeAttribute("smsCode");
			if(sSmsCode.equals(smsCode)){
				return ResponseUtil.getResponseJson(0, "验证成功", null);
			}else{
				return ResponseUtil.getResponseJson(204, "验证码错误", null);
			}
		}
	}
	/**
	 * 
	* @Title: loginOut 
	* @Description: 退出
	* @param request
	* @return  String 
	* @throws
	 */
	@RequestMapping("/longOut")
	public String loginOut(HttpServletRequest request){
		request.getSession().invalidate();
		return "redirect:loginPage.html";
	}

	@RequestMapping("/longOut_en")
	public String loginOut_en(HttpServletRequest request){
		request.getSession().invalidate();
		return "redirect:loginPage_en.html";
	}
}
