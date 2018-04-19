package com.wevolution.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.tiande.jbcc.clien.JBCCClien;
import com.wevolution.common.utils.DateUtils;
import com.wevolution.controller.BlockchainController;
import com.wevolution.ipchain.ConnectionManager;
import com.wevolution.mapper.UsersMapper;
import com.wevolution.mapper.WorksMapper;
import com.wevolution.service.IndexService;

import cn.tiandechain.jbcc.bean.QueryResult;
import cn.tiandechain.jbcc.message.JBCCResult;
import cn.tiandechain.jbcc.util.BCKeyStoreUtil;

@Service
public class IndexServiceImpl implements IndexService {
	private static Logger logger = LoggerFactory.getLogger(BlockchainController.class);
	@Resource
	WorksMapper worksMapper;
	
	@Resource
	UsersMapper userMapper;
	
	
	@Value("${jbcc.store.name}")
	private String storeName;
	
	@Value("${jbcc.store.pass}")
	private String storePass;
	
	@Value("${jbcc.alias}")
	private String alias;
	
	@Override
	public QueryResult dataStatistics() {
		BCKeyStoreUtil.createKeyStore(storeName, storePass, alias);
		JBCCClien jbccClient = ConnectionManager.getConnection();
		
		Map<String, Object> conditionMap = new HashMap<String, Object>();
		//当前时间的7天以内的数据
		String begindate = DateUtils.formatDate2ShortString(DateUtils.getPassDay(new Date(), 5));
		String endDate = DateUtils.formatDate2ShortString(DateUtils.getNextDay(new Date()));
		conditionMap.put("beginDate", begindate);
		conditionMap.put("endDate", endDate);
		conditionMap.put("selectNum", 30);
		QueryResult queryResult = null;
		try {
			queryResult = jbccClient.selectBlockListByDate("tdbc",conditionMap);
			JBCCResult r = queryResult.getJbccResult();
			logger.info("Block list statistics errorCode:" + r.getErrorCode() + " batchNo:" + r.getBatchNo() + " errorMsg:" + r.getErrorMsg());
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			ConnectionManager.closeConnection();
		}
		return queryResult;
	}

	@Override
	public QueryResult blockSizeStatistics() {
		String begindate = DateUtils.formatDate2ShortString(DateUtils.getPassDay(new Date(), 6));
		String endDate = DateUtils.formatDate2ShortString(new Date());
		BCKeyStoreUtil.createKeyStore(storeName, storePass, alias);
		JBCCClien jbccClient = ConnectionManager.getConnection();
		Map<String, Object> conditionMap = new HashMap<String, Object>();
		conditionMap.put("beginDate", begindate);
		conditionMap.put("endDate", endDate);
		QueryResult queryResult = null;
		try {
			queryResult = jbccClient.countBlockListByDate("tdbc",conditionMap);
			JBCCResult r = queryResult.getJbccResult();
			logger.info("Block size statistics errorCode:" + r.getErrorCode() + " batchNo:" + r.getBatchNo() + " errorMsg:" + r.getErrorMsg());
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			ConnectionManager.closeConnection();
		}
		return queryResult;
	}

}
