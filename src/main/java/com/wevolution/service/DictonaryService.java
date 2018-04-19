package com.wevolution.service;

import java.util.List;

import com.wevolution.domain.Dictionary;

public interface DictonaryService {
	Dictionary getValueById(String id);
	List<Dictionary> getValueByCode(String code);
	List<Dictionary> getDictionary();
}
