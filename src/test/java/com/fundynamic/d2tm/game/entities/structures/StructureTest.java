package com.fundynamic.d2tm.game.entities.structures;

import com.fundynamic.d2tm.game.entities.EntityData;
import com.fundynamic.d2tm.game.entities.Player;
import com.fundynamic.d2tm.math.Vector2D;
import junit.framework.Assert;
import org.junit.Test;
import org.newdawn.slick.Image;

import static org.mockito.Mockito.mock;

public class StructureTest {
    public static int TILE_SIZE = 32;

    @Test
    public void constructorCalculatesWidthInCells() {
        Structure structure = new Structure(Vector2D.zero(), mock(Image.class), mock(Player.class), new EntityData(TILE_SIZE * 3, TILE_SIZE * 2, 2));
        Assert.assertEquals(3, structure.getWidthInCells());
    }

    @Test
    public void constructorCalculatesHeightInCells() {
        Structure structure = new Structure(Vector2D.zero(), mock(Image.class), mock(Player.class), new EntityData(TILE_SIZE * 3, TILE_SIZE * 2, 3));
        Assert.assertEquals(2, structure.getHeightInCells());
    }

}