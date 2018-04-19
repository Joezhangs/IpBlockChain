package com.wevolution.controller;

import java.io.BufferedOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.tools.zip.ZipEntry;
import org.apache.tools.zip.ZipOutputStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.github.pagehelper.PageInfo;
import com.wevolution.common.utils.DateUtil;
import com.wevolution.common.utils.FileUtil;
import com.wevolution.common.utils.ResponseUtil;
import com.wevolution.common.utils.StringUtil;
import com.wevolution.conf.Auth;
import com.wevolution.conf.Token;
import com.wevolution.domain.Authentication;
import com.wevolution.domain.IdPhoto;
import com.wevolution.domain.Users;
import com.wevolution.service.AuthenticationService;
import com.wevolution.service.IdPhotoService;
import com.wevolution.service.UsersService;

import aws.s3.AWSS3Client;

/**
 * 
* @ClassName: AuthenticationController 
* @Description: 实名认证 
* @author jiangjian
* @date 2017年8月24日 上午11:34:26 
*
 */
@Controller
@RequestMapping("/certification")
public class AuthenticationController {
	private static final Logger log = LoggerFactory.getLogger(AuthenticationController.class);
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
	private UsersService userservice;
	
	@RequestMapping("/certificateSelect")
	@Auth
	public String certificateSelect(){
		return "authentication_select";
	}
	@RequestMapping("/{authType}")
	@Auth
	@Token(save=true)
	public String certificatePage(HttpServletRequest request,@PathVariable("authType")String authType,ModelMap map){
		Users user = (Users) request.getSession().getAttribute("user");
		if(user.getIdentifyStatus()!=null && (user.getIdentifyStatus()==(byte)2||user.getIdentifyStatus()==(byte)3)){
			return "redirect:/certification/getCertification.html";
		}
		Authentication auth = authenticationService.getCertification(user.getUserId());
		map.put("authId", auth==null?null:auth.getAuthId());
		List<IdPhoto> list = idPhotoService.selectByUserId(user.getUserId());
		map.put("photo1", list.size()>0?list.get(0):null);
		map.put("photo2", list.size()>1?list.get(1):null);
		map.put("photo3", list.size()>2?list.get(2):null);
		return authType;
	}
	/**
	 * 
	* @Title: certificate 
	* @Description: 实名认证提交
	* @param request
	* @param authentication
	* @param photoId 上传证件记录id
	* @return String 
	* @throws
	 */
	@Auth
	@RequestMapping(value="/certificate",method=RequestMethod.POST)
	@ResponseBody
	@Token(remove=true)
	public String certificate(HttpServletRequest request, Authentication authentication,@RequestParam("photoId")String[] photoId){
		//TODO
		Users user = (Users) request.getSession().getAttribute("user");
		Authentication auth = authenticationService.getCertification(user.getUserId());
		if(auth!=null&&auth.getIdentityStatus()==(byte)3){
			return ResponseUtil.getResponseJson(204, "已认证通过，请勿重复认证", null);
		}
		if(auth!=null&&auth.getIdentityStatus()==(byte)4){
			idPhotoService.deleteByUserId(user.getUserId(),(byte)4);
		}
		authentication.setSubmitTime(DateUtil.getCurrentDateString(DateUtil.DATETIMEPATTERN24H));
		
		authentication.setAccountType(user.getVerifyChannel().intValue());//账号类别
		authentication.setApprovedDescription(" ");
		String ret = authenticationService.newCertification(authentication,photoId);
		if("0".equals(ret))
			return ResponseUtil.getResponseJson(0, "认证提交成功", user.getUserId());
		else
			return ResponseUtil.getResponseJson(204, "认证提交失败", null);
	}
	
	/**
	 * 
	* @Title: getCertification 
	* @Description: 实名认证查询
	* @return String 
	 */
	@Auth
	@RequestMapping(value="/getCertification",method=RequestMethod.GET)
	public String getCertification(HttpServletRequest request,ModelMap map){
		HttpSession session = request.getSession();
		Users user = (Users) session.getAttribute("user");
		Authentication authentication = authenticationService.getCertification(user.getUserId());
		String type = authentication!=null && authentication.getIdentifyType() !=null ? authentication.getIdentifyType().toString() : null;
		map.put("authentication", authentication);
		map.put("type", type);
		//认证状态变化，更新session
		if (user.getIdentifyStatus() != null && user.getIdentifyStatus() != (byte) 3 && "3".equals(type)) {
			Users users = userservice.getUserById(user.getUserId());
			session.setAttribute("user", users);
		}
		return "user_auth";
	}

	@Auth
	@RequestMapping(value="/getCertification_en",method=RequestMethod.GET)
	public String getCertification_en(HttpServletRequest request,ModelMap map){
		HttpSession session = request.getSession();
		Users user = (Users) session.getAttribute("user");
		Authentication authentication = authenticationService.getCertification(user.getUserId());
		String type = authentication!=null && authentication.getIdentifyType() !=null ? authentication.getIdentifyType().toString() : null;
		map.put("authentication", authentication);
		map.put("type", type);
		//认证状态变化，更新session
		if (user.getIdentifyStatus() != null && user.getIdentifyStatus() != (byte) 3 && "3".equals(type)) {
			Users users = userservice.getUserById(user.getUserId());
			session.setAttribute("user", users);
		}
		return "user_auth_en";
	}
	/**
	 * 
	 * @Title: getEnterprise 
	 * @Description: 企业查询
	 * @return String 
	 */
	@Auth
	@RequestMapping(value="/getEnterprise",method=RequestMethod.GET)
	public String getEnterprise(HttpServletRequest request,ModelMap map){
		Users user = (Users) request.getSession().getAttribute("user");
		Users users = userservice.getUserById(user.getUserId());
		if(!StringUtil.isEmpty(users.getPhone())){
			users.setPhone(users.getPhone().replaceAll("(\\d{3})\\d{4}(\\d{4})","$1****$2"));
		}
		Authentication authentication = authenticationService.getCertification(user.getUserId());
		map.put("authentication", authentication);
		map.put("user", users);
		return "enterprise_info_en";
	}

	@Auth
	@RequestMapping(value="/getEnterprise_en",method=RequestMethod.GET)
	public String getEnterprise_en(HttpServletRequest request,ModelMap map){
		Users user = (Users) request.getSession().getAttribute("user");
		Users users = userservice.getUserById(user.getUserId());
		if(!StringUtil.isEmpty(users.getPhone())){
			users.setPhone(users.getPhone().replaceAll("(\\d{3})\\d{4}(\\d{4})","$1****$2"));
		}
		Authentication authentication = authenticationService.getCertification(user.getUserId());
		map.put("authentication", authentication);
		map.put("user", users);
		return "enterprise_info_en";
	}
	/**
	 * 
	* @Title: avatarS3 (aws s3)
	* @param request
	* @param file
	* @param photoId
	* @param photoType
	* @return 
	*
	 */
	@RequestMapping(value = "/avatarS3/auth", method = RequestMethod.POST)
	@ResponseBody
	@Auth
	public String avatarS3(HttpServletRequest request, @RequestParam("uploadfile") MultipartFile file,
			@RequestParam(value="photoId",required=false)String photoId,@RequestParam(value="photoType",required=false)byte photoType) {
		Users user = (Users) request.getSession().getAttribute("user");
		Authentication authentication = authenticationService.getCertification(user.getUserId());
		if(authentication!=null&&authentication.getIdentityStatus()==(byte)3){
			return ResponseUtil.getResponseJson(204, "已认证通过，请勿重复认证", null);
		}
		String fileName = null;
		Map<String,String> map = new HashMap<>();
		try {
			if (file.isEmpty()) {
			      return "文件为空";
			}
			String originalFilename = file.getOriginalFilename();
			// 获取文件的后缀名
			String suffixName = originalFilename.substring(originalFilename.lastIndexOf("."));
			String authPath = "useravtar/"+user.getUserId()+"/auth/";//"/用户id/avatar
			fileName = String.valueOf(System.currentTimeMillis())+suffixName;
			String url = AWSS3Client.uploadToS3(file.getInputStream(), authPath+fileName);
			IdPhoto idPhoto = idPhotoService.getPhotoByRecordId(photoId);
			if(idPhoto!=null){
				//未提交的更新
				if(StringUtil.isEmpty(idPhoto.getAuthId())){
					//未更换认证方式
					if((idPhoto.getPhotoType()==(byte)4&&photoType==(byte)4)||(idPhoto.getPhotoType()<(byte)4&&photoType<(byte)4)){
//						FileUtil.delete(authPath+idPhoto.getIdImageUrl());
//						file.transferTo(dest);
						idPhoto.setIdImageUrl(url);
						idPhotoService.updatePhoto(idPhoto);
					}else{
						/*List<IdPhoto> list = idPhotoService.selectByUserId(user.getUserId());
						list.forEach(idPhoto_tem->{
							FileUtil.delete(authPath+idPhoto_tem.getIdImageUrl());
						});*/
						idPhoto.setIdImageUrl(url);
						idPhotoService.deleteByUserId(user.getUserId(),(byte) 1);
						idPhoto = new IdPhoto();
//						file.transferTo(dest);
						idPhoto.setIdImageUrl(url);
						idPhoto.setUserId(user.getUserId());
						idPhoto.setPhotoType(photoType);
						idPhoto.setCreateTime(DateUtil.getCurrentDateString(DateUtil.DATETIMEPATTERN24H));
						idPhotoService.insertPhoto(idPhoto);
					}
				}else{//认证失败的更新
					idPhoto = new IdPhoto();
//					file.transferTo(dest);
					idPhoto.setIdImageUrl(url);
					idPhoto.setUserId(user.getUserId());
					idPhoto.setPhotoType(photoType);
					idPhoto.setCreateTime(DateUtil.getCurrentDateString(DateUtil.DATETIMEPATTERN24H));
					idPhotoService.insertPhoto(idPhoto);
				}
			}else{
				idPhoto = new IdPhoto();
//				file.transferTo(dest);
				idPhoto.setIdImageUrl(url);
				idPhoto.setUserId(user.getUserId());
				idPhoto.setPhotoType(photoType);
				idPhoto.setCreateTime(DateUtil.getCurrentDateString(DateUtil.DATETIMEPATTERN24H));
				idPhotoService.insertPhoto(idPhoto);
			}
			map.put("photoId", idPhoto.getRecordId().toString());
			map.put("fileName", url);
		} catch (IllegalStateException | IOException e) {
			e.printStackTrace();
		} 
		return ResponseUtil.getResponseJson(0, "上传成功", map);
	}
	/**
	 * 
	* @Title: avatar （hard disk）
	* @Description: 证件照
	* @param request
	* @param file
	* @return
	* @throws IOException 
	*
	 */
	@RequestMapping(value = "/auth", method = RequestMethod.POST)
	@ResponseBody
	@Auth
	public String avatar(HttpServletRequest request, @RequestParam("uploadfile") MultipartFile file,
			@RequestParam(value="photoId",required=false)String photoId,@RequestParam(value="photoType",required=false)byte photoType) {
		Users user = (Users) request.getSession().getAttribute("user");
		Authentication authentication = authenticationService.getCertification(user.getUserId());
		if(authentication!=null&&authentication.getIdentityStatus()==(byte)3){
			return ResponseUtil.getResponseJson(204, "已认证通过，请勿重复认证", null);
		}
		String fileName = null;
		Map<String,String> map = new HashMap<>();
		try {
			if (file.isEmpty()) {
			      return "文件为空";
			}
			String originalFilename = file.getOriginalFilename();
			// 获取文件的后缀名
			String suffixName = originalFilename.substring(originalFilename.lastIndexOf("."));
			String authPath = path+user.getUserId()+"/auth/";//"/用户id/avatar
			fileName = String.valueOf(System.currentTimeMillis())+suffixName;
			final File dest = new File(authPath, fileName);
			if (!dest.getParentFile().exists()) {
			      dest.getParentFile().mkdirs();
			 }
			IdPhoto idPhoto = idPhotoService.getPhotoByRecordId(photoId);
			if(idPhoto!=null){
				//未提交的更新
				if(StringUtil.isEmpty(idPhoto.getAuthId())){
					//未更换认证方式
					if((idPhoto.getPhotoType()==(byte)4&&photoType==(byte)4)||(idPhoto.getPhotoType()<(byte)4&&photoType<(byte)4)){
						FileUtil.delete(authPath+idPhoto.getIdImageUrl());
						file.transferTo(dest);
						idPhoto.setIdImageUrl(fileName);
						idPhotoService.updatePhoto(idPhoto);
					}else{
						List<IdPhoto> list = idPhotoService.selectByUserId(user.getUserId());
						list.forEach(idPhoto_tem->{
							FileUtil.delete(authPath+idPhoto_tem.getIdImageUrl());
						});
						idPhoto.setIdImageUrl(fileName);
						idPhotoService.deleteByUserId(user.getUserId(),(byte) 1);
						idPhoto = new IdPhoto();
						file.transferTo(dest);
						idPhoto.setIdImageUrl(fileName);
						idPhoto.setUserId(user.getUserId());
						idPhoto.setPhotoType(photoType);
						idPhoto.setCreateTime(DateUtil.getCurrentDateString(DateUtil.DATETIMEPATTERN24H));
						idPhotoService.insertPhoto(idPhoto);
					}
				}else{//认证失败的更新
					idPhoto = new IdPhoto();
					file.transferTo(dest);
					idPhoto.setIdImageUrl(fileName);
					idPhoto.setUserId(user.getUserId());
					idPhoto.setPhotoType(photoType);
					idPhoto.setCreateTime(DateUtil.getCurrentDateString(DateUtil.DATETIMEPATTERN24H));
					idPhotoService.insertPhoto(idPhoto);
				}
			}else{
				idPhoto = new IdPhoto();
				file.transferTo(dest);
				idPhoto.setIdImageUrl(fileName);
				idPhoto.setUserId(user.getUserId());
				idPhoto.setPhotoType(photoType);
				idPhoto.setCreateTime(DateUtil.getCurrentDateString(DateUtil.DATETIMEPATTERN24H));
				idPhotoService.insertPhoto(idPhoto);
			}
			map.put("photoId", idPhoto.getRecordId().toString());
			map.put("fileName", fileName);
		} catch (IllegalStateException | IOException e) {
			e.printStackTrace();
		} 
		return ResponseUtil.getResponseJson(0, "上传成功", map);
	}

	/**
	 * 
	* @Title: fileDownload （hard disk）
	* @Description: 证件照请求
	* @param request
	* @param name
	* @param response
	* @return 
	*
	 */
	@RequestMapping(value = "/auth/{name:.*}", method = RequestMethod.GET)
	@ResponseBody
	@Auth
	public FileSystemResource fileDownload(HttpServletRequest request,@PathVariable("name") String name, HttpServletResponse response) {
		Users user = (Users) request.getSession().getAttribute("user");
		String authPath = path+user.getUserId()+"/auth/";//"/用户id/auth
		final File source = new File(authPath, name);
		if (!source.exists()) {
			response.setStatus(HttpStatus.NOT_FOUND.value());
			return null;
		}

		log.info("downloading " + source.getAbsolutePath());
		return new FileSystemResource(source);
	}
	/**
	 * 
	* @Title: idDownloadS3 (aws s3)
	* @param userId
	* @param authId
	* @param request
	* @param response 
	*
	 */
	@RequestMapping(value = "/downloadS3/{userId}/{authId}", method = RequestMethod.GET)
	@ResponseBody
	public void idDownloadS3(@PathVariable("userId") String userId,@PathVariable("authId") String authId,HttpServletRequest request, HttpServletResponse response) {
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
				zipos.putNextEntry(new ZipEntry(idPhoto.getPhotoType().toString()));
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
	 * 
	* @Title: idDownload (hard disk)
	* @Description: 证件打包下载
	* @param request
	* @param name
	* @param response
	* @return 
	*
	 */
	@RequestMapping(value = "/download/{userId}/{authId}", method = RequestMethod.GET)
	@ResponseBody
	public void idDownload(@PathVariable("userId") String userId,@PathVariable("authId") String authId,HttpServletRequest request, HttpServletResponse response) {
		//响应头的设置
		response.reset(); 
		response.setCharacterEncoding("utf-8"); 
		response.setContentType("multipart/form-data"); 
		//设置压缩包的名字 //解决不同浏览器压缩包名字含有中文时乱码的问题 
		String zipName = String.valueOf(System.currentTimeMillis())+".zip";
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
		String authPath = path+userId+"/auth/";
		for (IdPhoto idPhoto : list) {
			File file = new File(authPath + idPhoto.getIdImageUrl());
			try {
				// 添加ZipEntry，并ZipEntry中写入文件流 
				zipos.putNextEntry(new ZipEntry(idPhoto.getIdImageUrl()));
				os = new DataOutputStream(zipos);
				InputStream is = new FileInputStream(file);
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
//		final File source = new File(authPath, zipName);
//		if (!source.exists()) {
//			response.setStatus(HttpStatus.NOT_FOUND.value());
//			return null;
//		}
//		List<IdPhoto> list = idPhotoService.selectByAuthId(authId);
//		log.info("downloading " + source.getAbsolutePath());
//		return new FileSystemResource(source);
	}
	@RequestMapping("/authenticationList")
	@ResponseBody
	public String getAuthenticationList(@RequestParam(value="pageNum",defaultValue="1")Integer pageNum, @RequestParam(value="pageSize",defaultValue="10")Integer pageSize,Authentication param){
		List<Authentication> list = authenticationService.getCertificationList(pageNum, pageSize, param);
		//Map<String,Object> map = new HashMap<>();
		PageInfo<Authentication> pageInfo = new PageInfo<Authentication>(list);  
	    //Page page = (Page) list; 
		return ResponseUtil.getResponseJson(0, "查询成功", pageInfo);
	}
	
	/**
	 * 认证状态查询
	* @Title: getCertification 
	* @param request
	* @param map
	* @return 
	*
	 */
	@ResponseBody
	@Auth
	@RequestMapping(value="/getCertificationStatus",method=RequestMethod.GET)
	public String getCertificationStatus(HttpServletRequest request){
		HttpSession session = request.getSession();
		Users user = (Users) session.getAttribute("user");
		Authentication authentication = authenticationService.getCertification(user.getUserId());
		if(authentication != null && authentication.getIdentityStatus()==(byte)3){
			Users users = userservice.getUserById(user.getUserId());
			session.setAttribute("user", users);
		}
		return ResponseUtil.getResponseJson(0, "获取认证状态成功", authentication);
	}
}
