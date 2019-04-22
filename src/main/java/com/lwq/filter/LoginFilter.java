package com.lwq.filter;

import com.lwq.common.Const;
import com.lwq.common.RequestHolder;
import com.lwq.common.ServerResponse;
import com.lwq.model.SysDept;
import com.lwq.model.SysUser;
import com.lwq.util.JsonMapper;
import org.codehaus.jackson.type.TypeReference;

import javax.servlet.*;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URLDecoder;

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

        SysUser sysUser = null;
        Cookie[] cookies = req.getCookies();//这里是取出Cookie
        if ((cookies!=null)){//判断Cookie是否为空
            for (Cookie cookie : cookies){//遍历Cookie判断有没有对应的name
                if (cookie.getName().equals(Const.CURRENT_USER)){
                    sysUser = JsonMapper.string2Obj(cookie.getValue(),new TypeReference<SysUser>() {
                    });
                }
            }
        }

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
