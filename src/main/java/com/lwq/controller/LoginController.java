package com.lwq.controller;

import com.lwq.common.Const;
import com.lwq.common.ServerResponse;
import com.lwq.model.SysUser;
import com.lwq.service.ILoginAndAclControlService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

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
    public ServerResponse<SysUser> login(String username, String password,HttpServletResponse httpServletResponse){
        return  iLoginAndAclControlService.login(username, password,httpServletResponse);
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
