package com.buerc.common.web;

import com.buerc.common.constants.ResultCode;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@ApiModel(value = "返回类")
public class Result<T> {
    @ApiModelProperty(value = "编码",required = true)
    private int code;
    @ApiModelProperty(value = "描述")
    private String msg;
    @ApiModelProperty(value = "总条数")
    private long total;
    @ApiModelProperty(value = "对象")
    private T data;

    public static Result success(){
        return new Result(ResultCode.SUCCESS_CODE,ResultCode.SUCCESS_MSG);
    }

    public static <T> Result<T> success(T data){
        return new Result<>(ResultCode.SUCCESS_CODE,data);
    }

    public static <T> Result<T> success(T data,long total){
        return new Result<>(ResultCode.SUCCESS_CODE,data,total);
    }

    public static Result fail(){
        return new Result(ResultCode.PARAM_ERROR_CODE,ResultCode.PARAM_ERROR_MSG);
    }

    public static Result error(){
        return new Result(ResultCode.INTERNAL_ERROR_CODE,ResultCode.INTERNAL_ERROR_MSG);
    }

    public static Result error(int code,String msg){
        return new Result(code,msg);
    }

    public static Result tokenNotExist(){
        return new Result(ResultCode.TOKEN_NOT_EXIST_CODE,ResultCode.TOKEN_NOT_EXIST_MSG);
    }

    private Result(int code) {
        this.code = code;
    }

    private Result(int code,String msg){
        this.code = code;
        this.msg = msg;
    }

    private Result(int code,T data){
        this.code = code;
        this.data = data;
    }

    private Result(int code,T data,long total){
        this.code = code;
        this.data = data;
        this.total = total;
    }
}
