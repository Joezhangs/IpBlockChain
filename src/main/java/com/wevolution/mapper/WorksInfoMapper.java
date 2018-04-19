package com.wevolution.mapper;

import java.util.List;
import java.util.Map;

import com.wevolution.domain.WorksInfo;

public interface WorksInfoMapper {

	/**
	 * 
	* @Title: insertWorksInfo 
	* @Description: 登记作品信息
	* @param worksInfos
	* @return  int 
	 */
    int insertWorksInfo(List<WorksInfo> worksInfos);
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
    * @Title: deleteByWorksId 
    * @Description: 根据作品信息删除
    * @param worksId
    * @return 
    *
     */
    int deleteByWorksId(String worksId);
    /**
     * 
    * @Title: updateByWorksId 
    * @Description: 更新作品信息（不包括作者、著作权人）
    * @param worksInfo
    * @return 
    *
     */
    int updateByWorksId(WorksInfo worksInfo);
    /**
     * 
    * @Title: insertWorksInfoByOne 
    * @Description: 第一次入库
    * @param worksInfo
    * @return 
    *
     */
	int insertWorksInfoByOne(WorksInfo worksInfo);
	/**
     * 作品信息（字段）
    * @Title: queryWorksInfoByWorksId 
    * @param worksId
    * @return 
    *
     */
	List<Map<String,String>> queryWorksInfoByWorksId(String worksId);
}