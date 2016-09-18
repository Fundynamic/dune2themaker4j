package com.fundynamic.d2tm.game.rendering.gui.battlefield;

import com.fundynamic.d2tm.utils.Colors;
import org.newdawn.slick.Color;
import org.newdawn.slick.Image;
import org.newdawn.slick.ImageBuffer;

import java.util.ArrayList;
import java.util.List;

public class Recolorer {

    private List<Color> colorsToRecolor;

    public Recolorer() {
        this.colorsToRecolor = new ArrayList<>();
        // TODO: Make these configurable via INI file? So that you can configure which RGB's are identified as 'team colors'
        // TODO: and must be redrawn?
        colorsToRecolor.add(Colors.create(214, 0, 0));
        colorsToRecolor.add(Colors.create(182, 0, 0));
        colorsToRecolor.add(Colors.create(153, 0, 0));
        colorsToRecolor.add(Colors.create(125, 0, 0));
        colorsToRecolor.add(Colors.create(89, 0, 0));
        colorsToRecolor.add(Colors.create(60, 0, 0));
        colorsToRecolor.add(Colors.create(32, 0, 0));
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
            return Colors.create(src.getGreen(), src.getRed(), src.getBlue(), src.getAlpha());
          case BLUE:
            return Colors.create(src.getBlue(), src.getGreen(), src.getRed(), src.getAlpha());
          // TODO: factions? Configurable in INI file? etc
          default:
            return src;
        }
    }

    public boolean isColorToRecolor(Color src) {
        return colorsToRecolor.contains(src);
    }
}

