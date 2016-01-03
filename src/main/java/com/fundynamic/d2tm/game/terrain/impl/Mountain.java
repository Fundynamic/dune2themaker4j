package com.fundynamic.d2tm.game.terrain.impl;

import com.fundynamic.d2tm.game.entities.Entity;
import com.fundynamic.d2tm.graphics.Theme;

public class Mountain extends DuneTerrain {

    public Mountain(Theme theme) {
        super(theme);
    }

    @Override
    public int getTerrainType() {
        return TERRAIN_MOUNTAIN;
    }

    @Override
    public boolean isPassable(Entity entity) {
        switch (entity.getEntityType()) {
            case UNIT:
                return false; // for now all units cannot walk over mountain
            default:
                return true;
        }
    }

}
