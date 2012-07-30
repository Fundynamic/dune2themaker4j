package com.fundynamic.dune2themaker.terrain;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.newdawn.slick.Image;
import com.fundynamic.dune2themaker.Theme;
import com.fundynamic.dune2themaker.game.terrain.Terrain;

import static org.mockito.Matchers.eq;

@RunWith(MockitoJUnitRunner.class)
public class FacingTest {

	@Mock
	private Theme theme;

	@Test
	public void shouldReturnFullFacingWhenTerrainTypeIsSurroundedBySameTerrainType() {
		Terrain terrain = new TerrainImpl(theme);

		// Act
		terrain.getTileImage();

		// Verify
		Mockito.verify(theme).getTileImage(Mockito.anyInt(), eq(TerrainFacing.FULL));
	}


	@Test
	public void shouldReturnFullFacingWhenTerrainTypeIsSurroundedBySameTerrainTypeVariant() {
		Terrain terrain = new TerrainImpl(theme);
		final Image imageToReturn = Mockito.mock(Image.class);
		Mockito.when(theme.getTileImage(Mockito.anyInt(), eq(TerrainFacing.FULL))).thenReturn(imageToReturn);

		// Act & Assert
		Assert.assertSame(imageToReturn, terrain.getTileImage());
	}


	private class TerrainImpl implements Terrain {
		private final Theme theme;

		public TerrainImpl(Theme theme) {
			this.theme = theme;
		}

		public int getRowOnSpriteSheet() {
			return 0;
		}

		public Image getTileImage() {
			return theme.getTileImage(0, TerrainFacing.FULL);
		}
	}
}
