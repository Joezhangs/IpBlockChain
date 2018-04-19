package com.wevolution.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.wevolution.domain.IdPhoto;

public interface IdPhotoMapper {

	/**
	 * 
	* @Title: updateAuthId 
	* @Description: 更新证件照认证id，建立认证关系
	* @return int 
	 */
	int updateAuthId(@Param("authId")String authId,@Param("photoIds")String[] photoIds);
	/**
	 * 
	* @Title: insertPhoto 
	* @Description: 证件上传
	* @param idPhoto
	* @return int 
	 */
	int insertPhoto(IdPhoto idPhoto);

	/**
	 * 
	* @Title: getPhotoByRecordId 
	* @Description: 根据上传记录id获取上传记录
	* @param recordId
	* @return 
	*
	 */
	IdPhoto selectPhotoByRecordId(String recordId);
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
	int updatePhoto(IdPhoto idPhoto);
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
	 * 
	* @Title: updatePhotoByAuthId 
	* @Description: 根据认证id更新
	* @param idPhoto
	* @return 
	*
	 */
	int updatePhotoByAuthId(IdPhoto idPhoto);
	/**
	 * 
	* @Title: deletByUserId 
	* @Description: 根据用户id删除认证照
	* @param userId
	* @return 
	*
	 */
	int deletByUserId(@Param("userId")String userId, @Param("status")byte status);
}