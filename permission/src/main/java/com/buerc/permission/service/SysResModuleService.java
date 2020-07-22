package com.buerc.permission.service;

import com.buerc.common.utils.TreeNode;
import com.buerc.common.web.Result;
import com.buerc.permission.model.SysPermissionModule;
import com.buerc.sys.dto.ResListParam;
import com.buerc.sys.dto.ResModuleFormParam;
import com.buerc.sys.dto.ResModuleListParam;
import com.buerc.sys.dto.UpdateStatusParam;
import com.buerc.sys.vo.ResModuleVo;

import java.util.List;

public interface SysResModuleService {
    SysPermissionModule checkIdExist(String id);

    SysPermissionModule add(ResModuleFormParam param);

    void delete(String id);

    SysPermissionModule edit(ResModuleFormParam param);

    SysPermissionModule detail(String id);

    boolean updateStatus(UpdateStatusParam param);

    Result<List<ResModuleVo>> list(ResModuleListParam param);

    List<TreeNode> superior(String id, String status);

    void up(String id);

    void down(String id);
}
