package com.fundynamic.dune2themaker.game.map;

import org.junit.Assert;
import org.junit.Test;

public class CellTest {

	@Test (expected = IllegalArgumentException.class)
	public void constructorThrowsIllegalArgumentExceptionWhenArgumentIsNull() {
		new Cell(null);
	}
}
