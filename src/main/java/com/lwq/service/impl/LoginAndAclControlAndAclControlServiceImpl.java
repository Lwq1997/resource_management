package com.lwq.service.impl;

import com.lwq.common.Const;
import com.lwq.common.ServerResponse;
import com.lwq.dao.SysUserMapper;
import com.lwq.model.SysUser;
import com.lwq.redis.RedisUtil;
import com.lwq.redis.SysUserKey;
import com.lwq.service.ILoginAndAclControlService;
import com.lwq.util.MD5Util;
import com.lwq.util.UUIDUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

/**
 * @Author: Lwq
 * @Date: 2019/4/3 15:35
 * @Version 1.0
 * @Describe
 */
@Service
public class LoginAndAclControlAndAclControlServiceImpl implements ILoginAndAclControlService {

    public static final String COOKI_NAME_TOKEN = "token";

    @Autowired
    SysUserMapper sysUserMapper;
    
    @Autowired
    RedisUtil redisUtil;

    /**
     * 登录
     * @param username
     * @param password
     * @param httpServletResponse
     * @return
     */
    @Override
    public ServerResponse<SysUser> login(String username, String password, HttpServletResponse httpServletResponse) {
        if(StringUtils.isBlank(username)){
            return ServerResponse.createByErrorMessage("用户名为空");
        }
        if(StringUtils.isBlank(password)){
            return ServerResponse.createByErrorMessage("密码为空");
        }

        SysUser sysUser = getUserByName(username);
        if (sysUser == null){
            return ServerResponse.createByErrorMessage("用户不存在");
        }
        if (sysUser.getStatus() != 1) {
            return ServerResponse.createByErrorMessage("用户已被冻结，请联系管理员");
        }
        String md5Password = MD5Util.MD5EncodeUtf8(password);
        if(!StringUtils.equals(sysUser.getPassword(),md5Password)){
            return ServerResponse.createByErrorMessage("密码错误");
        }
        sysUser.setPassword(StringUtils.EMPTY);
        //生成cookie
        String token = UUIDUtil.uuid();
        sysUser.setToken(token);
        addCookie(httpServletResponse, token, sysUser);
        return ServerResponse.createBySuccess("登陆成功",sysUser);
    }


    /**
     * 监测是不是管理员登录
     * @param user
     * @return
     */
    @Override
    public ServerResponse checkAdminRole(SysUser user) {
        if(StringUtils.equals(user.getUsername(),Const.Role.ROLE_ADMIN)){
            return ServerResponse.createBySuccess();
        }else {
            return ServerResponse.createByError();
        }
    }

    public SysUser getUserByName(String name){
        //取缓存
        SysUser user = redisUtil.get(SysUserKey.getByName, "" + name, SysUser.class);
        if(user!=null){
            return user;
        }
        user = sysUserMapper.checkUser(name);
        if(user!=null){
            redisUtil.set(SysUserKey.getByName,""+name,user);
        }
        return user;
    }

    private void addCookie(HttpServletResponse response, String token, SysUser user) {
        redisUtil.set(SysUserKey.token, token, user);
        Cookie cookie = new Cookie(COOKI_NAME_TOKEN, token);
        cookie.setMaxAge(SysUserKey.token.expireSeconds());
        cookie.setPath("/");
        response.addCookie(cookie);
    }

    public SysUser getByTokan(HttpServletResponse response,String token) {
        if(StringUtils.isEmpty(token)){
            return null;
        }
        SysUser user = redisUtil.get(SysUserKey.token,token,SysUser.class);
        if(user!=null){
            //延长有效期
            addCookie(response, token, user);
            return user;
        }
        return null;
    }
}
