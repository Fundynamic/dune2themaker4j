package com.fundynamic.dune2themaker.system.drawers;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;

import com.fundynamic.dune2themaker.system.repositories.ImageContainer;
import com.fundynamic.dune2themaker.system.repositories.ImageRepository;
import com.fundynamic.dune2themaker.system.repositories.RepositoryKey;

public class ImageDrawer {

	private final Graphics canvas; // the canvas we draw on
	private final ImageRepository imageRepository;
	
	public ImageDrawer(Graphics canvas, ImageRepository imageRepository) {
		this.canvas = canvas;
		this.imageRepository = imageRepository;
	}
	
	public void drawImage(Image image, int x, int y) {
		canvas.drawImage(image, 0, 0);
	}
	
	public void drawImage(RepositoryKey key, int x, int y) {
		Image image = imageRepository.getItem(key);
		if (image != null) {
			drawImage(image, x, y);
		}
	}
}
