package com.fundynamic.d2tm.game.terrain.impl;

import com.fundynamic.d2tm.game.terrain.ConstructionGround;
import com.fundynamic.d2tm.graphics.Theme;

public class Rock extends DuneTerrain implements ConstructionGround {

    public Rock(Theme theme) {
        super(theme);
    }

    public Rock() {
        super(null);
    }

    @Override
    protected int getTerrainType() {
        return TERRAIN_ROCK;
    }
}
