package cn.wolfcode.wms.util;

import java.lang.reflect.Method;

public abstract class PermissionUtil {
    public static String buildExpression(Method m) {
        String className = m.getDeclaringClass().getName();
        return className+":"+m.getName();
    }
}
