package com.wevolution.service.impl;

import org.junit.Assert;
import org.junit.Test;

import com.wevolution.BaseJUtilTest;

public class DictInitTest extends BaseJUtilTest {

	@Test
	public void test() {
		Assert.assertNotNull(DictInit.dictCoedMap);
	}

}
