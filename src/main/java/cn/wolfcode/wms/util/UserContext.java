package cn.wolfcode.wms.util;

import cn.wolfcode.wms.domain.Employee;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpSession;
import java.util.List;

public abstract class UserContext {

    private static final String EMP_IN_SESSION = "EMP_IN_SESSION";
    private static final String EXPS_IN_SESSION = "EXPS_IN_SESSION";

    private static HttpSession getSession() {
        return ((ServletRequestAttributes) (RequestContextHolder.getRequestAttributes()))
                .getRequest().getSession();
    }

    public static void setCurrentUser(Employee emp) {
        getSession().setAttribute(EMP_IN_SESSION, emp);
    }

    public static void setCurrentExpressions(List<String> exps) {

        getSession().setAttribute(EXPS_IN_SESSION, exps);
    }

    public static Employee getCurrentUser() {

        return (Employee) getSession().getAttribute(EMP_IN_SESSION);
    }

    public static List<String> getCurrentExpressions() {
        return (List<String>) getSession().getAttribute(EXPS_IN_SESSION);
    }
}
