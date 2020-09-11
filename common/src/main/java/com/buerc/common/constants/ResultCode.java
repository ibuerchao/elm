package com.buerc.common.constants;

public interface ResultCode {
    int SUCCESS_CODE = 200;
    String SUCCESS_MSG = "成功";

    int INTERNAL_ERROR_CODE = 1000;
    String INTERNAL_ERROR_MSG = "服务器错误";

    int PARAM_ERROR_CODE = 1001;
    String PARAM_ERROR_MSG = "参数错误";

    int TOKEN_EXPIRED_CODE = 1002;
    String TOKEN_EXPIRED_MSG = "token已过期";

    int TOKEN_INVALID_CODE = 1003;
    String TOKEN_INVALID_MSG = "token非法";

    int USER_NOT_EXIST_CODE = 1004;
    String USER_NOT_EXIST_MSG = "用户不存在";

    int TOKEN_NOT_EXIST_CODE = 1005;
    String TOKEN_NOT_EXIST_MSG = "请先登录";

    int NO_PERMISSION_CODE = 1006;
    String NO_PERMISSION_MSG = "权限不足";

    int URL_NOT_EXIST_CODE = 1007;
    String URL_NOT_EXIST_MSG = "资源不存在";

    int METHOD_NOT_SUPPORTED_CODE = 1008;
    String METHOD_NOT_SUPPORTED_MSG = "请求方法不支持";

    int USER_OR_PASSWORD_ERROR_CODE = 1009;
    String USER_OR_PASSWORD_ERROR_MSG = "用户名或密码不正确";

    int CONTENT_TYPE_ERROR_CODE = 1010;
    String CONTENT_TYPE_ERROR_MSG = "Content-Type不支持";

    int USER_STATUS_ERROR_CODE = 1011;
    String USER_STATUS_ERROR_MSG = "用户非正常状态";

    int USER_EXIST_ERROR_CODE = 1012;
    String USER_EXIST_ERROR_MSG = "用户名已被占用";

    int MAIL_EXIST_ERROR_CODE = 1013;
    String MAIL_EXIST_ERROR_MSG = "邮箱已被注册";

    int INVALID_ENCRYPT_STR_CODE = 1014;
    String INVALID_ENCRYPT_STR_MSG = "非法加密字符串";

    String EMAIL_NOT_BLANK_MSG = "邮箱不能为空";

    String CODE_FAILURE_MSG="验证码已失效";
    String CODE_ERROR_MSG="验证码错误";
    String PASSWORD_ERROR_MSG = "密码不能为空";
    String PASSWORD_INVALID_MSG = "密码不正确";
    String PASSWORD_LENGTH_ERROR_MSG="密码长度为6-18之间";
    String DEPT_NAME_REPEAT_MSG="部门名称重复";
    String TOKEN_BLANK_MSG="token为空";
    String DEPT_ID_BLANK_MSG="部门ID为空";
    String DEPT_NOT_EXIST_MSG="部门不存在";
    String PARENT_DEPT_NOT_EXIST_MSG="父级部门不存在";
    String START_AND_END_INVALID_MSG="结束时间小于开始时间";
    String DEPT_CANNOT_MOVE_MSG="当前部门无法移动";
    String DEPT_CANNOT_DEL_MSG="当前部门存在子部门，无法删除";
    String DEPT_STATUS_INVALID_MSG="部门状态非法";
    String FORBID_DELETE_DEPT_MSG="禁止删除当前部门";
    String FORBID_DEPT_TO_CHILD_MSG="禁止部门挪到其子部门";
    String FORBID_DEPT_TO_SELF_MSG="禁止自己作为自己上级部门";
    String PARENT_MODULE_NOT_EXIST_MSG="父级模块不存在";
    String RES_NAME_REPEAT_MSG="资源名称重复";
    String RES_CODE_REPEAT_MSG="资源编码重复";
    String PHONE_EXIST_ERROR_MSG="手机号已被占用";
    String USER_ID_BLANK_MSG="用户ID为空";
    String USER_STATUS_INVALID_MSG="用户状态非法";
    String ROLE_CODE_EXIST_ERROR_MSG="角色编码已存在";
    String ROLE_NOT_EXIST_ERROR_MSG="角色不存在";
    String USER_NOT_EXIST_ERROR_MSG="用户不存在";
    String CONTAIN_NOT_EXIST_ROLE_MSG="包含不存在的角色";
    String CONTAIN_NOT_EXIST_USER_MSG="包含不存在的用户";
    String CONTAIN_NOT_EXIST_DEPT_MSG="包含不存在的组织";
    String CONTAIN_NOT_EXIST_MODULE_MSG="包含不存在的模块";
    String CONTAIN_NOT_EXIST_RES_MSG="包含不存在的资源";
    String ROLE_STATUS_NORMAL_MSG="当前角色为可用状态，请先禁用后再删除";
    String ROLE_STATUS_INVALID_MSG="用户状态非法";
    String RES_ID_BLANK_MSG="资源ID为空";
    String RES_MODULE_ID_BLANK_MSG="资源模块ID为空";
    String RES_NOT_EXIST_MSG="资源不存在";
    String RES_MODULE_NOT_EXIST_MSG="资源模块不存在";
    String FORBID_DELETE_RES_MSG="禁止删除当前资源";
    String RES_MODULE_NAME_REPEAT_MSG="模块名称重复";
    String RES_MODULE_MAX_MSG="当前层级已达到最大数量，无法添加";
    String FORBID_DELETE_RES_MODULE_MSG="禁止删除当前模块";
    String MODULE_CANNOT_DEL_MSG="当前模块存在子模块，无法删除";
    String FORBID_MODULE_TO_CHILD_MSG="禁止模块挪到其子模块";
    String FORBID_MODULE_TO_SELF_MSG="禁止自己作为自己上级模块";
    String MODULE_STATUS_INVALID_MSG="模块状态非法";
    String MODULE_CANNOT_MOVE_MSG="当前模块无法移动";
}
