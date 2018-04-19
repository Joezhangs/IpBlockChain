package com.wevolution.service.impl;

import java.sql.Timestamp;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import com.github.pagehelper.PageHelper;
import com.tiande.jbcc.clien.JBCCClien;
import com.wevolution.common.utils.DateUtil;
import com.wevolution.common.utils.DateUtils;
import com.wevolution.common.utils.HttpInvoker;
import com.wevolution.common.utils.JacksonUtil;
import com.wevolution.common.utils.StringUtil;
import com.wevolution.common.utils.UUIDTool;
import com.wevolution.domain.CopyrightAudit;
import com.wevolution.domain.Works;
import com.wevolution.domain.WorksInfo;
import com.wevolution.ipchain.ConnectionManager;
import com.wevolution.mapper.CopyrightAuditMapper;
import com.wevolution.mapper.WorksInfoMapper;
import com.wevolution.mapper.WorksMapper;
import com.wevolution.mapper.WorksSampleMapper;
import com.wevolution.service.WorksService;

import cn.tiandechain.jbcc.bean.DataBuilder;
import cn.tiandechain.jbcc.bean.QueryResult;
import cn.tiandechain.jbcc.message.JBCCResult;
import cn.tiandechain.jbcc.message.RegulationType;
import cn.tiandechain.jbcc.message.Transaction;
import cn.tiandechain.jbcc.util.BCKeyStoreUtil;

@Service
public class WorksServiceImpl implements WorksService {
	private static Logger logger = LoggerFactory.getLogger(WorksService.class);
	
	@Resource
	private WorksMapper worksMapper;

	@Resource
	private WorksInfoMapper worksInfoMapper;

	@Resource
	private WorksSampleMapper worksSampleMapper;
	
	@Resource
	private CopyrightAuditMapper copyrightAuditMapper;

	@Value("${jbcc.store.name}")
	private String storeName;
	
	@Value("${jbcc.store.pass}")
	private String storePass;
	
	@Value("${jbcc.alias}")
	private String alias;
	
	@Value("${background.domain}")
	private String domain;
	
	@Override
	@Transactional("txManager")
	public String registerWorks(Works works) {
		String ret = null;
		try {
			if (works.getWorksId() == null) {// 新增
				works.setWorksId(UUIDTool.getUUID());
				works.setCreatedTime(DateUtil.getCurrentDateString(DateUtil.DATETIMEPATTERN24H));
				worksMapper.insertWorks(works);
			} else {// TODO 更新 待定
				works.setUpdateTime(DateUtil.getCurrentDateString(DateUtil.DATETIMEPATTERN24H));
				worksMapper.updateWorks(works);
			}
			ret = works.getWorksId();
		} catch (Exception e) {
			e.printStackTrace();
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
		}
		return ret;
	}

	@Override
	public List<Works> selectWorksByUserId(Integer pageNum, Integer pageSize, Works param) {
		PageHelper.startPage(pageNum, pageSize);
		List<Works> list = worksMapper.selectWorksByUserId(param);
		return list;
	}

	@Override
	public Works selectWorksById(String worksId) {
		Works works = worksMapper.selectWorksById(worksId);
		return works;
	}

	@Override
	@Transactional("txManager")
	public String approvedWorks(Works works,CopyrightAudit audit) {
		int ret = 0;
		try {
			works.setApprovedTime(DateUtil.getCurrentDateString(DateUtil.DATETIMEPATTERN24H));
			int uret = worksMapper.updateWorks(works);
			if(uret<1)
				return "更新失败";
			copyrightAuditMapper.updateByworksId(audit);
		} catch (Exception e) {
			e.printStackTrace();
			ret = -1;
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
		}
		return String.valueOf(ret);
	}

	@Override
	public List<Works> getWorksList(Integer pageNum, Integer pageSize, Works param) {
		PageHelper.startPage(pageNum, pageSize);
		List<Works> list = worksMapper.selectWorksList(param);
		// PageInfo pageInfo = new PageInfo(list);
		// Page page = (Page) list;
		return list;
	}

	@Override
	@Transactional("txManager")
	public JBCCResult sendMsg(String worksId) {
//		BCKeyStoreUtil.createKeyStore("admin_1", "123", "block_chain_user");
		BCKeyStoreUtil.createKeyStore(storeName, storePass, alias);
		JBCCClien connection = ConnectionManager.getConnection();
		String outerSerial = UUID.randomUUID().toString().substring(0, 32);
		String reqNo = outerSerial.substring(outerSerial.length() - 5, outerSerial.length() - 1);
		//audit_information initializing
		List<WorksInfo> infoList = worksInfoMapper.selectWorksInfoByWorksId(worksId);
		String date = DateUtil.getCurrentDateString(DateUtil.DATETIMEPATTERN24H);
		StringBuffer authors = new StringBuffer();
		StringBuffer authorSings = new StringBuffer();
		for (WorksInfo worksInfo : infoList) {
			authors.append(worksInfo.getAuthor()+"|");
			authorSings.append(worksInfo.getAuthorSing()+"|");
		}
		CopyrightAudit audit = new CopyrightAudit();
		WorksInfo info = infoList.get(0);
		audit.setReqNo(reqNo);
		audit.setWorksId(info.getWorksId());
		audit.setWorksName(info.getWorksName());
		audit.setUserId(info.getUserId());
		audit.setAuthor(StringUtil.removeLastChar(authors.toString(), "|").toString());
		audit.setAuthorSing(StringUtil.removeLastChar(authorSings.toString(), "|").toString());
		audit.setApprovedStatus((byte) 1);//（1：录入；2：共识成功；3：共识失败；4：共识超时；5：审核成功；6审核失败）
		audit.setCreatedTime(date);
		audit.setUpdateFlag("0");
		copyrightAuditMapper.insert(audit);
		
		boolean checkReqNo = false;
		try {
			checkReqNo = checkReqNo(reqNo, connection);
			logger.info("repeating transaction:" + checkReqNo);
		} catch (Exception e1) {
			e1.printStackTrace();
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
		}
		JBCCResult r = null;
		if (checkReqNo) {
			return r;
		}
		List<DataBuilder> dataList = new ArrayList<DataBuilder>();

		// works
		DataBuilder works = new DataBuilder("tdbc", "works", "n", outerSerial);
		Map<String, String> worksMap = worksMapper.queryWorksById(worksId);
		worksMap.put("req_no", reqNo);
		works.setParamMap(worksMap);
		Transaction tran = new Transaction();
		tran.setRegulationType(RegulationType.REGULATION_ORIGINAL_SAVE);
		works.setTransaction(tran);
		dataList.add(works);

		// works_info
		List<Map<String, String>> list = worksInfoMapper.queryWorksInfoByWorksId(worksId);
		list.forEach(map -> {
			DataBuilder worksInfo = new DataBuilder("tdbc", "works_info", "n", outerSerial);
			worksInfo.setParamMap(map);
			Transaction worksInfoTran = new Transaction();
			worksInfoTran.setRegulationType(RegulationType.REGULATION_ORIGINAL_SAVE);
			worksInfo.setTransaction(worksInfoTran);
			dataList.add(worksInfo);
		});
		// works_sample
		Map<String, String> sampleMap = worksSampleMapper.querySampleByWorksId(worksId);
		DataBuilder worksSample = new DataBuilder("tdbc", "works_sample", "n", outerSerial);
		worksSample.setParamMap(sampleMap);
		Transaction worksSampleTran = new Transaction();
		worksSampleTran.setRegulationType(RegulationType.REGULATION_ORIGINAL_SAVE);
		worksSample.setTransaction(worksSampleTran);
		dataList.add(worksSample);

		// send works information
		CopyrightAudit copyrightAudit = new CopyrightAudit();
		copyrightAudit.setWorksId(worksId);
		
		Works worksData = new Works();
		worksData.setWorksId(worksId);
		try {
			r = connection.sendAndCountReturnForIPBlockChain(dataList);
			logger.info("The return code of sending works information:" + r.getErrorCode() + " batchNo:" + r.getBatchNo() + " errorMsg:" + r.getErrorMsg()+"blockId:"+r.getBlockId());
			if("0".equals(r.getErrorCode())){
				if(StringUtil.isEmpty(r.getBlockId())){
					copyrightAudit.setApprovedStatus((byte) 4);//1：录入；2：共识成功；3：共识失败；4：共识超时；5：审核成功；6审核失败
					worksData.setApprovedStatus((byte) 4);//1：录入；2：共识成功；3：共识失败；4：共识超时；5：审核成功；6审核失败
					throw new RuntimeException("返回blockId异常");
				}else{
					copyrightAudit.setApprovedStatus((byte) 2);//1：录入；2：共识成功；3：共识失败；4：共识超时；5：审核成功；6审核失败
					copyrightAudit.setBlockId(Integer.parseInt(r.getBlockId()));
					
					worksData.setApprovedStatus((byte) 2);//1：录入；2：共识成功；3：共识失败；4：共识超时；5：审核成功；6审核失败
				}
			}else if("TDBC3009".equals(r.getErrorCode())){
				copyrightAudit.setApprovedStatus((byte) 4);//1：录入；2：共识成功；3：共识失败；4：共识超时；5：审核成功；6审核失败
				worksData.setApprovedStatus((byte) 4);//1：录入；2：共识成功；3：共识失败；4：共识超时；5：审核成功；6审核失败
			}else{
				copyrightAudit.setApprovedStatus((byte) 3);//1：录入；2：共识成功；3：共识失败；4：共识超时；5：审核成功；6审核失败
				worksData.setApprovedStatus((byte) 3);//1：录入；2：共识成功；3：共识失败；4：共识超时；5：审核成功；6审核失败
			}
			copyrightAudit.setUpdateTime(DateUtil.getCurrentDateString(DateUtil.DATETIMEPATTERN24H));
			worksData.setUpdateTime(DateUtil.getCurrentDateString(DateUtil.DATETIMEPATTERN24H));
		} catch (Exception e) {
			e.printStackTrace();
			copyrightAudit.setApprovedStatus((byte) 4);//1：录入；2：共识成功；3：共识失败；4：共识超时；5：审核成功；6审核失败
			worksData.setApprovedStatus((byte) 4);//1：录入；2：共识成功；3：共识失败；4：共识超时；5：审核成功；6审核失败
		}finally {
			copyrightAuditMapper.updateByworksId(copyrightAudit);
			worksMapper.updateWorks(worksData);
			ConnectionManager.closeConnection();
		}
		return r;
	}

	/**
	 * 检查是否有重复交易
	 * 
	 * @param req_no
	 * @return duplicateTran true:重复交易；false:不是重复交易
	 * @throws Exception
	 * @author liwh
	 */
	private boolean checkReqNo(String req_no, JBCCClien jbccClient) throws Exception {
		Map<String, Object> conditionMap = new HashMap<String, Object>();
		conditionMap.put("req_no", req_no);
		/*
		 * duplicateTran取值 true:重复交易 ；false:非重复交易
		 */
		boolean duplicateTran = jbccClient.checkReqNo("tdbc", "works", conditionMap);
		logger.info("[checkReqno " + req_no + "],duplicateTran:" + duplicateTran);
		return duplicateTran;
	}

	@Override
	public QueryResult selectBlock(String id, String hash, String begindate, String endDate, Integer pageNum,
			Integer pageSize) {
		BCKeyStoreUtil.createKeyStore(storeName, storePass, alias);
		JBCCClien jbccClient = ConnectionManager.getConnection();
		Map<String, Object> conditionMap = new HashMap<String, Object>();
		if(!StringUtil.isEmpty(id)){
			conditionMap.put("id", id);
		}else{
			if(!StringUtil.isEmpty(hash))
				conditionMap.put("hash", hash);
			else{
				if(!StringUtil.isEmpty(begindate)&&!StringUtil.isEmpty(endDate)){
					if(begindate.equals(endDate)){//开始时间与结束日期相同，结束时间后推一天
						/*Calendar calendar = Calendar.getInstance();
						calendar.setTime(DateUtil.stringToDate(endDate));
						calendar.add(Calendar.DAY_OF_MONTH, 1);
						endDate = DateUtil.dateToString(calendar.getTime(), DateUtil.DATE_FORMAT);*/
						try {
							endDate = DateUtils.formatDate2ShortString(DateUtils.getNextDay(DateUtils.praseString2Date(begindate)));
						} catch (ParseException e) {
							e.printStackTrace();
						}
					}
				}else{//默认查询当天
					/*begindate = DateUtil.getCurrentDateString(DateUtil.DATE_FORMAT);
					Calendar calendar = Calendar.getInstance();
					calendar.setTime(DateUtil.stringToDate(begindate));
					calendar.add(Calendar.DAY_OF_MONTH, 1);
					endDate = DateUtil.dateToString(calendar.getTime(), DateUtil.DATE_FORMAT);*/
					begindate = DateUtils.formatDate2ShortString(new Date());
					endDate = DateUtils.formatDate2ShortString(DateUtils.getNextDay(new Date()));
				}
				conditionMap.put("beginDate", begindate);
				conditionMap.put("endDate", endDate);
				conditionMap.put("endPos", pageSize); // 每次查询最大返回条数 默认10条
				conditionMap.put("startPos", (pageNum - 1) * pageSize);// 偏移量，从0开始，可以是每页的起始数
			}
		}
		QueryResult queryResult = null;
		try {//判断是否范围查询
			if(!StringUtil.isEmpty(id)||!StringUtil.isEmpty(hash)){
				queryResult = jbccClient.selectBlockByCondition("tdbc", conditionMap);
				logger.info("id或hash查询开始");
			}else{
				queryResult = jbccClient.selectBlockListByPage("tdbc", conditionMap);
				logger.info("时间范围查询开始");
				logger.info("begindate:"+begindate+",endDate:"+endDate);
			}
			JBCCResult r = queryResult.getJbccResult();
			logger.info("printJbccResult errorCode:" + r.getErrorCode() + " batchNo:" + r.getBatchNo() + " errorMsg:" + r.getErrorMsg());
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			ConnectionManager.closeConnection();
		}
		return queryResult;
	}

	@Override
	public List<CopyrightAudit> getAudit(Integer pageNum, Integer pageSize, CopyrightAudit param) {
		PageHelper.startPage(pageNum, pageSize);
		List<CopyrightAudit> list = copyrightAuditMapper.selectAuditList(param);
		return list;
	}
	@Override
	public CopyrightAudit getAuditStatus(String worksId) {
		CopyrightAudit copyrightAudit = copyrightAuditMapper.selectByWorksId(worksId).get(0);
		return copyrightAudit;
	}
	@Override
	public Map<String, Object> queryWorksRegister(String worksId) {
		CopyrightAudit copyrightAudit = copyrightAuditMapper.selectByWorksId(worksId).get(0);
		Map<String, Object> map = new HashMap<String, Object>();
		if(copyrightAudit!=null){
			BCKeyStoreUtil.createKeyStore(storeName, storePass, alias);
			JBCCClien jbccClient = ConnectionManager.getConnection();
			Map<String, Object> conditionMap = new HashMap<String, Object>();
			conditionMap.put("block_id", copyrightAudit.getBlockId().toString());
			conditionMap.put("works_id", worksId);
	    	
			try {
				QueryResult queryResult = jbccClient.selectBlockWorksInfo("tdbc",conditionMap);
				if (queryResult != null) {
					JBCCResult result = queryResult.getJbccResult();
					logger.info("printJbccResult errorCode:" + result.getErrorCode() + " batchNo:" + result.getBatchNo() + " errorMsg:" + result.getErrorMsg());
					String errorCode = result.getErrorCode();
					if ("0".equals(errorCode)) {// 交易执行成功
						List<Map<String, Object>> resultlist = queryResult.getResultlist();
						if (!resultlist.isEmpty()) {
							map = resultlist.get(0);
							Timestamp timestamp = DateUtil.stringDatetimeToTimestampss(map.get("timestamp").toString());
							map.put("timestamp", timestamp);
							String works_certificate_url = "";
							String url = null;
							try {
								HashMap<String, String> paramMap = new HashMap<>();
								paramMap.put("works_id", worksId);
								url = HttpInvoker.getInstance().invoke(domain+"download.do", paramMap, false);
							} catch (Exception e) {
								e.printStackTrace();
							}
							@SuppressWarnings("unchecked")
							Map<String,String> jsonToMap = JacksonUtil.jsonToMap(url);
							if(jsonToMap != null&&jsonToMap.containsKey("url")){
								works_certificate_url = jsonToMap.get("url");
							}
							map.put("works_certificate_url", works_certificate_url);
						} 
					}else{
						throw new RuntimeException("连接区块链异常");
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
				throw new RuntimeException("查询失败");
			}finally {
				ConnectionManager.closeConnection();
			}
		}
		return map;
	}

	@Override
	public List<Map<String, Object>> reSendMsg(String[] worksIds) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Map<String, String>> getWorksAndInfo(Integer pageNum, Integer pageSize,String type,Integer area) {
		PageHelper.startPage(pageNum, pageSize);
		List<Map<String, String>> list = worksMapper.selectWorksAndInfo(type,area);
		return list;
	}

	@Override
	public Map<String, String> getWorksAndInfoByWorksId(String worksId) {
		Map<String, String> map = worksMapper.selectWorksAndInfoByWorksId(worksId);
		return map;
	}
	
}
