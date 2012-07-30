package com.fundynamic.dune2themaker.game;

import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

public class TestableImage extends Image {

	private final int width;
	private final int height;

	public TestableImage(int width, int height) throws SlickException {
		this.width = width;
		this.height = height;
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}
}
