package cn.wolfcode.wms.web.interceptor;

import cn.wolfcode.wms.domain.Employee;
import cn.wolfcode.wms.exception.SecurityException;
import cn.wolfcode.wms.util.PermissionUtil;
import cn.wolfcode.wms.util.RequiredPermission;
import cn.wolfcode.wms.util.UserContext;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.util.List;

public class SecurityInterceptor extends HandlerInterceptorAdapter {

    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        //1:拿到当前登录的用户
        Employee emp = UserContext.getCurrentUser();
        //2:判断是否超级管理员
        if (emp.isAdmin()) {
            return true;
        }
        //3:判断当前方法是否需要权限
        HandlerMethod hm = (HandlerMethod) handler;
        Method m = hm.getMethod();
        if (!m.isAnnotationPresent(RequiredPermission.class)) {
            return true;
        }
        //4:当前用户是否拥有权限
        String exp = PermissionUtil.buildExpression(m);
        List<String> exps = UserContext.getCurrentExpressions();
        if (exps.contains(exp)) {
            return true;
        }
        //在正常的操作中,这里应该抛出异常
        throw new SecurityException();
    }
}
