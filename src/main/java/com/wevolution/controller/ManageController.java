
package com.wevolution.controller;

import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.tools.zip.ZipEntry;
import org.apache.tools.zip.ZipOutputStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.github.pagehelper.PageInfo;
import com.wevolution.common.utils.DateUtil;
import com.wevolution.common.utils.FileUtil;
import com.wevolution.common.utils.ResponseUtil;
import com.wevolution.domain.Authentication;
import com.wevolution.domain.CopyrightAudit;
import com.wevolution.domain.IdPhoto;
import com.wevolution.domain.Works;
import com.wevolution.service.AuthenticationService;
import com.wevolution.service.IdPhotoService;
import com.wevolution.service.WorksInfoService;
import com.wevolution.service.WorksSampleService;
import com.wevolution.service.WorksService;
/**
 * ipblockchain-admin
* @ClassName: ManageController 
* @author jiangjian
* @date 2017年9月7日 下午7:11:54 
*
 */
@Controller
@RequestMapping("/manage")
public class ManageController {
	private static Logger logger = LoggerFactory.getLogger(ManageController.class);
	/**
	 * 文件路径
	 */
	@Value("${web.upload-path}")
	private String path;
	
	@Resource
	private AuthenticationService authenticationService;
	
	@Resource
	private IdPhotoService idPhotoService;
	
	@Resource
	private WorksService worksService;
	
	@Resource
	private WorksInfoService worksInfoService;
	
	@Resource
	private WorksSampleService worksSampleService;
	
	/**
	 * 实名认证列表查询
	* @Title: getAuthenticationList 
	* @param pageNum 查询页码
	* @param pageSize 查询条数
	* @return 实名认证列表
	*
	 */
	@RequestMapping(value="/authenticationList/{pageNum}/{pageSize}",method=RequestMethod.GET)
	@ResponseBody
	public String getAuthenticationList(@PathVariable(value="pageNum")Integer pageNum, 
			@PathVariable(value="pageSize")Integer pageSize,HttpServletRequest request){
		Authentication authentication = new Authentication();
		try {
			BeanUtils.populate(authentication, request.getParameterMap());
		} catch (IllegalAccessException | InvocationTargetException e) {
			e.printStackTrace();
		} 
		List<Authentication> list = authenticationService.getCertificationList(pageNum, pageSize, authentication);
		//Map<String,Object> map = new HashMap<>();
		PageInfo<Authentication> pageInfo = new PageInfo<Authentication>(list);  
	    //Page page = (Page) list; 
		return ResponseUtil.getResponseJson(0, "查询成功", pageInfo);
	}
	/**
	 * 证件打包下载
	* @Title: idDownload 
	* @param userId 用户id
	* @param authId 认证id
	*
	 */
	@RequestMapping(value = "/download/{userId}/{authId}", method = RequestMethod.GET)
	@ResponseBody
	public void idDownload(@PathVariable("userId") String userId,@PathVariable("authId") String authId,HttpServletRequest request, HttpServletResponse response) {
		Authentication certification = authenticationService.getCertification(userId);
		//响应头的设置
		response.reset(); 
		response.setCharacterEncoding("utf-8"); 
		response.setContentType("multipart/form-data"); 
		//设置压缩包的名字 //解决不同浏览器压缩包名字含有中文时乱码的问题 
		String zipName = certification.getRealName()+".zip";
		String agent = request.getHeader("USER-AGENT"); 
		try {
			if (agent.contains("MSIE") || agent.contains("Trident")) {
				zipName = java.net.URLEncoder.encode(zipName, "UTF-8");
			} else {
				zipName = new String(zipName.getBytes("UTF-8"), "ISO-8859-1");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		response.setHeader("Content-Disposition", "attachment;fileName=\"" + zipName + "\""); 
		//设置压缩流：直接写入response，实现边压缩边下载 
		ZipOutputStream zipos = null; 
		try {
			zipos = new ZipOutputStream(new BufferedOutputStream(response.getOutputStream()));
			zipos.setMethod(ZipOutputStream.DEFLATED); // 设置压缩方法
		} catch (Exception e) {
			e.printStackTrace();
		}
		List<IdPhoto> list = idPhotoService.selectByAuthId(authId);
		//循环将文件写入压缩流 
		DataOutputStream os = null; 
		for (IdPhoto idPhoto : list) {
			try {
				// 添加ZipEntry，并ZipEntry中写入文件流 
				zipos.putNextEntry(new ZipEntry(idPhoto.getIdImageUrl().substring(idPhoto.getIdImageUrl().lastIndexOf("/"))));
				os = new DataOutputStream(zipos);
				InputStream is = FileUtil.getStreamFromUrl(idPhoto.getIdImageUrl());
				byte[] b = new byte[100];
				int length = 0;
				while ((length = is.read(b)) != -1) {
					os.write(b, 0, length);
				}
				is.close();
				zipos.closeEntry();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		try {
			os.flush();
			os.close();
			if(zipos!=null){
				zipos.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	/**
	 * 实名认证审核
	* @Title: auditCertification 
	* @param userId 用户id
	* @param authId 认证id
	* @param identityStatus 认证状态（1：未认证；2：认证中；3：认证成功；4：认证失败）
	* @param [description] 审核描述（失败原因）
	* @return 0:更新成功
	*
	 */
	@RequestMapping(value="/identify",method=RequestMethod.POST)
	@ResponseBody
	public String auditCertification(@RequestParam("userId")String userId,
			@RequestParam("authId")String authId,
			@RequestParam("status")String identityStatus,
			@RequestParam(value="description",required=false)String description){
		if (!"3".equals(identityStatus) && !"4".equals(identityStatus)) {
			return ResponseUtil.getResponseJson(204, "状态参数有误", null);
		}
		Authentication authentication = new Authentication();
		authentication.setUserId(userId);
		authentication.setAuthId(authId);
		authentication.setApprovedDescription(description);
		authentication.setIdentityStatus((byte)Integer.parseInt(identityStatus));
		String ret = authenticationService.auditCertification(authentication);
		if("0".equals(ret))
			return ResponseUtil.getResponseJson(0, "认证状态更新成功", null);
		return ResponseUtil.getResponseJson(204, "认证状态更新失败", ret);
	}
	/**
	 * 作品列表
	* @Title: worksList 
	* @param pageNum
	* @param pageSize
	* @param param
	* @return 
	*
	 *//*
	@RequestMapping("/worksList")
	@ResponseBody
	public String worksList(@RequestParam(value="pageNum",defaultValue="1")Integer pageNum, 
			@RequestParam(value="pageSize",defaultValue="10")Integer pageSize,HttpServletRequest request){
		Works works = new Works();
		try {
			BeanUtils.populate(works, request.getParameterMap());
		} catch (IllegalAccessException | InvocationTargetException e) {
			e.printStackTrace();
		} 
		List<Works> list = worksService.getWorksList(pageNum, pageSize, works);
		//Map<String,Object> map = new HashMap<>();
		PageInfo<Works> pageInfo = new PageInfo<>(list);  
	    //Page page = (Page) list; 
		return ResponseUtil.getResponseJson(0, "查询成功", pageInfo);
	}
	*//**
	 * 作品信息
	* @Title: worksInfoList 
	* @param worksId
	* @return 
	*
	 *//*
	@RequestMapping("/worksInfo")
	@ResponseBody
	public String worksInfoList(@RequestParam("worksId")String worksId){
		List<WorksInfo> list = worksInfoService.selectWorksInfoByWorksId(worksId);
		return ResponseUtil.getResponseJson(0, "查询成功", list);
	}
	*//**
	 * 作品样品查询
	* @Title: worksInfoSample 
	* @param worksId
	* @return 
	*
	 *//*
	@RequestMapping("/worksInfoSample")
	@ResponseBody
	public String worksInfoSample(@RequestParam("worksId")String worksId){
		WorksSample sample = worksSampleService.selectWorksSampleById(worksId);
		return ResponseUtil.getResponseJson(0, "查询成功", sample);
	}*/
	/**
	 * 审核作品
	* @Title: auditWorks 
	* @param worksId 作品id
	* @param approvedStatus 作品状态  1：录入；2：共识成功；3：共识失败；4：共识超时；5：审核成功；6：审核失败
	* @param blockId 区块id
	* @param [approvedDescription] 失败原因 （扩展字段）
	* @return 
	*
	 */
	@RequestMapping(value="/auditWorks",method=RequestMethod.POST)
	@ResponseBody
	public String auditWorks(@RequestParam("worksId")String worksId,
			@RequestParam("approvedStatus")String approvedStatus,
			@RequestParam(value="blockId",required=false)Integer blockId,
			@RequestParam(value="approvedDescription",required=false)String approvedDescription){
		if(!"2".equals(approvedStatus)&&!"3".equals(approvedStatus)&&!"4".equals(approvedStatus)&&!"5".equals(approvedStatus)&&!"6".equals(approvedStatus)){
			return ResponseUtil.getResponseJson(204, "状态参数有误", null);
		}
		Works works = new Works();
		works.setWorksId(worksId);
		works.setApprovedStatus((byte)Integer.parseInt(approvedStatus));
		works.setApprovedDescription(approvedDescription);
		//audit
		CopyrightAudit audit = new CopyrightAudit();
		audit.setWorksId(worksId);
		//audit.setBlockId(blockId);
		audit.setApprovedDescription(approvedDescription);
		audit.setApprovedStatus((byte)Integer.parseInt(approvedStatus));
		audit.setUpdateTime(DateUtil.getCurrentDateString(DateUtil.DATETIMEPATTERN24H));
		String ret = worksService.approvedWorks(works,audit);
		if("0".equals(ret))
			return ResponseUtil.getResponseJson(0, "更新作品状态成功", null);
		return ResponseUtil.getResponseJson(204, "更新作品状态失败", ret);
	}
	
	/**
	 * 审核列表
	* @Title: getAudit 
	* @param pageNum
	* @param pageSize
	* @param request
	* @return 
	*
	 */
	@RequestMapping("/audit/{pageNum}/{pageSize}")
	@ResponseBody
	public String getAudit(@PathVariable(value="pageNum")Integer pageNum, 
			@PathVariable(value="pageSize")Integer pageSize,HttpServletRequest request){
		CopyrightAudit copyrightAudit = new CopyrightAudit();
		try {
			BeanUtils.populate(copyrightAudit, request.getParameterMap());
		} catch (IllegalAccessException | InvocationTargetException e) {
			e.printStackTrace();
		} 
		List<CopyrightAudit> list = worksService.getAudit(pageNum, pageSize, copyrightAudit);
		//Map<String,Object> map = new HashMap<>();
		PageInfo<CopyrightAudit> pageInfo = new PageInfo<CopyrightAudit>(list);
	    //Page page = (Page) list; 
		return ResponseUtil.getResponseJson(0, "查询成功", pageInfo);
	}
	/**
	 * 审核信息查询
	* @Title: getAuditStatus 
	* @param worksId
	* @return 
	*
	 */
	@RequestMapping("/auditStatus/{worksId}")
	@ResponseBody
	public String getAuditStatus(@PathVariable(value="worksId")String worksId){
		CopyrightAudit copyrightAudit = worksService.getAuditStatus(worksId);
		if(copyrightAudit==null){
			logger.info("查询审核状态异常");
			return ResponseUtil.getResponseJson(204, "查询异常", null);
		}
		return ResponseUtil.getResponseJson(0, "查询成功", copyrightAudit);
	}

}
