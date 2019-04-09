package com.lwq.filter;

import com.google.common.base.Splitter;
import com.google.common.collect.Sets;
import com.lwq.common.ApplicationContextHelper;
import com.lwq.common.RequestHolder;
import com.lwq.common.ServerResponse;
import com.lwq.model.SysUser;
import com.lwq.service.ILoginAndAclControlService;
import com.lwq.util.JsonMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Set;

/**
 * @Author: Lwq
 * @Date: 2019/3/15 21:08
 * @Version 1.0
 * @Describe
 */
@Slf4j
public class AclControlFilter implements Filter {

    //白名单
    private static Set<String> exclusionUrlSet = Sets.newConcurrentHashSet();

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        String exclusionUrls = filterConfig.getInitParameter("exclusionUrls");
        List<String> exclusionUrlList = Splitter.on(",").trimResults().omitEmptyStrings().splitToList(exclusionUrls);
        exclusionUrlSet = Sets.newConcurrentHashSet(exclusionUrlList);
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        String servletPath = request.getServletPath();
        if(exclusionUrlSet.contains(servletPath)){
            filterChain.doFilter(servletRequest,servletResponse);
            return;
        }
        SysUser user = RequestHolder.getCurrentUser();
        ILoginAndAclControlService iLoginAndAclControlService = ApplicationContextHelper.popBean(ILoginAndAclControlService.class);
        if(!iLoginAndAclControlService.checkAdminRole(user).isSuccess()){
            response.setCharacterEncoding("UTF-8");
            response.setContentType("application/json; charset=utf-8");
            PrintWriter out = response.getWriter();
            ServerResponse serverResponse = ServerResponse.createByErrorMessage("您没有权限访问，请联系管理员");
            out.append(JsonMapper.object2String(serverResponse));
            return;
        }
        filterChain.doFilter(servletRequest,servletResponse);
        return;
    }

    @Override
    public void destroy() {

    }
}
