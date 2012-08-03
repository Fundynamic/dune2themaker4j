package com.fundynamic.dune2themaker.game;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.newdawn.slick.SlickException;
import com.fundynamic.dune2themaker.game.terrain.EmptyTerrain;
import com.fundynamic.dune2themaker.game.terrain.Terrain;
import com.fundynamic.dune2themaker.game.terrain.TerrainFacing;

import junit.framework.Assert;

public class MapTest {

	/*
		Specs: 32x32-map should have upper-left coordinate (1,1)
										 and lower-right coordinate (32, 32)
	 */

	@Mock
	private TerrainFactory terrainFactory;

	@Before
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
	}

	@Test
	public void constructorShouldCreateCellsWithEmptyTerrain() throws Exception {
		Map map = makeMap(1, 1);

		Cell cell = map.getCell(1, 1);
		Assert.assertEquals(EmptyTerrain.class, cell.getTerrain().getClass());
	}

	@Test
	public void mapWithOneCellDrawsFullRockCell() throws SlickException {
		Map map = makeMap(1, 1);
		Terrain rock = Mockito.mock(Terrain.class);
		map.getCell(1, 1).changeTerrain(rock);

		map.smooth();

		Mockito.verify(rock).setFacing(TerrainFacing.FULL);
	}


	private Map makeMap(int width, int height) throws SlickException {
		Map map = new Map(terrainFactory, width, height);
		map.init();
		return map;
	}

}
