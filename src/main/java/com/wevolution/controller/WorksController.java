package com.wevolution.controller;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.beanutils.BeanUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
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
import com.wevolution.common.utils.SmsUtil;
import com.wevolution.common.utils.StringUtil;
import com.wevolution.conf.Auth;
import com.wevolution.conf.Token;
import com.wevolution.domain.Dictionary;
import com.wevolution.domain.MessageRecorder;
import com.wevolution.domain.Users;
import com.wevolution.domain.Works;
import com.wevolution.domain.WorksInfo;
import com.wevolution.domain.WorksSample;
import com.wevolution.domain.veiw.WorksInfoVeiw;
import com.wevolution.service.MessageRecorderService;
import com.wevolution.service.WorksInfoService;
import com.wevolution.service.WorksSampleService;
import com.wevolution.service.WorksService;
import com.wevolution.service.impl.DictInit;

import aws.s3.AWSS3Client;
import net.coobird.thumbnailator.Thumbnails;
import net.coobird.thumbnailator.Thumbnails.Builder;
import net.coobird.thumbnailator.geometry.Positions;

@Controller
@RequestMapping("/works")
public class WorksController {
	private static Logger logger = LoggerFactory.getLogger(WorksController.class);
	@Resource
	private WorksService worksService;
	
	@Resource
	private WorksInfoService worksInfoService;
	
	@Resource
	private WorksSampleService worksSampleService;
	
	@Resource
	private MessageRecorderService messageRecorderService;
	
	/**
	 * 文件路径
	 */
	@Value("${web.upload-path}")
	private String path;
	/**
	 * 作品页面
	* @Title: getWorksByUser 
	* @param request
	* @return 
	*
	 */
	@RequestMapping(value="/workses",method=RequestMethod.GET)
	@Auth
	public String getWorksByUser(HttpServletRequest request,HttpServletResponse response){
		response.reset();
		return "works_list";
	}

	@RequestMapping(value="/workses_en",method=RequestMethod.GET)
	@Auth
	public String getWorksByUser_en(HttpServletRequest request,HttpServletResponse response){
		response.reset();
		return "works_list_en";
	}
	/**
	 * 根据用户id查询作品
	* @Title: getWorksListByUser 
	* @param pageNum 当前页
	* @param pageSize 默认查询条数
	* @param request
	* @return 
	*
	 */
	@RequestMapping(value="/worksList",method=RequestMethod.GET)
	@ResponseBody
	public String getWorksListByUser(@RequestParam(value="pageNum",defaultValue="1")Integer pageNum, 
			@RequestParam(value="pageSize",defaultValue="10")Integer pageSize,HttpServletRequest request,@RequestParam("userId")String userId){
		//Users user = (Users) request.getSession().getAttribute("user");
		Works works = new Works();
		try {
			BeanUtils.populate(works, request.getParameterMap());
		} catch (IllegalAccessException | InvocationTargetException e) {
			e.printStackTrace();
		} 
		works.setUserId(userId);
		List<Works> list = worksService.selectWorksByUserId(pageNum, pageSize,works);
		PageInfo<Works> pageInfo = new PageInfo<>(list);  
		return ResponseUtil.getResponseJson(0, "查询成功", pageInfo);
	}
	/**
	 * 
	* @Title: toWorksFirst 
	* @Description: 登记第一步页面（作品文件）
	* @param request
	* @param worksId
	* @param map
	* @return 
	*
	 */
	@RequestMapping("/worksFirst")
	@Auth
	@Token(save=true)
	public String toWorksFirst(HttpServletRequest request,@RequestParam(value="worksId",required=false)String worksId,ModelMap map){
		Users user = (Users) request.getSession().getAttribute("user");
		if(StringUtil.isEmpty(user.getPhone()))
			return "check_phone";
		Works works = new Works();
		if(worksId!=null){
			works = worksService.selectWorksById(worksId);
		}
		map.put("works", works);
		return "works_first";
	}

	@RequestMapping("/worksFirst_en")
	@Auth
	@Token(save=true)
	public String toWorksFirst_en(HttpServletRequest request,@RequestParam(value="worksId",required=false)String worksId,ModelMap map){
		Users user = (Users) request.getSession().getAttribute("user");
		if(StringUtil.isEmpty(user.getPhone()))
			return "check_phone";
		Works works = new Works();
		if(worksId!=null){
			works = worksService.selectWorksById(worksId);
		}
		map.put("works", works);
		return "works_first_en";
	}
	/**
	 * 
	* @Title: toWorksPage 
	* @Description: 根据登记进度跳转页面
	* @param worksId
	* @param map
	* @return 
	*
	 */
	@RequestMapping(value="/worksPage/{worksId}",method=RequestMethod.GET)
	@Auth
	public String toWorksPage(HttpServletRequest request,@PathVariable("worksId")String worksId,@RequestParam(value="progress",required=false)Integer progress,ModelMap map){
		Works works = worksService.selectWorksById(worksId);
		if(progress==null)
			progress = works.getProgress();
		String page = null;
		switch (progress) {
		case 0:
			WorksInfo worksInfo = worksInfoService.selectWorksInfoOneByWorksId(worksId);
			map.put("worksInfo", worksInfo==null?new WorksInfo():worksInfo);
			List<Dictionary> worksType = DictInit.dictCoedMap.get("works_type");
			List<Dictionary> worksNatrue = DictInit.dictCoedMap.get("works_natrue");
			map.put("worksType", worksType);
			map.put("worksNatrue", worksNatrue);
			page = "works_second";
			break;
		case 1:
			List<WorksInfo> worksInfos = worksInfoService.selectWorksInfoByWorksId(worksId);
			map.put("worksInfos", worksInfos);
			List<Dictionary> getway = DictInit.dictCoedMap.get("getway");
			List<Dictionary> belonging = DictInit.dictCoedMap.get("belonging");
			List<Dictionary> possess = DictInit.dictCoedMap.get("possess");
			List<Dictionary> corporationType = DictInit.dictCoedMap.get("corporation_type");
			List<Dictionary> certificateType = DictInit.dictCoedMap.get("certificate_type");
			map.put("getway", getway);
			map.put("belonging", belonging);
			map.put("possess", possess);
			map.put("corporationType", corporationType);
			map.put("certificateType", certificateType);
			page = "works_third";
			break;
		case 2:
			WorksSample worksSample = worksSampleService.selectWorksSampleById(worksId);
			map.put("worksSample", worksSample==null?new WorksSample():worksSample);
			page = "works_fourth";
			break;
//		case 3:
//			page = "works_fifth";
//			break;
		default:
			page = "redirect:/works/workses";
			break;
		}
		map.put("worksId", worksId);
		Users user = (Users) request.getSession().getAttribute("user");
		if(!user.getUserId().equals(works.getUserId()))
			page = "redirect:/works/workses";
		return page;
	}

	@RequestMapping(value="/worksPage_en/{worksId}",method=RequestMethod.GET)
	@Auth
	public String toWorksPage_en(HttpServletRequest request,@PathVariable("worksId")String worksId,@RequestParam(value="progress",required=false)Integer progress,ModelMap map){
		Works works = worksService.selectWorksById(worksId);
		if(progress==null)
			progress = works.getProgress();
		String page = null;
		switch (progress) {
			case 0:
				WorksInfo worksInfo = worksInfoService.selectWorksInfoOneByWorksId(worksId);
				map.put("worksInfo", worksInfo==null?new WorksInfo():worksInfo);
				List<Dictionary> worksType = DictInit.dictCoedMap.get("works_type");
				List<Dictionary> worksNatrue = DictInit.dictCoedMap.get("works_natrue");
				map.put("worksType", worksType);
				map.put("worksNatrue", worksNatrue);
				page = "works_second_en";
				break;
			case 1:
				List<WorksInfo> worksInfos = worksInfoService.selectWorksInfoByWorksId(worksId);
				map.put("worksInfos", worksInfos);
				List<Dictionary> getway = DictInit.dictCoedMap.get("getway");
				List<Dictionary> belonging = DictInit.dictCoedMap.get("belonging");
				List<Dictionary> possess = DictInit.dictCoedMap.get("possess");
				List<Dictionary> corporationType = DictInit.dictCoedMap.get("corporation_type");
				List<Dictionary> certificateType = DictInit.dictCoedMap.get("certificate_type");
				map.put("getway", getway);
				map.put("belonging", belonging);
				map.put("possess", possess);
				map.put("corporationType", corporationType);
				map.put("certificateType", certificateType);
				page = "works_third_en";
				break;
			case 2:
				WorksSample worksSample = worksSampleService.selectWorksSampleById(worksId);
				map.put("worksSample", worksSample==null?new WorksSample():worksSample);
				page = "works_fourth_en";
				break;
//		case 3:
//			page = "works_fifth";
//			break;
			default:
				page = "redirect:/works/workses_en";
				break;
		}
		map.put("worksId", worksId);
		Users user = (Users) request.getSession().getAttribute("user");
		if(!user.getUserId().equals(works.getUserId()))
			page = "redirect:/works/workses_en";
		return page;
	}
	/**
	 * 
	* @Title: registerWorks 
	* @Description: 登记作品（第一步）
	* @param works
	* @param
	* @return String 
	 */
	@RequestMapping(value="/register",method=RequestMethod.POST)
	@Auth
	@Token(remove=true)
	public String registerWorks(Works works){
		works.setProgress(0);
		String ret = worksService.registerWorks(works);
		if(ret!=null){
			return "redirect:/works/worksPage/"+ret;
		}else
			return "redirect:/worksFirst?worksId="+works.getWorksId();
	}

	@RequestMapping(value="/register_en",method=RequestMethod.POST)
	@Auth
	@Token(remove=true)
	public String registerWorks_en(Works works){
		works.setProgress(0);
//		String ret = worksService.registerWorks(works);
		String ret = "b235d8d3037d47658f9246e14e056fe8";
		if(ret!=null){
			return "redirect:/works/worksPage_en/"+ret;
		}else
			return "redirect:/worksFirst_en?worksId="+works.getWorksId();
	}
	/**
	 * 
	* @Title: worksInfo 
	* @Description: 作品信息登记
	* @param worksInfo 作品信息（不包括作者、著作权）
	* @return 
	*
	 */
	@RequestMapping(value="/worksInfo",method=RequestMethod.POST)
	@Auth
	public String worksInfo(WorksInfo worksInfo){
		if("海外".equals(worksInfo.getProvince())){
			worksInfo.setCity("");
			worksInfo.setArea("");
		}
		String ret = worksInfoService.worksInfo(worksInfo);
		if("0".equals(ret))
			return "redirect:/works/worksPage/"+worksInfo.getWorksId();
		return null;
	}

	@RequestMapping(value="/worksInfo_en",method=RequestMethod.POST)
	@Auth
	public String worksInfo_en(WorksInfo worksInfo){
		if("海外".equals(worksInfo.getProvince())){
			worksInfo.setCity("");
			worksInfo.setArea("");
		}
		String ret = worksInfoService.worksInfo(worksInfo);
		if("0".equals(ret))
			return "redirect:/works/worksPage_en/"+worksInfo.getWorksId();
		return null;
	}
	/**
	 * 作品名校验
	* @Title: checkWorksName 
	* @param worksName
	* @param worksId
	* @return 
	*
	 */
	@RequestMapping(value="/checkWorksName",method=RequestMethod.POST)
	@ResponseBody
	@Auth
	public String checkWorksName(String worksName,String worksId){
		Works works = worksService.selectWorksById(worksId);
		if(worksName.equals(works.getWorksName())){
			return ResponseUtil.getResponseJson(0, "作品名校验通过", null);
		}else{
			return ResponseUtil.getResponseJson(204, "作品名校验失败", null);
		}
	}
	/**
	 * 
	* @Title: authorAndRight 
	* @Description: 作者及著作权人信息登记
	* @param worksView
	* @return 
	*
	 */
	@RequestMapping(value="/author",method=RequestMethod.POST)
	@Auth
	public String authorAndRight(WorksInfoVeiw worksView){
		String ret = worksInfoService.authorAndRight(worksView.getWorksInfos());
		if("0".equals(ret))
			return "redirect:/works/worksPage/"+worksView.getWorksInfos().get(0).getWorksId();
		return null;
	}

	@RequestMapping(value="/author_en",method=RequestMethod.POST)
	@Auth
	public String authorAndRight_en(WorksInfoVeiw worksView){
		String ret = worksInfoService.authorAndRight(worksView.getWorksInfos());
		if("0".equals(ret))
			return "redirect:/works/worksPage_en/"+worksView.getWorksInfos().get(0).getWorksId();
		return null;
	}
	/**
	 * 
	 * @Title: worksSampl 
	 * @Description: 样品登记
	 * @param worksSample
	 * @return 
	 *
	 */
	@RequestMapping(value="/sample",method=RequestMethod.POST)
	public String worksSampl(WorksSample worksSample){
		String info =worksSampleService.registerWorks(worksSample,3);
		if("0".equals(info)){
			//Submit data to the blockchain
			//TODO
			new Thread(()->{
				worksService.sendMsg(worksSample.getWorksId());
			}).start();
			return "redirect:/works/worksPage/"+worksSample.getWorksId();
		}
		return null;
	}

	@RequestMapping(value="/sample_en",method=RequestMethod.POST)
	public String worksSampl_en(WorksSample worksSample){
		String info =worksSampleService.registerWorks(worksSample,3);
		if("0".equals(info)){
			//Submit data to the blockchain
			//TODO
			new Thread(()->{
				worksService.sendMsg(worksSample.getWorksId());
			}).start();
			return "redirect:/works/worksPage_en/"+worksSample.getWorksId();
		}
		return null;
	}
	/**
	 * 
	* @Title: worksSampleFile (aws s3)
	* @Description: 样品文件
	* @param request
	* @param file
	* @param worksId
	* @return 
	*
	 */
	@RequestMapping(value = "/worksSampleFileS3", method = RequestMethod.POST)
	@ResponseBody
	@Auth
	public String worksSampleFileS3(HttpServletRequest request, @RequestParam("uploadfile") MultipartFile file,
			@RequestParam(value="worksId")String worksId) {
		Users user = (Users) request.getSession().getAttribute("user");
		String fileName = null;
		Map<String,String> map = new HashMap<>();
		try {
			if (file.isEmpty()) {
			      return ResponseUtil.getResponseJson(204, "文件为空", null);
			}
			String originalFilename = file.getOriginalFilename();
			// 获取文件的后缀名
			String suffixName = originalFilename.substring(originalFilename.lastIndexOf("."));
			String worksPath = "workssample/"+user.getUserId()+"/worksSample/";//"/用户id/worksSample
			fileName = String.valueOf(System.currentTimeMillis())+suffixName;
			if (suffixName.indexOf(".jpg") != -1 || suffixName.indexOf(".jpeg") != -1
					|| suffixName.indexOf(".png") != -1) {
				BufferedImage image = ImageIO.read(file.getInputStream());
				if(StringUtil.isEmpty(image)){
					return ResponseUtil.getResponseJson(204, "图片文件异常,请勿直接修改后缀名", null);
				}
				Builder<BufferedImage> builder = null;

				int imageWidth = image.getWidth();
				int imageHeitht = image.getHeight();
				if ((float) 170 / 210 != (float) imageWidth / imageHeitht) {
					if (imageWidth > imageHeitht) {
						image = Thumbnails.of(file.getInputStream()).height(210).asBufferedImage();
					} else {
						image = Thumbnails.of(file.getInputStream()).width(170).asBufferedImage();
					}
					builder = Thumbnails.of(image).sourceRegion(Positions.CENTER, 170, 210).size(170, 210);
				} else {
					builder = Thumbnails.of(image).size(170, 210);
				}

				ByteArrayOutputStream out = new ByteArrayOutputStream();
				builder.outputFormat("jpg").toOutputStream(out);
				ByteArrayInputStream stream = new ByteArrayInputStream(out.toByteArray());
				AWSS3Client.uploadToS3(stream, worksPath +  fileName + ".jpg");
			}
			String url = AWSS3Client.uploadToS3(file.getInputStream(), worksPath+fileName);
			WorksSample worksSample = worksSampleService.selectWorksSampleById(worksId);
			if(!StringUtil.isEmpty(worksSample)){
				worksSample.setWorksUrl(url);
				worksSampleService.registerWorks(worksSample,2);
			}else{
				WorksSample sampl = new WorksSample();
				sampl.setWorksUrl(url);
				sampl.setUserId(user.getUserId());
				sampl.setWorksId(worksId);
				worksSampleService.registerWorks(sampl,2);
			}
			map.put("fileName", url);
		} catch (IllegalStateException | IOException e) {
			e.printStackTrace();
		} 
		return ResponseUtil.getResponseJson(0, "上传成功", map);
	}
	/**
	 * 
	* @Title: worksSampleFile (hard disk)
	* @Description: 样品文件
	* @param request
	* @param file
	* @param worksId
	* @return 
	*
	 */
	@RequestMapping(value = "/worksSampleFile", method = RequestMethod.POST)
	@ResponseBody
	@Auth
	public String worksSampleFile(HttpServletRequest request, @RequestParam("uploadfile") MultipartFile file,
			@RequestParam(value="worksId")String worksId) {
		Users user = (Users) request.getSession().getAttribute("user");
		String fileName = null;
		Map<String,String> map = new HashMap<>();
		try {
			if (file.isEmpty()) {
			      return ResponseUtil.getResponseJson(204, "文件为空", null);
			}
			String originalFilename = file.getOriginalFilename();
			// 获取文件的后缀名
			String suffixName = originalFilename.substring(originalFilename.lastIndexOf("."));
			String worksPath = path+user.getUserId()+"/worksSample/";//"/用户id/worksSample
			fileName = String.valueOf(System.currentTimeMillis())+suffixName;
			final File dest = new File(worksPath, fileName);
			if (!dest.getParentFile().exists()) {
			      dest.getParentFile().mkdirs();
			 }
			File destThu = new File(worksPath, fileName+".jpg");
			if (!destThu.getParentFile().exists()) {
				destThu.getParentFile().mkdirs();
			 }
			Builder<BufferedImage> builder = null;
			if (suffixName.indexOf(".jpg") != -1 || suffixName.indexOf(".jpeg") != -1 || suffixName.indexOf(".png") != -1) {
				BufferedImage image = ImageIO.read(file.getInputStream());
				if(StringUtil.isEmpty(image)){
					return ResponseUtil.getResponseJson(204, "图片文件异常,请勿直接修改后缀名", null);
				}
				int imageWidth = image.getWidth();
				int imageHeitht = image.getHeight();
				if ((float) 170 / 210 != (float) imageWidth / imageHeitht) {
					if (imageWidth > imageHeitht) {
						image = Thumbnails.of(file.getInputStream()).height(210).asBufferedImage();
					} else {
						image = Thumbnails.of(file.getInputStream()).width(170).asBufferedImage();
					}
					builder = Thumbnails.of(image).sourceRegion(Positions.CENTER, 170, 210).size(170, 210);
				} else {
					builder = Thumbnails.of(image).size(170, 210);
				}
			}
			WorksSample worksSample = worksSampleService.selectWorksSampleById(worksId);
			if(!StringUtil.isEmpty(worksSample)){
				String worksUrl = worksSample.getWorksUrl();
				if(!StringUtil.isEmpty(worksUrl)){
					FileUtil.delete(worksPath+worksSample.getWorksUrl().substring(worksSample.getWorksUrl().lastIndexOf("/")+1));
					FileUtil.delete(worksPath+worksSample.getWorksUrl().substring(worksSample.getWorksUrl().lastIndexOf("/")+1)+".jpg");
				}
				file.transferTo(dest);
				builder.toFile(destThu);
				worksSample.setWorksUrl("/file/"+user.getUserId()+"/worksSample/"+fileName);
				worksSampleService.registerWorks(worksSample,2);
			}else{
				WorksSample sampl = new WorksSample();
				file.transferTo(dest);
				builder.toFile(destThu);
				sampl.setWorksUrl("/file/"+user.getUserId()+"/worksSample/"+fileName);
				sampl.setUserId(user.getUserId());
				sampl.setWorksId(worksId);
				worksSampleService.registerWorks(sampl,2);
			}
			map.put("fileName", "/file/"+user.getUserId()+"/worksSample/"+fileName);
		} catch (IllegalStateException | IOException e) {
			e.printStackTrace();
		} 
		return ResponseUtil.getResponseJson(0, "上传成功", map);
	}
	/**
	 * 
	* @Title: sengSmsCode 
	* @Description: 发送手机验证码
	* @param request //TODO 暂时不用
	* @param phone 手机号
	* @return 
	 */
	@RequestMapping(value="/sendSmsCode",method=RequestMethod.POST)
	@ResponseBody
	public String sendSmsCode(HttpServletRequest request,@RequestParam("phone")String phone){
		HttpSession session = request.getSession();
		Users user = (Users) session.getAttribute("user");
		if(!phone.equals(user.getPhone())){
			return ResponseUtil.getResponseJson(204, "手机号与当前用户绑定手机号不匹配", null);
		}
		String smsCode = StringUtil.createRandomVcode();
		logger.info("phone:"+phone+",smsCode"+smsCode);
		try {
			if(StringUtil.checkMobile(phone)){
				//TODO 开发阶段暂停短信发送 
				MessageRecorder recorder = new MessageRecorder();
				recorder.setSendtime(DateUtil.getCurrentDateString(DateUtil.DATETIMEPATTERN24H));
				recorder.setPhone(phone);
				recorder.setSmsUsage("作品登记验证");
				recorder.setSmsCode(smsCode);
				
				String retCode = SmsUtil.sendPT(new StringBuffer(phone) , new StringBuffer("【我们信息科技有限公司】您的验证码是："+smsCode+"。请不要把验证码泄漏给其他人。"));
				String[] status = retCode.split(",");
				if("0".equals(status[0])){
					
					recorder.setSmsStatus(1);
					MessageRecorder insertMsg = messageRecorderService.insertMsg(recorder);
					session.setAttribute(phone, insertMsg.getRecordId());
					
					session.setAttribute("smsCode", smsCode);
					session.setAttribute("phone", phone);
					
					session.setMaxInactiveInterval(10*60);
					logger.info("phone:"+phone+",smsCode:"+smsCode+"，发送成功");
					return ResponseUtil.getResponseJson(0, "验证码发送成功", null);
				}else{
					recorder.setSmsStatus(2);
					messageRecorderService.insertMsg(recorder);
					logger.info("phone:"+phone+",smsCode:"+smsCode+"，发送失败");
					return ResponseUtil.getResponseJson(204, "验证码发送失败", null);
				}
			}else
				return ResponseUtil.getResponseJson(204, "手机号有误", null);
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseUtil.getResponseJson(204, "验证码发送失败", null);
		}
	}
	/**
	 * 
	* @Title: registerWorksInfo 
	* @Description: 短信确认 //TODO 暂时不用
	* @param request
	* @param
	* @param smsCode
	* @param phone
	* @return 
	*
	 */
	@RequestMapping(value="/checkInfo",method=RequestMethod.POST)
	@Auth
	@ResponseBody
	public String registerWorksInfo(HttpServletRequest request,
			@RequestParam("worksId")String worksId,@RequestParam("smsCode")String smsCode,@RequestParam("phone")String phone){
		HttpSession session = request.getSession();
		String sendCode = (String) session.getAttribute("smsCode");
		if(sendCode==null){
			return ResponseUtil.getResponseJson(204, "验证码超时", null);
		}else{
			String sendPhone = (String) session.getAttribute("phone");
			if(!phone.equals(sendPhone)){
				return ResponseUtil.getResponseJson(204, "提交手机号与接收短信手机号不匹配", null);
			}else{
				if(!sendCode.equals(smsCode)){
					return ResponseUtil.getResponseJson(204, "验证码错", null);
				}else{
					String info = worksInfoService.submitWorksInfo(worksId);
					if("0".equals(info)){
						Integer msgId = (Integer) session.getAttribute(phone);
						MessageRecorder recorder = new MessageRecorder();
						recorder.setRecordId(msgId);
						recorder.setSmsStatus(3);
						messageRecorderService.updateMsg(recorder);
						session.removeAttribute(phone);
						
						session.removeAttribute("phone");
						session.removeAttribute("smsCode");
						return ResponseUtil.getResponseJson(0, "作品信息登记成功", null);
					}else{
						return ResponseUtil.getResponseJson(204, "作品信息登记异常", null);
					}
				}
			}
		}
		
	}
	/**
	 * 
	* @Title: getWorksById 
	* @Description: 根据作品id查询
	* @param workdId
	* @param map
	* @return  String 
	 */
	@RequestMapping(value="/{workdId}",method=RequestMethod.GET)
	@Auth
	public String getWorksById(@PathVariable("workdId")String workdId,ModelMap map){
		Works works  = worksService.selectWorksById(workdId);
		map.put("workses", works);
		return "works_info";
	}
	/**
	 * 
	* @Title: worksFile (aws s3)
	* @Description: 作品文件
	* @param request
	* @param file
	* @param worksId
	* @return 
	*
	 */
	@RequestMapping(value = "/worksFileS3", method = RequestMethod.POST)
	@ResponseBody
	@Auth
	public String worksFileS3(HttpServletRequest request, @RequestParam("uploadfile") MultipartFile file,
			@RequestParam(value="worksId",required=false)String worksId) {
		Users user = (Users) request.getSession().getAttribute("user");
		String fileName = null;
		Map<String,String> map = new HashMap<>();
		try {
			if (file.isEmpty()) {
			      return ResponseUtil.getResponseJson(204, "文件为空", null);
			}
			String originalFilename = file.getOriginalFilename();
			// 获取文件的后缀名
			String suffixName = originalFilename.substring(originalFilename.lastIndexOf("."));
			String worksPath = "works/"+user.getUserId()+"/works/";//"/用户id/avatar
			fileName = String.valueOf(System.currentTimeMillis())+suffixName;
			
			if (suffixName.indexOf(".jpg") != -1 || suffixName.indexOf(".jpeg") != -1
					|| suffixName.indexOf(".png") != -1) {
				BufferedImage image = ImageIO.read(file.getInputStream());
				if(StringUtil.isEmpty(image)){
					return ResponseUtil.getResponseJson(204, "图片文件异常,请勿直接修改后缀名", null);
				}
				Builder<BufferedImage> builder = null;

				int imageWidth = image.getWidth();
				int imageHeitht = image.getHeight();
				if ((float) 170 / 210 != (float) imageWidth / imageHeitht) {
					if (imageWidth > imageHeitht) {
						image = Thumbnails.of(file.getInputStream()).height(210).asBufferedImage();
					} else {
						image = Thumbnails.of(file.getInputStream()).width(170).asBufferedImage();
					}
					builder = Thumbnails.of(image).sourceRegion(Positions.CENTER, 170, 210).size(170, 210);
				} else {
					builder = Thumbnails.of(image).size(170, 210);
				}

				ByteArrayOutputStream out = new ByteArrayOutputStream();
				builder.outputFormat("jpg").toOutputStream(out);
				ByteArrayInputStream stream = new ByteArrayInputStream(out.toByteArray());
				AWSS3Client.uploadToS3(stream, worksPath +  fileName + ".jpg");
			}
			String url = AWSS3Client.uploadToS3(file.getInputStream(), worksPath+fileName);
			
			if(!StringUtil.isEmpty(worksId)){
				Works works = worksService.selectWorksById(worksId);
				works.setWorksUrl(url);
				String newWorksId = worksService.registerWorks(works);
				map.put("worksId", newWorksId);
			}else{
				Works works = new Works();
				//file.transferTo(dest);
				works.setWorksUrl(url);
				works.setUserId(user.getUserId());
				works.setProgress(-1);
				String newWorksId = worksService.registerWorks(works);
				map.put("worksId", newWorksId);
			}
			map.put("fileName", url);
		} catch (IllegalStateException | IOException e) {
			e.printStackTrace();
		} 
		return ResponseUtil.getResponseJson(0, "上传成功", map);
	}
	/**
	 * 
	* @Title: worksFile (hard disk)
	* @Description: 作品文件
	* @param request
	* @param file
	* @param worksId
	* @return 
	*
	 */
	@RequestMapping(value = "/worksFile", method = RequestMethod.POST)
	@ResponseBody
	@Auth
	public String worksFile(HttpServletRequest request, @RequestParam("uploadfile") MultipartFile file,
			@RequestParam(value="worksId",required=false)String worksId) {
		Users user = (Users) request.getSession().getAttribute("user");
		String fileName = null;
		Map<String,String> map = new HashMap<>();
		try {
			if (file.isEmpty()) {
			      return ResponseUtil.getResponseJson(204, "文件为空", null);
			}
			String originalFilename = file.getOriginalFilename();
			// 获取文件的后缀名
			String suffixName = originalFilename.substring(originalFilename.lastIndexOf("."));
			String worksPath = path+user.getUserId()+"/works/";//"/用户id/avatar
			fileName = String.valueOf(System.currentTimeMillis())+suffixName;
			final File dest = new File(worksPath, fileName);
			if (!dest.getParentFile().exists()) {
			      dest.getParentFile().mkdirs();
			 }
			File destThu = new File(worksPath, fileName+".jpg");
			if (!destThu.getParentFile().exists()) {
				destThu.getParentFile().mkdirs();
			 }
			Builder<BufferedImage> builder = null;
			if (suffixName.indexOf(".jpg") != -1 || suffixName.indexOf(".jpeg") != -1 || suffixName.indexOf(".png") != -1) {
				BufferedImage image = ImageIO.read(file.getInputStream());
				if(StringUtil.isEmpty(image)){
					return ResponseUtil.getResponseJson(204, "图片文件异常,请勿直接修改后缀名", null);
				}
				int imageWidth = image.getWidth();
				int imageHeitht = image.getHeight();
				if ((float) 170 / 210 != (float) imageWidth / imageHeitht) {
					if (imageWidth > imageHeitht) {
						image = Thumbnails.of(file.getInputStream()).height(210).asBufferedImage();
					} else {
						image = Thumbnails.of(file.getInputStream()).width(170).asBufferedImage();
					}
					builder = Thumbnails.of(image).sourceRegion(Positions.CENTER, 170, 210).size(170, 210);
				} else {
					builder = Thumbnails.of(image).size(170, 210);
				}
			}
			
			if(!StringUtil.isEmpty(worksId)){
				Works works = worksService.selectWorksById(worksId);
				String worksUrl = works.getWorksUrl();
				if(!StringUtil.isEmpty(worksUrl)){
					FileUtil.delete(worksPath+works.getWorksUrl().substring(works.getWorksUrl().lastIndexOf("/")+1));
					FileUtil.delete(worksPath+works.getWorksUrl().substring(works.getWorksUrl().lastIndexOf("/")+1)+".jpg");
				}
				file.transferTo(dest);
				builder.outputFormat("jpg").toFile(destThu);
				works.setWorksUrl("/file/"+user.getUserId()+"/works/"+fileName);
				String newWorksId = worksService.registerWorks(works);
				map.put("worksId", newWorksId);
			}else{
				Works works = new Works();
				file.transferTo(dest);
				builder.outputFormat("jpg").toFile(destThu);
				works.setWorksUrl("/file/"+user.getUserId()+"/works/"+fileName);
				works.setUserId(user.getUserId());
				works.setProgress(-1);
				String newWorksId = worksService.registerWorks(works);
				map.put("worksId", newWorksId);
			}
			map.put("fileName", "/file/"+user.getUserId()+"/works/"+fileName);
		} catch (IllegalStateException | IOException e) {
			e.printStackTrace();
		} 
		return ResponseUtil.getResponseJson(0, "上传成功", map);
	}
	/**
	 * 
	 * @Title: fileDownload 
	 * @Description: 文件下载请求 TODO
	 * @param request
	 * @param name
	 * @param response
	 * @return 
	 *
	 */
	/*@RequestMapping(value = "/{avatar}/{name:.*}", method = RequestMethod.GET)
	@ResponseBody
	@Auth
	public FileSystemResource worksDownload(HttpServletRequest request,@PathVariable("avatar") String avatar,@PathVariable("name") String name, 
			HttpServletResponse response) {
		Users user = (Users) request.getSession().getAttribute("user");
		String avatarPath = path+user.getUserId()+"/"+avatar+"/";//"/用户id/avatar
		final File source = new File(avatarPath, name);
		if (!source.exists()) {
			response.setStatus(HttpStatus.NOT_FOUND.value());
			return null;
		}
		
		logger.info("downloading " + source.getAbsolutePath());
		return new FileSystemResource(source);
	}*/
}
