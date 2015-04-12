package com.fundynamic.d2tm.game.entities;

import com.fundynamic.d2tm.game.entities.structures.Structure;
import com.fundynamic.d2tm.game.entities.units.Unit;
import com.fundynamic.d2tm.game.map.Map;
import com.fundynamic.d2tm.game.rendering.Recolorer;
import com.fundynamic.d2tm.math.Vector2D;
import org.junit.Before;
import org.junit.Test;
import org.newdawn.slick.Image;

import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;
import static org.mockito.Mockito.mock;


public class PlayerTest {

    private Player player;

    @Before
    public void setUp() {
        player = new Player("I am awesome", Recolorer.FactionColor.BLUE);
    }

    @Test
    public void hasNoAliveEntitiesAtDefault() {
        assertFalse(player.hasAliveEntities());
    }

    @Test
    public void hasAliveEntitiesWhenAddingStructureWithEnoughHitPoints() {
        player.addEntity(makeStructure(100));

        assertTrue(player.hasAliveEntities());
    }

    @Test
    public void hasNoAliveEntitiesWhenAddingStructureWithNotEnoughHitPoints() {
        player.addEntity(makeStructure(0));

        assertFalse(player.hasAliveEntities());
    }

    @Test
    public void hasAliveEntitiesWhenAddingUnitWithEnoughHitPoints() {
        player.addEntity(makeUnit(100));

        assertTrue(player.hasAliveEntities());
    }

    @Test
    public void hasNoAliveEntitiesWhenAddingUnitWithNotEnoughHitPoints() {
        player.addEntity(makeUnit(0));

        assertFalse(player.hasAliveEntities());
    }

    @Test
    public void hasNoAliveEntitiesWhenAddingAndRemovingStructure() {
        Structure entity = makeStructure(100);
        player.addEntity(entity);
        player.removeEntity(entity);

        assertFalse(player.hasAliveEntities());
    }

    private Structure makeStructure(int hitPoints) {
        EntityData entityData = new EntityData(32, 32, 2);
        entityData.hitPoints = hitPoints;
        return new Structure(Vector2D.zero(), mock(Image.class), player, entityData);
    }

    private Unit makeUnit(int hitPoints) {
        EntityData entityData = new EntityData(32, 32, 2);
        entityData.hitPoints = hitPoints;
        return new Unit(mock(Map.class), Vector2D.zero(), mock(Image.class), player, entityData);
    }

}