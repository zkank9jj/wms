package cn.wolfcode.wms.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

//把对象转成JSON
public abstract class JSONUtil {

    private static ObjectMapper mapper = new ObjectMapper();

    public static String toJSONString(Object obj) throws JsonProcessingException {
        return mapper.writeValueAsString(obj);
    }
}
