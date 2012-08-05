package com.fundynamic.dune2themaker.game.map;

import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import com.fundynamic.dune2themaker.game.terrain.Terrain;
import com.fundynamic.dune2themaker.game.terrain.TerrainFacing;

public class Cell {

	private Terrain terrain;
	private TerrainFacing terrainFacing;

	public Cell(Terrain terrain) {
		if (terrain == null) throw new IllegalArgumentException("Terrain argument may not be null");
		this.terrain = terrain;
	}

	public void changeTerrain(Terrain terrain) {
		this.terrain = terrain;
	}

	public Image getTileImage() throws SlickException {
		return terrain.getTileImage();
	}

	public Terrain getTerrain() {
		return terrain;
	}

}
