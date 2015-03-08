package com.fundynamic.d2tm.game.rendering;

import org.newdawn.slick.Color;
import org.newdawn.slick.Image;
import org.newdawn.slick.ImageBuffer;

public enum Palette {
    RED,
    GREEN,
    BLUE;

    public Image recolor(Image image) {
        final int width = image.getWidth();
        final int height = image.getHeight();

        ImageBuffer buffer = new ImageBuffer(width, height);
        for (int x = 0; x < buffer.getWidth(); x++) {
            for (int y = 0; y < buffer.getHeight(); y++) {
                final Color pixel = image.getColor(x, y);
                final Color newColor = recolor(pixel);
                buffer.setRGBA(x, y, newColor.getRed(), newColor.getGreen(), newColor.getBlue(), pixel.getAlpha());
            }
        }
        return buffer.getImage();
    }

    public Color recolor(Color src) {
        // Only recolor when the color is a bright red hue. This assumes that
        // sprites are always red structures or units.
        if (src.getRed() < src.getGreen() + src.getBlue()) {
          return src;
        }

        switch (this) {
          case GREEN:
            return new Color(src.getGreen(), src.getRed(), src.getBlue(), src.getAlpha());
          case BLUE:
            return new Color(src.getBlue(), src.getGreen(), src.getRed(), src.getAlpha());
          default:
            return src;
        }
    }
}

