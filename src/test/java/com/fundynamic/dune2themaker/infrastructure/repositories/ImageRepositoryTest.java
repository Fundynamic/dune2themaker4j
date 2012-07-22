package com.fundynamic.dune2themaker.infrastructure.repositories;

import org.junit.Test;
import org.newdawn.slick.Image;


public class ImageRepositoryTest {

	@Test (expected = IllegalArgumentException.class)
	public void mustThrowIllegalArgumentExceptionWhenKeyAlreadyExists() {
		ImageRepository repository = new ImageRepository();
		Image image = new Image() {};
		repository.addItem("test", image);
		repository.addItem("test", image);
	}
	
	@Test (expected = IllegalArgumentException.class)
	public void mustThrowIllegalArgumentExceptionWhenKeyIsNull() {
		ImageRepository repository = new ImageRepository();
		Image image = new Image() {};
		repository.addItem(null, image);
	}
	
	@Test (expected = IllegalArgumentException.class)
	public void mustThrowIllegalArgumentExceptionWhenImageIsNull() {
		ImageRepository repository = new ImageRepository();
		repository.addItem("test", (Image)null);
	}
}
