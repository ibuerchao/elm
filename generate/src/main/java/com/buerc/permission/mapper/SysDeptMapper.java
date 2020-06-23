package com.buerc.permission.mapper;

import com.buerc.common.utils.TreeNode;
import com.buerc.permission.model.SysDept;
import com.buerc.permission.model.SysDeptExample;
import com.buerc.sys.dto.DeptListParam;
import com.buerc.sys.vo.DeptVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface SysDeptMapper {
    long countByExample(SysDeptExample example);

    int deleteByExample(SysDeptExample example);

    int deleteByPrimaryKey(String id);

    int insert(SysDept record);

    int insertSelective(SysDept record);

    List<SysDept> selectByExample(SysDeptExample example);

    SysDept selectByPrimaryKey(String id);

    int updateByExampleSelective(@Param("record") SysDept record, @Param("example") SysDeptExample example);

    int updateByExample(@Param("record") SysDept record, @Param("example") SysDeptExample example);

    int updateByPrimaryKeySelective(SysDept record);

    int updateByPrimaryKey(SysDept record);

    List<DeptVo> list(DeptListParam param);

    long count(DeptListParam param);

    List<TreeNode> tree(@Param("status") Integer status);
}