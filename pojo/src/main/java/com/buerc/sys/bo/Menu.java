package com.buerc.sys.bo;

import lombok.Data;

import java.util.List;

/**
 * [{
 *     name: '系统',
 *     path: '/system',
 *     hidden: false,
 *     component:'Main',
 *     meta:{'icon':'el-icon-message'},
 *     children: [
 *       {
 *         name: '部门管理',
 *         path: '/system/dept',
 *         component: 'system/dept/Department',
 *         hidden: false,
 *         meta:{'icon':'el-icon-message'},
 *       },
 *       {
 *         name: '用户管理',
 *         path: '/system/user',
 *         component: 'system/user/User',
 *         hidden: false,
 *         meta:{'icon':'el-icon-loading'},
 *       }
 *     ]
 *   }]
 */
@Data
public class Menu {
    //菜单名称
    private String name;
    //菜单路径
    private String path;
    //是否隐藏
    private boolean hidden;
    //组件路径
    private String component;
    //元信息 {'icon':'el-icon-message'}
    private String meta;
    //子菜单
    private List<Menu> children;
}
