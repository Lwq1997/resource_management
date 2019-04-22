package com.lwq.controller;

import com.lwq.common.Const;
import com.lwq.common.ServerResponse;
import com.lwq.model.SysUser;
import com.lwq.service.ILoginAndAclControlService;
import com.lwq.util.JsonMapper;
import com.sun.deploy.net.HttpResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.net.HttpCookie;

/**
 * @Author: Lwq
 * @Date: 2019/3/14 14:20
 * @Version 1.0
 * @Describe
 */
@Controller
public class LoginController {

    @Autowired
    private ILoginAndAclControlService iLoginAndAclControlService;

    /**
     * 用户登录
     * @param username
     * @param password
     * @param httpServletResponse
     * @return
     */
    @RequestMapping(value = "/login.json")
    @ResponseBody
    public ServerResponse<SysUser> login(String username, String password, HttpServletResponse httpServletResponse){
        ServerResponse<SysUser> response = iLoginAndAclControlService.login(username, password);
        if(response.isSuccess()){
            Cookie cookie = new Cookie(Const.CURRENT_USER, JsonMapper.object2String(response.getData()));
            cookie.setMaxAge(60*60);//这里设置设置有效时间，单位的秒，我这里是一小时
            cookie.setPath("/");//这里是之根目录下所有的目录都可以共享Cookie
            httpServletResponse.addCookie(cookie);
        }
        return response;
    }

    /**
     * 用户登出
     * @param session
     * @return
     */
    @RequestMapping(value = "/logout.json",method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<String> logout(HttpSession session){
        session.removeAttribute(Const.CURRENT_USER);
        return ServerResponse.createBySuccess();
    }
}
