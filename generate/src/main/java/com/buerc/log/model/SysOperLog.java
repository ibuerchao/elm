package com.buerc.log.model;

import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.util.Date;

public class SysOperLog implements Serializable {
    @ApiModelProperty(value = "主键id")
    private String id;

    @ApiModelProperty(value = "权限更新的类型，1：部门，2：用户，3：权限模块，4：权限，5：角色，6：角色用户关系，7：角色权限关系")
    private Byte module;

    @ApiModelProperty(value = "操作类型，1.增 2.删 3.改 4.查")
    private Byte type;

    @ApiModelProperty(value = "接口名称")
    private String name;

    @ApiModelProperty(value = "基于type后指定的对象id，比如用户、权限、角色等表的主键")
    private String targetId;

    @ApiModelProperty(value = "用户id")
    private String operateId;

    @ApiModelProperty(value = "操作者")
    private String operateName;

    @ApiModelProperty(value = "最后一次更新的时间")
    private Date operateTime;

    @ApiModelProperty(value = "最后一次更新者的ip地址")
    private String operateIp;

    @ApiModelProperty(value = "当前是否复原过，0：没有，1：复原过")
    private Byte status;

    @ApiModelProperty(value = "接口参数")
    private String args;

    @ApiModelProperty(value = "返回值")
    private String result;

    @ApiModelProperty(value = "旧值")
    private String oldValue;

    @ApiModelProperty(value = "新值")
    private String newValue;

    private static final long serialVersionUID = 1L;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Byte getModule() {
        return module;
    }

    public void setModule(Byte module) {
        this.module = module;
    }

    public Byte getType() {
        return type;
    }

    public void setType(Byte type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTargetId() {
        return targetId;
    }

    public void setTargetId(String targetId) {
        this.targetId = targetId;
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

    public Byte getStatus() {
        return status;
    }

    public void setStatus(Byte status) {
        this.status = status;
    }

    public String getArgs() {
        return args;
    }

    public void setArgs(String args) {
        this.args = args;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getOldValue() {
        return oldValue;
    }

    public void setOldValue(String oldValue) {
        this.oldValue = oldValue;
    }

    public String getNewValue() {
        return newValue;
    }

    public void setNewValue(String newValue) {
        this.newValue = newValue;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", module=").append(module);
        sb.append(", type=").append(type);
        sb.append(", name=").append(name);
        sb.append(", targetId=").append(targetId);
        sb.append(", operateId=").append(operateId);
        sb.append(", operateName=").append(operateName);
        sb.append(", operateTime=").append(operateTime);
        sb.append(", operateIp=").append(operateIp);
        sb.append(", status=").append(status);
        sb.append(", args=").append(args);
        sb.append(", result=").append(result);
        sb.append(", oldValue=").append(oldValue);
        sb.append(", newValue=").append(newValue);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}