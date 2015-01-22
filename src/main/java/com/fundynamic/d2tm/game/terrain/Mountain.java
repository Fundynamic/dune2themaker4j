package com.fundynamic.d2tm.game.terrain;

import com.fundynamic.d2tm.graphics.Theme;

public class Mountain extends DuneTerrain {

    public Mountain(Theme theme) {
        super(theme);
    }

    @Override
    protected int getTerrainType() {
        return DuneTerrain.TERRAIN_MOUNTAIN;
    }
}
