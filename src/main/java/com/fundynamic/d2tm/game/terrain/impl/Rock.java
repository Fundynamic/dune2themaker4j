package com.fundynamic.d2tm.game.terrain.impl;

import com.fundynamic.d2tm.game.terrain.ConstructionGround;
import com.fundynamic.d2tm.game.terrain.Terrain;
import com.fundynamic.d2tm.graphics.Theme;

public class Rock extends DuneTerrain implements ConstructionGround {

    public Rock(Theme theme) {
        super(theme);
    }

    public Rock() {
        super(null);
    }

    @Override
    public boolean isSame(Terrain terrain) {
        // rock and mountain are of the same 'type'
        if (terrain.getTerrainType() == TERRAIN_MOUNTAIN) return true;
        return super.isSame(terrain);
    }

    @Override
    public int getTerrainType() {
        return TERRAIN_ROCK;
    }

}
