package com.wevolution.common.utils;

import java.io.IOException;
import java.io.StringWriter;
import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Jackson工具类
 */
public class JacksonUtil {
    /** 记录日志的变量 */
    private static Logger logger = LoggerFactory.getLogger(JacksonUtil.class);
    /** 静态实例变量 */
    private static JacksonUtil jacksonUtil = new JacksonUtil();
    /** 数组最大下标 */
    private static final int ARRAY_MAX = 1024;    
    /** 静态ObjectMapper */
    private ObjectMapper mapper;

    /**
     * 私有构造函数
     */
    private JacksonUtil() {
        mapper = new ObjectMapper();
        //解析器支持解析单引号
        //mapper.configure(Feature.ALLOW_SINGLE_QUOTES,true);
        //解析器支持解析结束符
        //mapper.configure(Feature.ALLOW_UNQUOTED_CONTROL_CHARS,false);
       // mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
    }

    /**
     * 获得ObjectMapper实例
     * @return ObjectMapper
     */
    public static ObjectMapper getInstance() {
        return jacksonUtil.mapper;
    }

    /**
     * JSON对象转换为JavaBean
     * @param json JSON对象
     * @param valueType Bean类
     * @return 泛型对象
     */
    public static <T> T jsonToBean(String json, Class<T> valueType) {
        if (json == null || json.length() == 0 || "nil".equals(json)) {
            return null;
        }
        try {
            return getInstance().readValue(json, valueType);
        } catch (JsonParseException e) {
            logger.error(json + e.getMessage());
        } catch (JsonMappingException e) {
            logger.error(e.getMessage());
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
        return null;
    }
    
    /**
     * JSON对象转换为Map
     * 
     * @param json JSON对象
     * @param valueType Bean类
     * @return 泛型对象
     */
    @SuppressWarnings("rawtypes")
	public static Map jsonToMap(String json) {
        if (json == null || json.length() == 0 || "nil".equals(json)) {
            return null;
        }
        try {
            return getInstance().readValue(json, Map.class);
        } catch (JsonParseException e) {
            logger.error(json + e.getMessage());
        } catch (JsonMappingException e) {
            logger.error(e.getMessage());
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
        return null;
    }

    /**
     * JavaBean转换为JSON字符串
     * 
     * @param bean JavaBean对象
     * @return json字符串
     */
    public static String beanToJson(Object bean) {
        StringWriter sw = new StringWriter();
        JsonGenerator gen = null;
        try {
            gen = new JsonFactory().createGenerator(sw);
            getInstance().writeValue(gen, bean);
            gen.close();
        } catch (IOException e) {
            logger.error(e.getMessage());
        }

        return sw.toString();
    }
    
    /**
     * Map转换为JSON字符串
     * 
     * @param bean JavaBean对象
     * @return json字符串
     */
    public static String mapToJson(Map<?,?> map) {
        StringWriter sw = new StringWriter();
        JsonGenerator gen = null;
        try {
            gen = new JsonFactory().createGenerator(sw);
            getInstance().writeValue(gen, map);
            gen.close();
        } catch (IOException e) {
            logger.error(e.getMessage());
        }

        return sw.toString();
    }

    /**
     * Json转List
     * 
     * @param json
     * @param clazz
     * @return
     */
    @SuppressWarnings("unchecked")
    public static <T> List<T> jsonToList(String json, Class<T> clazz) {
        if (json == null || json.length() == 0 || "nil".equals(json)) {
            return null;
        }
        T[] t = (T[]) Array.newInstance(clazz, ARRAY_MAX);
        try {
            t = (T[]) getInstance().readValue(json, t.getClass());
            return (List<T>) Arrays.asList(t);
        } catch (JsonGenerationException e) {
            logger.error(e.getMessage());
        } catch (JsonMappingException e) {
            logger.error(e.getMessage());
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
        return null;
    }

    /**
     * List转为Json
     * 
     * @param t
     * @return
     */
    public static String listToJson(List<?> t) {
        try {
            return getInstance().writeValueAsString(t);
        } catch (JsonGenerationException e) {
            logger.error(e.getMessage());
        } catch (JsonMappingException e) {
            logger.error(e.getMessage());
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
        return null;
    }

    /**
     * Json转换为数组
     * 
     * @param json json串
     * @param clazz 实例类
     * @return
     */
    @SuppressWarnings("unchecked")
	public static <T> T[] jsonToArray(String json, Class<T> clazz) {
        if (json == null || json.length() == 0 || "nil".equals(json)) {
            return null;
        }
        T[] t = (T[]) Array.newInstance(clazz, ARRAY_MAX);
        try {
            t = (T[]) getInstance().readValue(json, t.getClass());
            return t;
        } catch (JsonGenerationException e) {
            logger.error(e.getMessage());
        } catch (JsonMappingException e) {
            logger.error(e.getMessage());
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
        return null;
    }

    /**
     * 数组转Json
     * 
     * @param t
     * @return
     */
    public static <T> String arrayToJson(T[] t) {
        try {
            return getInstance().writeValueAsString(t);
        } catch (JsonGenerationException e) {
            logger.error(e.getMessage());
        } catch (JsonMappingException e) {
            logger.error(e.getMessage());
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
        return null;
    }
    
    public static void main(String[] args) {
//    	String s = "{\"fldCode\":\"fdsf\",\"fldName\":\"dsfds\",\"fldType\":\"0\"}";
//    	
//    	String ss = "{\"fldCode\":\"fdsf\",\"fldName\":\"dsfds\",\"fldType\":\"0\"},{\"fldCode\":\"aa\",\"fldName\":\"qq\",\"fldType\":\"0\"}";
//    	int post = ss.indexOf("},");
//    	ss = ss.replaceAll("},", "};");
    	
    	/*Map<String, Object> map = new HashMap<String, Object>();
    	map.put("a", 123);
    	map.put("b", null);
    	String json2 = mapToJson(map);
    	logger.debug("json2=" + json2);*/
    	//String str = "{\"list\":[{\"ArticleId\":53925907,\"BlogId\":1246210,\"CommentId\":6855535,\"Content\":\"[reply]frinder[/reply]\n代码：\ncheck(){\n    tpid=`ps -ef|grep $APP_NAME|grep -v grep|grep -v kill|awk &#39;{print $2}&#39;`\n    echo tpid-$tpid\n    if [[ ${tpid} ]]; then\n        echo &quot;$APP_NAME is running.&quot;\n    else\n        echo &quot;$APP_NAME is NOT running.&quot;\n    fi\n}\",\"ParentId\":6855532,\"PostTime\":\"2017-05-24 14:42\",\"Replies\":null,\"UserName\":\"frinder\",\"Userface\":\"http://avatar.csdn.net/5/3/1/3_frinder.jpg\"},{\"ArticleId\":53925907,\"BlogId\":1246210,\"CommentId\":6855532,\"Content\":\"效果：\nroot@ubuntu:~/wms/apps/sh$ ./springboot.sh check\ntpid-26731 26732\nsid-service is running.\",\"ParentId\":0,\"PostTime\":\"2017-05-24 14:41\",\"Replies\":null,\"UserName\":\"frinder\",\"Userface\":\"http://avatar.csdn.net/5/3/1/3_frinder.jpg\"},{\"ArticleId\":53925907,\"BlogId\":1246210,\"CommentId\":6855527,\"Content\":\"你好，咨询个问题，使用脚本时遇到的问题，check方法，不管服务有没有运行，总是返回在运行，添加一行：echo tpid-$tpid，发现每次都有打印 tpid，而kills方法则正常，对比发现获取tpid的代码是一样的，感觉好凌乱啊！！！谢谢\",\"ParentId\":0,\"PostTime\":\"2017-05-24 14:40\",\"Replies\":null,\"UserName\":\"frinder\",\"Userface\":\"http://avatar.csdn.net/5/3/1/3_frinder.jpg\"}],\"page\":{\"PageSize\":20,\"PageIndex\":1,\"RecordCount\":0,\"PageCount\":0},\"total\":0,\"fileName\":53925907}";
    	String str = "{\"name\":\"zitong\", \"age\":\"26\"}";  
        Map<String, String> map = new HashMap<String, String>();  
        ObjectMapper mapper = new ObjectMapper();  
          
        try{  
            map = mapper.readValue(str, new TypeReference<HashMap<String,String>>(){});  
            System.out.println(map);  
        }catch(Exception e){  
            e.printStackTrace();  
        }  
    	/*map = jsonToMap(str);
    	System.out.println(map);*/
	}

}
