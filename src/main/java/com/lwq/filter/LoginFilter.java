package com.lwq.filter;

import com.lwq.common.ApplicationContextHelper;
import com.lwq.common.RequestHolder;
import com.lwq.common.ServerResponse;
import com.lwq.model.SysUser;
import com.lwq.service.impl.LoginAndAclControlAndAclControlServiceImpl;
import com.lwq.util.JsonMapper;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.*;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * @Author: Lwq
 * @Date: 2019/3/14 17:19
 * @Version 1.0
 * @Describe  过滤登录请求
 */
public class LoginFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) servletRequest;
        HttpServletResponse resp = (HttpServletResponse) servletResponse;

        LoginAndAclControlAndAclControlServiceImpl loginService= ApplicationContextHelper.popBean(LoginAndAclControlAndAclControlServiceImpl.class);

        String paramToken = req.getParameter(loginService.COOKI_NAME_TOKEN);
        String cookieToken = getCookieValue(req,LoginAndAclControlAndAclControlServiceImpl.COOKI_NAME_TOKEN);

        String token = StringUtils.isEmpty(paramToken)?cookieToken:paramToken;

        SysUser user = loginService.getByTokan(resp,token);

        if(user==null){
            resp.setCharacterEncoding("UTF-8");
            resp.setContentType("application/json; charset=utf-8");
            PrintWriter out = resp.getWriter();
            ServerResponse serverResponse = ServerResponse.createByErrorMessage("您没有登录，请先登录");
            out.append(JsonMapper.object2String(serverResponse));
            return;
        }

        RequestHolder.add(user);
        RequestHolder.add(req);

        filterChain.doFilter(servletRequest,servletResponse);

        return;
    }

    private String getCookieValue(HttpServletRequest request, String cookieName) {
        Cookie[] cookies = request.getCookies();
        if(cookies==null || cookies.length<=0){
            return null;
        }
        for(Cookie cookie:cookies){
            if(cookie.getName().equals(cookieName)){
                return cookie.getValue();
            }
        }
        return null;
    }

    @Override
    public void destroy() {

    }
}
