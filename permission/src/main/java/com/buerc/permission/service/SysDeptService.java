package com.buerc.permission.service;

import com.buerc.common.web.Result;
import com.buerc.permission.model.SysDept;
import com.buerc.sys.dto.DeptFormParam;
import com.buerc.sys.dto.DeptListParam;
import com.buerc.sys.vo.DeptVo;

import java.util.List;

public interface SysDeptService {
    /**
     * 新增部门
     */
    SysDept add(DeptFormParam dept);

    /**
     * 编辑部门
     */
    SysDept edit(DeptFormParam dept);

    /**
     * 部门列表
     */
    Result<List<DeptVo>> list(DeptListParam param);

    /**
     * 部门详情
     */
    DeptVo detail(String id);

    /**
     * 上移部门
     */
    void up(String id);

    /**
     * 下移部门
     */
    void down(String id);
}
