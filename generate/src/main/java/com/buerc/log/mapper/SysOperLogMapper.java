package com.buerc.log.mapper;

import com.buerc.log.model.SysOperLog;
import com.buerc.log.model.SysOperLogExample;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface SysOperLogMapper {
    long countByExample(SysOperLogExample example);

    int deleteByExample(SysOperLogExample example);

    int deleteByPrimaryKey(String id);

    int insert(SysOperLog record);

    int insertSelective(SysOperLog record);

    List<SysOperLog> selectByExampleWithBLOBs(SysOperLogExample example);

    List<SysOperLog> selectByExample(SysOperLogExample example);

    SysOperLog selectByPrimaryKey(String id);

    int updateByExampleSelective(@Param("record") SysOperLog record, @Param("example") SysOperLogExample example);

    int updateByExampleWithBLOBs(@Param("record") SysOperLog record, @Param("example") SysOperLogExample example);

    int updateByExample(@Param("record") SysOperLog record, @Param("example") SysOperLogExample example);

    int updateByPrimaryKeySelective(SysOperLog record);

    int updateByPrimaryKeyWithBLOBs(SysOperLog record);

    int updateByPrimaryKey(SysOperLog record);
}