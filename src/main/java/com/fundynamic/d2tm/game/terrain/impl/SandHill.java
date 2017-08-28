package com.fundynamic.d2tm.game.terrain.impl;

import com.fundynamic.d2tm.graphics.Theme;
import org.newdawn.slick.Color;

public class SandHill extends DuneTerrain {

    private static final Color terrainColor = new Color(180, 120, 55);

    public SandHill(Theme theme) {
        super(theme);
    }

    @Override
    public int getTerrainType() {
        return TERRAIN_SAND_HILL;
    }

    @Override
    public Color getTerrainColor() {
        return terrainColor;
    }
}
