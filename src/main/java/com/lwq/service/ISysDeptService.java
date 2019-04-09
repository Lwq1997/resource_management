package com.lwq.service;

import com.lwq.common.ServerResponse;
import com.lwq.param.DeptParam;

/**
 * @Author: Lwq
 * @Date: 2019/4/3 16:42
 * @Version 1.0
 * @Describe
 */
public interface ISysDeptService {

    ServerResponse save(DeptParam param);

    ServerResponse update(DeptParam param);

    ServerResponse delete(int deptId);
}
