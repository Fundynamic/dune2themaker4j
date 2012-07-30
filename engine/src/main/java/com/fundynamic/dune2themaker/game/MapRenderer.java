package com.fundynamic.dune2themaker.game;

import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

public class MapRenderer {

	private Image image;

	public MapRenderer(int width, int height) throws SlickException {
		this.image = makeImage(width * Tile.WIDTH, height * Tile.HEIGHT);
	}

	protected Image makeImage(int width, int height) throws SlickException {
		return new Image(width, height);
	}

	public Image getImage() {
		return image;
	}
}
