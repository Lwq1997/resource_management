package com.lwq.dao;

import com.lwq.model.SysUserRecord;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface SysUserRecordMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(SysUserRecord record);

    int insertSelective(SysUserRecord record);

    SysUserRecord selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(SysUserRecord record);

    int updateByPrimaryKey(SysUserRecord record);

    List<Integer> selectResIdByUserId(@Param("userId") int userId);

    void delete(@Param("userId") int userId,@Param("resId") int resId);
}