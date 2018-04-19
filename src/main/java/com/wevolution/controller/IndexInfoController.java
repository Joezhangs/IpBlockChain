package com.wevolution.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.wevolution.common.utils.HttpInvoker;
import com.wevolution.common.utils.ResponseUtil;

@Controller
@RequestMapping("indexInfo")
public class IndexInfoController {
	
	@Value("${background.domain}")
	private String domain;
	
	/**
	 * 咨询阅读列表
	* @Title: consultingList 
	* @return 
	*
	 */
	@RequestMapping("/consultingList")
	@ResponseBody
    public String consultingList() {
		Map<String, Object> map = new HashMap<>();
		String list = null;
    	try {
			list = HttpInvoker.getInstance().invoke(domain+"atricleApi/articles.do", true);
		} catch (Exception e) {
			e.printStackTrace();
		}
    	map.put("list", list);
    	map.put("domain", domain);
    	return ResponseUtil.getResponseJson(0, "查询成功", map);
    }
	/**
	 * 咨询阅读详情
	* @Title: consultingDetails 
	* @param id
	* @return 
	*
	 */
	@RequestMapping("/consultingDetails")
	@ResponseBody
	public String consultingDetails(@RequestParam("id")String id) {
		Map<String, Object> map = new HashMap<>();
		String details = null;
		try {
			details = HttpInvoker.getInstance().invoke(domain+"atricleApi/detail/"+id+".do", true);
		} catch (Exception e) {
			e.printStackTrace();
		}
		map.put("details", details);
		map.put("domain", domain);
		return ResponseUtil.getResponseJson(0, "查询成功", map);
	}
	/**
	 * 经典案例
	* @Title: caseList 
	* @return 
	*
	 */
	@RequestMapping("/caseList")
	@ResponseBody
	public String caseList() {
		Map<String, Object> map = new HashMap<>();
		String list = null;
		try {
			list = HttpInvoker.getInstance().invoke(domain+"caserApi/casers.do", true);
		} catch (Exception e) {
			e.printStackTrace();
		}
		map.put("list", list);
		map.put("domain", domain);
		return ResponseUtil.getResponseJson(0, "查询成功", map);
	}
	/**
	 * 经典案例详情
	* @Title: caseDetails 
	* @return 
	*
	 */
	@RequestMapping("/caseDetails")
	@ResponseBody
	public String caseDetails(@RequestParam("id")String id) {
		Map<String, Object> map = new HashMap<>();
		String details = null;
		try {
			details = HttpInvoker.getInstance().invoke(domain+"caserApi/detail/"+id+".do", true);
		} catch (Exception e) {
			e.printStackTrace();
		}
		map.put("details", details);
		map.put("domain", domain);
		return ResponseUtil.getResponseJson(0, "查询成功", map);
	}
}
