package com.buerc.permission.service;

import com.buerc.common.utils.TreeNode;
import com.buerc.common.web.Result;
import com.buerc.permission.model.SysDept;
import com.buerc.sys.dto.DeptFormParam;
import com.buerc.sys.dto.DeptListParam;
import com.buerc.sys.dto.UpdateStatusParam;
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

    /**
     * 删除部门
     */
    void delete(String id);

    /**
     * 查询当前节点的部门树
     */
    List<TreeNode> superior(String id,String status);

    /**
     * 更新部门状态
     */
    boolean updateStatus(UpdateStatusParam param);

    /**
     * 验证部门ID是否存在
     * @param id
     * @return
     */
    SysDept checkIdExist(String id);
}
