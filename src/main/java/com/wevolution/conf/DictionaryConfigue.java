package com.wevolution.conf;

import org.oauth2.sdk.utils.config.ReaderXml;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import com.wevolution.service.impl.DictInit;

@Component
public class DictionaryConfigue implements ApplicationListener<ContextRefreshedEvent> {
    
    @Autowired
    private DictInit dictInit;
    
    //YC:当一个ApplicationContext被初始化或刷新触发 
    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        if (event.getApplicationContext().getParent() == null) {//root application context 初始化执行
            dictInit.start();
            //第三方登录配置文件
            ReaderXml.getXmls();
        } else {//其他容器
        }
    }

}
