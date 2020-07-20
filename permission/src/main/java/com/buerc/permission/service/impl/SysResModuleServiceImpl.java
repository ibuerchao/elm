package com.buerc.permission.service.impl;

import com.buerc.common.constants.ResultCode;
import com.buerc.common.exception.BizException;
import com.buerc.common.utils.ValidateKit;
import com.buerc.permission.mapper.SysPermissionMapper;
import com.buerc.permission.mapper.SysPermissionModuleMapper;
import com.buerc.permission.model.SysPermissionModule;
import com.buerc.permission.service.SysResModuleService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class SysResModuleServiceImpl implements SysResModuleService {

    @Resource
    private SysPermissionModuleMapper sysPermissionModuleMapper;

    @Override
    public SysPermissionModule checkIdExist(String id) {
        ValidateKit.notBlank(id, ResultCode.RES_MODULE_ID_BLANK_MSG);
        SysPermissionModule module = sysPermissionModuleMapper.selectByPrimaryKey(id);
        if (module == null) {
            throw new BizException(ResultCode.PARAM_ERROR_CODE, ResultCode.RES_MODULE_NOT_EXIST_MSG);
        }
        return module;
    }
}
