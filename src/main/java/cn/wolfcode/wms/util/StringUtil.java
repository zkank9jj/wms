package cn.wolfcode.wms.util;

import org.springframework.util.StringUtils;

public abstract class StringUtil {

    public static String empty2Null(String s) {
        return hasLength(s) ? s : null;
    }

    public static boolean hasLength(String s) {
        return StringUtils.hasLength(s);
    }
}
