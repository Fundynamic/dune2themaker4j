package com.fundynamic.d2tm.game.terrain.impl;

import com.fundynamic.d2tm.graphics.Theme;
import org.newdawn.slick.Color;

public class Sand extends DuneTerrain {

    private static final Color terrainColor = new Color(180, 120, 55);

    public Sand(Theme theme) {
        super(theme);
    }

    public Sand() {
        super(null);
    }

    @Override
    public int getTerrainType() {
        return TERRAIN_SAND;
    }

    @Override
    public Color getTerrainColor() {
        return terrainColor;
    }
}