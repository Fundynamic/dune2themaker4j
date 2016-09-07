package com.fundynamic.d2tm.utils;


import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;

public class SlickUtils {

    /**
     * Draws a text with shadow
     */
    public static void drawShadowedText(Graphics graphics, Color color, String msg, int x, int y) {
        drawText(graphics, Colors.BLACK_ALPHA_128, msg, x + 2, y + 2); // shadow
        drawText(graphics, color, msg, x, y); // text
    }

    /**
     * Draws a text
     */
    public static void drawText(Graphics graphics, Color color, String msg, int x, int y) {
        graphics.setColor(color);
        graphics.drawString(msg, x, y);
    }

}
