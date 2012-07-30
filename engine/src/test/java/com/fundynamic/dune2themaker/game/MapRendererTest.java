package com.fundynamic.dune2themaker.game;

import org.junit.Test;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

import junit.framework.Assert;

public class MapRendererTest {

	private static final int ANY_WIDTH = 0;
	private static final int ANY_HEIGHT = 0;

	@Test
	public void constructorShouldCreateImage() throws Exception {
		MapRenderer mapRenderer = makeMapRenderer(ANY_WIDTH, ANY_HEIGHT);

		Assert.assertEquals(TestableImage.class, mapRenderer.getImage().getClass());
	}

	@Test
	public void constructorShouldCreateImageWithWidthOfNumberOfTilesTimesTileWidth1() throws SlickException {
		TestingMapRenderer mapRenderer = makeMapRenderer(1, ANY_HEIGHT);

		Assert.assertEquals(32, mapRenderer.getImage().getWidth());
	}

	@Test
	public void constructorShouldCreateImageWithWidthOfNumberOfTilesTimesTileWidth2() throws SlickException {
		TestingMapRenderer mapRenderer = makeMapRenderer(2, ANY_HEIGHT);

		Assert.assertEquals(64, mapRenderer.getImage().getWidth());
	}

	@Test
	public void constructorShouldCreateImageWithHeightOfNumberOfTilesTimesTileHeight1() throws SlickException {
		TestingMapRenderer mapRenderer = makeMapRenderer(ANY_WIDTH, 1);

		Assert.assertEquals(32, mapRenderer.getImage().getHeight());
	}

	@Test
	public void constructorShouldCreateImageWithHeightOfNumberOfTilesTimesTileHeight2() throws SlickException {
		TestingMapRenderer mapRenderer = makeMapRenderer(ANY_WIDTH, 2);

		Assert.assertEquals(64, mapRenderer.getImage().getHeight());
	}

	
	private TestingMapRenderer makeMapRenderer(int width, int height) throws SlickException {
		return new TestingMapRenderer(width, height);
	}

	private class TestingMapRenderer extends MapRenderer {

		public TestingMapRenderer(int width, int height) throws SlickException {
			super(width, height);
		}

		@Override
		protected Image makeImage(int width, int height) throws SlickException {
			return new TestableImage(width, height);
		}

	}
}
