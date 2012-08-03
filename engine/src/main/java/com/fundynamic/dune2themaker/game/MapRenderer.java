package com.fundynamic.dune2themaker.game;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

public class MapRenderer {

	public Image render(Map map) throws SlickException {
		Image image = makeImage(map.getWidth() * Tile.WIDTH, map.getHeight() * Tile.HEIGHT);
		renderMap(image.getGraphics(), map);
		return image;
	}

	protected Image makeImage(int width, int height) throws SlickException {
		return new Image(width, height);
	}

	private void renderMap(Graphics graphics, Map map) throws SlickException {
		for (int x = 1; x <= map.getWidth(); x++) {
			for (int y = 1; y <= map.getHeight(); y++) {
				Cell cell = map.getCell(x, y);
				graphics.drawImage(cell.getTileImage(), (x - 1) * Tile.WIDTH, (y - 1) * Tile.HEIGHT);
			}
		}
	}
}
