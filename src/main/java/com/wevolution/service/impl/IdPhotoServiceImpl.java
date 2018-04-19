package com.wevolution.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import com.wevolution.common.utils.DateUtil;
import com.wevolution.domain.IdPhoto;
import com.wevolution.mapper.IdPhotoMapper;
import com.wevolution.service.IdPhotoService;

@Service
public class IdPhotoServiceImpl implements IdPhotoService {

	@Resource
	private IdPhotoMapper idPhotoMapper;
	
	@Override
	@Transactional("txManager")
	public String updateAuthId(String authId,String[] photoId){
		int ret = 0;
		try {
			idPhotoMapper.updateAuthId(authId, photoId);
		} catch (Exception e) {
			e.printStackTrace();
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			return "证件照异常";
		}
		return String.valueOf(ret);
	}

	@Override
	@Transactional("txManager")
	public String insertPhoto(IdPhoto idPhoto) {
		try {
			idPhoto.setCreateTime(DateUtil.getCurrentDateString(DateUtil.DATETIMEPATTERN24H));
			idPhoto.setIdentityStatus((byte) 1);
			idPhotoMapper.insertPhoto(idPhoto);
		} catch (Exception e) {
			e.printStackTrace();
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			return "上传失败";
		}
		return idPhoto.getRecordId().toString();
	}

	@Override
	public IdPhoto getPhotoByRecordId(String recordId) {
		return idPhotoMapper.selectPhotoByRecordId(recordId);
	}
	
	@Override
	public List<IdPhoto> selectByUserId(String userId){
		return idPhotoMapper.selectByUserId(userId);
	}
	
	@Override
	@Transactional("txManager")
	public String updatePhoto(IdPhoto idPhoto) {
		try {
			idPhoto.setUpdateTime(DateUtil.getCurrentDateString(DateUtil.DATETIMEPATTERN24H));
			idPhotoMapper.updatePhoto(idPhoto);
			return "0";
		} catch (Exception e) {
			e.printStackTrace();
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			return "证件上传异常";
		}
	}

	@Override
	public List<IdPhoto> selectByAuthId(String authId) {
		return idPhotoMapper.selectByAuthId(authId);
	}

	@Override
	@Transactional("txManager")
	public String deleteByUserId(String userId, byte status) {
		idPhotoMapper.deletByUserId(userId,status);
		return "0";
	}

}
