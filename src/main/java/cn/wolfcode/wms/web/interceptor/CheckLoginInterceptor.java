package cn.wolfcode.wms.web.interceptor;

import cn.wolfcode.wms.domain.Employee;
import cn.wolfcode.wms.util.UserContext;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Optional;

public class CheckLoginInterceptor extends HandlerInterceptorAdapter {

    public boolean preHandle(HttpServletRequest request, HttpServletResponse response,
                             Object handler)
            throws Exception {
        Employee emp = UserContext.getCurrentUser();
        if (!Optional.ofNullable(emp).isPresent()) { //没有登录
            response.sendRedirect("/login.html");
            return false;
        }
        return true; //已经登录,放行
    }
}
