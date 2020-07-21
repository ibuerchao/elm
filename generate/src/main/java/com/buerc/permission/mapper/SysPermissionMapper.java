package com.buerc.permission.mapper;

import com.buerc.permission.model.SysPermission;
import com.buerc.permission.model.SysPermissionExample;
import com.buerc.sys.dto.ResListParam;
import com.buerc.sys.vo.ResVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface SysPermissionMapper {
    long countByExample(SysPermissionExample example);

    int deleteByExample(SysPermissionExample example);

    int deleteByPrimaryKey(String id);

    int insert(SysPermission record);

    int insertSelective(SysPermission record);

    List<SysPermission> selectByExample(SysPermissionExample example);

    SysPermission selectByPrimaryKey(String id);

    int updateByExampleSelective(@Param("record") SysPermission record, @Param("example") SysPermissionExample example);

    int updateByExample(@Param("record") SysPermission record, @Param("example") SysPermissionExample example);

    int updateByPrimaryKeySelective(SysPermission record);

    int updateByPrimaryKey(SysPermission record);

    List<ResVo> list(ResListParam param);

    long count(ResListParam param);
}