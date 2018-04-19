package com.wevolution.service;

import java.util.List;
import java.util.Map;

import com.wevolution.domain.CopyrightAudit;
import com.wevolution.domain.Works;

import cn.tiandechain.jbcc.bean.QueryResult;
import cn.tiandechain.jbcc.message.JBCCResult;

public interface WorksService {
	/**
	 * 
	* @Title: registerWorks 
	* @Description: 登记作品
	* @param works
	* @return  int 
	 */
	String registerWorks(Works works);
    /**
     * 
    * @Title: selectWorksByUserId 
    * @Description: 通过用户id查询作品
    * @param userId
    * @return  List<Works> 
     */
    List<Works> selectWorksByUserId(Integer pageNum, Integer pageSize, Works param);
    
    /**
     * 
    * @Title: selectWorksById 
    * @Description: 作品查询
    * @param workId
    * @return  Works 
     */
    Works selectWorksById(String worksId);
    
    /**
     * 
    * @Title: updateWorks 
    * @Description: 审核
    * @param works
    * @return int 
     */
    String approvedWorks(Works works,CopyrightAudit audit);
    /**
     * 作品列表
    * @Title: getWorksList 
    * @param pageNum
    * @param pageSize
    * @param param
    * @return 
    *
     */
	List<Works> getWorksList(Integer pageNum, Integer pageSize, Works param);
	/**
	 * 版权信息提交区块链
	* @Title: sendMsg 
	* @param worksId
	* @return 
	*
	 */
	JBCCResult sendMsg(String worksId);
	/**
	 * 区块信息查询
	* @Title: selectBlock 
	* @param id
	* @param hash
	* @param begindate
	* @param endDate
	* @param pageNum
	* @param pageSize
	* @return 
	*
	 */
	QueryResult selectBlock(String id,String hash,String begindate,String endDate,Integer pageNum,Integer pageSize);
	
	/**
	 * 审核表查询
	* @Title: getAudit 
	* @param pageNum
	* @param pageSize
	* @param param
	* @return 
	*
	 */
	List<CopyrightAudit> getAudit(Integer pageNum, Integer pageSize, CopyrightAudit param);
	/**
	 * 作品审核信息
	* @Title: getAuditStatus 
	* @param pageNum
	* @param pageSize
	* @param param
	* @return 
	*
	 */
	CopyrightAudit getAuditStatus(String worksId);
	
	/**
	 * 作品登记详情
	* @Title: queryWorksRegister 
	* @param worksId 作品id
	* @return 
	*
	 */
	Map<String,Object> queryWorksRegister(String worksId);
	/**
	 * 重新共识
	* @Title: reSendMsg 
	* @param worksIds
	* @return 
	*
	 */
	List<Map<String,Object>> reSendMsg(String[] worksIds);
	
	/**
	 * 已审核作品列表
	* @Title: getWorksAndInfo 
	* @param pageNum
	* @param pageSize
	* @param type
	* @return 
	*
	 */
	List<Map<String,String>> getWorksAndInfo(Integer pageNum, Integer pageSize,String type,Integer area);
	/**
	 * 已审核作品信息
	* @Title: getWorksAndInfoByWorksId 
	* @param pageNum
	* @param pageSize
	* @param type
	* @return 
	*
	 */
	Map<String,String> getWorksAndInfoByWorksId(String worksId);
}
