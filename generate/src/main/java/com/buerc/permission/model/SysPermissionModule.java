package com.buerc.permission.model;

import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.util.Date;

public class SysPermissionModule implements Serializable {
    @ApiModelProperty(value = "权限模块id")
    private String id;

    @ApiModelProperty(value = "权限模块名称")
    private String name;

    @ApiModelProperty(value = "上级权限模块id")
    private String parentId;

    @ApiModelProperty(value = "权限模块层级")
    private String level;

    @ApiModelProperty(value = "权限模块在当前层级下的顺序，由小到大")
    private Integer seq;

    @ApiModelProperty(value = "状态，1：正常，0：冻结")
    private Byte status;

    @ApiModelProperty(value = "备注")
    private String remark;

    @ApiModelProperty(value = "用户id")
    private String operateId;

    @ApiModelProperty(value = "操作者")
    private String operateName;

    @ApiModelProperty(value = "最后一次操作时间")
    private Date operateTime;

    @ApiModelProperty(value = "最后一次更新操作者的ip地址")
    private String operateIp;

    private static final long serialVersionUID = 1L;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public Integer getSeq() {
        return seq;
    }

    public void setSeq(Integer seq) {
        this.seq = seq;
    }

    public Byte getStatus() {
        return status;
    }

    public void setStatus(Byte status) {
        this.status = status;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getOperateId() {
        return operateId;
    }

    public void setOperateId(String operateId) {
        this.operateId = operateId;
    }

    public String getOperateName() {
        return operateName;
    }

    public void setOperateName(String operateName) {
        this.operateName = operateName;
    }

    public Date getOperateTime() {
        return operateTime;
    }

    public void setOperateTime(Date operateTime) {
        this.operateTime = operateTime;
    }

    public String getOperateIp() {
        return operateIp;
    }

    public void setOperateIp(String operateIp) {
        this.operateIp = operateIp;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", name=").append(name);
        sb.append(", parentId=").append(parentId);
        sb.append(", level=").append(level);
        sb.append(", seq=").append(seq);
        sb.append(", status=").append(status);
        sb.append(", remark=").append(remark);
        sb.append(", operateId=").append(operateId);
        sb.append(", operateName=").append(operateName);
        sb.append(", operateTime=").append(operateTime);
        sb.append(", operateIp=").append(operateIp);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}