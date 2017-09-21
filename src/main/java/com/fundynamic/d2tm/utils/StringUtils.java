package com.fundynamic.d2tm.utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

// Yet another string util class, because I refuse to include the whole world or JAR's for one or two methods
public class StringUtils {

    public static boolean isEmpty(String string) {
        if (string == null) return true;
        return string.length() == 0;
    }

    public static List<String> splitLenientToList(String string, String delimiter) {
        if (StringUtils.isEmpty(string)) {
            return Collections.emptyList();
        }
        String[] split = string.split(delimiter);
        List<String> result = new ArrayList<>();
        for (String val : split) {
            result.add(val.trim());
        }
        return result;

    }

    public static int parseIntOrDefault(String input, int defaultToReturn) {
        try {
            return Integer.parseInt(input);
        } catch (NumberFormatException e) {
            return defaultToReturn;
        }
    }
}
