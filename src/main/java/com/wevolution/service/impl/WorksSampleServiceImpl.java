package com.wevolution.service.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import com.wevolution.common.utils.DateUtil;
import com.wevolution.common.utils.StringUtil;
import com.wevolution.domain.WorksSample;
import com.wevolution.mapper.WorksInfoMapper;
import com.wevolution.mapper.WorksMapper;
import com.wevolution.mapper.WorksSampleMapper;
import com.wevolution.service.WorksSampleService;

@Service
public class WorksSampleServiceImpl implements WorksSampleService {
	@Resource
	private WorksSampleMapper worksSampleMapper;
	
	@Resource
	private WorksMapper worksMapper;

	@Resource
	private WorksInfoMapper worksInfoMapper;
	
	@Override
	@Transactional("txManager")
	public String registerWorks(WorksSample worksSample,Integer Progress) {
		try {
			WorksSample sample = selectWorksSampleById(worksSample.getWorksId());
			if (StringUtil.isEmpty(sample)) {
				worksSample.setCreatedTime(DateUtil.getCurrentDateString(DateUtil.DATETIMEPATTERN24H));
				worksSampleMapper.insertWorksSample(worksSample);
				if(Progress == 3)
					worksMapper.updateWorksProgress(worksSample.getWorksId(),3);//更新作品登记进度
			}else{//！！！不存在更新
				worksSample.setUpdateTime(DateUtil.getCurrentDateString(DateUtil.DATETIMEPATTERN24H));
				worksSampleMapper.updateWorksSample(worksSample);
				if(Progress == 3)
					worksMapper.updateWorksProgress(worksSample.getWorksId(),3);//更新作品登记进度
			}
			return "0";
		} catch (Exception e) {
			e.printStackTrace();
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			return "登记失败";
		}
	}

	@Override
	public WorksSample selectWorksSampleById(String worksId) {
		return worksSampleMapper.selectSampleByWorksId(worksId);
	}

}
