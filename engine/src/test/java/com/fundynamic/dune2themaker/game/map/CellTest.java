package com.fundynamic.dune2themaker.game.map;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;
import com.fundynamic.dune2themaker.game.terrain.Terrain;

import static org.mockito.Matchers.any;

public class CellTest {

	@Test (expected = IllegalArgumentException.class)
	public void constructorThrowsIllegalArgumentExceptionWhenArgumentIsNull() {
		new Cell(null);
	}

	@Test
	public void isSameTerrainReturnsTrueWhenTerrainEquals() {
		Terrain terrain = Mockito.mock(Terrain.class);
		Mockito.when(terrain.isSame(any(Terrain.class))).thenReturn(true);

		final Cell cell = new Cell(terrain);
		Assert.assertTrue(cell.isSameTerrain(Mockito.mock(Terrain.class)));
	}

//
//	@Test
//	public void isSameTerrainReturnsFalseWhenClassNameDoesNotEqual() {
//		final Cell cell = new Cell(new TestableSandTerrain());
//		Assert.assertTrue(cell.isSameTerrain(new TestableSandTerrain()));
//	}


}
