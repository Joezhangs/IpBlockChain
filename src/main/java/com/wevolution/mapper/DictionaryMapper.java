package com.wevolution.mapper;

import java.util.List;

import com.wevolution.domain.Dictionary;

public interface DictionaryMapper {
	List<Dictionary> selectAll();
	List<Dictionary> selectByCode(String code);
	Dictionary selctById(Integer id);
}