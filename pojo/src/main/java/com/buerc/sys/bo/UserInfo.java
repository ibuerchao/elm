package com.buerc.sys.bo;

import com.buerc.sys.vo.UserVo;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
public class UserInfo {
    @ApiModelProperty(value = "用户信息")
    private UserVo info;
    @ApiModelProperty(value = "角色集合")
    private Set<String> roles = new HashSet<>();
    @ApiModelProperty(value = "权限集合")
    private Set<String> permissions = new HashSet<>();
    @ApiModelProperty(value = "菜单集合")
    private List<Menu> menus = new ArrayList<>();
    @ApiModelProperty(value = "按钮集合")
    private List<Button> buttons = new ArrayList<>();
}
