package com.fundynamic.dune2themaker.game.map;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import com.fundynamic.dune2themaker.game.TerrainFactory;
import com.fundynamic.dune2themaker.game.terrain.EmptyTerrain;
import com.fundynamic.dune2themaker.game.terrain.Terrain;
import com.fundynamic.dune2themaker.game.terrain.TerrainFacing;

import junit.framework.Assert;

import static org.mockito.Matchers.*;

public class MapTest {

	/*
		Specs: 32x32-map should have upper-left coordinate (1,1)
										 and lower-right coordinate (32, 32)
	 */

	@Mock
	private TerrainFactory terrainFactory;
	@Mock
	private Terrain emptyTerrain;

	@Before
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
		Mockito.when(terrainFactory.createEmptyTerrain()).thenReturn(emptyTerrain);
	}

	@Test
	public void constructorShouldCreateCellsWithEmptyTerrain() throws Exception {
		Map map = makeMap(1, 1);

		map.getCell(1, 1);

		Mockito.verify(terrainFactory, Mockito.atLeastOnce()).createEmptyTerrain();
	}

	@Test
	public void constructorShouldCreateEightEmptyBorderCellsWithOneEmptyTerrain() throws Exception {
		Map map = makeMap(1, 1);

		map.getCell(1, 1);

		Mockito.verify(terrainFactory, Mockito.times(9)).createEmptyTerrain();
	}

	@Test
	public void constructorShouldSetEmptyTerrainAsCellTerrain() throws Exception {
		Terrain expectedTerrain = makeTerrain();
		Mockito.when(terrainFactory.createEmptyTerrain()).thenReturn(expectedTerrain);
		Map map = makeMap(1, 1);

		final Cell cell = map.getCell(1, 1);

		Assert.assertSame(expectedTerrain, cell.getTerrain());
	}

	@Test
	public void smoothCellWithBorderNeighboursSetTerrainFacingToFull() throws Exception {
		Map map = makeMap(1, 1);
		Terrain rock = makeTerrain();
		map.getCell(1, 1).changeTerrain(rock);

		map.smooth();

		Mockito.verify(rock).setFacing(TerrainFacing.FULL);
	}

	@Test
	public void smoothCellWithBorderNeighboursAndRightNeighbourOfDifferentTerrainSetTerrainFacingToTopBottomLeft() throws Exception {
		Map map = makeMap(2, 1);
		Terrain rock = makeTerrain();
		Terrain sand = makeTerrain();

		map.getCell(1, 1).changeTerrain(rock);
		map.getCell(2, 1).changeTerrain(sand);

		map.smooth();

		Mockito.verify(rock).setFacing(TerrainFacing.TOP_BOTTOM_LEFT);
	}

    @Test
    public void smoothCellWithBorderNeighboursAndLeftNeighbourOfDifferentTerrainSetTerrainFacingToTopRightBottom() throws Exception {
        Map map = makeMap(2, 1);
        Terrain rock = makeTerrain();
        Terrain sand = makeTerrain();

        map.getCell(1, 1).changeTerrain(rock);
        map.getCell(2, 1).changeTerrain(sand);

        map.smooth();

        Mockito.verify(rock).setFacing(TerrainFacing.TOP_RIGHT_BOTTOM);
    }

    private Map makeMap(int width, int height) throws Exception {
		Map map = new Map(terrainFactory, width, height);
		map.init();
		return map;
	}

	private Terrain makeTerrain() {
		Terrain terrain = Mockito.mock(Terrain.class);
		Mockito.when(terrain.isSame(emptyTerrain)).thenReturn(true);
		return terrain;
	}
}
