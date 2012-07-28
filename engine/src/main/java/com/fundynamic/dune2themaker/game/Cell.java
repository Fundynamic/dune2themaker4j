package com.fundynamic.dune2themaker.game;

import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import com.fundynamic.dune2themaker.game.terrain.Terrain;

public class Cell {

	private Terrain terrain;

	private Cell() {
	}

	public void changeTerrain(Terrain terrain) {
		this.terrain = terrain;
	}

	public static Cell create(TerrainFactory terrainFactory, int terrainType) {
		Cell cell = new Cell();
		Terrain terrain = terrainFactory.create(terrainType, cell);
		cell.changeTerrain(terrain);
		return cell;
	}

	public Image getTileImage() throws SlickException {
		return terrain.getTileImage();
	}
}
