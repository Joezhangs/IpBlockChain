package com.wevolution.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.wevolution.domain.Works;

public interface WorksMapper {

	/**
	 * 
	* @Title: registerWorks 
	* @Description: 登记作品
	* @param works
	* @return  int 
	 */
    int insertWorks(Works works);
    /**
     * 
    * @Title: selectWorksByUserId 
    * @Description: 通过用户id查询作品
    * @param param
    * @return  List<Works> 
     */
    List<Works> selectWorksByUserId(Works param);
    
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
    * @Description: 更新作品
    * @param works
    * @return int 
     */
    int updateWorks(Works works);
    /**
     * 
    * @Title: updateWorksProgress 
    * @Description: 更新作品进度
    * @param worksId
    * @param progress
    * @return 
    *
     */
	int updateWorksProgress(@Param("worksId")String worksId, @Param("progress")Integer progress);
	/**
	 * 作品列表
	* @Title: selectWorksList 
	* @param param
	* @return 
	*
	 */
	List<Works> selectWorksList(Works param);
	/**
     * 作品查询（字段）
    * @Title: updateWorksProgress 
    * @param worksId
    * @return 
    *
     */
	Map<String,String> queryWorksById(String worksId);
	/**
	 * 已审核作品列表
	* @Title: selectWorksAndInfo 
	* @param type
	* @return 
	*
	 */
	List<Map<String,String>> selectWorksAndInfo(@Param("type")String type,@Param("area")Integer area);
	/**
	 * 已审核作品信息
	* @Title: selectWorksAndInfoByWorksId 
	* @param worksId
	* @return 
	*
	 */
	Map<String,String> selectWorksAndInfoByWorksId(@Param("worksId")String worksId);
}