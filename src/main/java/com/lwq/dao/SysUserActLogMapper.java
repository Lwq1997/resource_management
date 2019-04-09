package com.lwq.dao;

import com.lwq.model.SysUserActLog;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface SysUserActLogMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(SysUserActLog record);

    int insertSelective(SysUserActLog record);

    SysUserActLog selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(SysUserActLog record);

    int updateByPrimaryKey(SysUserActLog record);

    List<String> find(@Param("userId") int userId);
}