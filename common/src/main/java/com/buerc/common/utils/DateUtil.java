package com.buerc.common.utils;

import com.buerc.common.constants.DateConstant;
import org.apache.commons.lang3.time.DateFormatUtils;

import java.util.Date;

public class DateUtil {

    public static String formatLong(){
        return DateFormatUtils.format(new Date(), DateConstant.FORMAT1);
    }

    public static String formatLongCompact(){
        return DateFormatUtils.format(new Date(), DateConstant.FORMAT2);
    }

    public static String formatShort(){
        return DateFormatUtils.format(new Date(), DateConstant.FORMAT3);
    }

    public static String formatShortCompact(){
        return DateFormatUtils.format(new Date(), DateConstant.FORMAT4);
    }
}
