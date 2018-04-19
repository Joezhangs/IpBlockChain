package com.wevolution.common;

/**
 * 
* @ClassName: Constant 
* @Description: 常量定义 
* @author jiangjian
* @date 2017年8月16日 上午11:21:05 
*
 */
public interface Constants {

    // ================================================================
    // Constants
    // ================================================================

    /**
     * 测试模式
     */
    boolean MODE_DEMO = false;
    /**
     * 自定义的配置的前缀
     */
    String CONFIGURATION_DEFINED_PREFIX = "wevolution.";

    /**
     * 系统默认编码
     */
    String CHARSET_UTF_8 = "UTF-8";

    /**
     * cookie 的默认时间
     */
    int COOKIE_DEFAULT_AGE = 60 * 60 * 24;

    /**
     * session 的默认时间
     */
    int SESSION_DEFAULT_AGE = 30 * 24 * 60 * 60;

    /**
     * 默认查询数据每页数量
     */
    int PAGE_DEFAULT_SIZE = 10;

    /**
     * cookie cipher key
     */
    String COOKIE_CIPHER_KEY = "4AvVhmFLUs0KTA3Kprsdag==";

    /**
     * 显示/隐藏
     */
    String SHOW = "1";
    String HIDE = "0";

    /**
     * 是/否
     */
    String YES = "1";
    String NO = "0";

    /**
     * 对/错
     */
    String TRUE = "true";
    String FALSE = "false";

    /**
     * 模板视图的前缀
     */
    String WEB_VIEW_PREFIX = "templates";
    /**
     * 模板视图的后缀
     */
    String WEB_VIEW_SUFFIX = ".html";

    /**
     * 刷新主页退出登录
     */
    boolean NOT_ALLOW_REFRESH_INDEX = false;

    // ================================================================
    // Methods from/for super Interfaces or SuperClass
    // ================================================================

    // ================================================================
    // Public or Protected Methods
    // ================================================================
    String STATUS = "status";
    String MSG = "msg";
	String DATA = "data";
    public enum AuthType{
    	WX,WB
    }
}
