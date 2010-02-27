package com.fundynamic.dune2themaker.game;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.geom.Rectangle;

import com.fundynamic.dune2themaker.Dune2themaker;
import com.fundynamic.dune2themaker.game.pieces.Piece;

public class TiledBoard {

	private int width, height;
	private static TiledBoard instance;
	
	private final static int TILE_WIDTH = 48;
	private final static int TILE_HEIGHT = 48;
	
	public static TiledBoard getInstance() {
		if (instance == null) {
			instance = new TiledBoard();
		}
		return instance;
	}
	
	public TiledBoard() {
		width = Dune2themaker.SCREEN_WIDTH / TILE_WIDTH;
		height = Dune2themaker.SCREEN_HEIGHT / TILE_HEIGHT;
	}
	
	public void setWidth(int width) {
		this.width = width;
	}
	
	public void setHeight(int height) {
		this.height = height;
	}
	
	/**
	 * Draw image on grid (x, y is grid location)
	 * @param image
	 * @param x
	 * @param y
	 */
	public void draw(Image image, Graphics graphics, int x, int y) {
		int drawX = getDrawX(x);
		int drawY = getDrawY(y);
		graphics.drawImage(image, drawX, drawY);
	}
	
	public void draw(Piece piece, Graphics graphics) {
		draw(piece.getImage(), graphics, piece.getX(), piece.getY());
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
	
	public void draw(Graphics graphics) {
		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				int drawX = getDrawX(x);
				int drawY = getDrawY(y);
				Rectangle rect = new Rectangle(drawX, drawY, TILE_WIDTH, TILE_HEIGHT);
				graphics.draw(rect);
			}
		}
	}
}
