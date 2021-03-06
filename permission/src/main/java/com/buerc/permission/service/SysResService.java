package com.buerc.permission.service;

import com.buerc.common.web.Result;
import com.buerc.permission.model.SysPermission;
import com.buerc.sys.dto.*;
import com.buerc.sys.vo.ResVo;

import java.util.List;

public interface SysResService {
    SysPermission add(ResFormParam param);

    void delete(String id);

    SysPermission edit(ResFormParam param);

    SysPermission detail(String id);

    boolean updateStatus(UpdateStatusParam param);

    Result<List<ResVo>> list(ResListParam param);
}
