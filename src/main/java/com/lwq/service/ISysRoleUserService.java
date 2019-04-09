package com.lwq.service;

import com.lwq.model.SysUser;

import java.util.List;

/**
 * @Author: Lwq
 * @Date: 2019/4/4 13:49
 * @Version 1.0
 * @Describe
 */
public interface ISysRoleUserService {

    void changeRoleUsers(int roleId, List<Integer> userIdList);

    List<SysUser> getListByRoleId(int roleId);

}
