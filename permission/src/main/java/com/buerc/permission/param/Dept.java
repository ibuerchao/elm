package com.buerc.permission.param;

import lombok.Data;

import javax.validation.constraints.*;

/**
 * 新增部门实体类
 */
@Data
public class Dept {
    @NotBlank(message = "部门名称不能为空")
    @Size(min = 1,max = 20,message = "部门名称长度在 1 到 20 个字符")
    private String name;

    private String parentId = "root";

    @NotNull(message = "状态不能为空")
    @Min(value = 0,message = "状态值最小为0")
    @Max(value = 1,message = "状态值最大为1")
    private Byte status;

    private String remark;
}
