package com.fundynamic.dune2themaker.game.pieces;

import org.newdawn.slick.Image;

/**
 * Represents a piece for on the stack
 * @author S. Hendriks
 *
 */
abstract public class Piece {

	private int x = 0, y = 0;

	public Piece(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	public abstract int getScore();
	
	public abstract Image getImage();

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}
	
}
