package com.lwq.dao;

import com.lwq.model.SysRoleRes;
import org.apache.ibatis.annotations.Param;

import java.util.ArrayList;
import java.util.List;

public interface SysRoleResMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(SysRoleRes record);

    int insertSelective(SysRoleRes record);

    SysRoleRes selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(SysRoleRes record);

    int updateByPrimaryKey(SysRoleRes record);

    void deleteByRoleId(@Param("roleId") Integer roleId);

    void batchInsert(@Param("roleResList") List<SysRoleRes> roleResList);

    List<Integer> getResIdListByRoleIdList(@Param("roleIdList") List<Integer> roleIdList);
}