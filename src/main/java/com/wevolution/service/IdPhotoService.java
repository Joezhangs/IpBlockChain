package com.wevolution.service;

import java.util.List;

import com.wevolution.domain.IdPhoto;

public interface IdPhotoService {
	/**
	 * 
	* @Title: updateAuthId 
	* @Description: 更新证件照认证id，建立认证关系
	* @return String 
	 */
	String updateAuthId(String authId,String[] photoId)throws Exception;
	/**
	 * 
	* @Title: insertPhoto 
	* @Description: 证件上传
	* @param idPhoto
	* @return String 证件照记录id
	 */
	String insertPhoto(IdPhoto idPhoto);
	/**
	 * 
	* @Title: getPhotoByRecordId 
	* @Description: 根据上传记录id获取上传记录
	* @param recordId
	* @return 
	*
	 */
	IdPhoto getPhotoByRecordId(String recordId);
	
	/**
	 * 
	* @Title: selectByUserId 
	* @Description: 根据用户id查询
	* @param userId
	* @return 
	*
	 */
	List<IdPhoto> selectByUserId(String userId);
	/**
	 * 
	* @Title: updatePhoto 
	* @Description: 更新
	* @param idPhoto
	* @return 
	*
	 */
	String updatePhoto(IdPhoto idPhoto);
	/**
	 * 
	* @Title: selectByAuthId 
	* @Description: 认证id查询证件
	* @param authId
	* @return 
	*
	 */
	List<IdPhoto> selectByAuthId(String authId);
	/**
	 * 删除证件
	* @Title: deleteByUserId 
	* @param userId
	 * @param status 
	* @return 
	*
	 */
	String deleteByUserId(String userId, byte status);
}
