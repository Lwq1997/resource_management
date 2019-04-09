package com.lwq.service.impl;

import com.lwq.common.Const;
import com.lwq.common.ServerResponse;
import com.lwq.dao.SysUserMapper;
import com.lwq.model.SysUser;
import com.lwq.service.ILoginAndAclControlService;
import com.lwq.util.MD5Util;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Author: Lwq
 * @Date: 2019/4/3 15:35
 * @Version 1.0
 * @Describe
 */
@Service
public class LoginAndAclControlAndAclControlServiceImpl implements ILoginAndAclControlService {

    @Autowired
    SysUserMapper sysUserMapper;

    /**
     * 登录
     * @param username
     * @param password
     * @return
     */
    @Override
    public ServerResponse<SysUser> login(String username, String password) {
        if(StringUtils.isBlank(username)){
            return ServerResponse.createByErrorMessage("用户名为空");
        }
        if(StringUtils.isBlank(password)){
            return ServerResponse.createByErrorMessage("密码为空");
        }
        SysUser sysUser = sysUserMapper.checkUser(username);
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
}
