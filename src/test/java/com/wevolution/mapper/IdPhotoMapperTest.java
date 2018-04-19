package com.wevolution.mapper;

import javax.annotation.Resource;

import org.junit.Assert;
import org.junit.Test;

import com.wevolution.BaseJUtilTest;
import com.wevolution.common.utils.UUIDTool;
import com.wevolution.domain.IdPhoto;

public class IdPhotoMapperTest extends BaseJUtilTest {
	@Resource
	private IdPhotoMapper idPhotoMapper;

	@Test
	public void testUpdateAuthId() {
		int i = idPhotoMapper.updateAuthId(UUIDTool.getUUID(), new String[]{"1","6"});
		Assert.assertEquals(2, i);
	}

	@Test
	public void testInsertPhoto() {
		IdPhoto idPhoto1 = new IdPhoto();
		idPhoto1.setIdImageUrl("http://sdfsf/sdfswqeq.jpg");
		int i = idPhotoMapper.insertPhoto(idPhoto1);
		Assert.assertEquals(1, i);
	}

}
