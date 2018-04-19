package com.wevolution.service;

import java.util.List;

import com.wevolution.domain.Authentication;

public interface AuthenticationService {
	/**
	 * 
	* @Title: newCertification 
	* @Description: 新增认证。1、上传证件照；2、提交认证；3、更新证件认证id，建立认证关系
	* @param authentication
	* @return int 
	 */
	String newCertification(Authentication authentication,String[] photoId);
	/**
	 * 
	* @Title: getCertification 
	* @Description: 认证查询
	* @param userId
	* @return Authentication 
	 */
	Authentication getCertification(String userId);
	/**
	 * 
	* @Title: auditCertification 
	* @Description: 审核
	* @param authentication
	* @return 
	*
	 */
	String auditCertification(Authentication authentication);
	/**
	 * 认证列表
	* @Title: getCertificationList 
	* @param pageNum
	* @param pageSize
	* @param param
	* @return 
	*
	 */
	List<Authentication> getCertificationList(Integer pageNum, Integer pageSize,Authentication param);
}
