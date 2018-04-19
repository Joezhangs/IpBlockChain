package com.wevolution.service;
/**
 * 
* @ClassName: IndexSerivce 
* @author jiangjian
* @date 2017年9月23日 下午12:15:35 
*
 */

import cn.tiandechain.jbcc.bean.QueryResult;

public interface IndexService {
	/**
	 * 区块展示页数据统计
	* @Title: dataStatistics 
	* @return 
	*
	 */
	QueryResult dataStatistics();
	QueryResult blockSizeStatistics();
}
