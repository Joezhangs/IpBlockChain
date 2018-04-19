package com.wevolution.mapper;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import com.wevolution.domain.Users;
@RunWith(SpringJUnit4ClassRunner.class)
//@SpringApplicationConfiguration(classes = IpBlockChainApplication.class)
@SpringBootTest
@WebAppConfiguration
public class UsersMapperTest {
	@Autowired
	UsersMapper usersMapper;

	@Test
	public void testSelectByPrimaryKey() {
		Users users = usersMapper.selectByPrimaryKey(1);
		Assert.assertNotNull(users);
	}

}
