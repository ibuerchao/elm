package com.buerc.permission.mapper;

import com.buerc.permission.model.SysPermissionModule;
import com.buerc.permission.model.SysPermissionModuleExample;
import io.swagger.annotations.ApiModelProperty;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface SysPermissionModuleMapper {
    long countByExample(SysPermissionModuleExample example);

    int deleteByExample(SysPermissionModuleExample example);

    int deleteByPrimaryKey(String id);

    int insert(SysPermissionModule record);

    int insertSelective(SysPermissionModule record);

    List<SysPermissionModule> selectByExample(SysPermissionModuleExample example);

    SysPermissionModule selectByPrimaryKey(String id);

    int updateByExampleSelective(@Param("record") SysPermissionModule record, @Param("example") SysPermissionModuleExample example);

    int updateByExample(@Param("record") SysPermissionModule record, @Param("example") SysPermissionModuleExample example);

    int updateByPrimaryKeySelective(SysPermissionModule record);

    int updateByPrimaryKey(SysPermissionModule record);
}