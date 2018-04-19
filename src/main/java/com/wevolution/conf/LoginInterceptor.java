package com.wevolution.conf;

import java.lang.reflect.Method;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.wevolution.domain.Users;
/**
 * 
* @ClassName: LoginInterceptor 
* @Description: 登录拦截器
* @author jiangjian
* @date 2017年8月22日 下午5:19:20 
*
 */
@Component
public class LoginInterceptor extends HandlerInterceptorAdapter {

    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response, Object handler) throws Exception {
        // 如果不是映射到方法直接通过
        if (!(handler instanceof HandlerMethod)) {
            return true;
        }
        HandlerMethod handlerMethod = (HandlerMethod) handler;
        Method method = handlerMethod.getMethod();

        // 判断接口是否需要登录
        Auth methodAnnotation = method.getAnnotation(Auth.class);
        // 有 @Auth 注解，需要认证
        if (methodAnnotation != null) {
            // 执行认证
        	HttpSession session = request.getSession();
        	Users user = (Users) session.getAttribute("user");
        	if(user==null){
        		request.getRequestDispatcher("/auth/loginPage").forward(request, response);
        		//response.sendRedirect("/auth/loginPage");
        		return false;
        	}
            return true;
        }
        return true;
    }
}
