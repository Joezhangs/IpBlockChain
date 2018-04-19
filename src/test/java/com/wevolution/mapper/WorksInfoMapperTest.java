package com.wevolution.mapper;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.junit.Assert;
import org.junit.Test;

import com.wevolution.BaseJUtilTest;
import com.wevolution.common.utils.DateUtil;
import com.wevolution.common.utils.UUIDTool;
import com.wevolution.domain.WorksInfo;

public class WorksInfoMapperTest extends BaseJUtilTest {
	@Resource
	WorksInfoMapper worksInfoMapper;
	@Test
	public void testInsertWorksInfo() {
		List<WorksInfo> list = new ArrayList<>();
		String uuid = UUIDTool.getUUID();
		WorksInfo info = new WorksInfo();
		info.setWorksId(uuid);
		info.setAuthor("test");
		info.setCreatedTime(DateUtil.getCurrentDateString(DateUtil.DATETIMEPATTERN24H));
		list.add(info);
		WorksInfo info1 = new WorksInfo();
		info1.setWorksId(uuid);
		info1.setAuthor("tset");
		//info1.setIsUsable(true);
		list.add(info1);
		worksInfoMapper.insertWorksInfo(list);
	}

	@Test
	public void testQueryWorksInfoByWorksId() {
		List<Map<String, String>> map = worksInfoMapper.queryWorksInfoByWorksId("8921ac76014c440eb7a0c37ca31d222d");
		Assert.assertEquals("", map.get(0).get("user_id"));
		
	}

}
