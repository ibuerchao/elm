package com.buerc.sys.bo;

import com.buerc.sys.vo.UserVo;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Set;

@Data
public class UserInfo {
    @ApiModelProperty(value = "用户信息")
    private UserVo info;
    @ApiModelProperty(value = "角色集合")
    private Set<String> roles;
    @ApiModelProperty(value = "权限集合")
    private Set<String> permissions;
}
