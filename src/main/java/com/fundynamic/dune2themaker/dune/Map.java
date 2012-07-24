package com.fundynamic.dune2themaker.dune;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import com.fundynamic.dune2themaker.dune.terrain.Rock;
import com.fundynamic.dune2themaker.dune.terrain.Terrain;
import com.fundynamic.dune2themaker.dune.terrain.TerrainType;

public class Map {

	private Image mapImage;
	private final Theme theme;
	private boolean initialized;

	public Map(int width, int height, Theme theme) throws SlickException {
		this.theme = theme;
		this.mapImage = new Image(width * Tile.WIDTH, height * Tile.HEIGHT);
	}

	public void init() throws SlickException {
	}

	public Image getMapImage() throws SlickException {
		if (!initialized) {
			// this does not work when we move the code in the constructor?
			final Graphics mapImageGraphics = mapImage.getGraphics();
			mapImageGraphics.drawImage(theme.getTileImage(Terrain.ROCK, null), 0, 0);
			mapImageGraphics.drawImage(theme.getTileImage(Terrain.SAND, null), 32, 0);
			initialized=true;
		}

		return mapImage;
	}
}
