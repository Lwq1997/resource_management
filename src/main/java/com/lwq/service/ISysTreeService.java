package com.lwq.service;

import com.lwq.dto.DeptLevelDto;
import com.lwq.dto.ResModuleLevelDto;

import java.util.List;

/**
 * @Author: Lwq
 * @Date: 2019/4/3 16:42
 * @Version 1.0
 * @Describe
 */
public interface ISysTreeService {

    List<DeptLevelDto> deptTree();

    List<ResModuleLevelDto> resModuleTree();

    List<ResModuleLevelDto> roleTree(int roleId);

}
