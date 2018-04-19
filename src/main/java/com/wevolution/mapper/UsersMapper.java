package com.wevolution.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.wevolution.domain.Users;

public interface UsersMapper {

	/**
	 * selectByPrimaryKey
	 */
	Users selectByPrimaryKey(Integer recordId);
	/**
	 * 平台用户校验
	* @Title: selectByNameOrPhoneSys 
	* @param userName 用户名或电话
	* @return 
	*
	 */
	Users selectByNameOrPhoneSys(String userName);
	/**
	 * 
	* @Title: insertUser 
	* @Description:新增用户 
	* @param user
	* @return 
	* Integer 
	* @throws
	 */
	Integer insertUser(Users user);
	/**
	 * 
	* @Title: checkUser 
	* @Description: 已存在用户校验
	* @param userName 
	* @param phone
	* @return Integer 
	* @throws
	 */
	Integer checkUser(@Param("userName")String userName,@Param("phone") String phone);
	/**
	 * 
	* @Title: selcetUserByParam 
	* @Description: 用户查询（密码登录校验）
	* @param nameOrPhone 用户名或手机号
	* @param credential 校验凭据
	* @return 
	* Users 
	* @throws
	 */
	Users selcetUserByParam(@Param("nameOrPhone")String nameOrPhone,@Param("credential")String credential);
	/**
	 * 
	* @Title: selectUserByPhone 
	* @Description: 手机号查找用户
	* @param phone
	* @param verifyChannel 渠道
	* @return 
	*
	 */
	Users selectUserByPhone(@Param("phone")String phone,@Param("verifyChannel")Byte verifyChannel);
	/**
	 * 
	* @Title: selectUserIdByNameAndPhone 
	* @Description: 根据用户名、手机号查找用户id（防止通过自己手机号修改他人密码）
	* @param userName
	* @param phone
	* @return  String 
	* @throws
	 */
	String selectUserIdByNameAndPhone(@Param("userName")String userName,@Param("phone")String phone);
	/**
	 * 
	* @Title: selectUserById 
	* @Description: 根据用户id查询用户
	* @param userId
	* @return  Users 
	 */
	Users selectUserById(String userId);
	/**
	 * 
	* @Title: changePwd 
	* @Description: 密码修改
	* @param userId
	* @param password
	* @return  Integer 
	 */
	Integer changePwd(@Param("userId")String userId,@Param("verifyCredential")String verifyCredential);
	/**
	 * 
	* @Title: updateUser 
	* @Description: 更新用户
	* @param user
	* @return Integer 
	 */
	Integer updateUserById(Users user);
	/**
	 * 
	* @Title: selectUserByVerify 
	* @Description: 第三方用户标识
	* @param verifyUser
	* @param verifyChannel
	* @return  Users 
	 */
	Users selectUserByVerify(@Param("verifyUser")String verifyUser,@Param("verifyChannel")Byte verifyChannel);
	
	/**
	 * 
	* @Title: registrations 
	* @return 
	*
	 */
	List<Map<String,Object>>  registrations(@Param("begindate")String begindate,@Param("endDate")String endDate);
}