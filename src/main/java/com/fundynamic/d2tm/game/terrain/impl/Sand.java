package com.fundynamic.d2tm.game.terrain.impl;

import com.fundynamic.d2tm.graphics.Theme;

public class Sand extends DuneTerrain {

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
}