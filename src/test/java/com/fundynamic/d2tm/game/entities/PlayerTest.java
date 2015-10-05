package com.fundynamic.d2tm.game.entities;

import com.fundynamic.d2tm.game.entities.structures.Structure;
import com.fundynamic.d2tm.game.entities.structures.StructureFactory;
import com.fundynamic.d2tm.game.entities.units.Unit;
import com.fundynamic.d2tm.game.entities.units.UnitFactory;
import com.fundynamic.d2tm.game.rendering.Recolorer;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;


public class PlayerTest {

    private Player player;

    @Before
    public void setUp() {
        player = new Player("I am awesome", Recolorer.FactionColor.BLUE);
    }

    @Test
    public void hasNoAliveEntitiesAtDefault() {
        assertEquals(0, player.aliveEntities());
    }

    @Test
    public void hasAliveEntitiesWhenAddingStructureWithEnoughHitPoints() {
        player.addEntity(makeStructure(100));

        assertEquals(1, player.aliveEntities());
    }

    @Test
    public void hasNoAliveEntitiesWhenAddingStructureWithNotEnoughHitPoints() {
        player.addEntity(makeStructure(0));

        assertEquals(0, player.aliveEntities());
    }

    @Test
    public void hasAliveEntitiesWhenAddingUnitWithEnoughHitPoints() {
        player.addEntity(makeUnit(100));

        assertEquals(1, player.aliveEntities());
    }

    @Test
    public void hasNoAliveEntitiesWhenAddingUnitWithNotEnoughHitPoints() {
        player.addEntity(makeUnit(0));

        assertEquals(0, player.aliveEntities());
    }

    @Test
    public void hasNoAliveEntitiesWhenAddingAndRemovingStructure() {
        Structure entity = makeStructure(100);
        player.addEntity(entity);
        player.removeEntity(entity);

        assertEquals(0, player.aliveEntities());
    }

    private Structure makeStructure(int hitPoints) {
        return StructureFactory.makeStructure(player, hitPoints, mock(EntityRepository.class));
    }


    private Unit makeUnit(int hitPoints) {
        return UnitFactory.makeUnit(player, hitPoints);
    }

}