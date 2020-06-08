package com.buerc.common.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolationException;
import java.util.HashMap;

/**
 * 全局异常处理器，主要处理自定义异常以及spring抛出的异常
 */
public class DefaultExceptionResolver implements HandlerExceptionResolver {
    private static final Logger log = LoggerFactory.getLogger(DefaultExceptionResolver.class);

    @Override
    public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        Throwable cause = this.getRootCause(ex);
        String message;
        int code;
        if (cause instanceof BizException) {
            BizException bizException = (BizException) cause;
            message = bizException.getMessage();
            code = bizException.getCode();
        } else if (cause instanceof NoHandlerFoundException) {
            message = "请求URL[" + request.getRequestURI() + "]未找到";
            log.error(message);
            code = 404;
        } else if (cause instanceof HttpRequestMethodNotSupportedException) {
            message = "请求URL[" + request.getRequestURI() + "]不支持" + ((HttpRequestMethodNotSupportedException) cause).getMethod();
            log.error(message);
            code = 405;
        } else if (!(cause instanceof ConstraintViolationException) && !(cause instanceof MethodArgumentNotValidException)) {
            log.error("Request URI:" + request.getRequestURI(), ex);
            message = "服务器异常，请稍后再试";
            code = 500;
        } else {
            message = "请求参数非法";
            code = 400;
        }

        return writeJsonToResponse(message, code);
    }

    private Throwable getRootCause(Throwable t) {
        Throwable cause = t.getCause();
        return cause == null ? t : this.getRootCause(cause);
    }

    private ModelAndView writeJsonToResponse(String msg, int code) {
        MappingJackson2JsonView view = new MappingJackson2JsonView();
        HashMap<String, Object> model = new HashMap<>(2);
        model.put("code", code);
        model.put("msg", msg);
        return new ModelAndView(view, model);
    }

    public DefaultExceptionResolver() {
    }
}
