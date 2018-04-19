package com.wevolution.mapper;

import java.util.Map;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.wevolution.BaseJUtilTest;

public class WorksMapperTest extends BaseJUtilTest{
	@Autowired
	private WorksMapper worksMapper;
	@Test
	public void testQueryWorksById() {
		Map<String, String> map = worksMapper.queryWorksById("8921ac76014c440eb7a0c37ca31d222d");
		System.out.println(map);
	}

}
