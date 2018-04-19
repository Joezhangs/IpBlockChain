package com.wevolution.service;

import com.wevolution.domain.WorksSample;

public interface WorksSampleService {
	/**
	 * 
	* @Title: registerWorks 
	* @Description: 登记作品样品
	* @param worksSample
	* @return 
	*
	 */
	String registerWorks(WorksSample worksSample,Integer Progress);
    
    /**
     * 
    * @Title: selectWorksSampleById 
    * @Description: 样品查询
    * @param workId
    * @return  Works 
     */
    WorksSample selectWorksSampleById(String worksId);
    
}
