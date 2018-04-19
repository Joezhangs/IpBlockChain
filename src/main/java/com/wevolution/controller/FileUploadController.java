package com.wevolution.controller;

import java.io.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.activemq.util.ByteArrayInputStream;
import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.wevolution.common.utils.FileUtil;
import com.wevolution.common.utils.StringUtil;
import com.wevolution.conf.Auth;
import com.wevolution.domain.Users;
import com.wevolution.service.UsersService;

import aws.s3.AWSS3Client;

@Controller
@RequestMapping("/file")
public class FileUploadController {
	private static final Logger log = LoggerFactory.getLogger(FileUploadController.class);
	/**
	 * 文件路径
	 */
	@Value("${web.upload-path}")
	private String path;

	@Resource
	private UsersService userservice;
	
	@RequestMapping(value = "/uploadpage", method = RequestMethod.GET)
	public String testPage() {
		return "upload";
	}

	/**
	 * POST /uploadFile -> receive and locally save a file.
	 * 
	 * @param uploadfile
	 *            The uploaded file as Multipart file parameter in the HTTP
	 *            request. The RequestParam name must be the same of the
	 *            attribute "name" in the input tag with type file.
	 * 
	 * @return An http OK status in case of success, an http 4xx status in case
	 *         of errors.
	 */
	@RequestMapping(value = "/uploadFile", method = RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<?> uploadFile(@RequestParam("uploadfile") MultipartFile uploadfile) {
		String filename = null;
		File file = null;
		try {
			// Get the filename and build the local file path
			filename = uploadfile.getOriginalFilename();
			file = new File(path + filename);
			uploadfile.transferTo(file);
			// filepath = Paths.get(directory, filename).toString();
			// Save the file locally
			// BufferedOutputStream stream = new BufferedOutputStream(new
			// FileOutputStream(new File(filepath)));
			// stream.write(uploadfile.getBytes());
			// stream.close();
		} catch (Exception e) {
			System.out.println(e.getMessage());
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		// return the file path
		return new ResponseEntity<>(file.getAbsolutePath(), HttpStatus.OK);

	}

	/**
	 * 
	* @Title: avatar (hard disk)
	* @Description: 头像上传
	* @param request
	* @param file
	* @return
	* @throws IOException 
	*
	 */
	@RequestMapping(value = "/{avatar}", method = RequestMethod.POST)
	@ResponseBody
	@Auth
	public String avatar(HttpServletRequest request, @RequestParam("uploadfile") MultipartFile file,@PathVariable("avatar")String avatar) {
		String fileName = null;
		Users user = (Users) request.getSession().getAttribute("user");
		try {
			if (file.isEmpty()) {
			      return "文件为空";
			}
			if(!"avatar".equals(avatar)&&!"logo".equals(avatar)){
				return "上传异常";
			}
			String originalFilename = file.getOriginalFilename();
			log.info(originalFilename);
			log.info(file.getContentType());
			log.info("" + file.getSize());
			// 获取文件的后缀名
			String suffixName = originalFilename.substring(originalFilename.lastIndexOf("."));
			String avatarPath = path+user.getUserId()+"/"+avatar+"/";//"/用户id/avatar
			fileName = String.valueOf(System.currentTimeMillis())+suffixName;
			final File dest = new File(avatarPath, fileName);
			log.info(dest.getAbsolutePath());
			if (!dest.getParentFile().exists()) {
			      dest.getParentFile().mkdirs();
			 }
			if("avatar".equals(avatar)&&!"logo".equals(avatar)){
				if(user.getUserImageUrl()!=null){
					FileUtil.delete(avatarPath+user.getUserImageUrl().substring(user.getUserImageUrl().lastIndexOf("/")+1));
				}
			}
			if("logo".equals(avatar)){
				if(user.getCompanyLogoUrl()!=null){
					FileUtil.delete(avatarPath+user.getCompanyLogoUrl().substring(user.getCompanyLogoUrl().lastIndexOf("/")+1));
				}
			}
			file.transferTo(dest);
			Users users = new Users();
			users.setUserId(user.getUserId());
			if("avatar".equals(avatar)){
				users.setUserImageUrl("/file/"+user.getUserId()+"/"+avatar+"/"+fileName);
				user.setUserImageUrl("/file/"+user.getUserId()+"/"+avatar+"/"+fileName);
			}else{
				users.setCompanyLogoUrl("/file/"+user.getUserId()+"/"+avatar+"/"+fileName);
				user.setCompanyLogoUrl("/file/"+user.getUserId()+"/"+avatar+"/"+fileName);
			}
			userservice.updateUserById(users);
			request.getSession().setAttribute("user", user);
		} catch (IllegalStateException | IOException e) {
			e.printStackTrace();
		} 
		return "/file/"+user.getUserId()+"/"+avatar+"/"+fileName;
	}
	/**
	 * 
	* @Title: avatar (aws s3)
	* @Description: 头像上传
	* @param request
	* @param file
	* @return
	* @throws IOException 
	*
	 */
	@RequestMapping(value = "/avatarS3/{avatar}", method = RequestMethod.POST)
	@ResponseBody
	@Auth
	public String avatarS3(HttpServletRequest request, @RequestParam("uploadfile") MultipartFile file,@PathVariable("avatar")String avatar) {
		String url = null;
		Users user = (Users) request.getSession().getAttribute("user");
		try {
			if (file.isEmpty()) {
			      return "文件为空";
			}
			if(!"avatar".equals(avatar)&&!"logo".equals(avatar)){
				return "上传异常";
			}
			String originalFilename = file.getOriginalFilename();
			log.info(originalFilename);
			log.info(file.getContentType());
			log.info("" + file.getSize());
			// 获取文件的后缀名
			String suffixName = originalFilename.substring(originalFilename.lastIndexOf("."));
			String avatarPath = "userimage/"+user.getUserId()+"/"+avatar+"/";//"/用户id/avatar
			String fileName = String.valueOf(System.currentTimeMillis())+suffixName;
			url = AWSS3Client.uploadToS3(file.getInputStream(), avatarPath+fileName);
			Users users = new Users();
			users.setUserId(user.getUserId());
			if("avatar".equals(avatar)){
				users.setUserImageUrl(url);
				user.setUserImageUrl(url);
			}else{
				users.setCompanyLogoUrl(url);
				user.setCompanyLogoUrl(url);
			}
			userservice.updateUserById(users);
			request.getSession().setAttribute("user", user);
		} catch (IllegalStateException | IOException e) {
			e.printStackTrace();
		} 
		return url;
	}
	/**
	 * 头像上传（aws s3）
	* @Title: avatarBase64S3 
	* @param request
	* @param image
	* @param avatar
	* @return 
	*
	 */
	@RequestMapping(value = "baseS3/{avatar}", method = RequestMethod.POST)
	@ResponseBody
	@Auth
	public String avatarBase64S3(HttpServletRequest request, @RequestParam("image") String image,@PathVariable("avatar")String avatar) {
		String url = null;
		Users user = (Users) request.getSession().getAttribute("user");
		try {
			String[] split = image.split(",");
			//目录
			String avatarPath = "userimage/"+user.getUserId()+"/"+avatar+"/";//"/用户id/avatar
			//文件名
			String fileName = String.valueOf(System.currentTimeMillis())+"."+split[0].substring(split[0].lastIndexOf("/")+1,split[0].indexOf(";"));
			byte[] bs = Base64.decodeBase64(split[1]);
			// 处理数据
			for (int i = 0; i < bs.length; ++i) {
				if (bs[i] < 0) {
					bs[i] += 256;
				}
			}
			ByteArrayInputStream inputStream = new ByteArrayInputStream(bs);
			url = AWSS3Client.uploadToS3(inputStream, avatarPath+fileName);
			//file.transferTo(dest);
			Users users = new Users();
			users.setUserId(user.getUserId());
			if("avatar".equals(avatar)){
				users.setUserImageUrl(url);
				user.setUserImageUrl(url);
			}else{
				users.setCompanyLogoUrl(url);
				user.setCompanyLogoUrl(url);
			}
			userservice.updateUserById(users);
			request.getSession().setAttribute("user", user);
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} 
		return url;
	}
	/**
	 * 
	* @Title: avatar 
	* @Description: 头像上传
	* @param request
	* @param file
	* @return
	* @throws IOException 
	*
	 */
	@RequestMapping(value = "base/{avatar}", method = RequestMethod.POST)
	@ResponseBody
	@Auth
	public String avatarBase64(HttpServletRequest request, @RequestParam("image") String image,@PathVariable("avatar")String avatar) {
		String fileName = null;
		Users user = (Users) request.getSession().getAttribute("user");
		try {
			String[] split = image.split(",");
			
			String avatarPath = path+user.getUserId()+"/"+avatar+"/";//"/用户id/avatar
			fileName = "avatar"+String.valueOf(System.currentTimeMillis())+"."+split[0].substring(split[0].lastIndexOf("/")+1,split[0].indexOf(";"));
			if(!StringUtil.isEmpty(user.getUserImageUrl())){
				FileUtil.delete(avatarPath+user.getUserImageUrl().substring(user.getUserImageUrl().lastIndexOf("/")+1));
			}
			final File dest = new File(avatarPath, fileName);
			log.info(dest.getAbsolutePath());
			if (!dest.getParentFile().exists()) {
			      dest.getParentFile().mkdirs();
			 }
			byte[] bs = Base64.decodeBase64(split[1]);
			// 处理数据
			for (int i = 0; i < bs.length; ++i) {
				if (bs[i] < 0) {
					bs[i] += 256;
				}
			}
			OutputStream out = new FileOutputStream(avatarPath+fileName);
			out.write(bs);
			out.flush();
			out.close();
			//file.transferTo(dest);
			Users users = new Users();
			users.setUserId(user.getUserId());
			if("avatar".equals(avatar)){
				users.setUserImageUrl("/file/"+user.getUserId()+"/"+avatar+"/"+fileName);
				user.setUserImageUrl("/file/"+user.getUserId()+"/"+avatar+"/"+fileName);
			}else{
				users.setCompanyLogoUrl("/file/"+user.getUserId()+"/"+avatar+"/"+fileName);
				user.setCompanyLogoUrl("/file/"+user.getUserId()+"/"+avatar+"/"+fileName);
			}
			userservice.updateUserById(users);
			request.getSession().setAttribute("user", user);
		} catch (IllegalStateException | IOException e) {
			e.printStackTrace();
		} 
		return "/file/"+user.getUserId()+"/"+avatar+"/"+fileName;
	}
	/**
	 * 
	* @Title: fileDownload 
	* @Description: 文件请求
	* @param userId 用户id
	* @param avatar 文件类型
	* @param name 文件名
	* @param response
	* @return 
	*
	 */
	@RequestMapping(value = "/{userId}/{avatar}/{name:.*}", method = RequestMethod.GET)
	@ResponseBody
	public FileSystemResource fileDownload(@PathVariable("userId")String userId,@PathVariable("avatar")String avatar,@PathVariable("name") String name, HttpServletResponse response) {
		//Users user = (Users) request.getSession().getAttribute("user");
//		String avatarPath = path+user.getUserId()+"/"+avatar+"/";//"/用户id/avatar
		String avatarPath = path+userId+"/"+avatar+"/";//"/用户id/avatar
		final File source = new File(avatarPath, name);
		if (!source.exists()) {
			response.setStatus(HttpStatus.NOT_FOUND.value());
			return null;
		}

		log.info("downloading " + source.getAbsolutePath());
		return new FileSystemResource(source);
	}
}
