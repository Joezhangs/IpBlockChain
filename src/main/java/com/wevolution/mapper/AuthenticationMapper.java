package com.wevolution.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.wevolution.domain.Authentication;

public interface AuthenticationMapper {
	/**
	 * 
	* @Title: newCertification 
	* @Description: 新增认证
	* @param authentication
	* @return int 
	 */
	int newCertification(Authentication authentication);
	/**
	 * 
	* @Title: getCertification 
	* @Description: 认证查询
	* @param userId 用户id
	* @return Authentication 
	 */
	Authentication getCertification(@Param("userId")String userId,@Param("phone")String phone);
	/**
	 * 
	* @Title: updateCertification 
	* @Description: 更新
	* @param authentication
	* @return 
	*
	 */
	int updateCertification(Authentication authentication);
	/**
	 * 认证列表查询
	* @Title: selectAuthentication 
	* @param param
	* @return 
	*
	 */
	List<Authentication> selectAuthentication(Authentication param);
}