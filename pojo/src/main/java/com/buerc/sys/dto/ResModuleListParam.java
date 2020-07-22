package com.buerc.sys.dto;

import com.buerc.common.constants.SysConstant;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.util.Date;

@Data
public class ResModuleListParam {
    @ApiModelProperty(value = "模块名称")
    private String name;

    @ApiModelProperty(value = "模块状态 1正常 0禁用")
    @Min(value = 0, message = "模块状态 1正常 0禁用")
    @Max(value = 1, message = "模块状态 1正常 0禁用")
    private Byte status;

    @ApiModelProperty(value = "操作人名称")
    private String operateName;

    @ApiModelProperty(value = "父级ID")
    private String parentId =SysConstant.Sys.DEFAULT_RES_MODULE_ID;

    @ApiModelProperty(value = "开始时间")
    private Date start;

    @ApiModelProperty(value = "结束时间")
    private Date end;

    @ApiModelProperty(value = "排序规则")
    private String order = "seq asc";

    @ApiModelProperty(value = "分页起始值")
    private Integer offset = SysConstant.Sys.OFFSET;

    @ApiModelProperty(value = "分页每页大小")
    private Integer limit = SysConstant.Sys.LIMIT;

    //当以下参数有值时，全局条件查询而不是只查parentId层级
    public String getParentId(){
        if (SysConstant.Sys.DEFAULT_RES_MODULE_ID.equals(parentId)){
            if (StringUtils.isNotBlank(name) || status!=null || StringUtils.isNotBlank(operateName) || start!=null || end!=null){
                return null;
            }
        }
        return parentId;
    }
}
