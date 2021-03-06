package com.buerc.permission.mapper;

import com.buerc.common.utils.TreeNode;
import com.buerc.permission.model.SysPermissionModule;
import com.buerc.permission.model.SysPermissionModuleExample;
import com.buerc.sys.dto.ResListParam;
import com.buerc.sys.dto.ResModuleListParam;
import com.buerc.sys.vo.ResModuleVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

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

    List<TreeNode> tree(@Param("status") Integer status);

    List<ResModuleVo> list(ResModuleListParam param);

    long count(ResModuleListParam param);
}