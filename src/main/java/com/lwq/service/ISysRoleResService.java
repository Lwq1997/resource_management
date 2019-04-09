package com.lwq.service;

import java.util.List;

/**
 * @Author: Lwq
 * @Date: 2019/4/4 13:54
 * @Version 1.0
 * @Describe
 */
public interface ISysRoleResService {

    void changeRoleRess(int roleId, List<Integer> resIdList);

}
