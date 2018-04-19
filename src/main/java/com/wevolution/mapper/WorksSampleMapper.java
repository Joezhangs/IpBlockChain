package com.wevolution.mapper;

import java.util.Map;

import com.wevolution.domain.WorksSample;

public interface WorksSampleMapper {

	/**
	 * 
	* @Title: insertWorksSample 
	* @Description: 新增作品样品 
	* @param worksSample
	* @return 
	*
	 */
    int insertWorksSample(WorksSample worksSample);
    /**
     * 
    * @Title: selectSampleByWorksId 
    * @Description: 根据作品id查询样品 
    * @param worksId
    * @return 
    *
     */
    WorksSample selectSampleByWorksId(String worksId);
    /**
     * 
    * @Title: updateWorksSample 
    * @Description: 样品更新 
    * @param worksSample
    * @return 
    *
     */
    int updateWorksSample(WorksSample worksSample);
    /**
     * 作品样品（字段）
    * @Title: querySampleByWorksId 
    * @param worksId
    * @return 
    *
     */
    Map<String,String> querySampleByWorksId(String worksId);
}