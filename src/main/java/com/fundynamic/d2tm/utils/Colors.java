package com.fundynamic.d2tm.utils;


import org.newdawn.slick.Color;

import java.util.HashMap;
import java.util.Map;

public class Colors {

    public static final Color BLACK = Color.black;
    public static final Color WHITE = Color.white;
    public static final Color BLACK_ALPHA_128 = new Color(0,0,0,128);
    public static final Color WHITE_ALPHA_128 = new Color(255, 255, 255, 128);

    public static Map<String,Color> colorMap = new HashMap<>();

    /**
     * Creates a color and cashes it.
     * @param r
     * @param g
     * @param b
     * @param a
     * @return
     */
    public static Color create(int r, int g, int b, int a) {
        String key = "" + r + "-" + g + "-" + b + "-" + a;
        if (!colorMap.containsKey(key)) {
            colorMap.put(key, makeColor(r, g, b, a));
        }
        return colorMap.get(key);
    }

    private static Color makeColor(int r, int g, int b, int a) {
        if (a > -1) {
            return new Color(r, g, b, a);
        }
        return new Color(r, g, b);
    }

    /**
     * Shorthand for {@link #create(float, float, float, -1)} but with ints instead of floats
     * @param r
     * @param g
     * @param b
     * @return
     */
    public static Color create(int r, int g, int b) {
        return create(r, g, b, -1);
    }

    /**
     * Shorthand for {@link #create(float, float, float, -1)}
     * @param r
     * @param g
     * @param b
     * @return
     */
    public static Color create(float r, float g, float b) {
        return create((int)(r * 255.0f), (int)(g * 255.0f), (int)(b * 255.0f), -1);
    }

    public static String toString(Color c) {
        return "Color(" + c.getRed() + "," + c.getGreen() + "," + c.getBlue() + "," + c.getAlpha() + ")";
    }
}
