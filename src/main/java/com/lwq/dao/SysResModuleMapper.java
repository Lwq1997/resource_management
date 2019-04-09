package com.lwq.dao;

import com.lwq.model.SysResModule;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface SysResModuleMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(SysResModule record);

    int insertSelective(SysResModule record);

    SysResModule selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(SysResModule record);

    int updateByPrimaryKey(SysResModule record);

    int countByNameAndParentId(@Param("parentId") Integer parentId, @Param("name") String name, @Param("id") Integer id);

    List<SysResModule> getChildresModuleListByLevel(@Param("level") String oldLevelPerfix);

    void batchUpdateLevel(@Param("sysResModuleList") List<SysResModule> sysResModuleList);

    int countByParentId(@Param("resModuleId") Integer resModuleId);

    List<SysResModule> getAllResModule();
}