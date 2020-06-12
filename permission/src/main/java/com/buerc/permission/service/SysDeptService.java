package com.buerc.permission.service;

import com.buerc.permission.model.SysDept;
import com.buerc.permission.param.Dept;
import com.buerc.permission.param.DeptListParam;

import java.util.List;

public interface SysDeptService {
    /**
     * 新增部门
     */
    SysDept add(Dept dept);

    /**
     * 编辑部门
     */
    SysDept edit(Dept dept);

    /**
     * 部门列表
     */
    List<SysDept> list(DeptListParam param);
}
