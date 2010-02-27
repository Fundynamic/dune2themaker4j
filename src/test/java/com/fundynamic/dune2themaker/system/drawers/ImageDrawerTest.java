package com.fundynamic.dune2themaker.system.drawers;

import org.easymock.classextension.EasyMock;
import org.junit.Test;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

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
		final Image myImage = EasyMock.createMock(Image.class);
		Graphics stub = new Graphics() {
			public void drawImage(Image image, float x, float y) {
				if (!(x == 1 && y == 1 && image == myImage)) {
					throw new IllegalArgumentException("Call failed");
				}
			}
		};
		ImageDrawer drawer = new ImageDrawer(stub, new ImageRepository());
		drawer.drawImage(myImage, 1, 1);
	}

}
