package com.buerc.permission.service.impl;

import com.buerc.common.constants.ResultCode;
import com.buerc.common.constants.SysConstant;
import com.buerc.common.exception.BizException;
import com.buerc.common.utils.TreeUtil;
import com.buerc.common.utils.TreeNode;
import com.buerc.common.utils.ValidateKit;
import com.buerc.common.web.Result;
import com.buerc.permission.config.WebLogAspect;
import com.buerc.permission.mapper.SysDeptMapper;
import com.buerc.permission.model.SysDept;
import com.buerc.permission.model.SysDeptExample;
import com.buerc.permission.service.SysDeptService;
import com.buerc.sys.dto.DeptFormParam;
import com.buerc.sys.dto.DeptListParam;
import com.buerc.sys.vo.DeptVo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Slf4j
public class SysDeptServiceImpl implements SysDeptService {
    @Resource
    private SysDeptMapper sysDeptMapper;

    @Override
    public SysDept add(DeptFormParam dept) {
        checkParentIdIsExist(dept.getParentId());
        checkNameIsRepeat(null, dept.getParentId(), dept.getName());

        SysDept sysDept = new SysDept();
        BeanUtils.copyProperties(dept, sysDept);
        sysDept.setId(UUID.randomUUID().toString());
        sysDept.setSeq(getNextSeq(dept.getParentId()));
        sysDeptMapper.insert(sysDept);
        WebLogAspect.fillTextValue(null,sysDept);
        return sysDept;
    }

    /**
     * 判断名称是否存在
     */
    private void checkNameIsRepeat(String id, String parentId, String name) {
        SysDeptExample example = new SysDeptExample();
        example.createCriteria().andNameEqualTo(name).andParentIdEqualTo(parentId);
        List<SysDept> depts = sysDeptMapper.selectByExample(example);
        if (CollectionUtils.isNotEmpty(depts)) {
            if (StringUtils.isBlank(id)) {
                throw new BizException(ResultCode.PARAM_ERROR_CODE, ResultCode.DEPT_NAME_REPEAT_MSG);
            } else {
                List<String> collect = depts.stream().map(SysDept::getId).collect(Collectors.toList());
                if (!collect.contains(id)) {
                    throw new BizException(ResultCode.PARAM_ERROR_CODE, ResultCode.DEPT_NAME_REPEAT_MSG);
                }
            }
        }
    }

    /**
     * 验证父级ID是否存在
     */
    private void checkParentIdIsExist(String parentId) {
        if (!SysConstant.Sys.DEFAULT_DEPT_PARENT_ID.equals(parentId)) {
            SysDept sysDept = sysDeptMapper.selectByPrimaryKey(parentId);
            ValidateKit.notNull(sysDept, ResultCode.PARENT_DEPT_NOT_EXIST_MSG);
        }
    }

    /**
     * 查询父组织下最大的seq
     */
    private Integer getNextSeq(String parentId) {
        SysDeptExample example = new SysDeptExample();
        example.setOrderByClause("seq desc");
        example.createCriteria().andParentIdEqualTo(parentId);
        List<SysDept> depts = sysDeptMapper.selectByExample(example);
        if (CollectionUtils.isNotEmpty(depts)) {
            return depts.get(0).getSeq() + 1;
        } else {
            return 1;
        }
    }

    @Override
    public SysDept edit(DeptFormParam dept) {
        ValidateKit.notBlank(dept.getId(), ResultCode.DEPT_ID_BLANK_MSG);
        SysDept sysDept = checkIdExist(dept.getId());
        checkParentIdIsExist(dept.getParentId());
        checkNameIsRepeat(dept.getId(), dept.getParentId(), dept.getName());
        checkHierarchicalRelationship(dept.getId(), dept.getParentId());
        SysDept update = new SysDept();
        BeanUtils.copyProperties(dept, update);
        sysDeptMapper.updateByPrimaryKeySelective(update);
        WebLogAspect.fillTextValue(sysDept,update);
        return update;
    }

    /**
     * 检查层级关系(不允许将父级部门挪到其子级部门下)
     */
    private void checkHierarchicalRelationship(String id, String parentId) {
        List<SysDept> list = new ArrayList<>();
        childrenList(list, id, Boolean.TRUE);
        if (CollectionUtils.isNotEmpty(list)) {
            List<String> collect = list.stream().map(SysDept::getId).collect(Collectors.toList());
            if (collect.contains(parentId)) {
                throw new BizException(ResultCode.PARAM_ERROR_CODE, ResultCode.FORBID_DEPT_TO_CHILD_MSG);
            }
        }
    }

    @Override
    public Result<List<DeptVo>> list(DeptListParam param) {
        if (param.getEnd() != null && param.getStart() != null) {
            ValidateKit.assertTrue(param.getEnd().compareTo(param.getStart()) < 0, ResultCode.START_AND_END_INVALID_MSG);
        }
        return Result.success(sysDeptMapper.list(param), sysDeptMapper.count(param));
    }

    @Override
    public DeptVo detail(String id) {
        ValidateKit.notBlank(id, ResultCode.PARAM_ERROR_MSG);
        SysDept sysDept = checkIdExist(id);
        DeptVo deptVo = new DeptVo();
        BeanUtils.copyProperties(sysDept, deptVo);
        SysDeptExample example = new SysDeptExample();
        example.createCriteria().andParentIdEqualTo(sysDept.getId());
        List<SysDept> depts = sysDeptMapper.selectByExample(example);
        deptVo.setHasChildren(CollectionUtils.isNotEmpty(depts));
        return deptVo;
    }

    @Override
    @Transactional
    public void up(String id) {
        SysDept sysDept = checkIdExist(id);
        SysDept moveSysDept = getMoveSysDept(sysDept.getParentId(), sysDept.getSeq(), Boolean.TRUE);
        doMove(sysDept, moveSysDept);
    }

    @Override
    @Transactional
    public void down(String id) {
        SysDept sysDept = checkIdExist(id);
        SysDept moveSysDept = getMoveSysDept(sysDept.getParentId(), sysDept.getSeq(), Boolean.FALSE);
        doMove(sysDept, moveSysDept);
    }

    /**
     * 上移或者下移时被动更改seq的记录
     */
    private SysDept getMoveSysDept(String parentId, Integer seq, boolean flag) {
        SysDeptExample example = new SysDeptExample();
        SysDeptExample.Criteria criteria = example.createCriteria();
        criteria.andParentIdEqualTo(parentId);
        if (flag) {
            criteria.andSeqLessThan(seq);
            example.setOrderByClause("seq desc");
        } else {
            criteria.andSeqGreaterThan(seq);
            example.setOrderByClause("seq asc");
        }
        List<SysDept> sysDepts = sysDeptMapper.selectByExample(example);
        if (CollectionUtils.isEmpty(sysDepts)) {
            throw new BizException(ResultCode.PARAM_ERROR_CODE, ResultCode.DEPT_CANNOT_MOVE_MSG);
        }
        return sysDepts.get(0);
    }

    private void doMove(SysDept source, SysDept target) {
        SysDept update1 = new SysDept();
        SysDept update2 = new SysDept();

        update1.setId(source.getId());
        update1.setSeq(target.getSeq());

        update2.setId(target.getId());
        update2.setSeq(source.getSeq());

        sysDeptMapper.updateByPrimaryKeySelective(update1);
        sysDeptMapper.updateByPrimaryKeySelective(update2);
    }

    /**
     * 检测id是否合法
     */
    private SysDept checkIdExist(String id) {
        SysDept sysDept = sysDeptMapper.selectByPrimaryKey(id);
        if (sysDept == null) {
            throw new BizException(ResultCode.PARAM_ERROR_CODE, ResultCode.DEPT_NOT_EXIST_MSG);
        }
        return sysDept;
    }

    @Override
    @Transactional
    public void delete(String id) {
        ValidateKit.notBlank(id, ResultCode.DEPT_ID_BLANK_MSG);
        SysDept dept = checkIdExist(id);
        List<SysDept> list = new ArrayList<>();
        childrenList(list, id, Boolean.FALSE);
        if (CollectionUtils.isNotEmpty(list)) {
            throw new BizException(ResultCode.PARAM_ERROR_CODE, ResultCode.DEPT_CANNOT_DEL_MSG);
        }
        sysDeptMapper.deleteByPrimaryKey(id);
        WebLogAspect.fillTextValue(dept,null);
    }

    /**
     * 是否迭代获取当前节点的子节点
     */
    private void childrenList(List<SysDept> list, String id, boolean flag) {
        SysDeptExample example = new SysDeptExample();
        example.createCriteria().andParentIdEqualTo(id);
        List<SysDept> depts = sysDeptMapper.selectByExample(example);
        list.addAll(depts);
        if (flag) {
            if (CollectionUtils.isNotEmpty(depts)) {
                for (SysDept dept : depts) {
                    childrenList(list, dept.getId(), Boolean.TRUE);
                }
            }
        }
    }

    /**
     * 查询当前节点的部门树
     */
    @Override
    public List<TreeNode> superior(String id, String status) {
        Integer s = NumberUtils.toInt(status, -1);
        if (s.equals(-1)) {
            s = null;
        }
        List<TreeNode> data = sysDeptMapper.tree(s);
        return TreeUtil.tree(data,id);
    }
}
