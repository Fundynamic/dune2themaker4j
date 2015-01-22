package com.fundynamic.d2tm.game.terrain;

import com.fundynamic.d2tm.game.terrain.impl.EmptyTerrain;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;

public class EmptyTerrainTest {

    @Test
    public void isSameReturnsTrueWithAnyArgument() {
        final EmptyTerrain emptyTerrain = EmptyTerrain.builder().getInstance();
        Assert.assertTrue(emptyTerrain.isSame(Mockito.mock(Terrain.class)));
    }

    @Test (expected = IllegalArgumentException.class)
    public void isSameThrowsIllegalArgumentExceptionWhenArgumentIsNull() {
        final EmptyTerrain emptyTerrain = EmptyTerrain.builder().getInstance();
        emptyTerrain.isSame(null);
    }
}