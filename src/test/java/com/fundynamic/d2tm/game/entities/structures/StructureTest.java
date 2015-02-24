package com.fundynamic.d2tm.game.entities.structures;

import com.fundynamic.d2tm.math.Vector2D;
import junit.framework.Assert;
import org.junit.Test;
import org.mockito.Mockito;
import org.newdawn.slick.Image;

public class StructureTest {
    public static int TILE_SIZE = 32;

    @Test
    public void constructorCalculatesWidthInCells() {
        Structure structure = new Structure(Vector2D.zero(), Mockito.mock(Image.class), TILE_SIZE * 3, TILE_SIZE * 2);
        Assert.assertEquals(3, structure.getWidthInCells());
    }

    @Test
    public void constructorCalculatesHeightInCells() {
        Structure structure = new Structure(Vector2D.zero(), Mockito.mock(Image.class), TILE_SIZE * 3, TILE_SIZE * 2);
        Assert.assertEquals(2, structure.getHeightInCells());
    }

}