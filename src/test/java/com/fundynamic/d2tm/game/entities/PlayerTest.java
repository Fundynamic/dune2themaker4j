package com.fundynamic.d2tm.game.entities;

import com.fundynamic.d2tm.game.AbstractD2TMTest;
import com.fundynamic.d2tm.game.entities.structures.Structure;
import org.junit.Test;

import static org.junit.Assert.assertEquals;


public class PlayerTest extends AbstractD2TMTest {

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