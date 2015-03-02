package com.fundynamic.d2tm.game.rendering;

import org.newdawn.slick.Color;
import org.newdawn.slick.ImageBuffer;
import org.newdawn.slick.SpriteSheet;

public enum Palette {
    RED(java.awt.Color.RED),
    GREEN(java.awt.Color.GREEN),
    BLUE(java.awt.Color.BLUE);

    private final float hue;

    private Palette(final java.awt.Color color) {
        this(java.awt.Color.RGBtoHSB(color.getRed(), color.getGreen(), color.getBlue(), null)[0]);
    }

    private Palette(final float hue) {
        this.hue = hue;
    }

    public final float getHue() {
        return hue;
    }

    public SpriteSheet recolor(SpriteSheet spriteSheet) {
        final int width = spriteSheet.getWidth();
        final int height = spriteSheet.getHeight();

        ImageBuffer buffer = new ImageBuffer(width, height);
        for (int x = 0; x < buffer.getWidth(); x++) {
            for (int y = 0; y < buffer.getHeight(); y++) {
                final Color pixel = spriteSheet.getColor(x, y);
                final float[] hsbColor = java.awt.Color.RGBtoHSB(pixel.getRed(), pixel.getGreen(), pixel.getBlue(), null);
                final Color rgbColor = new Color(java.awt.Color.HSBtoRGB(hue, hsbColor[1], hsbColor[2]));
                buffer.setRGBA(x, y, rgbColor.getRed(), rgbColor.getGreen(), rgbColor.getBlue(), pixel.getAlpha());
            }
        }
        return new SpriteSheet(buffer.getImage(), width / spriteSheet.getHorizontalCount(), height / spriteSheet.getVerticalCount());
    }
}

