package com.fundynamic.dune2themaker.domain.tile;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.geom.Rectangle;

import com.fundynamic.dune2themaker.Dune2themaker;
import com.fundynamic.dune2themaker.domain.tile.Tile;

public class TiledBoard {

	private int width, height;
	
	private Graphics graphics;
	
	private final static int TILE_WIDTH = 48;
	private final static int TILE_HEIGHT = 48;
	
	public TiledBoard(Graphics graphics, int screenWidth, int screenHeight) {
		this.width = screenWidth / TILE_WIDTH;
		this.height = screenHeight / TILE_HEIGHT;
		this.graphics = graphics; 
	}
	
	public void setWidth(int width) {
		this.width = width;
	}
	
	public void setHeight(int height) {
		this.height = height;
	}
	
	public void draw(Image image, int x, int y) {
		int drawX = getDrawX(x);
		int drawY = getDrawY(y);
		graphics.drawImage(image, drawX, drawY);
	}
	
	public void draw(Tile tile) {
		draw(tile.getImage(), tile.getX(), tile.getY());
	}

	public int getDrawX(int xOnGrid) {
		int startX = Dune2themaker.SCREEN_WIDTH / 2;
		startX -= (width * TILE_WIDTH) / 2;
		return (startX + (xOnGrid * TILE_WIDTH));
	}
	
	public int getDrawY(int yOnGrid) {
		int startY = Dune2themaker.SCREEN_HEIGHT - TILE_HEIGHT;
		return (startY - (yOnGrid * TILE_HEIGHT));
	}
	
	public void draw() {
		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				int drawX = getDrawX(x);
				int drawY = getDrawY(y);
				Rectangle rect = new Rectangle(drawX, drawY, TILE_WIDTH, TILE_HEIGHT);
				graphics.draw(rect);
			}
		}
	}

	public void setGraphics(Graphics graphics) {
		this.graphics = graphics;
	}

}
