package com.fundynamic.d2tm.game.terrain.impl;

import com.fundynamic.d2tm.graphics.Theme;

public class Mountain extends DuneTerrain {

    public Mountain(Theme theme) {
        super(theme);
    }

    @Override
    public int getTerrainType() {
        return TERRAIN_MOUNTAIN;
    }
}
