package com.wevolution.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.wevolution.common.utils.ResponseUtil;
import com.wevolution.common.utils.StringUtil;
import com.wevolution.conf.Auth;
import com.wevolution.service.IndexService;
import com.wevolution.service.UsersService;
import com.wevolution.service.WorksService;

import cn.tiandechain.jbcc.bean.QueryResult;
import cn.tiandechain.jbcc.message.JBCCResult;

@Controller
@RequestMapping("/chain")
public class BlockchainController {
	@Resource
	private WorksService worksService;
	
	@Resource
	private UsersService usersService;
	
	@Resource
	private IndexService indexService;
	
	@Value("${background.domain}")
	private String domain;

	@RequestMapping(value = "/search", method = RequestMethod.GET)
	@Auth
	public String searchChain() {
		return "blockchain";
	}

	@RequestMapping(value = "/search_en", method = RequestMethod.GET)
	@Auth
	public String searchChain_en() {
		return "blockchain_en";
	}
	@Auth
	@RequestMapping("/statistics")
    public String statistics(ModelMap map) {
		QueryResult queryResult = indexService.dataStatistics();
		if (queryResult != null) {
			JBCCResult r = queryResult.getJbccResult();
			String errorCode = r.getErrorCode();
			if ("0".equals(errorCode)) {// 交易执行成功
				map.put("list", queryResult.getResultlist());
				map.put("total", queryResult.getTotalNum());
			}
		}
		List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
		QueryResult result = indexService.blockSizeStatistics();
		if (result != null) {
			JBCCResult r = result.getJbccResult();
			String errorCode = r.getErrorCode();
			if ("0".equals(errorCode)) {// 交易执行成功
				list = result.getResultlist();
			}
		}
		List<Map<String,Object>> signNum = usersService.registrations();
		for (Map<String, Object> numMap : signNum) {
			for (Map<String,Object> sizeMap : list) {
				if(numMap.get("date_day").equals(sizeMap.get("date_day"))){
					numMap.put("block_size", sizeMap.get("blockSize"));
				}
			}
		}
		map.put("signNum", signNum);
    	return "blockchain_show";
    }

	@Auth
	@RequestMapping("/statistics_en")
	public String statistics_en(ModelMap map) {
		QueryResult queryResult = indexService.dataStatistics();
		if (queryResult != null) {
			JBCCResult r = queryResult.getJbccResult();
			String errorCode = r.getErrorCode();
			if ("0".equals(errorCode)) {// 交易执行成功
				map.put("list", queryResult.getResultlist());
				map.put("total", queryResult.getTotalNum());
			}
		}
		List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
		QueryResult result = indexService.blockSizeStatistics();
		if (result != null) {
			JBCCResult r = result.getJbccResult();
			String errorCode = r.getErrorCode();
			if ("0".equals(errorCode)) {// 交易执行成功
				list = result.getResultlist();
			}
		}
		List<Map<String,Object>> signNum = usersService.registrations();
		for (Map<String, Object> numMap : signNum) {
			for (Map<String,Object> sizeMap : list) {
				if(numMap.get("date_day").equals(sizeMap.get("date_day"))){
					numMap.put("block_size", sizeMap.get("blockSize"));
				}
			}
		}
		map.put("signNum", signNum);
		return "blockchain_show_en";
	}
	/**
	 * 登记详情
	 * 
	 * @Title: certificateInfo
	 * @return
	 *
	 */
	@RequestMapping(value = "/certificate/{worksId}", method = RequestMethod.GET)
	@Auth
	public String certificateInfo(ModelMap map, @PathVariable("worksId")String worksId) {
		try {
			Map<String, Object> worksRegister = worksService.queryWorksRegister(worksId);
			map.put("info", worksRegister);
			map.put("domain", domain);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "certificate_info";
	}

	/**
	 * 区块搜索
	 * 
	 * @Title: blockList
	 * @param id
	 * @param hash
	 * @param begindate
	 * @param endDate
	 * @param pageNum
	 * @param pageSize
	 * @return
	 *
	 */
	@RequestMapping(value = "/blockList", method = RequestMethod.GET)
	@ResponseBody
	public String blockList(@RequestParam(value = "id", required = false) String id,
			@RequestParam(value = "hash", required = false) String hash,
			@RequestParam(value = "begindate", required = false) String begindate,
			@RequestParam(value = "endDate", required = false) String endDate,
			@RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
			@RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize) {
		QueryResult result = worksService.selectBlock(id, hash, begindate, endDate, pageNum, pageSize);
		if (result != null) {
			if ("0".equals(result.getJbccResult().getErrorCode())) {
				Map<String, Object> map = new HashMap<>();
				List<Map<String, Object>> list = result.getResultlist();
				int total = result.getTotalNum();
				if (!StringUtil.isEmpty(id) || !StringUtil.isEmpty(hash)) {
					total = 1;
				}
				map.put("list", list);
				map.put("total", total);
				map.put("pageNum", pageNum);
				return ResponseUtil.getResponseJson(0, "查询成功", map);
			}
		}
		return ResponseUtil.getResponseJson(204, "查询失败", null);
	}

}
