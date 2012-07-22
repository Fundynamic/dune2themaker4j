package com.fundynamic.dune2themaker.dune;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import com.fundynamic.dune2themaker.infrastructure.math.Vector2D;

public class Map {

	private Image image;

	public Map() throws SlickException {
		image = new Image("background.bmp");
	}

	public void draw(Graphics graphics, Vector2D vector) {
		graphics.drawImage(image, vector.getX(), vector.getY());
	}

	public Image getImage() {
		return image;
	}
}
