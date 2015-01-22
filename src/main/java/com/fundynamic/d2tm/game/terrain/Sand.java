package com.fundynamic.d2tm.game.terrain;

import com.fundynamic.d2tm.graphics.Theme;

public class Sand extends DuneTerrain {

    public Sand(Theme theme) {
        super(theme);
    }

    @Override
    protected int getTerrainType() {
        return DuneTerrain.TERRAIN_SAND;
    }
}