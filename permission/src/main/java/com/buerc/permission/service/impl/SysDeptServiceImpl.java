package com.buerc.permission.service.impl;

import com.buerc.common.constants.ResultCode;
import com.buerc.common.constants.SysConstant;
import com.buerc.common.exception.BizException;
import com.buerc.common.utils.ValidateKit;
import com.buerc.permission.mapper.SysDeptMapper;
import com.buerc.permission.model.SysDept;
import com.buerc.permission.model.SysDeptExample;
import com.buerc.permission.param.Dept;
import com.buerc.permission.param.DeptListParam;
import com.buerc.permission.service.SysDeptService;
import com.buerc.permission.util.IpUtil;
import com.buerc.security.holder.SecurityContextHolder;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Slf4j
public class SysDeptServiceImpl implements SysDeptService {
    @Resource
    private SysDeptMapper sysDeptMapper;

    @Override
    public SysDept add(Dept dept) {
        checkParentIdIsExist(dept.getParentId());
        checkNameIsRepeat(null,dept.getParentId(),dept.getName());

        SysDept sysDept = new SysDept();
        BeanUtils.copyProperties(dept,sysDept);
        sysDept.setId(UUID.randomUUID().toString());
        sysDept.setSeq(getNextSeq(dept.getParentId()));
        sysDept.setOperateId(SecurityContextHolder.getUserId());
        sysDept.setOperateName(SecurityContextHolder.getUserName());
        sysDept.setOperateTime(new Date());
        sysDept.setOperateIp(IpUtil.getRemoteAddr());
        sysDeptMapper.insert(sysDept);
        return sysDept;
    }

    /**
     * 判断名称是否存在
     */
    private void checkNameIsRepeat(String id,String parentId,String name){
        SysDeptExample example = new SysDeptExample();
        example.createCriteria().andNameEqualTo(name).andParentIdEqualTo(parentId);
        List<SysDept> depts = sysDeptMapper.selectByExample(example);
        if (CollectionUtils.isNotEmpty(depts)){
            if (StringUtils.isBlank(id)){
                throw new BizException(ResultCode.PARAM_ERROR_CODE,ResultCode.DEPT_NAME_REPEAT_MSG);
            }else{
                List<String> collect = depts.stream().map(SysDept::getId).collect(Collectors.toList());
                if (!collect.contains(id)){
                    throw new BizException(ResultCode.PARAM_ERROR_CODE,ResultCode.DEPT_NAME_REPEAT_MSG);
                }
            }
        }
    }

    /**
     * 验证父级ID是否存在
     */
    private void checkParentIdIsExist(String parentId){
        if (!SysConstant.Sys.DEFAULT_DEPT_PARENT_ID.equals(parentId)){
            SysDept sysDept = sysDeptMapper.selectByPrimaryKey(parentId);
            ValidateKit.notNull(sysDept,ResultCode.PARENT_DEPT_NOT_EXIST_MSG);
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
        checkParentIdIsExist(dept.getParentId());
        checkNameIsRepeat(dept.getId(),dept.getParentId(),dept.getName());

        SysDept update = new SysDept();
        BeanUtils.copyProperties(dept,update);
        update.setOperateId(SecurityContextHolder.getUserId());
        update.setOperateName(SecurityContextHolder.getUserName());
        update.setOperateTime(new Date());
        update.setOperateIp(IpUtil.getRemoteAddr());
        sysDeptMapper.updateByPrimaryKeySelective(update);
        return update;
    }

    @Override
    public List<SysDept> list(DeptListParam param) {
        if (param.getEnd()!=null && param.getStart()!=null){
            ValidateKit.assertTrue(param.getEnd().compareTo(param.getStart())<0,ResultCode.START_AND_END_INVALID_MSG);
        }
        SysDeptExample example = new SysDeptExample();
        SysDeptExample.Criteria criteria = example.createCriteria();
        if (StringUtils.isNotBlank(param.getName())){
            criteria.andNameLike("%"+param.getName()+"%");
        }
        if (param.getStatus()!=null){
            criteria.andStatusEqualTo(param.getStatus());
        }
        if (StringUtils.isNotBlank(param.getOperateName())){
            criteria.andOperateNameLike("%"+param.getOperateName()+"%");
        }
        if (StringUtils.isNotBlank(param.getParentId())){
            criteria.andParentIdEqualTo(param.getParentId());
        }
        if (param.getStart()!=null){
            criteria.andOperateTimeGreaterThanOrEqualTo(param.getStart());
        }
        if (param.getEnd()!=null){
            criteria.andOperateTimeLessThanOrEqualTo(param.getEnd());
        }
        example.setOffset(param.getOffset());
        example.setLimit(param.getLimit());
        example.setOrderByClause("seq asc");
        return sysDeptMapper.selectByExample(example);
    }
}
