package com.wevolution;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.tiande.jbcc.clien.JBCCClien;

import cn.tiandechain.jbcc.bean.DataBuilder;
import cn.tiandechain.jbcc.bean.QueryResult;
import cn.tiandechain.jbcc.message.JBCCResult;
import cn.tiandechain.jbcc.message.RegulationType;
import cn.tiandechain.jbcc.message.Transaction;
import cn.tiandechain.jbcc.util.BCKeyStoreUtil;
import cn.tiandechain.jbcc.util.GsonUtil;

public class JBCCTestForIPBlockchain {
	private String startTime = null;
	private static JBCCClien jbccClient = null;
	static String trade_no = UUID.randomUUID().toString().substring(0, 32);
	static String req_no = UUID.randomUUID().toString().substring(0, 32);
//	String[] nodeMqTcps = new String[] { "tcp://106.39.31.12:61616?" };
//	String[] nodeMqTcps = new String[] { "tcp://192.168.0.201:61616?", "tcp://192.168.0.202:61616?",
//			"tcp://192.168.0.203:61616?", "tcp://192.168.0.204:61616?" };
	
	String[] nodeMqTcps = new String[] { 
			"tcp://ec2-52-80-61-50.cn-north-1.compute.amazonaws.com.cn:61616?",
			"tcp://ec2-54-223-138-92.cn-north-1.compute.amazonaws.com.cn:61616?",
			"tcp://ec2-54-223-75-107.cn-north-1.compute.amazonaws.com.cn:61616?",
			"tcp://ec2-54-222-155-229.cn-north-1.compute.amazonaws.com.cn:61616?"
 };

	@Before
	public void setup() throws Exception {
		/* 初始化当前用户密钥库 */
		BCKeyStoreUtil.createKeyStore("admin_1", "123", "block_chain_user");
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		startTime = sdf.format(new Date());
		jbccClient = getJBCCClien();
	}

	@After
	public void cleanup() throws Exception {
		jbccClient.close();
	}

	@Test
	public void test_createIndex() throws Exception {
		JBCCResult r = createIndex();
		printJbccResult(r);
	}

	@Test
	public void createChains() throws Exception {
		JBCCResult r = null;
		r = createChainWorks();
		r = createChainWorksInfo();
		r = createChainWorksSample();
//		r = createCopyrightAudit();

		printJbccResult(r);

	}

	private static void printJbccResult(JBCCResult r) {
		System.out.println("printJbccResult errorCode:" + r.getErrorCode() +  " errorMsg:"
				+ r.getErrorMsg() +" batchNo:" + r.getBatchNo() +" blockId:" +r.getBlockId());
	}

	@Test
	public void sendAndRecv() throws Exception {
		for(int i = 0; i < 1;i++) {
			sendMsg();
			
		}
	}



	/**
	 * 创建新链测试
	 * 
	 * @author xiaoming
	 * @throws Exception
	 */
	private JBCCClien getJBCCClien() throws Exception {
		JBCCClien jbccClient = new JBCCClien();
		jbccClient.startClien("admin_1", nodeMqTcps, "ipmanagermq", "ipblockchain798");
		return jbccClient;
	}

	private JBCCResult createChainWorks() throws Exception {
		JBCCResult result = null;
		String outerSerial = UUID.randomUUID().toString();

		DataBuilder workBuilder = new DataBuilder("tdbc", "works", "y", outerSerial);
		workBuilder.addParam("req_no", "varchar(32) not null");
		workBuilder.addParam("user_id", "char(36) DEFAULT NULL COMMENT '用户id'");
		workBuilder.addParam("works_id", "char(36) DEFAULT NULL COMMENT '作品id'");
		workBuilder.addParam("works_name", "varchar(100) DEFAULT NULL COMMENT '作品名'");
		workBuilder.addParam("works_sign", "varchar(80) DEFAULT NULL COMMENT '作品署名'");
		workBuilder.addParam("works_url", "varchar(400) DEFAULT NULL COMMENT '作品URL'");
		workBuilder.addParam("created_time", "char(20) DEFAULT NULL COMMENT '创建时间'");
		workBuilder.addParam("update_time", "char(20) DEFAULT NULL COMMENT '更新时间'");
		workBuilder.addParam("approved_status","tinyint(2) DEFAULT '1' COMMENT '审核状态（1：录入；2：共识成功；3：共识失败；4：共识超时；5：审核成功；6审核失败）'");
		workBuilder.addParam("approved_time", "char(20) DEFAULT NULL COMMENT '审核通过时间'");
		workBuilder.addParam("approved_description", "varchar(200) DEFAULT NULL COMMENT '审核描述（失败原因）'");
		// update_flag:应用直接存储(0)还是系统更新(1)
		workBuilder.addParam("update_flag", "CHAR(1)");
		try {
			result = jbccClient.createChain(workBuilder);
			printJbccResult(result);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return result;
	}

	private JBCCResult createChainWorksInfo() throws Exception {
		JBCCResult result = null;
		String outerSerial = UUID.randomUUID().toString();
		DataBuilder dataBuilder = new DataBuilder("tdbc", "works_info", "n", outerSerial);
		dataBuilder.addParam("works_id", "char(36) DEFAULT NULL COMMENT '作品id'");
		dataBuilder.addParam("user_id", "char(36) DEFAULT NULL COMMENT '用户id'");
		dataBuilder.addParam("works_name", "varchar(100) DEFAULT NULL COMMENT '作品名称'");
		dataBuilder.addParam("works_type", "varchar(50) DEFAULT NULL COMMENT '作品类别id'");
		dataBuilder.addParam("works_explain", "varchar(200) DEFAULT NULL COMMENT '作品类别说明'");
		dataBuilder.addParam("works_nature", "varchar(50) DEFAULT NULL COMMENT '作品创作性质id'");
		dataBuilder.addParam("works_nature_explain", "varchar(200) DEFAULT NULL COMMENT '作品性质说明'");
		dataBuilder.addParam("accomplish_time", "varchar(20) DEFAULT NULL COMMENT '创作完成时间'");
		dataBuilder.addParam("province", "varchar(40) DEFAULT NULL COMMENT '创作省份'");
		dataBuilder.addParam("city", "varchar(40) DEFAULT NULL COMMENT '创作城市'");
		dataBuilder.addParam("area", "varchar(40) DEFAULT NULL COMMENT '创作区域'");
		dataBuilder.addParam("publish_status", "tinyint(2) DEFAULT NULL COMMENT '发表状态（1：已发表；2：未发表）'");
		dataBuilder.addParam("first_publish_status", "varchar(20) DEFAULT NULL COMMENT '首次发表日期'");
		dataBuilder.addParam("publish_address", "varchar(150) DEFAULT NULL COMMENT '发表地点'");
		dataBuilder.addParam("author", "varchar(50) DEFAULT NULL COMMENT '作者名称'");
		dataBuilder.addParam("author_sing", "varchar(80) DEFAULT NULL COMMENT '作者署名'");
		dataBuilder.addParam("access_rights_type", "varchar(50) DEFAULT NULL COMMENT '权利获取方式'");
		dataBuilder.addParam("access_rights_type_explain", "varchar(200) DEFAULT NULL COMMENT '权利获取方式说明'");
		dataBuilder.addParam("rights_affiliation_type", "varchar(50) DEFAULT NULL COMMENT '权利归属方式'");
		dataBuilder.addParam("rights_affiliation_type_explain", "varchar(200) DEFAULT NULL COMMENT '权利归属方式说明'");
		dataBuilder.addParam("own_right_status", "varchar(200) DEFAULT NULL COMMENT '权利拥有状况'");
		dataBuilder.addParam("own_right_status_explain", "varchar(200) DEFAULT NULL COMMENT '权利拥有状况说明'");
		dataBuilder.addParam("owner_name", "varchar(50) DEFAULT NULL COMMENT '著作权姓名'");
		dataBuilder.addParam("owner_sign", "varchar(200) DEFAULT NULL COMMENT '著作权署名情况'");
		dataBuilder.addParam("owner_type", "varchar(50) DEFAULT NULL COMMENT '类别'");
		dataBuilder.addParam("owner_id_type", "varchar(50) DEFAULT NULL COMMENT '证件类型'");
		dataBuilder.addParam("owner_id_number", "char(20) DEFAULT NULL COMMENT '证件号码'");
		dataBuilder.addParam("owner_nationality", "varchar(40) DEFAULT NULL COMMENT '国籍'");

		dataBuilder.addParam("owner_province","varchar(40) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL COMMENT '省份'");
		dataBuilder.addParam("owner_city", "varchar(40) DEFAULT NULL COMMENT '城市'");
		dataBuilder.addParam("electronic_medium", "char(2) DEFAULT NULL COMMENT '电子介质'");
		dataBuilder.addParam("created_time", "char(20) DEFAULT NULL COMMENT '创建时间'");
		dataBuilder.addParam("updated_time", "char(20) DEFAULT NULL COMMENT '更新时间'");
		// update_flag:应用直接存储(0)还是系统更新(1)
		dataBuilder.addParam("update_flag", "CHAR(1)");

		try {
			result = jbccClient.createChain(dataBuilder);
			printJbccResult(result);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	private JBCCResult createChainWorksSample() throws Exception {
		JBCCResult result = null;
		String outerSerial = UUID.randomUUID().toString();

		DataBuilder workBuilder = new DataBuilder("tdbc", "works_sample", "n", outerSerial);
		workBuilder.addParam("works_id", "char(36) DEFAULT NULL COMMENT '作品id'");
		workBuilder.addParam("works_name", "varchar(100) DEFAULT NULL COMMENT '作品名'");
		workBuilder.addParam("user_id", "char(36) DEFAULT NULL COMMENT '用户id'");
		workBuilder.addParam("works_url", "varchar(400) DEFAULT NULL COMMENT '作品URL'");
		workBuilder.addParam("created_time", "char(20) DEFAULT NULL COMMENT '创建时间'");
		workBuilder.addParam("update_time", "char(20) DEFAULT NULL COMMENT '更新时间'");
		workBuilder.addParam("approved_time", "char(20) DEFAULT NULL COMMENT '审核通过时间'");
		// update_flag:应用直接存储(0)还是系统更新(1)
		workBuilder.addParam("update_flag", "CHAR(1)");

		try {
			result = jbccClient.createChain(workBuilder);
			printJbccResult(result);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	private JBCCResult createCopyrightAudit() throws Exception {
		JBCCResult result = null;
		String outerSerial = UUID.randomUUID().toString();

		DataBuilder workBuilder = new DataBuilder("tdbc", "copyright_audit", "n", outerSerial);
		workBuilder.addParam("req_no", "varchar(32) not null");
		workBuilder.addParam("user_id", "char(36) DEFAULT NULL COMMENT '用户id'");
		workBuilder.addParam("works_id", "char(36) DEFAULT NULL COMMENT '作品id'");
		workBuilder.addParam("works_name", "varchar(100) DEFAULT NULL COMMENT '作品名'");
		workBuilder.addParam("author", "varchar(50) DEFAULT NULL COMMENT '作者'");
		workBuilder.addParam("author_sing", "varchar(80) DEFAULT NULL COMMENT '作者署名'");
		workBuilder.addParam("approved_status","tinyint(2) DEFAULT '1' COMMENT '审核状态（1：录入；2：共识成功；3：共识失败；4：共识超时；5：审核成功；6审核失败）'");
		workBuilder.addParam("created_time", "char(20) DEFAULT NULL COMMENT '创建时间'");
		workBuilder.addParam("update_time", "char(20) DEFAULT NULL COMMENT '更新时间'");

		// update_flag:应用直接存储(0)还是系统更新(1)
		workBuilder.addParam("update_flag", "CHAR(1)");

		try {
			result = jbccClient.createChain(workBuilder);
			printJbccResult(result);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return result;
	}

	private void sendMsg() throws Exception {
		String outerSerial = UUID.randomUUID().toString().substring(0, 32);
		String reqNo = outerSerial.substring(outerSerial.length() - 5, outerSerial.length() - 1);

		List<DataBuilder> dataList = new ArrayList<DataBuilder>();

		// works
		DataBuilder works = new DataBuilder("tdbc", "works", "n", outerSerial);
		works.addParam("req_no", reqNo);// 唯一标识一次请求
		works.addParam("user_id", "zhangsan");
		works.addParam("works_id", reqNo);
		works.addParam("works_name", "大话西游");
		works.addParam("works_sign", "zhangsan");
		works.addParam("works_url", "www.baidu.com");
		works.addParam("created_time", startTime);
		works.addParam("update_time", startTime);
		works.addParam("approved_status", "1");// 录入
		works.addParam("approved_time", "");
		works.addParam("approved_description", "");

		Transaction tran = new Transaction();
		tran.setRegulationType(RegulationType.REGULATION_ORIGINAL_SAVE);
		works.setTransaction(tran);
		dataList.add(works);

		// works_info
		DataBuilder worksInfo = new DataBuilder("tdbc", "works_info", "n", outerSerial);
		worksInfo.addParam("works_id", reqNo);
		worksInfo.addParam("user_id", "zhangsan");
		worksInfo.addParam("works_name", "大话西游");
		worksInfo.addParam("works_type", "文字");
		worksInfo.addParam("works_explain", "");
		worksInfo.addParam("works_nature", "注释");
		worksInfo.addParam("works_nature_explain", "");
		worksInfo.addParam("accomplish_time", "2017-08-30");
		worksInfo.addParam("province", "guizhou");
		worksInfo.addParam("city", "guiyan");
		worksInfo.addParam("area", "1");
		worksInfo.addParam("publish_status", "1");
		worksInfo.addParam("first_publish_status", "");
		worksInfo.addParam("publish_address", "贵阳");
		worksInfo.addParam("author", "zhangsan");
		worksInfo.addParam("author_sing", "zhangsan");
		worksInfo.addParam("access_rights_type", "承受");
		worksInfo.addParam("access_rights_type_explain", "");
		worksInfo.addParam("rights_affiliation_type", "个人作品");
		worksInfo.addParam("rights_affiliation_type_explain", "");
		worksInfo.addParam("own_right_status", "全部");
		worksInfo.addParam("own_right_status_explain", "");
		worksInfo.addParam("owner_name", "zhangsan");
		worksInfo.addParam("owner_sign", "zhangsan");
		worksInfo.addParam("owner_type", "自然人");
		worksInfo.addParam("owner_id_type", "居民身份证");
		worksInfo.addParam("owner_id_number", "420000000000000X");
		worksInfo.addParam("owner_nationality", "china");
		worksInfo.addParam("owner_province", "guizhou");

		worksInfo.addParam("owner_city", "guiyan");
		worksInfo.addParam("electronic_medium", "10");
		worksInfo.addParam("created_time", startTime);
		worksInfo.addParam("updated_time", startTime);

		Transaction worksInfoTran = new Transaction();
		worksInfoTran.setRegulationType(RegulationType.REGULATION_ORIGINAL_SAVE);
		worksInfo.setTransaction(worksInfoTran);
		dataList.add(worksInfo);
		
		DataBuilder worksInfo2 = new DataBuilder("tdbc", "works_info", "n", outerSerial);
		worksInfo2.addParam("works_id", reqNo);
		worksInfo2.addParam("user_id", "zhangsan");
		worksInfo2.addParam("works_name", "大话西游");
		worksInfo2.addParam("works_type", "文字");
		worksInfo2.addParam("works_explain", "");
		worksInfo2.addParam("works_nature", "注释");
		worksInfo2.addParam("works_nature_explain", "");
		worksInfo2.addParam("accomplish_time", "2017-08-30");
		worksInfo2.addParam("province", "guizhou");
		worksInfo2.addParam("city", "guiyan");
		worksInfo2.addParam("area", "1");
		worksInfo2.addParam("publish_status", "1");
		worksInfo2.addParam("first_publish_status", "");
		worksInfo2.addParam("publish_address", "贵阳");
		worksInfo2.addParam("author", "lisi");
		worksInfo2.addParam("author_sing", "zhangsan");
		worksInfo2.addParam("access_rights_type", "承受");
		worksInfo2.addParam("access_rights_type_explain", "");
		worksInfo2.addParam("rights_affiliation_type", "个人作品");
		worksInfo2.addParam("rights_affiliation_type_explain", "");
		worksInfo2.addParam("own_right_status", "全部");
		worksInfo2.addParam("own_right_status_explain", "");
		worksInfo2.addParam("owner_name", "lisi");
		worksInfo2.addParam("owner_sign", "lisi");
		worksInfo2.addParam("owner_type", "自然人");
		worksInfo2.addParam("owner_id_type", "居民身份证");
		worksInfo2.addParam("owner_id_number", "420000000000000X");
		worksInfo2.addParam("owner_nationality", "china");
		worksInfo2.addParam("owner_province", "guizhou");

		worksInfo2.addParam("owner_city", "guiyan");
		worksInfo2.addParam("electronic_medium", "10");
		worksInfo2.addParam("created_time", startTime);
		worksInfo2.addParam("updated_time", startTime);

		Transaction worksInfoTran2 = new Transaction();
		worksInfoTran2.setRegulationType(RegulationType.REGULATION_ORIGINAL_SAVE);
		worksInfo2.setTransaction(worksInfoTran2);
		dataList.add(worksInfo2);

		// works_sample
		DataBuilder worksSample = new DataBuilder("tdbc", "works_sample", "n", outerSerial);
		worksSample.addParam("user_id", "zhangsan");
		worksSample.addParam("works_id", reqNo);
		worksSample.addParam("works_name", "大话西游");
		worksSample.addParam("works_url", "www.baidu.com");
		worksSample.addParam("created_time", startTime);
		worksSample.addParam("update_time", startTime);
		worksSample.addParam("approved_time", "2017-09-11");

		Transaction worksSampleTran = new Transaction();
		worksSampleTran.setRegulationType(RegulationType.REGULATION_ORIGINAL_SAVE);
		worksSample.setTransaction(worksSampleTran);
		dataList.add(worksSample);

		// copyright_audit
//		DataBuilder copyright_audit = new DataBuilder("tdbc", "copyright_audit", "n", outerSerial);
//		copyright_audit.addParam("req_no", reqNo);// 唯一标识一次请求
//		copyright_audit.addParam("user_id", "zhangsan");
//		copyright_audit.addParam("works_id", "w123456789");
//		copyright_audit.addParam("works_name", "大话西游");
//		copyright_audit.addParam("author", "zhangsan");
//		copyright_audit.addParam("author_sing", "zhangsan");
//		copyright_audit.addParam("approved_status", "2");// 默认共识成功
//		copyright_audit.addParam("created_time", startTime);
//		copyright_audit.addParam("update_time", startTime);
//
//		Transaction copyrightAuditTran = new Transaction();
//		copyrightAuditTran.setRegulationType(RegulationType.REGULATION_ORIGINAL_SAVE);
//		copyright_audit.setTransaction(copyrightAuditTran);
//		dataList.add(copyright_audit);

		// 发送交易
		try {
			JBCCResult r = jbccClient.sendAndCountReturnForIPBlockChain(dataList);
			printJbccResult(r);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 检查是否有重复交易
	 * 
	 * @param req_no
	 * @return duplicateTran true:重复交易；false:不是重复交易
	 * @throws Exception
	 * @author liwh
	 */
	private void checkReqNo(String req_no) throws Exception {
		Map<String, Object> conditionMap = new HashMap<String, Object>();
		conditionMap.put("req_no", req_no);
		/*
		 * duplicateTran取值 true:重复交易 ；false:非重复交易
		 */
		boolean duplicateTran = jbccClient.checkReqNo("tdbc", "account_evidence", conditionMap);
		System.out.println("[checkReqno " + req_no + "],duplicateTran:" + duplicateTran);
	}

	/**
	 * 创建唯一索引
	 * 
	 * @author liwh
	 */
	public static JBCCResult createUniqueIndex() throws Exception {
		List<String> columnList = new ArrayList<String>();
		columnList.add("account_no");
		JBCCResult r = jbccClient.createUniqueIndex("tdbc", "account_lock", columnList);

		jbccClient.close();
		return r;
	}

	/**
	 * 创建索引
	 * 
	 * @author liwh
	 */
	public static JBCCResult createIndex() throws Exception {
		List<String> columnList = new ArrayList<String>();
		columnList.add("account_no");
		JBCCResult r = jbccClient.createIndex("tdbc", "account_lock", columnList);
		jbccClient.close();
		return r;
	}

	
	@Test
	public void selectBlockByCondition() throws Exception {
		Map<String, Object> conditionMap = new HashMap<String, Object>();
		conditionMap.put("id", "1");
//		conditionMap.put("hash", "b0d6d71f7ab402d473b498096564538233d3b432fbda4b9bdbe26eb7e7c67eee");
		QueryResult queryResult = jbccClient.selectBlockByCondition("tdbc",conditionMap);
		System.out.println("------------result:" + GsonUtil.getGson().toJson(queryResult));
		if (queryResult != null) {
			JBCCResult result = queryResult.getJbccResult();
			String errorCode = result.getErrorCode();
			if ("0".equals(errorCode)) {// 交易执行成功
				List<Map<String, Object>> resultlist = queryResult.getResultlist();
				if (!resultlist.isEmpty()) {
					Map<String, Object> map = resultlist.get(0);
					System.out.println("result:" + GsonUtil.getGson().toJson(map));
				} else {
					System.out.println("resultlist is null");
				}
			}
		}

	}
	
	
	@Test
	public void selectBlockByPage() throws Exception {
		Map<String, Object> conditionMap = new HashMap<String, Object>();
		conditionMap.put("beginDate", "2017-09-19");
		conditionMap.put("endDate", "2017-10-30");
		conditionMap.put("startPos", 0);//偏移量，从0开始，可以是每页的起始数
		conditionMap.put("endPos", 10); //每次查询最大返回条数
		QueryResult queryResult = jbccClient.selectBlockListByPage("tdbc",conditionMap);
		System.out.println("------------result:" + GsonUtil.getGson().toJson(queryResult));
		if (queryResult != null) {
			JBCCResult result = queryResult.getJbccResult();
			String errorCode = result.getErrorCode();
			if ("0".equals(errorCode)) {// 交易执行成功
				List<Map<String, Object>> resultlist = queryResult.getResultlist();
				if (!resultlist.isEmpty()) {
					Map<String, Object> map = resultlist.get(0);
					System.out.println("result:" + GsonUtil.getGson().toJson(map));
				} else {
					System.out.println("resultlist is null");
				}
			}
		}

	}
	
	
	@Test
	public void selectBlockWorksInfo() throws Exception {
		Map<String, Object> conditionMap = new HashMap<String, Object>();
		conditionMap.put("block_id", "1");
		conditionMap.put("works_id", "28a3aeb0dbec4f1cb3056f9d16e23f48");
		QueryResult queryResult = jbccClient.selectBlockWorksInfo("tdbc",conditionMap);
		System.out.println("------------result:" + GsonUtil.getGson().toJson(queryResult));
		if (queryResult != null) {
			JBCCResult result = queryResult.getJbccResult();
			String errorCode = result.getErrorCode();
			if ("0".equals(errorCode)) {// 交易执行成功
				List<Map<String, Object>> resultlist = queryResult.getResultlist();
				if (!resultlist.isEmpty()) {
					Map<String, Object> map = resultlist.get(0);
					System.out.println("result:" + GsonUtil.getGson().toJson(map));
				} else {
					System.out.println("resultlist is null");
				}
			}
		}

	}
	
	
	/**
	 * 根据起{@startDate}止{@endDate}时间查询当前最新的{@selectNum}区块
	 * @throws Exception
	 */
	
	@Test
	public void selectBlockListByDate() throws Exception {
		Map<String, Object> conditionMap = new HashMap<String, Object>();
		conditionMap.put("beginDate", "2017-09-23");
		conditionMap.put("endDate", "2017-09-25");
		conditionMap.put("selectNum", 3);
		
		QueryResult queryResult = jbccClient.selectBlockListByDate("tdbc",conditionMap);
		System.out.println("------------result:" + GsonUtil.getGson().toJson(queryResult));
		if (queryResult != null) {
			JBCCResult result = queryResult.getJbccResult();
			String errorCode = result.getErrorCode();
			if ("0".equals(errorCode)) {// 交易执行成功
				List<Map<String, Object>> resultlist = queryResult.getResultlist();
				if (!resultlist.isEmpty()) {
					Map<String, Object> map = resultlist.get(0);
					System.out.println("result:" + GsonUtil.getGson().toJson(map));
				} else {
					System.out.println("resultlist is null");
				}
			}
		}

	}
	
	
	@Test
	public void countBlockListByDate() throws Exception {
		Map<String, Object> conditionMap = new HashMap<String, Object>();
		conditionMap.put("beginDate", "2017-09-22");
		conditionMap.put("endDate", "2017-09-28");
		
		QueryResult queryResult = jbccClient.countBlockListByDate("tdbc",conditionMap);
		System.out.println("------------result:" + GsonUtil.getGson().toJson(queryResult));
		if (queryResult != null) {
			JBCCResult result = queryResult.getJbccResult();
			String errorCode = result.getErrorCode();
			if ("0".equals(errorCode)) {// 交易执行成功
				List<Map<String, Object>> resultlist = queryResult.getResultlist();
				if (!resultlist.isEmpty()) {
					Map<String, Object> map = resultlist.get(0);
					System.out.println("result:" + GsonUtil.getGson().toJson(map));
				} else {
					System.out.println("resultlist is null");
				}
			}
		}

	}


}
