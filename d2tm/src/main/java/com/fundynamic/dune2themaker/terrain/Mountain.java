package com.fundynamic.dune2themaker.terrain;

import com.fundynamic.dune2themaker.Theme;
import org.newdawn.slick.Image;

public class Mountain extends DuneTerrain {

	public Mountain(Theme theme) {
		super(theme);
	}

    @Override
    protected int getTerrainType() {
        return DuneTerrain.TERRAIN_MOUNTAIN;
    }
}
