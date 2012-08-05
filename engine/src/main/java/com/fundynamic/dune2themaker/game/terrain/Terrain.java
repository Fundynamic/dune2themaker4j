package com.fundynamic.dune2themaker.game.terrain;

import org.newdawn.slick.Image;

public interface Terrain {

	// events??
	Image getTileImage();

	void setFacing(TerrainFacing terrainFacing);

	boolean isSame(Terrain terrain);
}

