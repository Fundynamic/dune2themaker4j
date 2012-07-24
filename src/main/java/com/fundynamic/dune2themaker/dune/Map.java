package com.fundynamic.dune2themaker.dune;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import com.fundynamic.dune2themaker.dune.terrain.Concrete;
import com.fundynamic.dune2themaker.dune.terrain.Mountain;
import com.fundynamic.dune2themaker.dune.terrain.Rock;
import com.fundynamic.dune2themaker.dune.terrain.Sand;
import com.fundynamic.dune2themaker.dune.terrain.SandHill;
import com.fundynamic.dune2themaker.dune.terrain.Spice;
import com.fundynamic.dune2themaker.dune.terrain.SpiceHill;
import com.fundynamic.dune2themaker.dune.terrain.Terrain;

public class Map {

	private Image mapImage;
	private final Theme theme;
	private boolean initialized;
	private Terrain[][] terrain;
	private int height, width;

	public Map(int width, int height, Theme theme) throws SlickException {
		this.theme = theme;
		this.mapImage = new Image(width * Tile.WIDTH, height * Tile.HEIGHT);
		this.height = height;
		this.width = width;

		this.terrain = new Terrain[width][height];
		for (int x = 0; x < this.width; x++) {
			for (int y = 0; y < this.height; y++) {
				if (x == 0 || y == 0 || x == (width - 1) || y == (height - 1)) {
					terrain[x][y] = new Rock();
				} else {
					final int terrainType = (int)(Math.random() * 7);
					terrain[x][y] = getTile(terrainType);
				}
			}
		}
	}

	private Terrain getTile(int terrainType) {
		switch (terrainType) {
			case 0:
				return new Sand();
			case 1:
				return new Rock();
			case 2:
				return new SandHill();
			case 3:
				return new Spice();
			case 4:
				return new Mountain();
			case 5:
				return new SpiceHill();
			case 6:
				return new Concrete();
			default:
				throw new IndexOutOfBoundsException("Invalid value for terrainType: " + terrainType);
		}
	}

	public void init() throws SlickException {
		if (!initialized) {
			// this does not work when we move the code in the constructor?
			final Graphics mapImageGraphics = mapImage.getGraphics();
			mapImageGraphics.clear();
			for (int x = 0; x < this.width; x++) {
				for (int y = 0; y < this.height; y++) {
					Terrain cell = terrain[x][y];
					mapImageGraphics.drawImage(theme.getTileImage(cell, null), x * Tile.WIDTH, y * Tile.HEIGHT);
				}
			}
			initialized=true;
		}
	}

	public Image getMapImage() throws SlickException {


		return mapImage;
	}
}
