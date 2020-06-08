package com.buerc.common.exception;

public class BizException extends RuntimeException {
    private int code;

    public BizException(String message,Throwable throwable){
        super(message,throwable);
    }

    public BizException(int code,String message) {
        super(message);
        this.code = code;
    }

    public int getCode() {
        return code;
    }
}
