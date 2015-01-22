package com.fundynamic.d2tm.game.terrain.impl;

import com.fundynamic.d2tm.graphics.Theme;

public class Sand extends DuneTerrain {

    public Sand(Theme theme) {
        super(theme);
    }

    @Override
    protected int getTerrainType() {
        return TERRAIN_SAND;
    }
}