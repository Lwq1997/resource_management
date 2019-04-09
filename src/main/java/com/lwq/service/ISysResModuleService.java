package com.lwq.service;

import com.lwq.common.ServerResponse;
import com.lwq.param.ResModuleParam;

/**
 * @Author: Lwq
 * @Date: 2019/4/3 20:48
 * @Version 1.0
 * @Describe
 */
public interface ISysResModuleService {
    ServerResponse save(ResModuleParam param);

    ServerResponse update(ResModuleParam param);

    ServerResponse delete(int aclModuleId);
}
