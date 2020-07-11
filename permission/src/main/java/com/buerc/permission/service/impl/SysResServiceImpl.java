package com.buerc.permission.service.impl;

import com.buerc.common.constants.ResultCode;
import com.buerc.common.constants.SysConstant;
import com.buerc.common.exception.BizException;
import com.buerc.common.utils.ValidateKit;
import com.buerc.permission.mapper.SysPermissionMapper;
import com.buerc.permission.model.SysDept;
import com.buerc.permission.model.SysPermission;
import com.buerc.permission.model.SysPermissionExample;
import com.buerc.permission.service.SysResService;
import com.buerc.sys.dto.ResFormParam;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class SysResServiceImpl implements SysResService {

    @Resource
    private SysPermissionMapper sysPermissionMapper;

    @Override
    public SysPermission add(ResFormParam param) {
        checkParentModuleIsExist(param.getModuleId());
        checkNameIsRepeat(null, param.getModuleId(), param.getName());

//        SysDept sysDept = new SysDept();
//        BeanUtils.copyProperties(dept, sysDept);
//        sysDept.setId(UUID.randomUUID().toString());
//        sysDept.setSeq(getNextSeq(dept.getParentId()));
//        sysDeptMapper.insert(sysDept);
//        WebLogAspect.fillTextValue(null,sysDept);



        SysPermission sysPermission = new SysPermission();
        BeanUtils.copyProperties(param,sysPermission);
        sysPermissionMapper.insertSelective(sysPermission);
        return sysPermission;
    }

    /**
     * 验证父级模块是否存在
     */
    private void checkParentModuleIsExist(String moduleId) {
        if (!SysConstant.Sys.DEFAULT_RES_MODULE_ID.equals(moduleId)) {
            SysPermission sysPermission = sysPermissionMapper.selectByPrimaryKey(moduleId);
            ValidateKit.notNull(sysPermission, ResultCode.PARENT_MODULE_NOT_EXIST_MSG);
        }
    }

    /**
     * 判断名称是否存在
     */
    private void checkNameIsRepeat(String id, String parentId, String name) {
        SysPermissionExample example = new SysPermissionExample();
        example.createCriteria().andNameEqualTo(name).andModuleIdEqualTo(parentId);
        List<SysPermission> list = sysPermissionMapper.selectByExample(example);
        if (CollectionUtils.isNotEmpty(list)) {
            if (StringUtils.isBlank(id)) {
                throw new BizException(ResultCode.PARAM_ERROR_CODE, ResultCode.RES_NAME_REPEAT_MSG);
            } else {
                List<String> collect = list.stream().map(SysPermission::getId).collect(Collectors.toList());
                if (!collect.contains(id)) {
                    throw new BizException(ResultCode.PARAM_ERROR_CODE, ResultCode.RES_NAME_REPEAT_MSG);
                }
            }
        }
    }
}
