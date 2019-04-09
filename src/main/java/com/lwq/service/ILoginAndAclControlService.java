package com.lwq.service;

import com.lwq.common.ServerResponse;
import com.lwq.model.SysUser;

/**
 * @Author: Lwq
 * @Date: 2019/4/3 15:34
 * @Version 1.0
 * @Describe
 */
public interface ILoginAndAclControlService {

    ServerResponse<SysUser> login(String username, String password);

    ServerResponse checkAdminRole(SysUser user);
}
