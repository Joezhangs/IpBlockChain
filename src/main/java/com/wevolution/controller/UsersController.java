package com.wevolution.controller;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.wevolution.common.utils.BeanUtil;
import com.wevolution.common.utils.DateUtil;
import com.wevolution.common.utils.ResponseUtil;
import com.wevolution.common.utils.SmsUtil;
import com.wevolution.common.utils.StringUtil;
import com.wevolution.conf.Auth;
import com.wevolution.domain.MessageRecorder;
import com.wevolution.domain.Users;
import com.wevolution.service.MessageRecorderService;
import com.wevolution.service.UsersService;

@Controller
@RequestMapping("/user")
public class UsersController {
	private static Logger logger = LoggerFactory.getLogger(UsersController.class);
	
	
	@Resource
	private UsersService userservice;
	
	
	@Resource
	private MessageRecorderService messageRecorderService;
	/**
	 * 
	* @Title: signUpPage 
	* @Description: 注册页
	* @return String 
	* @throws
	 */
	@RequestMapping("/signUppage")
	public String signUpPage(HttpServletRequest request){
		return "sign_up";
	}

	@RequestMapping("/signUppage_en")
	public String signUpPage_en(HttpServletRequest request){
		return "sign_up_en";
	}
	/**
	 * 重置密码
	* @Title: retrievePassword 
	* @return 
	*
	 */
	@RequestMapping("/retrieve")
	public String retrievePassword(){
		return "retrieve_password";
	}

	@RequestMapping("/retrieve_en")
	public String retrievePassword_en(){
		return "retrieve_password_en";
	}
	/**
	 * 
	* @Title: signUp 
	* @Description: 注册
	* @param request
	* @param user
	* @return String 
	* @throws
	 */
	@RequestMapping(value="/signUp",method=RequestMethod.POST)
	@ResponseBody
	public String signUp(HttpServletRequest request,@ModelAttribute Users user,@RequestParam("smsCode")String smsCode,@RequestParam("vrifyCode")String vrifyCode){
		HttpSession session = request.getSession();
		String captchaId = (String) session.getAttribute("vrifyCode");
		if (captchaId == null || !captchaId.equals(vrifyCode)) 
			return ResponseUtil.getResponseJson(204, "验证码错误", null);
		//手机验证码
		String sSmsCode = (String) session.getAttribute("smsCode");
		if(sSmsCode==null){
			return ResponseUtil.getResponseJson(204, "验证码超时", null);
		}else{
			String phone = (String) session.getAttribute("phone");
			if(phone != null && phone.equals(user.getPhone())){
				if(!sSmsCode.equals(smsCode)){
					return ResponseUtil.getResponseJson(204, "验证码错误", null);
				}
			}else{
				return ResponseUtil.getResponseJson(204, "注册手机号与接收短信手机号不一致", null);
			}
		}
		String credential = user.getVerifyCredential();
		String retCode = null;
		if(StringUtil.matchingPwd(credential)){
			user.setVerifyChannel((byte) 0);
			retCode = userservice.signUp(user);
			if("0".equals(retCode)){
				Integer msgId = (Integer) session.getAttribute(user.getPhone());
				MessageRecorder recorder = new MessageRecorder();
				recorder.setRecordId(msgId);
				recorder.setSmsStatus(3);
				messageRecorderService.updateMsg(recorder);
				session.removeAttribute(user.getPhone());
				
				session.removeAttribute("smsCode");
				session.removeAttribute("vrifyCode");
				session.removeAttribute("phone");
				Users users = userservice.getUserByPhone(user.getPhone(), (byte) 0);
				session.setAttribute("user", users);
				return ResponseUtil.getResponseJson(0, "注册成功", retCode);
			}else{
				return ResponseUtil.getResponseJson(204, retCode, null);
			}
		}else{
			retCode = "密码过于简单，请尝试“字母+数字”的组合";
		}
		return ResponseUtil.getResponseJson(204, retCode, null);
	}
	/**
	 * 
	* @Title: getUserByName 
	* @Description: 用户名校验
	* @param userName
	* @return String 
	 */
	@RequestMapping(value="/getUserByName",method=RequestMethod.POST)
	@ResponseBody
	public String getUserByName(HttpServletRequest request,@RequestParam("userName")String userName,@RequestParam("vrifyCode")String vrifyCode){
		HttpSession session = request.getSession();
		String captchaId = (String) session.getAttribute("vrifyCode");
		if (captchaId == null || !captchaId.equals(vrifyCode)) {
			return ResponseUtil.getResponseJson(204, "验证码错误", 1);
		}
		Boolean flag = userservice.checkUserName(userName);
		if(flag){
			return ResponseUtil.getResponseJson(204, "该用户名号尚未注册", 1);
		}
		session.removeAttribute("vrifyCode");
		return ResponseUtil.getResponseJson(0, "校验通过", 2);
	}
	/**
	 * 
	* @Title: getUserByPhone 
	* @Description: 手机号校验(密码重置)
	* @param userName
	* @return  String 
	 */
	@RequestMapping(value="/getUserByPhone",method=RequestMethod.POST)
	@ResponseBody
	public String getUserByPhone(HttpServletRequest request,@RequestParam("userName")String userName,@RequestParam("phone")String phone){
		if(StringUtil.checkMobile(phone)){
			String userId = userservice.checkUserName(userName,phone);
			HttpSession session = request.getSession();
			if(userId==null){
				return ResponseUtil.getResponseJson(204, "用户名与手机号不匹配", 2);
			}else{
				try {
					String smsCode = StringUtil.createRandomVcode();
					String retCode = SmsUtil.sendPT(new StringBuffer(phone) , new StringBuffer("【我们信息科技有限公司】您的验证码是："+smsCode+"。请不要把验证码泄漏给其他人。"));
					String[] status = retCode.split(",");
					
					MessageRecorder recorder = new MessageRecorder();
					recorder.setSendtime(DateUtil.getCurrentDateString(DateUtil.DATETIMEPATTERN24H));
					recorder.setPhone(phone);
					recorder.setSmsUsage("密码重置");
					recorder.setSmsCode(smsCode);
					
					if("0".equals(status[0])){
						recorder.setSmsStatus(1);
						MessageRecorder insertMsg = messageRecorderService.insertMsg(recorder);
						session.setAttribute(phone, insertMsg.getRecordId());
						
						session.setAttribute("smsCode", smsCode);
						session.setAttribute("phone", phone);
						
						session.setMaxInactiveInterval(10*60);
						
						logger.info("phone:"+phone+",smsCode:"+smsCode+"，发送成功");
						return ResponseUtil.getResponseJson(0, "验证码发送成功", userId);
					}else{
						recorder.setSmsStatus(2);
						messageRecorderService.insertMsg(recorder);
						logger.info("phone:"+phone+",smsCode:"+smsCode+"，发送失败");
						return ResponseUtil.getResponseJson(204, "验证码发送失败", 2);
					}
						
				} catch (Exception e) {
					e.printStackTrace();
					return ResponseUtil.getResponseJson(204, "验证码发送失败", 2);
				}
			}
		}else{
			return ResponseUtil.getResponseJson(204, "手机号有误", 2);
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
	public String smsVrify(HttpServletRequest request,@RequestParam("smsCode")String smsCode,@RequestParam("phone")String phone){
		HttpSession session = request.getSession();
		String sSmsCode = (String) session.getAttribute("smsCode");
		if(sSmsCode==null){
			return ResponseUtil.getResponseJson(204, "验证码超时", null);
		}else{
			String sendPhone = (String) session.getAttribute("phone");
			if(phone.equals(sendPhone)){
				if(sSmsCode.equals(smsCode)){
					Integer msgId = (Integer) session.getAttribute(phone);
					MessageRecorder recorder = new MessageRecorder();
					recorder.setRecordId(msgId);
					recorder.setSmsStatus(3);
					messageRecorderService.updateMsg(recorder);
					session.removeAttribute(phone);
					
					session.removeAttribute("smsCode");
					return ResponseUtil.getResponseJson(0, "验证成功", null);
				}else{
					return ResponseUtil.getResponseJson(204, "验证码错误", null);
				}
			}else{
				return ResponseUtil.getResponseJson(204, "提交手机号与接收短信手机号不一致", null);
			}
		}
	}
	/**
	 * 
	* @Title: changePwd 
	* @Description: 密码修改
	* @param user
	* @return  String 
	* @throws
	 */
	@RequestMapping(value="/changePwd",method=RequestMethod.POST)
	@ResponseBody
	public String changePwd(@RequestParam("userId")String userId,@RequestParam("verifyCredential")String verifyCredential){
		if(StringUtil.matchingPwd(verifyCredential)){
			String ret = userservice.changePwd(userId,verifyCredential);
			if(ret!=null){
				if("0".equals(ret)){
					return ResponseUtil.getResponseJson(0, "修改成功", ret);
				}else{
					return ResponseUtil.getResponseJson(204, ret, ret);
				}
			}else{
				return ResponseUtil.getResponseJson(204, "修改失败", ret);
			}
		}else
			return ResponseUtil.getResponseJson(204, "密码过于简单，请尝试“字母+数字”的组合", null);
	}
	/**
	 * 用户信息
	* @Title: getUserInfo 
	* @param request
	* @param map
	* @return 
	*
	 */
	@RequestMapping(value="/userInfo",method=RequestMethod.GET)
	@Auth
	public String getUserInfo(HttpServletRequest request,ModelMap map){
		Users user = (Users) request.getSession().getAttribute("user");
		Users users = userservice.getUserById(user.getUserId());
		if(!StringUtil.isEmpty(users.getPhone())){
			users.setPhone(users.getPhone().replaceAll("(\\d{3})\\d{4}(\\d{4})","$1****$2"));
		}
		map.put("user", users);
		return "user";
	}

	@RequestMapping(value="/userInfo_en",method=RequestMethod.GET)
	@Auth
	public String getUserInfo_en(HttpServletRequest request,ModelMap map){
		Users user = (Users) request.getSession().getAttribute("user");
		Users users = userservice.getUserById(user.getUserId());
		if(!StringUtil.isEmpty(users.getPhone())){
			users.setPhone(users.getPhone().replaceAll("(\\d{3})\\d{4}(\\d{4})","$1****$2"));
		}
		map.put("user", users);
		return "user_en";
	}
	/**
	 * 
	* @Title: updateUser 
	* @Description: 用户信息修改
	* @param user
	* @param request
	* @return  String 
	 */
	@RequestMapping(value="/updateUser",method=RequestMethod.POST)
	@ResponseBody
	@Auth
	public String updateUser(Users user,HttpServletRequest request){
		if(user.getUserName()!=null&&!userservice.checkUserName(user.getUserName())){
			return ResponseUtil.getResponseJson(204, "用户名已存在", null);
		}
		String ret = userservice.updateUserById(user);
		if(ret!=null){
			Users users = userservice.getUserById(user.getUserId());
			request.getSession().setAttribute("user", users);
			return ResponseUtil.getResponseJson(0, "修改成功", ret);
		}else
			return ResponseUtil.getResponseJson(204, "修改失败", ret);
	}
	@RequestMapping(value="/bind",method=RequestMethod.GET)
	@Auth
	public String bindPhonePage(){
		return "check_phone";
	}
	
	/**
	 * 
	* @Title: sengSmsCode 
	* @Description: 发送手机验证码
	* @param request
	* @param phone 手机号
	* @return 
	* ResponseEntity<?> 
	* @throws
	 */
	@RequestMapping(value="/sendSmsCode",method=RequestMethod.POST)
	@ResponseBody
	@Auth
	public String sendSmsCode(HttpServletRequest request,@RequestParam("phone")String phone){
		String smsCode = StringUtil.createRandomVcode();
		HttpSession session = request.getSession();
		Users user = (Users) session.getAttribute("user");
		//验证手机号是否被当前渠道的其他账号绑定
		Users users = userservice.getUserByPhone(phone, user.getVerifyChannel());
		if(users!=null){
			return ResponseUtil.getResponseJson(204, "该手机号已绑定其他账号", null);
		}
		try {
			if(StringUtil.checkMobile(phone)){
				//TODO 开发阶段暂停短信发送 
				String retCode = SmsUtil.sendPT(new StringBuffer(phone) , new StringBuffer("【我们信息科技有限公司】您的验证码是："+smsCode+"。请不要把验证码泄漏给其他人。"));
				String[] status = retCode.split(",");
				
				MessageRecorder recorder = new MessageRecorder();
				recorder.setSendtime(DateUtil.getCurrentDateString(DateUtil.DATETIMEPATTERN24H));
				recorder.setPhone(phone);
				recorder.setSmsUsage("绑定手机号");
				recorder.setSmsCode(smsCode);
				
				if("0".equals(status[0])){
					
					recorder.setSmsStatus(1);
					MessageRecorder insertMsg = messageRecorderService.insertMsg(recorder);
					session.setAttribute(phone, insertMsg.getRecordId());
					
					session.setAttribute("smsCode", smsCode);
					session.setAttribute("phone", phone);
					session.setMaxInactiveInterval(10*60);
					
					logger.info("phone:"+phone+",smsCode:"+smsCode+"，发送成功");
					
					return ResponseUtil.getResponseJson(0, "验证码发送成功", null);
				}else{
					recorder.setSmsStatus(2);
					messageRecorderService.insertMsg(recorder);
					logger.info("phone:"+phone+",smsCode:"+smsCode+"，发送失败");
					return ResponseUtil.getResponseJson(204, "验证码发送失败", null);
				}
			}else
				return ResponseUtil.getResponseJson(204, "手机号有误", null);
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseUtil.getResponseJson(204, "验证码发送失败", null);
		}
	}
	/**
	 * 
	* @Title: bindPhone 
	* @Description: 手机号绑定
	* @param request
	* @param smsCode
	* @param phone
	* @return 
	*
	 */
	@RequestMapping(value="/bindPhone",method=RequestMethod.POST)
	@ResponseBody
	@Auth
	public String bindPhone(HttpServletRequest request,@RequestParam("smsCode")String smsCode,@RequestParam("phone")String phone){
		HttpSession session = request.getSession();
		String sSmsCode = (String) session.getAttribute("smsCode");
		if(sSmsCode==null){
			return ResponseUtil.getResponseJson(204, "验证码超时", null);
		}else{
			String sendPhone = (String) session.getAttribute("phone");
			if(phone.equals(sendPhone)){
				if(sSmsCode.equals(smsCode)){
					Users user = (Users) request.getSession().getAttribute("user");
					user.setPhone(sendPhone);
					//判断是否有平台账号,若没有注册平台账号
					Users terraceUser = userservice.findByUserParam(sendPhone, null);
					if(terraceUser==null){
						Users users = new Users();
						BeanUtil.copyProperties(user, users);
						users.setUserName(sendPhone);
						users.setVerifyChannel((byte) 0);
						users.setVerifyUser(phone);
						userservice.signUp(users);
					}else{//判断两个账号是否实名
						if(StringUtil.isEmpty(terraceUser.getIdentifyStatus())||"1".equals(terraceUser.getIdentifyStatus().toString())){//平台未实名
							if(StringUtil.isEmpty(user.getIdentifyStatus())||"1".equals(user.getIdentifyStatus().toString())){
								
							}else{//第三方实名，更新平台
								terraceUser.setIdentifyStatus(user.getIdentifyStatus());
								terraceUser.setIdentifyType(user.getIdentifyType());
								userservice.updateUserById(terraceUser);
							}
						}
					}
					//更新当前用户手机号
					String ret = userservice.updateUserById(user);
					if(ret==null){
						return ResponseUtil.getResponseJson(204, "绑定失败", null);
					}else{
						Users newUser = userservice.findByUserParam(sendPhone, null);
						request.getSession().setAttribute("user", newUser);
						
						Integer msgId = (Integer) session.getAttribute(phone);
						MessageRecorder recorder = new MessageRecorder();
						recorder.setRecordId(msgId);
						recorder.setSmsStatus(3);
						messageRecorderService.updateMsg(recorder);
						session.removeAttribute(phone);
						
						session.removeAttribute("phone");
						session.removeAttribute("smsCode");
						return ResponseUtil.getResponseJson(0, "绑定成功", null);
					}
				}else{
					return ResponseUtil.getResponseJson(204, "验证码错误", null);
				}
			}else{
				return ResponseUtil.getResponseJson(204, "提交手机号与接收短信手机号不一致", null);
			}
		}
	}
}
