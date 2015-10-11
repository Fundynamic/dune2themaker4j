package com.fundynamic.d2tm.game.entities;

import com.fundynamic.d2tm.game.AbstractD2TMTest;
import com.fundynamic.d2tm.game.entities.structures.Structure;
import com.fundynamic.d2tm.game.rendering.Recolorer;
import org.junit.Before;
import org.junit.Test;
import org.newdawn.slick.SlickException;

import static org.junit.Assert.assertEquals;


public class PlayerTest extends AbstractD2TMTest {

    private Player player;

    @Before
    public void setUp() throws SlickException {
        super.setUp();
        player = new Player("I am awesome", Recolorer.FactionColor.BLUE);
    }

    @Test
    public void hasNoAliveEntitiesAtDefault() {
        assertEquals(0, player.aliveEntities());
    }

    @Test
    public void hasAliveEntitiesWhenAddingStructureWithEnoughHitPoints() {
        player.addEntity(makeStructure(player, 100));

        assertEquals(1, player.aliveEntities());
    }

    @Test
    public void hasNoAliveEntitiesWhenAddingStructureWithNotEnoughHitPoints() {
        player.addEntity(makeStructure(player, 0));

        assertEquals(0, player.aliveEntities());
    }

    @Test
    public void hasAliveEntitiesWhenAddingUnitWithEnoughHitPoints() {
        player.addEntity(makeUnit(player, 100));

        assertEquals(1, player.aliveEntities());
    }

    @Test
    public void hasNoAliveEntitiesWhenAddingUnitWithNotEnoughHitPoints() {
        player.addEntity(makeUnit(player, 0));

        assertEquals(0, player.aliveEntities());
    }

    @Test
    public void hasNoAliveEntitiesWhenAddingAndRemovingStructure() {
        Structure entity = makeStructure(player, 100);
        player.addEntity(entity);
        player.removeEntity(entity);

        assertEquals(0, player.aliveEntities());
    }

}