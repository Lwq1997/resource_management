package com.lwq.dao;

import com.lwq.beans.PageQuery;
import com.lwq.model.SysRes;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface SysResMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(SysRes record);

    int insertSelective(SysRes record);

    SysRes selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(SysRes record);

    int updateByPrimaryKey(SysRes record);

    int countByResModuleId(@Param("resModuleId") Integer resModuleId);

    List<SysRes> getPageByResModuleId(@Param("resModuleId") Integer resModuleId, @Param("page") PageQuery page);

    int countByNameAndResModuleId(@Param("resModuleId") Integer resModuleId,@Param("name") String name,@Param("id") Integer id);

    int countByCodeAndResModuleId(@Param("resModuleId") Integer resModuleId,@Param("id") Integer id, @Param("code") String code);

    List<SysRes> getAll();

    List<SysRes> getByIdList(@Param("resIdList") List<Integer> roleIdList);
}