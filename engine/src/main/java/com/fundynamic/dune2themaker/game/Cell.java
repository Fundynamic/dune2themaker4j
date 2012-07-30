package com.fundynamic.dune2themaker.game;

import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import com.fundynamic.dune2themaker.game.terrain.EmptyTerrain;
import com.fundynamic.dune2themaker.game.terrain.Terrain;

public class Cell {

	private Terrain terrain;

	public Cell() {
		terrain = new EmptyTerrain();
	}

	public void changeTerrain(Terrain terrain) {
		this.terrain = terrain;
	}

	public Image getTileImage() throws SlickException {
		return terrain.getTileImage();
	}
}
