package com.fundynamic.dune2themaker.terrain;

import org.newdawn.slick.Image;
import com.fundynamic.dune2themaker.game.terrain.EmptyTerrain;
import com.fundynamic.dune2themaker.game.terrain.Terrain;
import com.fundynamic.dune2themaker.game.terrain.TerrainFacing;

public abstract class DuneTerrain implements Terrain {

	private final Image tileImage;

	public void setFacing(TerrainFacing terrainFacing) {
	}

	public DuneTerrain(Image tileImage) {
		this.tileImage = tileImage;
	}

	public Image getTileImage() {
		return tileImage;
	}

	public boolean isSame(Terrain terrain) {
		if (terrain instanceof EmptyTerrain) return true;
		return this.getClass().equals(terrain.getClass());
	}

}
