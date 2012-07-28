package com.fundynamic.dune2themaker.game;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

public class Map {

	private Image mapImage;
	private final Theme theme;
	private boolean initialized;
	private Cell[][] terrain;
	private int height, width;

	public Map(int width, int height, Theme theme) throws SlickException {
		this.theme = theme;
		this.mapImage = new Image(width * Tile.WIDTH, height * Tile.HEIGHT);
		this.height = height;
		this.width = width;

		this.terrain = new Cell[width][height];
		for (int x = 0; x < this.width; x++) {
			for (int y = 0; y < this.height; y++) {
				if (x == 0 || y == 0 || x == (width - 1) || y == (height - 1)) {
					terrain[x][y] = Cell.create(1);
				} else {
					final int terrainType = (int)(Math.random() * 7);
					terrain[x][y] = Cell.create(terrainType);
				}
			}
		}
	}



	public void init() throws SlickException {
		if (!initialized) {
			// this does not work when we move the code in the constructor?
			final Graphics mapImageGraphics = mapImage.getGraphics();
			mapImageGraphics.clear();
			for (int x = 0; x < this.width; x++) {
				for (int y = 0; y < this.height; y++) {
					Cell cell = terrain[x][y];
					mapImageGraphics.drawImage(cell.getImage(this.theme), x * Tile.WIDTH, y * Tile.HEIGHT);
				}
			}
			initialized=true;
		}
	}

	public Image getMapImage() throws SlickException {


		return mapImage;
	}
}
