package com.wevolution.controller;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.wevolution.common.utils.ResponseUtil;
import com.wevolution.common.utils.SmsUtil;
import com.wevolution.common.utils.StringUtil;
import com.wevolution.service.MessageRecorderService;

@Controller
@RequestMapping("/sms")
public class SmsController {
	private static Logger logger = LoggerFactory.getLogger(SmsController.class);
	
	@Resource
	private MessageRecorderService messageRecorderService;
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
	public String sendSmsCode(HttpServletRequest request,@RequestParam("phone")String phone){
		String smsCode = StringUtil.createRandomVcode();
		HttpSession session = request.getSession(true);
		try {
			if(StringUtil.checkMobile(phone)){
				//TODO 开发阶段暂停短信发送 
				String retCode = SmsUtil.sendPT(new StringBuffer(phone) , new StringBuffer("【我们信息科技有限公司】您的验证码是："+smsCode+"。请不要把验证码泄漏给其他人。"));
				String[] status = retCode.split(",");
				if("0".equals(status[0])){
					session.setAttribute("smsCode", smsCode);
					session.setAttribute("phone", phone);
					session.setMaxInactiveInterval(10*60);
					logger.info("phone:"+phone+",smsCode"+smsCode);
					return ResponseUtil.getResponseJson(0, "验证码发送成功", null);
				}else
					return ResponseUtil.getResponseJson(204, "验证码发送失败", null);
			}else
				return ResponseUtil.getResponseJson(204, "手机号有误", null);
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseUtil.getResponseJson(204, "验证码发送失败", null);
		}
	}
}
