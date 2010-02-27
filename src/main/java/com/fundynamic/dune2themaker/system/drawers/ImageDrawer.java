package com.fundynamic.dune2themaker.system.drawers;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;

import com.fundynamic.dune2themaker.game.Game;
import com.fundynamic.dune2themaker.system.repositories.ImageRepository;

public class ImageDrawer {
	private final Graphics canvas; // the canvas we draw on
	private final ImageRepository imageRepository;
	
	public ImageDrawer(Graphics canvas, ImageRepository imageRepository) {
		if (canvas == null) throw new IllegalArgumentException("canvas may not be null");
		if (imageRepository == null) throw new IllegalArgumentException("imageRepository may not be null");
		this.canvas = canvas;
		this.imageRepository = imageRepository;
	}
	
	public void drawImage(Image image, int x, int y) {
		canvas.drawImage(image, x, y);
	}
	
	public void drawImage(String key, int x, int y) {
		Image image = imageRepository.getItem(key);
		if (image != null) {
			drawImage(image, x, y);
		}
	}
}
