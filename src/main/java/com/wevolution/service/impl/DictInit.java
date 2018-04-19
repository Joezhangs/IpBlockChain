package com.wevolution.service.impl;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wevolution.domain.Dictionary;
import com.wevolution.service.DictonaryService;
@Service
public class DictInit {
	/**存放系统参数*/
    public static Map<String, String> dictMap = new HashMap<>();
    public static Map<String, List<Dictionary>> dictCoedMap = new HashMap<>();
    
    @Autowired
    private DictonaryService dictonaryService;
    
    /**参数初始化工作*/
    public void start() {
        injectDictConfigByDb();
    }
    
    /**读取字典表 */
    private void injectDictConfigByDb() {
        List<Dictionary> exDictList = dictonaryService.getDictionary();
        Set<String> set = new HashSet<>();
        if(exDictList != null && exDictList.size() > 0){
            for (Dictionary exDict : exDictList) {
                dictMap.put(exDict.getId().toString(), exDict.getName());
                if(exDict.getCode()!=null){
                	set.add(exDict.getCode());
                }
            }
        }
        for (String code : set) {
			List<Dictionary> list = dictonaryService.getValueByCode(code);
			dictCoedMap.put(code, list);
		}
        //打印map
//        HashMap<String, String> dictMap = (HashMap<String, String>) DictInit.dictMap;
//        for (HashMap.Entry<String, String> entry : dictMap.entrySet()) {  
//            Constant.MY_LOG.debug("Key = " + entry.getKey() + ", Value = " + entry.getValue());  
//        }  
    }

}
