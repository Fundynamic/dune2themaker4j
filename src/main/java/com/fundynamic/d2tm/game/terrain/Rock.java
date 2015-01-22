package com.fundynamic.d2tm.game.terrain;

import com.fundynamic.d2tm.graphics.Theme;

public class Rock extends DuneTerrain implements ConstructionGround {

    public Rock(Theme theme) {
        super(theme);
    }

    @Override
    protected int getTerrainType() {
        return DuneTerrain.TERRAIN_ROCK;
    }
}
