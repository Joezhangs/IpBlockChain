package com.wevolution.service;

import java.util.List;

import com.wevolution.domain.WorksInfo;

public interface WorksInfoService {
	/**
	 * 
	* @Title: worksInfo 
	* @Description: 作品信息登记
	* @param worksInfos
	* @return 
	*
	 */
    String worksInfo(WorksInfo worksInfo);
    /**
     * 
    * @Title: authorAndRight 
    * @Description: 作者及著作权
    * @param worksInfos
    * @return 
    *
     */
    String authorAndRight(List<WorksInfo> worksInfos);
    /**
     * 
    * @Title: selectWorksInfoByWorksId 
    * @Description: 根据作品id查询作品信息 
    * @param worksId
    * @return List<WorksInfo> 
     */
    List<WorksInfo> selectWorksInfoByWorksId(String worksId);
    /**
     * 
    * @Title: selectWorksInfoOneByWorksId 
    * @Description: 仅查询作品信息（不包括作者、著作权人）
    * @param worksId
    * @return 
    *
     */
    WorksInfo selectWorksInfoOneByWorksId(String worksId);
    
    /**
     * 
    * @Title: submitWorksInfo 
    * @Description:  作品信息提交
    * @param worksId
    * @return 
    *
     */
    String submitWorksInfo(String worksId);
}
