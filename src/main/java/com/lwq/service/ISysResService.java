package com.lwq.service;

import com.lwq.beans.PageQuery;
import com.lwq.common.ServerResponse;
import com.lwq.model.SysRes;
import com.lwq.param.ResParam;

import java.util.List;

/**
 * @Author: Lwq
 * @Date: 2019/4/3 22:27
 * @Version 1.0
 * @Describe
 */
public interface ISysResService {
    ServerResponse detail(Integer resId);

    ServerResponse save(ResParam param);

    ServerResponse update(ResParam param);

    ServerResponse getPageByResModuleId(Integer resModuleId, PageQuery pageQuery);

    List<SysRes> getRoleResList(int roleId);

    List<SysRes> getUserResList(int userId);
}
