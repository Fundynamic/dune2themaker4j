package com.fundynamic.dune2themaker.game;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import com.fundynamic.dune2themaker.infrastructure.math.Vector2D;

public class Viewport {

	private final Map map;

	private final int width, height;

	private Image image;

	public Viewport(int width, int height, Map map) throws SlickException {
		this.map = map;
		this.height = height;
		this.width = width;
		this.image = new Image(width, height);
	}

	public void draw(Graphics graphics, Vector2D drawingVector, Vector2D viewingVector) throws SlickException {
		Image subImage = map.getImage().getSubImage(viewingVector.getX(), viewingVector.getY(), width, height);
		this.image.getGraphics().drawImage(subImage, 0, 0);
		graphics.drawImage(image, drawingVector.getX(), drawingVector.getY());
	}


}
