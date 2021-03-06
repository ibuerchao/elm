package com.buerc.permission.exception;

import com.buerc.common.constants.ResultCode;
import com.buerc.common.exception.BizException;
import com.buerc.common.web.Result;
import org.apache.shiro.authz.UnauthorizedException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.NoHandlerFoundException;

@ControllerAdvice
public class MyExceptionHandler {

    @ExceptionHandler(UnauthorizedException.class)
    @ResponseBody
    public Result unauthorized(){
        return Result.error(ResultCode.NO_PERMISSION_CODE,ResultCode.NO_PERMISSION_MSG);
    }

    @ExceptionHandler(BizException.class)
    @ResponseBody
    public Result bizException(BizException e){
        return Result.error(e.getCode(),e.getMessage());
    }

    @ExceptionHandler(NoHandlerFoundException.class)
    @ResponseBody
    public Result noHandlerFound(){
        return Result.error(ResultCode.URL_NOT_EXIST_CODE,ResultCode.URL_NOT_EXIST_MSG);
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    @ResponseBody
    public Result methodNotSupported(){
        return Result.error(ResultCode.METHOD_NOT_SUPPORTED_CODE,ResultCode.METHOD_NOT_SUPPORTED_MSG);
    }

    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    @ResponseBody
    public Result exception(){
        return Result.error(ResultCode.CONTENT_TYPE_ERROR_CODE,ResultCode.CONTENT_TYPE_ERROR_MSG);
    }

    @ExceptionHandler(Exception.class)
    @ResponseBody
    public Result exception(Exception e){
//        return Result.fail(e.getMessage());
        return Result.fail("出现bug啦");
    }
}
