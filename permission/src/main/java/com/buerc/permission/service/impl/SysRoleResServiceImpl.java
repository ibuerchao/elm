package com.buerc.permission.service.impl;

import com.buerc.CodeUtil;
import com.buerc.common.utils.DateUtil;
import com.buerc.permission.enums.CodeConfigEnum;
import com.buerc.permission.mapper.SysRolePermissionMapper;
import com.buerc.permission.model.SysRolePermission;
import com.buerc.permission.model.SysRolePermissionExample;
import com.buerc.permission.service.SysRoleResService;
import com.buerc.permission.service.SysRoleService;
import com.buerc.sys.dto.RoleResFormParam;
import com.buerc.sys.dto.RoleResListParam;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Service
public class SysRoleResServiceImpl implements SysRoleResService {

    @Resource
    private SysRoleService sysRoleService;

    @Resource
    private SysRolePermissionMapper sysRolePermissionMapper;

    @Override
    public void save(RoleResFormParam param) {
        sysRoleService.detail(param.getRoleId());
        delete(param);
        insert(param);
    }

    private void delete(RoleResFormParam param){
        SysRolePermissionExample example = new SysRolePermissionExample();
        example.createCriteria().andRoleIdEqualTo(param.getRoleId()).andTargetTypeEqualTo(param.getTargetType());
        sysRolePermissionMapper.deleteByExample(example);
    }

    private void insert(RoleResFormParam param){
        List<String> targetIds = param.getTargetIds();
        List<SysRolePermission> list = new ArrayList<>();
        for (String targetId:targetIds){
            SysRolePermission sysRolePermission = new SysRolePermission();
            sysRolePermission.setId(CodeUtil.getCode(CodeConfigEnum.ROLE_RES.getKey(), DateUtil.formatShortCompact()));
            sysRolePermission.setRoleId(param.getRoleId());
            sysRolePermission.setTargetId(targetId);
            sysRolePermission.setTargetType(param.getTargetType());
            list.add(sysRolePermission);
        }
        sysRolePermissionMapper.insertBatch(list);
    }

    @Override
    public List<SysRolePermission> list(RoleResListParam param) {
        SysRolePermissionExample example = new SysRolePermissionExample();
        example.createCriteria().andRoleIdEqualTo(param.getRoleId()).andTargetTypeEqualTo(param.getTargetType());
        return sysRolePermissionMapper.selectByExample(example);
    }
}
