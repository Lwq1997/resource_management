package com.lwq.filter;

import com.lwq.common.Const;
import com.lwq.common.RequestHolder;
import com.lwq.common.ServerResponse;
import com.lwq.model.SysUser;
import com.lwq.util.JsonMapper;

import javax.servlet.*;
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

        SysUser sysUser = (SysUser) req.getSession().getAttribute(Const.CURRENT_USER);

        if(sysUser==null){
            resp.setCharacterEncoding("UTF-8");
            resp.setContentType("application/json; charset=utf-8");
            PrintWriter out = resp.getWriter();
            ServerResponse serverResponse = ServerResponse.createByErrorMessage("您没有登录，请先登录");
            out.append(JsonMapper.object2String(serverResponse));
            return;
        }

        RequestHolder.add(sysUser);
        RequestHolder.add(req);

        filterChain.doFilter(servletRequest,servletResponse);

        return;
    }

    @Override
    public void destroy() {

    }
}
