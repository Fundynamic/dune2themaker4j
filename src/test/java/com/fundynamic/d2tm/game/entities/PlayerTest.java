package com.fundynamic.d2tm.game.entities;

import com.fundynamic.d2tm.game.AbstractD2TMTest;
import com.fundynamic.d2tm.game.entities.structures.Structure;
import com.fundynamic.d2tm.game.entities.units.Unit;
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
        player.addEntity(makeUnit(player));

        assertEquals(1, player.aliveEntities());
    }

    @Test
    public void hasNoAliveEntitiesWhenUnitIsDestroyed() {
        Unit unit = makeUnit(player);
        unit.takeDamage(unit.getHitPoints()); // destroys unit
        unit.update(1); // updates internal state so it really is marked destroyed

        player.addEntity(unit);

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