package com.fundynamic.dune2themaker.dune;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import com.fundynamic.dune2themaker.dune.terrain.Terrain;

public class Map {

	private Image mapImage;
	private final Theme theme;
	private boolean initialized;
	private Terrain[][] terrain;

	public Map(int width, int height, Theme theme) throws SlickException {
		this.theme = theme;
		this.mapImage = new Image(width * Tile.WIDTH, height * Tile.HEIGHT);
		this.terrain = new Terrain[width][height];
	}

	public void init() throws SlickException {
		if (!initialized) {
			// this does not work when we move the code in the constructor?
			final Graphics mapImageGraphics = mapImage.getGraphics();
			mapImageGraphics.drawImage(theme.getTileImage(Terrain.ROCK, null), 0, 0);
			mapImageGraphics.drawImage(theme.getTileImage(Terrain.SAND, null), 32, 0);
			initialized=true;
		}
	}

	public Image getMapImage() throws SlickException {


		return mapImage;
	}
}
