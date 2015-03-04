package com.fundynamic.d2tm.game.rendering;

import org.newdawn.slick.Color;
import org.newdawn.slick.ImageBuffer;
import org.newdawn.slick.SpriteSheet;

public enum Palette {
    RED,
    GREEN,
    BLUE;

    public SpriteSheet recolor(SpriteSheet spriteSheet) {
        final int width = spriteSheet.getWidth();
        final int height = spriteSheet.getHeight();

        ImageBuffer buffer = new ImageBuffer(width, height);
        for (int x = 0; x < buffer.getWidth(); x++) {
            for (int y = 0; y < buffer.getHeight(); y++) {
                final Color pixel = spriteSheet.getColor(x, y);
                final Color newColor = recolor(pixel);
                buffer.setRGBA(x, y, newColor.getRed(), newColor.getGreen(), newColor.getBlue(), pixel.getAlpha());
            }
        }
        return new SpriteSheet(buffer.getImage(), width / spriteSheet.getHorizontalCount(), height / spriteSheet.getVerticalCount());
    }

    public Color recolor(Color color) {
        // Only recolor when the color is a bright red hue. This assumes that
        // sprites are always red structures or units.
        if (color.getRed() < color.getGreen() + color.getBlue()) {
          return color;
        }

        switch (this) {
          case GREEN:
            return new Color(color.getGreen(), color.getRed(), color.getBlue(), color.getAlpha());
          case BLUE:
            return new Color(color.getBlue(), color.getGreen(), color.getRed(), color.getAlpha());
          default:
            return color;
        }
    }
}

