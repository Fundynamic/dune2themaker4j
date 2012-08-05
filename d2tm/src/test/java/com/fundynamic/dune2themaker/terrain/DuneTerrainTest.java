package com.fundynamic.dune2themaker.terrain;

import org.junit.Test;
import org.mockito.Mockito;
import org.newdawn.slick.Image;
import com.fundynamic.dune2themaker.game.terrain.Terrain;

import static org.hamcrest.core.Is.*;
import static org.junit.Assert.*;

public class DuneTerrainTest {

	@Test
	public void isSameReturnsTrueWhenClassNameEquals() {
		Terrain sand = makeSand();
		Terrain anotherSand = makeSand();

		boolean isSame = sand.isSame(anotherSand);

		assertThat(isSame, is(true));
	}

	@Test
	public void isSameReturnsFalseWhenClassNamesNotEquals() {
		Terrain sand = makeSand();
		Terrain rock = makeRock();

		boolean isSame = sand.isSame(rock);

		assertThat(isSame, is(false));
	}

	private Rock makeRock() {
		return new Rock(Mockito.mock(Image.class));
	}

	private Sand makeSand() {
		return new Sand(Mockito.mock(Image.class));
	}


}
