package com.wevolution.service;

import com.wevolution.domain.MessageRecorder;

public interface MessageRecorderService {
	/**
	 * 发送短信入库
	 * 
	 * @Title: insertMsg
	 * @param record
	 * @return
	 *
	 */
	MessageRecorder insertMsg(MessageRecorder record);

	/**
	 * 更新短信状态
	 * 
	 * @Title: updateMsg
	 * @param record
	 * @return
	 *
	 */
	String updateMsg(MessageRecorder record);
}
