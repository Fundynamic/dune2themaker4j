package com.fundynamic.dune2themaker.terrain;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import com.fundynamic.dune2themaker.Theme;
import com.fundynamic.dune2themaker.game.Cell;
import com.fundynamic.dune2themaker.game.Map;
import com.fundynamic.dune2themaker.game.TerrainFactory;
import com.fundynamic.dune2themaker.game.terrain.Terrain;
import com.fundynamic.dune2themaker.game.terrain.TerrainFacing;

import static org.mockito.Matchers.*;

@RunWith(MockitoJUnitRunner.class)
public class FacingTest {

	@Mock
	private Theme theme;

	private Terrain terrain;

	@Before
	public void setUp() throws Exception {
		terrain = new TerrainImpl(theme);
	}

	@Test
	public void getTileImageReturnsFullFacingWhenTerrainTypeIsSurroundedBySameTerrainType() {
		// where to get neighbouring data!?
		// Act & Assert
		Assert.assertSame(getExpectedImage(TerrainFacing.FULL), terrain.getTileImage());
	}

	@Test
	public void getTileImageReturnsFullFacingWhenTerrainTypeIsSurroundedBySameTerrainTypeUsingMap() throws SlickException {
		Map map = new Map(Mockito.mock(TerrainFactory.class), 3, 3);
		int type = 0;
		int x = 1;
		int y = 1;
//		Cell cell = map.makeCell(type, x, y);
	}

	private Image getExpectedImage(TerrainFacing terrainFacing) {
		final Image imageToReturn = Mockito.mock(Image.class);
		Mockito.when(theme.getTileImage(anyInt(), eq(terrainFacing))).thenReturn(imageToReturn);
		return imageToReturn;
	}

	private class TerrainImpl implements Terrain {
		private final Theme theme;

		public TerrainImpl(Theme theme) {
			this.theme = theme;
		}

		public Image getTileImage() {
			return theme.getTileImage(0, TerrainFacing.FULL);
		}

		public void setFacing(TerrainFacing terrainFacing) {
		}
	}

}
