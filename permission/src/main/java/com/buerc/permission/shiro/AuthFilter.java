package com.buerc.permission.shiro;

import com.buerc.common.constants.RedisConstant;
import com.buerc.common.constants.ResultCode;
import com.buerc.common.exception.BizException;
import com.buerc.common.utils.ApplicationContextUtil;
import com.buerc.common.utils.JSONUtil;
import com.buerc.common.utils.JwtTokenUtil;
import com.buerc.common.web.Result;
import com.buerc.permission.mapper.SysUserMapper;
import com.buerc.permission.model.SysUser;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.web.filter.authc.AuthenticatingFilter;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.URLEncoder;

public class AuthFilter extends AuthenticatingFilter {

    /**
     *  options方法则直接通过
     */
    @Override
    protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) {
        return ((HttpServletRequest) request).getMethod().equals(RequestMethod.OPTIONS.name());
    }

    @Override
    protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws Exception {
        String token = getRequestToken(request);
        //token不存在直接返回token不存在信息
        if (StringUtils.isBlank(token)){
            sendErrorMessage(response);
            return false;
        }
        //由于AuthFilter继承链路是javax.servlet.Filter,在DispatchServlet之前,如果期间抛出异常，spring全局异常增强器将无法捕获
        //所以在验证token时如果有异常，则表示token过期或者非法，此时throw出去的异常无法被spring异常处理器捕获，则重定向到未登录接口返回友好提示
        try {
            JwtTokenUtil jwtTokenUtil = ApplicationContextUtil.getApplicationContext().getBean(JwtTokenUtil.class);
            String id = jwtTokenUtil.getUserId(token);
            SysUserMapper sysUserMapper = ApplicationContextUtil.getApplicationContext().getBean(SysUserMapper.class);
            SysUser user = sysUserMapper.selectByPrimaryKey(id);
            if (user == null){
                throw new BizException(ResultCode.USER_NOT_EXIST_CODE,ResultCode.USER_NOT_EXIST_MSG);
            }
        }catch (BizException e){
            HttpServletResponse httpServletResponse = (HttpServletResponse) response;
            //设置编码，否则中文字符在重定向时会变为空字符串
            String msg = URLEncoder.encode(e.getMessage(), "UTF-8");
            httpServletResponse.sendRedirect("/unauthorized?code="+e.getCode()+"&msg="+ msg);
            return false;
        }

        return executeLogin(request, response);
    }

    @Override
    protected AuthenticationToken createToken(ServletRequest servletRequest, ServletResponse servletResponse) {
        return new AuthToken(getRequestToken(servletRequest));
    }

    /**
     * 获取请求头中token
     * @param servletRequest
     * @return
     */
    private String getRequestToken(ServletRequest servletRequest){
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        return request.getHeader(RedisConstant.PARAM_HEADER);
    }

    /**
     * 发送错误信息，提示请先登录
     * @param response
     */
    private void sendErrorMessage(ServletResponse response){
        try {
            HttpServletResponse httpResponse = (HttpServletResponse) response;
            httpResponse.setHeader("Access-Control-Allow-Credentials", "true");
            httpResponse.setHeader("Content-Type", "application/json");
            httpResponse.setCharacterEncoding("UTF-8");

            String json = JSONUtil.toStr(Result.tokenNotExist());
            httpResponse.getWriter().print(json);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
