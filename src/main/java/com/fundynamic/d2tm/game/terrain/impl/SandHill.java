package com.fundynamic.d2tm.game.terrain.impl;

import com.fundynamic.d2tm.graphics.Theme;

public class SandHill extends DuneTerrain {

    public SandHill(Theme theme) {
        super(theme);
    }

    @Override
    public int getTerrainType() {
        return TERRAIN_SAND_HILL;
    }

}
