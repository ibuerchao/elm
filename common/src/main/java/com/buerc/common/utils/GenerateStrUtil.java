package com.buerc.common.utils;

import java.util.Random;

public class GenerateStrUtil {
    private static final String ALL_CHAR = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final Integer ALL_CHAR_LENGTH = ALL_CHAR.length();

    public static String code(int length) {
        int count = 0;
        StringBuilder pwd = new StringBuilder();
        Random r = new Random();
        while (count < length) {
            pwd.append(ALL_CHAR.charAt(r.nextInt(ALL_CHAR_LENGTH)));
            count++;
        }
        return pwd.toString();
    }

    public static String like(String condition) {
        if (condition != null && !condition.equals("")) {
            return "%" + condition + "%";
        }
        return null;
    }
}
