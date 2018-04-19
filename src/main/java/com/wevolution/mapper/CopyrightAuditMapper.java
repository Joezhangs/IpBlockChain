package com.wevolution.mapper;

import java.util.List;

import com.wevolution.domain.CopyrightAudit;

public interface CopyrightAuditMapper {
	/**
	 * 
	* @Title: selectByWorksId 
	* @param worksId
	* @return 
	*
	 */
	List<CopyrightAudit>selectByWorksId(String worksId);
	/**
	 * 
	* @Title: insert 
	* @param copyrightAudit
	* @return 
	*
	 */
	int insert(CopyrightAudit copyrightAudit);
	/**
	 * 
	* @Title: updateByworksId 
	* @param copyrightAudit
	* @return 
	*
	 */
	int updateByworksId(CopyrightAudit copyrightAudit);
	/**
	 * 审核列表查询
	* @Title: selectAuditList 
	* @param param
	* @return 
	*
	 */
	List<CopyrightAudit> selectAuditList(CopyrightAudit param);
	/**
	 * 共识失败列表
	* @Title: selectFailAuditList 
	* @return 
	*
	 */
	List<CopyrightAudit> selectFailAuditList();
}