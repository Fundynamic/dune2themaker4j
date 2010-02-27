package com.fundynamic.dune2themaker.system.repositories;

import java.util.HashMap;

import org.newdawn.slick.Color;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

public class ImageRepository implements Repository<Image>{

	private Color colorKey = new Color(255, 0, 255);
	private HashMap<String, Image> images = new HashMap<String, Image>();
	
	public void addItem(String key, Image item) {
		images.put(key, item);
	}
	
	public void addItem(String key, String filename) {
		images.put(key, loadImage(filename, false));
	}


	/**
	 * Add image to cache.
	 * 
	 * @param location
	 */
	protected Image loadImage(String location, boolean transparant) {
		try {
			Image image;
			if (transparant) {
				image = new Image(location, colorKey);
			} else {
				image = new Image(location);
			}
			return image;
		} catch (SlickException e) {
			System.err.println(e);
		}
		return null;
	}
	
	public Image getItem(String key) {
		return images.get(key);
	}

}
