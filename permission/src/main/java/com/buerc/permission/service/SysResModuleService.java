package com.buerc.permission.service;

import com.buerc.common.utils.TreeNode;
import com.buerc.common.web.Result;
import com.buerc.permission.model.SysPermissionModule;
import com.buerc.sys.dto.ResListParam;
import com.buerc.sys.dto.ResModuleFormParam;
import com.buerc.sys.dto.UpdateStatusParam;

import java.util.List;

public interface SysResModuleService {
    SysPermissionModule checkIdExist(String id);

    SysPermissionModule add(ResModuleFormParam param);

    void delete(String id);

    SysPermissionModule edit(ResModuleFormParam param);

    SysPermissionModule detail(String id);

    boolean updateStatus(UpdateStatusParam param);

    Result<List<SysPermissionModule>> list(ResListParam param);

    List<TreeNode> superior(String id, String status);

    void up(String id);

    void down(String id);
}
