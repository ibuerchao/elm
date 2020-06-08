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
}
