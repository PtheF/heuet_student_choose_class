package cn.chl.robClass.utils;

import cn.chl.robClass.entity.vo.UserVO;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class WebInterceptor implements HandlerInterceptor {

    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

//        System.out.println("entry");

        String jwt = request.getHeader("authorization");

        if(StringUtils.isEmpty(jwt)){
            return false;
        }

        if(!JwtUtils.verify(jwt)){
            return false;
        }

        UserVO bean = JwtUtils.getBean(UserVO.class, jwt);

        UserHolder.holdUser(bean);

        return true;
    }

    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        UserHolder.freeUser();
    }
}
