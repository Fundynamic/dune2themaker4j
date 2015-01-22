package com.fundynamic.d2tm.game.terrain;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;

public class EmptyTerrainTest {

    @Test
    public void isSameReturnsTrueWithAnyArgument() {
        final EmptyTerrain emptyTerrain = EmptyTerrain.testInstance();
        Assert.assertTrue(emptyTerrain.isSame(Mockito.mock(Terrain.class)));
    }

    @Test (expected = IllegalArgumentException.class)
    public void isSameThrowsIllegalArgumentExceptionWhenArgumentIsNull() {
        final EmptyTerrain emptyTerrain = EmptyTerrain.testInstance();
        emptyTerrain.isSame(null);
    }
}