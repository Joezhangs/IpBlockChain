package com.wevolution.service.impl;

import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import com.wevolution.common.utils.DateUtil;
import com.wevolution.common.utils.DateUtils;
import com.wevolution.common.utils.MD5Util;
import com.wevolution.common.utils.StringUtil;
import com.wevolution.common.utils.UUIDTool;
import com.wevolution.domain.Users;
import com.wevolution.mapper.UsersMapper;
import com.wevolution.service.UsersService;
@Service
public class UsersServiceImpl implements UsersService {

	@Resource
	private UsersMapper usersMapper;
	@Override
	public Users getUserByPhone(String phone,Byte verifyChannel) {
		return usersMapper.selectUserByPhone(phone, verifyChannel);
	}

	@Override
	public Boolean checkUserName(String userName) {
		return usersMapper.checkUser(userName, null)==0;
	}


	@Override
	@Transactional("txManager")
	public String signUp(Users user) {
		if(!checkUserName(user.getUserName())){
			return "用户名已存在";
		}
		if(getUserByPhone(user.getPhone(),(byte) 0)!=null){
			return "该手机号已注册过";
		}
		String credential = user.getVerifyCredential();
		user.setVerifyCredential(credential==null?null:MD5Util.encode(credential));
		user.setUserId(UUIDTool.getUUID());
		user.setIdentifyStatus((byte) 1);//认证状态 1：未认证
		user.setIdentifyType((byte) 1);//认证方式1：未认证
		user.setCreateTime(DateUtil.getCurrentDateString(DateUtil.DATETIMEPATTERN24H));
		Integer ret = usersMapper.insertUser(user);
		if(ret>0)
			return "0";
		return "注册失败";
	}

	@Override
	@Transactional("txManager")
	public String updateUserById(Users user) {
		Users users = getUserById(user.getUserId());
		Integer ret = null;
		if (users != null) {
			user.setUpdateTime(DateUtil.getCurrentDateString(DateUtil.DATETIMEPATTERN24H));
			try {
				ret = usersMapper.updateUserById(user);
			} catch (Exception e) {
				e.printStackTrace();
				TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			}
		}
		return ret == null ? null : ret.toString();
	}

	@Override
	public Users getUserById(String userId) {
		Users users = usersMapper.selectUserById(userId);
		Byte channel = users.getVerifyChannel();
		if(channel==(byte)0){
			return users;
		}
		if(StringUtil.isEmpty(users.getPhone())){//判断是否绑定手机号
			return users;
		}else{
			return usersMapper.selectUserByPhone(users.getPhone(),(byte) 0);
		}
	}

	@Override
	public Users findByUserParam(String nameOrPhone, String credential) {
		if(credential==null)
			//手机号查询
			return usersMapper.selectUserByPhone(nameOrPhone,(byte) 0);
		else
			return usersMapper.selcetUserByParam(nameOrPhone, MD5Util.encode(credential));
	}

	@Override
	public String checkUserName(String userName, String phone) {
		String userId = usersMapper.selectUserIdByNameAndPhone(userName, phone);
		return userId==null?null:userId;
	}

	@Override
	@Transactional("txManager")
	public String changePwd(String userId, String password) {
		Users users = getUserById(userId);
		String ret = null;
		if(MD5Util.encode(password).equals(users.getVerifyCredential())){
			return "修改密码不能与原密码相同";
		}
		if (users != null) {
			try {
				Integer retCount = usersMapper.changePwd(userId, MD5Util.encode(password));
				return retCount > 0 ? "0":"修改失败";
			} catch (Exception e) {
				e.printStackTrace();
				ret = "服务器错误";
			}
		}
		return ret;
	}

	@Override
	public Users getUserByVerify(String verifyUser,Byte verifyChannel) {
		return usersMapper.selectUserByVerify(verifyUser, verifyChannel);
	}

	@Override
	public Users signUpByThird(Users user) {
		user.setUserId(UUIDTool.getUUID());
		user.setCreateTime(DateUtil.getCurrentDateString(DateUtil.DATETIMEPATTERN24H));
		usersMapper.insertUser(user);
		return user;
	}

	@Override
	public List<Map<String, Object>> registrations() {
		String begindate = DateUtils.formatDate2ShortString(DateUtils.getPassDay(new Date(), 6));
		String endDate = DateUtils.formatDate2ShortString(new Date());
		List<Map<String,Object>> list = usersMapper.registrations(begindate, endDate);
		return list;
	}

	@Override
	public Users selectByNameOrPhoneSys(String userName) {
		return usersMapper.selectByNameOrPhoneSys(userName);
	}



}
