package com.fundynamic.dune2themaker.system.repositories;

import java.util.HashMap;

import org.newdawn.slick.Image;

public class ImageRepository implements Repository<Image>{

	private HashMap<RepositoryKey, Image> images = new HashMap<RepositoryKey, Image>();
	
	public void addItem(RepositoryKey key, Image item) {
		images.put(key, item);
	}

	public Image getItem(RepositoryKey key) {
		return images.get(key);
	}


}
