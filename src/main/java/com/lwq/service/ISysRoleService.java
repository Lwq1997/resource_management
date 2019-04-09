package com.lwq.service;

import com.lwq.common.ServerResponse;
import com.lwq.model.SysRole;
import com.lwq.param.RoleParam;

import java.util.List;

/**
 * @Author: Lwq
 * @Date: 2019/4/4 13:35
 * @Version 1.0
 * @Describe
 */
public interface ISysRoleService {
    ServerResponse save(RoleParam param);

    ServerResponse update(RoleParam param);

    List<SysRole> getAll();
}
