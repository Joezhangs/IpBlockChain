package com.wevolution.service.impl;

import java.util.Iterator;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import com.wevolution.common.utils.BeanUtil;
import com.wevolution.common.utils.DateUtil;
import com.wevolution.common.utils.StringUtil;
import com.wevolution.domain.WorksInfo;
import com.wevolution.mapper.WorksInfoMapper;
import com.wevolution.mapper.WorksMapper;
import com.wevolution.mapper.WorksSampleMapper;
import com.wevolution.service.WorksInfoService;
@Service
public class WorksInfoServiceImpl implements WorksInfoService {
	@Resource
	private WorksInfoMapper worksInfoMapper;

	@Resource
	private WorksMapper worksMapper;
	
	@Resource
	private WorksSampleMapper worksSampleMapper;

	@Override
	@Transactional("txManager")
	public String worksInfo(WorksInfo worksInfo) {
		try {
			List<WorksInfo> list = selectWorksInfoByWorksId(worksInfo.getWorksId());
			if(list.size()>0){//更新（暂存数据），可能多条（如从作者及著作权人信息返回修改）
				worksInfo.setProgress(1);//1：作品信息；2：作者及著作权
				worksInfo.setUpdatedTime(DateUtil.getCurrentDateString(DateUtil.DATETIMEPATTERN24H));
				worksInfoMapper.updateByWorksId(worksInfo);
				worksMapper.updateWorksProgress(worksInfo.getWorksId(),1);//更新作品登记进度
			}else{//新增，作品信息只有一条
				worksInfo.setProgress(1);//1：作品信息；2：作者及著作权
				worksInfo.setCreatedTime(DateUtil.getCurrentDateString(DateUtil.DATETIMEPATTERN24H));
				worksInfoMapper.insertWorksInfoByOne(worksInfo);
				worksMapper.updateWorksProgress(worksInfo.getWorksId(),1);//更新作品登记进度
			}
			return "0";
		} catch (Exception e) {
			e.printStackTrace();
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			return "登记失败";
		}
	}

	@Override
	@Transactional("txManager")
	public String authorAndRight(List<WorksInfo> worksInfos) {
		WorksInfo worksInfo = worksInfoMapper.selectWorksInfoOneByWorksId(worksInfos.get(0).getWorksId());
		Iterator<WorksInfo> iterator = worksInfos.iterator();
		while (iterator.hasNext()) {
			WorksInfo worksInfo_new = iterator.next();
			//获得包含公有信息的worksInfo
			if(!StringUtil.isEmpty(worksInfo_new.getAccessRightsType())){
				worksInfo.setAccessRightsType(worksInfo_new.getAccessRightsType());
				worksInfo.setAccessRightsTypeExplain(worksInfo_new.getAccessRightsTypeExplain());
				worksInfo.setRightsAffiliationType(worksInfo_new.getRightsAffiliationType());
				worksInfo.setRightsAffiliationTypeExplain(worksInfo_new.getRightsAffiliationTypeExplain());
				worksInfo.setOwnRightStatus(worksInfo_new.getOwnRightStatus());
				worksInfo.setOwnRightStatusExplain(worksInfo_new.getOwnRightStatusExplain());
				worksInfo.setProgress(2);
			}
			//去除作者信息、著作权人信息为空的元素（页面表单校验，该情况不会发生）
			if(StringUtil.isEmpty(worksInfo_new.getAuthor())&&StringUtil.isEmpty(worksInfo_new.getOwnerName()))
				iterator.remove();
		}
		for (WorksInfo worksInfo_new : worksInfos) {
			BeanUtil.copyPropertiesIgnoreNull(worksInfo, worksInfo_new);
			worksInfo_new.setCreatedTime(worksInfo.getCreatedTime());
			worksInfo_new.setUpdatedTime(DateUtil.getCurrentDateString(DateUtil.DATETIMEPATTERN24H));
		}
		try {
			worksInfoMapper.deleteByWorksId(worksInfos.get(0).getWorksId());//删除旧数据
			worksInfoMapper.insertWorksInfo(worksInfos);
			worksMapper.updateWorksProgress(worksInfo.getWorksId(),2);//更新作品登记进度
		} catch (Exception e) {
			e.printStackTrace();
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
		}
		return "0";
	}
	@Override
	public List<WorksInfo> selectWorksInfoByWorksId(String worksId) {
		List<WorksInfo> list = worksInfoMapper.selectWorksInfoByWorksId(worksId);
		return list;
	}
	@Override
	public WorksInfo selectWorksInfoOneByWorksId(String worksId){
		return worksInfoMapper.selectWorksInfoOneByWorksId(worksId);
	}
	@Override
	@Transactional("txManager")
	public String submitWorksInfo(String worksId) {
		try {
			worksMapper.updateWorksProgress(worksId,3);//更新作品登记进度
		} catch (Exception e) {
			e.printStackTrace();
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
		}
		return "0";
	}

}
