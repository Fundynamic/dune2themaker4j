package com.fundynamic.d2tm.utils;

// Yet another string util class, because I refuse to include the whole world or JAR's for one or two methods
public class StringUtils {

    public static boolean isEmpty(String string) {
        if (string == null) return true;
        return string.length() == 0;
    }
}
