package com.fundynamic.d2tm.game.entities;

import com.fundynamic.d2tm.game.AbstractD2TMTest;
import com.fundynamic.d2tm.game.entities.structures.Structure;
import com.fundynamic.d2tm.game.entities.units.Unit;
import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;


public class PlayerTest extends AbstractD2TMTest {

    @Test
    public void update() {
        int startingCredits = player.getCredits();

        // update state should do nothing, since no changes in credit
        player.update(1F);

        assertEquals(startingCredits, player.getCredits());
        assertEquals(startingCredits, player.getAnimatedCredits());

        // Change credit amount
        player.addCredits(50);

        // update one hundreth of a second, should increase one credit
        player.update(0.01f);

        assertEquals(startingCredits + 50, player.getCredits());
        assertEquals(startingCredits + 1, player.getAnimatedCredits());
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
        player.addEntity(makeUnit(player));

        assertEquals(1, player.aliveEntities());
    }

    @Test
    public void hasNoAliveEntitiesWhenUnitIsDestroyed() {
        Unit unit = makeUnit(player);
        unit.takeDamage(unit.getHitPoints(), null); // destroys unit
        unit.update(1);

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

    @Test
    public void cannotBuyThingsThatCostMoreThanCreditsOwned() {
        player.setCredits(100);
        assertFalse(player.canBuy(101));
        assertEquals(100, player.getCredits());
    }

    @Test
    public void canBuyThingsThatCostEqualThanCreditsOwned() {
        player.setCredits(100);
        assertTrue(player.canBuy(100));
        assertEquals(100, player.getCredits());
    }

    @Test
    public void canBuyThingsThatCostLessThanCreditsOwned() {
        player.setCredits(100);
        assertTrue(player.canBuy(99));
        assertEquals(100, player.getCredits());
    }

    @Test
    public void cannotSpendThingsThatCostMoreThanCreditsOwned() {
        player.setCredits(100);
        assertFalse(player.spend(101));
        assertEquals(100, player.getCredits());
    }

    @Test
    public void canSpendThingsThatCostEqualThanCreditsOwned() {
        player.setCredits(100);
        assertTrue(player.spend(100));
        assertEquals(0, player.getCredits());
    }

    @Test
    public void canSpendThingsThatCostLessThanCreditsOwned() {
        player.setCredits(100);
        assertTrue(player.spend(99));
        assertEquals(1, player.getCredits());
    }

    @Test
    public void spendingScenario() {
        player.setCredits(0);

        assertFalse(player.spend(50));
        assertEquals(0, player.getCredits());

        player.addCredits(100);

        assertTrue(player.spend(50));
        assertEquals(50, player.getCredits());
    }

}