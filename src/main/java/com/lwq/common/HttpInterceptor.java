package com.lwq.common;

import com.lwq.util.JsonMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * @Author: Lwq
 * @Date: 2019/3/11 20:31
 * @Version 1.0
 * @Describe    http前后的监听工具
 */
@Slf4j
public class HttpInterceptor extends HandlerInterceptorAdapter {

    private static final String STAT_TIME = "requestStartTime";
    /**
     * 请求之前
     * @param request
     * @param response
     * @param handler
     * @return
     * @throws Exception
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String url = request.getRequestURL().toString();
        Map parameterMap = request.getParameterMap();
        log.info("request start url: {}, params: {}",url, JsonMapper.object2String(parameterMap));
        long startTime = System.currentTimeMillis();
        request.setAttribute(STAT_TIME,startTime);
        return true;
    }

    /**
     * 请求之后（正常）
     * @param request
     * @param response
     * @param handler
     * @param modelAndView
     * @throws Exception
     */
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
//        String url = request.getRequestURL().toString();
//        Map parameterMap = request.getParameterMap();
//        long startTime = (long) request.getAttribute(STAT_TIME);
//        long endTime = System.currentTimeMillis();
//        log.info("request finished url: {}, params: {}, cost: {}",url, JsonMapper.object2String(parameterMap),endTime-startTime);
         removeThreadLocalInfo();
    }

    /**
     * 请求之后（正常+异常）
     * @param request
     * @param response
     * @param handler
     * @param ex
     * @throws Exception
     */
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        String url = request.getRequestURL().toString();
        Map parameterMap = request.getParameterMap();
        long startTime = (long) request.getAttribute(STAT_TIME);
        long endTime = System.currentTimeMillis();
        log.info("request complete url: {}, params: {}, cost: {}",url, JsonMapper.object2String(parameterMap),endTime-startTime);
        removeThreadLocalInfo();
    }

    public void removeThreadLocalInfo(){
        RequestHolder.remove();
    }
}
