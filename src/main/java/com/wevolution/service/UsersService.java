package com.wevolution.service;

import java.util.List;
import java.util.Map;

import com.wevolution.domain.Users;

/**
 * 
* @ClassName: UsersService 
* @Description:用户service 
* @author jiangjian
* @date 2017年8月16日 上午11:02:43 
*
 */
public interface UsersService {
	/**
	 * 
	* @Title: getUserByPhone 
	* @Description: 手机号查询用户
	* @param phone
	* @param verifyChannel 渠道
	* @return 
	*
	 */
	Users getUserByPhone(String phone,Byte verifyChannel);
	/**
	* @Title: checkUserName 
	* @Description: 用户名校验 
	* @param username 用户名
	* @return  Boolean 
	 */
	Boolean checkUserName(String username);
	/**
	 * 
	* @Title: checkUserName 
	* @Description: 用户名手机号校验
	* @param username
	* @param phone
	* @return  String 用户id 
	 */
	String checkUserName(String username,String phone);
	/**
	 * 
	* @Title: signUp 
	* @Description: 用户注册
	* @param user 
	* @return String 
	 */
	String signUp(Users user);
	/**
	 * 
	* @Title: updateUser 
	* @Description: 用户信息更新
	* @param user
	* @return  String 
	 */
	String updateUserById(Users user);
	/**
	 * 
	* @Title: getUserById 
	* @Description: 通过用户id查询用户,若是第三方用户，通过手机号判断是否存在平台用户，存在则返回平台用户信息
	* @param userId
	* @return Users 
	 */
	Users getUserById(String userId);
	/**
	 * 
	* @Title: findByUserParam 
	* @Description: 根据用户名或手机号查询用户,按照平台，微信，微博顺序取第一条
	* @param nameOrPhone 用户名或手机号
	* @param [credential]
	* @return Users 
	 */
	Users findByUserParam(String nameOrPhone, String credential);
	/**
	 * 
	* @Title: changePwd 
	* @Description: 密码修改
	* @param userId
	* @param password
	* @return String 
	* @throws
	 */
	String changePwd(String userId,String password);
	/**
	 * 
	* @Title: selectUserByVerify 
	* @Description: 第三方用户标识
	* @param verifyUser
	* @param verifyChannel
	* @return  Users 
	 */
	Users getUserByVerify(String verifyUser,Byte verifyChannel);
	/**
	 * 
	* @Title: signUpByThird 
	* @Description: 第三方信息入库
	* @param user
	* @return  Users 
	 */
	Users signUpByThird(Users user);
	/**
	 * 注册量登记量统计
	* @Title: registrations 
	* @return 
	*
	 */
	List<Map<String,Object>> registrations();
	
	/**
	 * 平台用户校验
	* @Title: selectByNameOrPhoneSys 
	* @param userName 用户名或电话
	* @return 
	*
	 */
	Users selectByNameOrPhoneSys(String userName);
}
