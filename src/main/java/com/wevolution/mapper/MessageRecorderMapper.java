package com.wevolution.mapper;

import com.wevolution.domain.MessageRecorder;

public interface MessageRecorderMapper {
    /**
     * insertSelective
     */
    int insertSelective(MessageRecorder record);

    /**
     * updateByPrimaryKeySelective
     */
    int updateByPrimaryKeySelective(MessageRecorder record);

}