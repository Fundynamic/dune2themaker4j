package com.fundynamic.d2tm.game.entities.structures;

import com.fundynamic.d2tm.game.AbstractD2TMTest;
import com.fundynamic.d2tm.game.entities.EntityData;
import com.fundynamic.d2tm.game.entities.EntityRepository;
import com.fundynamic.d2tm.game.entities.Player;
import com.fundynamic.d2tm.math.Vector2D;
import org.junit.Assert;
import org.junit.Test;
import org.newdawn.slick.Image;

import static org.mockito.Mockito.mock;

public class StructureTest extends AbstractD2TMTest {

    @Test
    public void constructorCalculatesWidthInCells() {
        Structure structure = new Structure(Vector2D.zero(), mock(Image.class), mock(Player.class), new EntityData(TILE_SIZE * 3, TILE_SIZE * 2, 2), mock(EntityRepository.class));
        Assert.assertEquals(3, structure.getWidthInCells());
    }

    @Test
    public void constructorCalculatesHeightInCells() {
        Structure structure = new Structure(Vector2D.zero(), mock(Image.class), mock(Player.class), new EntityData(TILE_SIZE * 3, TILE_SIZE * 2, 3), mock(EntityRepository.class));
        Assert.assertEquals(2, structure.getHeightInCells());
    }

}