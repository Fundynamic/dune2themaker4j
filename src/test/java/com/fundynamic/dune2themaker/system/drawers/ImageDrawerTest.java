package com.fundynamic.dune2themaker.system.drawers;

import org.junit.Test;
import org.mockito.Mockito;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

import com.fundynamic.dune2themaker.stubs.slick.ImageStub;
import com.fundynamic.dune2themaker.system.repositories.ImageRepository;

public class ImageDrawerTest {

	@Test (expected=IllegalArgumentException.class)
	public void mustThrowIllegalArgumentExceptionWhenCanvasIsNull() {
		new ImageDrawer(null, new ImageRepository());
	}

	@Test (expected=IllegalArgumentException.class)
	public void mustThrowIllegalArgumentExceptionImageRepositoryIsNull() {
		new ImageDrawer(new Graphics(), null);
	}
	
	@Test
	public void mustDrawWithProperDrawMethodFromCanvas() throws SlickException {
		Image image = new ImageStub();
		Graphics graphics = Mockito.mock(Graphics.class);
		ImageDrawer drawer = new ImageDrawer(graphics, new ImageRepository());
		
		// Act
		drawer.drawImage(image, 1, 1);
		
		// Verify
		Mockito.verify(graphics).drawImage(image, 1, 1);
	}

}
