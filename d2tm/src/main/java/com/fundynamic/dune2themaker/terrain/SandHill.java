package com.fundynamic.dune2themaker.terrain;

import com.fundynamic.dune2themaker.Theme;
import org.newdawn.slick.Image;

public class SandHill extends DuneTerrain {

	public SandHill(Theme theme) {
		super(theme);
	}

    @Override
    protected int getTerrainType() {
        return DuneTerrain.TERRAIN_SAND_HILL;
    }
}
