package com.wevolution.service.impl;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.wevolution.domain.MessageRecorder;
import com.wevolution.mapper.MessageRecorderMapper;
import com.wevolution.service.MessageRecorderService;
@Service
public class MessageRecorderServiceImpl implements MessageRecorderService{
	private static Logger logger = LoggerFactory.getLogger(MessageRecorderServiceImpl.class);
	
	@Resource
	private MessageRecorderMapper messageRecorderMapper;
	@Override
	public MessageRecorder insertMsg(MessageRecorder record) {
		int ret = messageRecorderMapper.insertSelective(record);
		if(ret>0){
			logger.info(record.getSmsUsage()+"的短信入库成功，短信状态："+record.getSmsStatus());
		}else{
			logger.info(record.getSmsUsage()+"的短信入库失败");
		}
		return record;
	}

	@Override
	public String updateMsg(MessageRecorder record) {
		int ret = messageRecorderMapper.updateByPrimaryKeySelective(record);
		if(ret>0){
			logger.info("短信更新成功，短信状态："+record.getSmsStatus());
		}else{
			logger.info("短信更新失败");
		}
		return ret>0?"0":"更新短信失败";
	}
}
