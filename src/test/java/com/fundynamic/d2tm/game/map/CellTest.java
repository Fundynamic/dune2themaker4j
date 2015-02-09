package com.fundynamic.d2tm.game.map;

import com.fundynamic.d2tm.game.terrain.Terrain;
import org.junit.Assert;
import org.junit.Test;

public class CellTest {

    @Test (expected = IllegalArgumentException.class)
    public void constructorThrowsIllegalArgumentExceptionWhenArgumentIsNull() {
        new Cell(null);
    }

}