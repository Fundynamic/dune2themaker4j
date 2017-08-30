package com.fundynamic.d2tm.game.rendering.gui.battlefield;

import com.fundynamic.d2tm.game.entities.Faction;
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
        colorsToRecolor.add(new Color(214, 0, 0));
        colorsToRecolor.add(new Color(182, 0, 0));
        colorsToRecolor.add(new Color(153, 0, 0));
        colorsToRecolor.add(new Color(125, 0, 0));
        colorsToRecolor.add(new Color(89, 0, 0));
        colorsToRecolor.add(new Color(60, 0, 0));
        colorsToRecolor.add(new Color(32, 0, 0));
    }

    /**
     * Given a base image, it will copy its buffer and recolor it. Returning a new Image as result.
     *
     * TODO: Use caching here? (or in its callee)
     *
     * @param image
     * @param faction
     * @return
     */
    public Image createCopyRecoloredToFaction(Image image, Faction faction) {
        final int width = image.getWidth();
        final int height = image.getHeight();

        ImageBuffer buffer = new ImageBuffer(width, height);
        for (int x = 0; x < buffer.getWidth(); x++) {
            for (int y = 0; y < buffer.getHeight(); y++) {
                final Color pixel = image.getColor(x, y);
                final Color factionColor = createCopyRecoloredToFaction(pixel, faction);
                buffer.setRGBA(x, y, factionColor.getRed(), factionColor.getGreen(), factionColor.getBlue(), pixel.getAlpha());
            }
        }
        return buffer.getImage();
    }

    public Color createCopyRecoloredToFaction(Color src, Faction faction) {
        if (!isColorToRecolor(src)) {
          return src;
        }

        int srcGreen = src.getGreen();
        int srcRed = src.getRed();
        int srcBlue = src.getBlue();
        int srcAlpha = src.getAlpha();

        switch (faction) {
          case GREEN:
            return Colors.create(srcGreen, srcRed, srcBlue, srcAlpha);
          case BLUE:
            return Colors.create(srcBlue, srcGreen, srcRed, srcAlpha);
          // TODO: factions? Configurable in INI file? etc
          default:
            return src;
        }

    }

    public boolean isColorToRecolor(Color src) {
        return colorsToRecolor.contains(src);
    }
}

