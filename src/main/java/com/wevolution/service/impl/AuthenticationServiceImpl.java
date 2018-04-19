package com.wevolution.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import com.github.pagehelper.PageHelper;
import com.wevolution.common.utils.DateUtil;
import com.wevolution.common.utils.StringUtil;
import com.wevolution.common.utils.UUIDTool;
import com.wevolution.domain.Authentication;
import com.wevolution.domain.IdPhoto;
import com.wevolution.domain.Users;
import com.wevolution.mapper.AuthenticationMapper;
import com.wevolution.mapper.IdPhotoMapper;
import com.wevolution.mapper.UsersMapper;
import com.wevolution.service.AuthenticationService;
@Service
public class AuthenticationServiceImpl implements AuthenticationService {

	@Resource
	private AuthenticationMapper authenticationMapper;
	
	@Resource
	private IdPhotoMapper idPhotoMapper;
	
	@Resource
	private UsersMapper usersMapper;
	
	@Override
	@Transactional("txManager")
	public String newCertification(Authentication authentication,String[] photoIds) {
		int ret = 0;
		try {
			if(photoIds.length>0){
				authentication.setIdentityStatus((byte) 2);
				authentication.setSubmitTime(DateUtil.getCurrentDateString(DateUtil.DATETIMEPATTERN24H));
				if(StringUtil.isEmpty(authentication.getAuthId())){
					authentication.setAuthId(UUIDTool.getUUID());
					if((byte)2==authentication.getIdentifyType() && photoIds.length==3){
						ret = authenticationMapper.newCertification(authentication);
					}else if((byte)3==authentication.getIdentifyType() && photoIds.length==1){
						ret = authenticationMapper.newCertification(authentication);
					}
				}else{
					if((byte)2==authentication.getIdentifyType() && photoIds.length==3){
						ret = authenticationMapper.updateCertification(authentication);
					}else if((byte)3==authentication.getIdentifyType() && photoIds.length==1){
						ret = authenticationMapper.updateCertification(authentication);
					}
				}
			}
			Users users = new Users();
			users.setUserId(authentication.getUserId());
			users.setIdentifyStatus(authentication.getIdentityStatus());
			users.setIdentifyType(authentication.getIdentifyType());
			usersMapper.updateUserById(users);
			if(ret>0){
				int result = idPhotoMapper.updateAuthId(authentication.getAuthId(), photoIds);
				return result>0?"0":"认证提交失败";
			}
		} catch (Exception e) {
			e.printStackTrace();
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			return "认证提交失败";
		}
		return "证件照异常";
	}

	@Override
	public Authentication getCertification(String userId) {
		Users users = usersMapper.selectUserById(userId);
		Authentication authentication = authenticationMapper.getCertification(userId,users.getPhone());
		return authentication;
	}

	@Override
	@Transactional("txManager")
	public String auditCertification(Authentication authentication) {
		//认证更新
		try {
			authentication.setApprovedTime(DateUtil.getCurrentDateString(DateUtil.DATETIMEPATTERN24H));
			int ret = authenticationMapper.updateCertification(authentication);
			//用户认证状态更新
			Users users = new Users();
			users.setUserId(authentication.getUserId());
			users.setIdentifyStatus(authentication.getIdentityStatus());
			usersMapper.updateUserById(users);
			//证件认证状态更新
			IdPhoto idPhoto = new IdPhoto();
			idPhoto.setAuthId(authentication.getAuthId());
			idPhoto.setIdentityStatus(authentication.getIdentityStatus());
			idPhotoMapper.updatePhotoByAuthId(idPhoto);
			return ret>0?"0":"审核错误";
		} catch (Exception e) {
			e.printStackTrace();
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			return "审核错误";
		}
	}

	@Override
	public List<Authentication> getCertificationList(Integer pageNum, Integer pageSize,Authentication param) {
		pageNum = pageNum == null ? 1 : pageNum;  
	    pageSize = pageSize == null ? 10 : pageSize;  
	    PageHelper.startPage(pageNum, pageSize);  
	    List<Authentication> list = authenticationMapper.selectAuthentication(param);
//	    PageInfo pageInfo = new PageInfo(list);  
//	    Page page = (Page) list;  
		return list;
	}
}
