package com.fundynamic.dune2themaker.terrain;

import com.fundynamic.dune2themaker.Theme;
import com.fundynamic.dune2themaker.game.terrain.ConstructionGround;

public class Rock extends DuneTerrain implements ConstructionGround {

	public Rock(Theme theme) {
		super(theme);
	}

    @Override
    protected int getTerrainType() {
        return DuneTerrain.TERRAIN_ROCK;
    }
}
