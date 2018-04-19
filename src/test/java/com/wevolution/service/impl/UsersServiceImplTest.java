package com.wevolution.service.impl;

import javax.annotation.Resource;

import org.junit.Assert;
import org.junit.Test;

import com.wevolution.BaseJUtilTest;
import com.wevolution.domain.Users;
import com.wevolution.service.UsersService;

public class UsersServiceImplTest extends BaseJUtilTest {

	@Resource
	UsersService userService;
	@Test
	public void testSignUp() {
		Users user = new Users();
		user.setUserName("test2");
		user.setVerifyCredential("123456");
		user.setPhone("123456789");
		String string = userService.signUp(user);
		Assert.assertEquals("0", string);
	}


}
