package com.fundynamic.d2tm.game.map;

import org.junit.Test;

public class CellTest {

    @Test (expected = IllegalArgumentException.class)
    public void terrainConstructorThrowsIllegalArgumentExceptionWhenArgumentIsNull() {
        Cell.withTerrain(null);
    }

    @Test (expected = IllegalArgumentException.class)
    public void constructorThrowsIllegalArgumentExceptionWhenArgumentIsNull() {
        Cell.copy(null);
    }

}