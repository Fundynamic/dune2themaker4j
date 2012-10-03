package com.fundynamic.dune2themaker.terrain;

import com.fundynamic.dune2themaker.Theme;

public class Sand extends DuneTerrain {

	public Sand(Theme theme) {
		super(theme);
	}

    @Override
    protected int getTerrainType() {
        return DuneTerrain.TERRAIN_SAND;
    }
}
