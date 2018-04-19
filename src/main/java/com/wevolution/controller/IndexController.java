package com.wevolution.controller;

import java.io.File;
import java.io.FileInputStream;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import com.github.pagehelper.PageInfo;
import com.wevolution.domain.Dictionary;
import com.wevolution.service.WorksService;
import com.wevolution.service.impl.DictInit;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
public class IndexController {

	@Value("${background.domain}")
	private String domain;
	
	@Autowired
	private WorksService worksService;

	@RequestMapping("/")
	public String index() {
		return "company";
	}

	@RequestMapping("/protocol")
	public String protocol() {
		return "protocol";
	}

	@RequestMapping("/affirm")
	public String affirm() {
		return "affirm";
	}

	@GetMapping("/copyright")
	public String copyright(@RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
			@RequestParam(value = "type", required = false) String type,@RequestParam(value = "area", required = false) Integer area,
			@RequestParam(value = "pageSize", defaultValue = "12") Integer pageSize, ModelMap map) {
		List<Dictionary> worksType = DictInit.dictCoedMap.get("works_type");
		map.put("worksType", worksType);
		List<Map<String,String>> list = worksService.getWorksAndInfo(pageNum, pageSize, type,area);
		PageInfo<Map<String, String>> pageInfo = new PageInfo<>(list);  
		map.put("list", pageInfo);
		map.put("type", type);
		map.put("area", area);
		return "copyright_show";
	}
	@GetMapping("/copyright/{worksId}")
	public String details(@PathVariable("worksId")String worksId, ModelMap map) {
		Map<String, String> works = worksService.getWorksAndInfoByWorksId(worksId);
		if(works == null){
			return "404";
		}
		String status = works.get("own_right_status");
		String[] ownRightStatus = status.split(",");
		Map<String, Object> worksRegister = worksService.queryWorksRegister(worksId);
		map.put("info", worksRegister);
		map.put("works", works);
		map.put("status", ownRightStatus);
		map.put("domain", domain);
		return "copyright_info";
	}

	@RequestMapping("/case")
	public String casePgae() {
		return "case";
	}

	@RequestMapping("/consulting")
	public String consulting() {
		return "consulting";
	}

	@RequestMapping("/consulting/info")
	public String consultingInfo() {
		return "consulting_info";
	}

	@RequestMapping("/case/info")
	public String caseInfo() {
		return "case_info";
	}

	@RequestMapping("/home")
	public String home() {
		return "home";
	}

	@RequestMapping("/whitebook")
	public String whiteBook() {
		return "whitebook";
	}
	/**
	 * 预览pdf文件
	 * @param fileName
	 */
	@RequestMapping(value = "/IpBlockChain", method = RequestMethod.GET)
	public void pdfStreamHandler(String fileName,HttpServletRequest request,HttpServletResponse response) {

//		File file = new File("D:/workspace/"+fileName);
		File file = new File("/home/ec2-user/wevolution1/pdf/"+fileName);
		if (file.exists()){
			byte[] data = null;
			try {
				FileInputStream input = new FileInputStream(file);
				data = new byte[input.available()];
				input.read(data);
				response.getOutputStream().write(data);
				input.close();
			} catch (Exception e) {
				Logger.getLogger("pdf文件处理异常：" + e.getMessage());
			}

		}else{
			return;
		}
	}

	@RequestMapping("/company_en")
	public String company_en() {
		return "company_en";
	}

	@RequestMapping("/home_en")
	public String home_en() {
		return "home_en";
	}

	@RequestMapping("/affirm_en")
	public String affirm_en() {
		return "affirm_en";
	}

	@GetMapping("/copyright_show_en")
	public String copyright_en(@RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
							@RequestParam(value = "type", required = false) String type,@RequestParam(value = "area", required = false) Integer area,
							@RequestParam(value = "pageSize", defaultValue = "12") Integer pageSize, ModelMap map) {
		List<Dictionary> worksType = DictInit.dictCoedMap.get("works_type");
		map.put("worksType", worksType);
		List<Map<String,String>> list = worksService.getWorksAndInfo(pageNum, pageSize, type,area);
		PageInfo<Map<String, String>> pageInfo = new PageInfo<>(list);
		map.put("list", pageInfo);
		map.put("type", type);
		map.put("area", area);
		return "copyright_show_en";
	}

	@RequestMapping("/consulting_en")
	public String consulting_en() {
		return "consulting_en";
	}

	@RequestMapping("/case_en")
	public String casePgae_en() {
		return "case_en";
	}

	@RequestMapping("/protocol_en")
	public String protocol_en() {
		return "protocol_en";
	}

	@GetMapping("/copyright_en/{worksId}")
	public String details_en(@PathVariable("worksId")String worksId, ModelMap map) {
		Map<String, String> works = worksService.getWorksAndInfoByWorksId(worksId);
		if(works == null){
			return "404";
		}
		String status = works.get("own_right_status");
		String[] ownRightStatus = status.split(",");
		Map<String, Object> worksRegister = worksService.queryWorksRegister(worksId);
		map.put("info", worksRegister);
		map.put("works", works);
		map.put("status", ownRightStatus);
		map.put("domain", domain);
		return "copyright_info_en";
	}

	@RequestMapping("/consulting_en/info")
	public String consultingInfo_en() {
		return "consulting_info_en";
	}

	@RequestMapping("/case_en/info")
	public String caseInfo_en() {
		return "case_info_en";
	}
}
