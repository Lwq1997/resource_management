package com.lwq.service;

import com.lwq.beans.PageQuery;
import com.lwq.common.ServerResponse;
import com.lwq.model.SysRes;
import com.lwq.model.SysUser;
import com.lwq.param.UserParam;

import java.util.List;

/**
 * @Author: Lwq
 * @Date: 2019/4/3 19:27
 * @Version 1.0
 * @Describe
 */
public interface ISysUserService {
    ServerResponse save(UserParam param);

    ServerResponse update(UserParam param);

    ServerResponse getPageByDeptId(int deptId, PageQuery pageQuery);

    List<SysUser> getAll();

    List<SysRes> userNoHaveRes(int userId);

    List<SysRes> userHaveRes(int userId);

    ServerResponse borrow(int userId, int resId);

    ServerResponse ret(int userId, int resId);

    ServerResponse find(int userId);
}
