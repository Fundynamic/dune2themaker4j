package com.fundynamic.d2tm.game.terrain.impl;

import com.fundynamic.d2tm.graphics.Theme;

public class SandHill extends DuneTerrain {

    public SandHill(Theme theme) {
        super(theme);
    }

    @Override
    protected int getTerrainType() {
        return TERRAIN_SAND_HILL;
    }

}
