package com.fundynamic.dune2themaker.infrastructure.math;

public class Vector2D {

	private final int x, y;

	public Vector2D(int x, int y) {
		this.x = x;
		this.y = y;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public Vector2D moveUp() {
		return new Vector2D(x, y - 1);
	}
}
