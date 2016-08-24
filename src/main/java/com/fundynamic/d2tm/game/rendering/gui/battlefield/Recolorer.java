package com.fundynamic.d2tm.game.rendering.gui.battlefield;

import org.newdawn.slick.Color;
import org.newdawn.slick.Image;
import org.newdawn.slick.ImageBuffer;

import java.util.ArrayList;
import java.util.List;

public class Recolorer {

    private List<Color> colorsToRecolor;

    public Recolorer() {
        this.colorsToRecolor = new ArrayList<>();
        colorsToRecolor.add(new Color(214, 0, 0));
        colorsToRecolor.add(new Color(182, 0, 0));
        colorsToRecolor.add(new Color(153, 0, 0));
        colorsToRecolor.add(new Color(125, 0, 0));
        colorsToRecolor.add(new Color(89, 0, 0));
        colorsToRecolor.add(new Color(60, 0, 0));
        colorsToRecolor.add(new Color(32, 0, 0));
    }

    public enum FactionColor {
        RED, GREEN, BLUE
    }

    public Image recolorToFactionColor(Image image, FactionColor factionColor) {
        final int width = image.getWidth();
        final int height = image.getHeight();

        ImageBuffer buffer = new ImageBuffer(width, height);
        for (int x = 0; x < buffer.getWidth(); x++) {
            for (int y = 0; y < buffer.getHeight(); y++) {
                final Color pixel = image.getColor(x, y);
                final Color newColor = recolorToFactionColor(pixel, factionColor);
                buffer.setRGBA(x, y, newColor.getRed(), newColor.getGreen(), newColor.getBlue(), pixel.getAlpha());
            }
        }
        return buffer.getImage();
    }

    public Color recolorToFactionColor(Color src, FactionColor factionColor) {
        if (!isColorToRecolor(src)) {
          return src;
        }

        switch (factionColor) {
          case GREEN:
            return new Color(src.getGreen(), src.getRed(), src.getBlue(), src.getAlpha());
          case BLUE:
            return new Color(src.getBlue(), src.getGreen(), src.getRed(), src.getAlpha());
          // TODO: factions? Configurable in INI file? etc
          default:
            return src;
        }
    }

    public boolean isColorToRecolor(Color src) {
        return colorsToRecolor.contains(src);
    }
}

