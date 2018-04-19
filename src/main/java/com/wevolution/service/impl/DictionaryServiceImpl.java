package com.wevolution.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.wevolution.domain.Dictionary;
import com.wevolution.mapper.DictionaryMapper;
import com.wevolution.service.DictonaryService;

@Service
public class DictionaryServiceImpl implements DictonaryService {

	@Resource
	private DictionaryMapper dictionaryMapper;
	@Override
	public Dictionary getValueById(String id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Dictionary> getValueByCode(String code) {
		// TODO Auto-generated method stub
		List<Dictionary> list = dictionaryMapper.selectByCode(code);
		return list;
	}

	@Override
	public List<Dictionary> getDictionary() {
		// TODO Auto-generated method stub
		List<Dictionary> list = dictionaryMapper.selectAll();
		return list;
	}

}
