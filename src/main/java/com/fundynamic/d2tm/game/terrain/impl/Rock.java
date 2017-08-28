package com.fundynamic.d2tm.game.terrain.impl;

import com.fundynamic.d2tm.game.terrain.ConstructionGround;
import com.fundynamic.d2tm.game.terrain.Terrain;
import com.fundynamic.d2tm.graphics.Theme;
import org.newdawn.slick.Color;

public class Rock extends DuneTerrain implements ConstructionGround {

    private static final Color terrainColor = new Color(73, 67, 43);

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

    @Override
    public Color getTerrainColor() {
        return terrainColor;
    }
}
