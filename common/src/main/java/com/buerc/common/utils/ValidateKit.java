package com.buerc.common.utils;

import com.buerc.common.constants.ResultCode;
import com.buerc.common.exception.BizException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.CollectionUtils;

import java.util.Collection;
import java.util.Objects;

public class ValidateKit {

    public static void notNull(Object o, String message) {
        if (Objects.isNull(o)) {
            throw new BizException(ResultCode.PARAM_ERROR_CODE, message);
        }
    }

    public static void notBlank(CharSequence s, String message) {
        if (StringUtils.isBlank(s)) {
            throw new BizException(ResultCode.PARAM_ERROR_CODE, message);
        }
    }

    public static void notEmpty(Collection<?> c, String message) {
        if (CollectionUtils.isEmpty(c)) {
            throw new BizException(ResultCode.PARAM_ERROR_CODE, message);
        }
    }

    public static void assertTrue(boolean b, String message) {
        if (b) {
            throw new BizException(ResultCode.PARAM_ERROR_CODE, message);
        }
    }

    public static void illegalStr(String regEx, String targetMessage, String message) {
        if (targetMessage.matches(regEx)) {
            throw new BizException(ResultCode.PARAM_ERROR_CODE, message);
        }
    }
}
