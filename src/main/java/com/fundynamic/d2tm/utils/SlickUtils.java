package com.fundynamic.d2tm.utils;


import com.fundynamic.d2tm.math.Coordinate;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;

public class SlickUtils {

    public static void drawPercentage(Graphics graphics, Color color, float percentageAsFloat, int x, int y) {
        int progress = (int)(percentageAsFloat * 100F);
        if (progress < 10) {
            drawShadowedText(graphics, color, "00" + progress + " %", x, y);
        } else if (progress < 100) {
            drawShadowedText(graphics, color, "0" + progress + " %", x, y);
        } else if (progress < 1000) {
            drawShadowedText(graphics, color, "" + progress + " %", x, y);
        }
    }

    /**
     * Draws a text with shadow
     */
    public static void drawShadowedText(Graphics graphics, Color color, String msg, int x, int y) {
        drawText(graphics, Colors.BLACK_ALPHA_128, msg, x + 2, y + 2); // shadow
        drawText(graphics, color, msg, x, y); // text
    }

    public static void drawLine(Graphics graphics, Coordinate from, Coordinate to) {
        graphics.drawLine(from.getXAsInt(), from.getYAsInt(), to.getXAsInt(), to.getYAsInt());
    }

    /**
     * Draws a text
     */
    public static void drawText(Graphics graphics, Color color, String msg, int x, int y) {
        graphics.setColor(color);
        graphics.drawString(msg, x, y);
    }

}
