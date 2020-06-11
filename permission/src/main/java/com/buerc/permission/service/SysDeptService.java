package com.buerc.permission.service;

import com.buerc.permission.model.SysDept;
import com.buerc.permission.param.Dept;

public interface SysDeptService {
    /**
     * 新增部门
     */
    SysDept add(Dept dept);

    /**
     * 编辑部门
     */
    SysDept edit(Dept dept);
}
