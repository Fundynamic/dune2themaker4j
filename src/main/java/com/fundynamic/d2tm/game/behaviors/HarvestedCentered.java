package com.fundynamic.d2tm.game.behaviors;

import com.fundynamic.d2tm.utils.Colors;
import org.newdawn.slick.Color;

public class HarvestedCentered extends HitPointBasedDestructibilityCentered {

    public HarvestedCentered(int max, int widthInPixels, int heightInPixels) {
        super(max, widthInPixels, heightInPixels);
    }

    @Override
    public Color getBarColor() {
        return Colors.LIGHT_BLUE;
    }
}
