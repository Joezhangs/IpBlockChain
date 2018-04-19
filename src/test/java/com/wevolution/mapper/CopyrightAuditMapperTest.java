package com.wevolution.mapper;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.wevolution.BaseJUtilTest;
import com.wevolution.domain.CopyrightAudit;


public class CopyrightAuditMapperTest extends BaseJUtilTest{

	@Autowired
	private CopyrightAuditMapper copyrightAuditMapper;
	@Test
	public void testUpdateByworksId() {
		CopyrightAudit audit = new CopyrightAudit();
		audit.setWorksId("fdsajfalfkjai5893402");
		audit.setApprovedStatus((byte) 3);
		audit.setBlockId(3);
		int i = copyrightAuditMapper.updateByworksId(audit);
		Assert.assertEquals(2, i);
	}

}
