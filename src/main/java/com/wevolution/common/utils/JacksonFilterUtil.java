package com.wevolution.common.utils;

import java.io.IOException;
import java.io.StringWriter;
import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Jackson工具类
 */
public class JacksonFilterUtil {
    /** 记录日志的变量 */
    private static Logger logger = LoggerFactory.getLogger(JacksonFilterUtil.class);
    /** 静态实例变量 */
    private static JacksonFilterUtil jacksonUtil = new JacksonFilterUtil();
    /** 数组最大下标 */
    private static final int ARRAY_MAX = 1024;    
    /** 静态ObjectMapper */
    private ObjectMapper mapper;

    /**
     * 私有构造函数
     */
    private JacksonFilterUtil() {
        mapper = new ObjectMapper();
        //空值过滤掉(""和NULL)
		mapper.setSerializationInclusion(JsonInclude.Include.NON_EMPTY);
        //mapper.configure(JsonGenerator.Feature.IGNORE_UNKNOWN, true);  
        mapper.configure(JsonGenerator.Feature.ESCAPE_NON_ASCII, true); 
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

}
