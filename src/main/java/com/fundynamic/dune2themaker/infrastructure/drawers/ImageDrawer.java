package com.fundynamic.dune2themaker.infrastructure.drawers;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;

import com.fundynamic.dune2themaker.infrastructure.repositories.ImageRepository;
import com.fundynamic.dune2themaker.util.Validate;

public class ImageDrawer {
	private final Graphics canvas; // the canvas we draw on
	private final ImageRepository imageRepository;
	
	public ImageDrawer(Graphics canvas, ImageRepository imageRepository) {
		Validate.notNull(canvas, "canvas may not be null");
		Validate.notNull(imageRepository, "imageRepository may not be null");
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
