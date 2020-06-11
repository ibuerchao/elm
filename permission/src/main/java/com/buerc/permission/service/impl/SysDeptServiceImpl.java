package com.buerc.permission.service.impl;

import com.buerc.common.constants.ResultCode;
import com.buerc.common.exception.BizException;
import com.buerc.common.utils.ValidateKit;
import com.buerc.permission.mapper.SysDeptMapper;
import com.buerc.permission.model.SysDept;
import com.buerc.permission.model.SysDeptExample;
import com.buerc.permission.param.Dept;
import com.buerc.permission.service.SysDeptService;
import com.buerc.permission.util.IpUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
@Slf4j
public class SysDeptServiceImpl implements SysDeptService {
    @Resource
    private SysDeptMapper sysDeptMapper;

    @Override
    public SysDept add(Dept dept) {
        isDeptNameExist(dept.getParentId(),dept.getName());
        SysDept sysDept = new SysDept();
        BeanUtils.copyProperties(dept,sysDept);
        sysDept.setId(UUID.randomUUID().toString());
        sysDept.setSeq(getNextSeq(dept.getParentId()));
        sysDept.setOperateId("1");
        sysDept.setOperateName("2");
        sysDept.setOperateTime(new Date());
        sysDept.setOperateIp(IpUtil.getRemoteAddr());
        sysDeptMapper.insert(sysDept);
        return sysDept;
    }

    /**
     * 判断名称是否存在
     */
    private void isDeptNameExist(String parentId,String name){
        SysDeptExample example = new SysDeptExample();
        example.createCriteria().andNameEqualTo(name).andParentIdEqualTo(parentId);
        List<SysDept> depts = sysDeptMapper.selectByExample(example);
        if (CollectionUtils.isNotEmpty(depts)){
            throw new BizException(ResultCode.PARAM_ERROR_CODE,ResultCode.DEPT_NAME_REPEAT_MSG);
        }
    }

    /**
     * 查询父组织下最大的seq
     */
    private Integer getNextSeq(String parentId){
        SysDeptExample example = new SysDeptExample();
        example.setOrderByClause("seq desc");
        example.createCriteria().andParentIdEqualTo(parentId);
        List<SysDept> depts = sysDeptMapper.selectByExample(example);
        if (CollectionUtils.isNotEmpty(depts)){
            return depts.get(0).getSeq()+1;
        }else{
            return 1;
        }
    }

    @Override
    public SysDept edit(Dept dept) {
        ValidateKit.notBlank(dept.getId(),ResultCode.DEPT_ID_BLANK_MSG);
        SysDept sysDept = sysDeptMapper.selectByPrimaryKey(dept.getId());
        ValidateKit.notNull(sysDept,ResultCode.DEPT_NOT_EXIST_MSG);
        SysDept update = new SysDept();
        BeanUtils.copyProperties(dept,update);
        sysDeptMapper.updateByPrimaryKeySelective(update);
        return update;
    }
}
