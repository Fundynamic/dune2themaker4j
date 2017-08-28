package com.fundynamic.d2tm.game.entities;

import com.fundynamic.d2tm.game.AbstractD2TMTest;
import com.fundynamic.d2tm.game.entities.structures.Structure;
import com.fundynamic.d2tm.game.entities.units.Unit;
import com.fundynamic.d2tm.math.MapCoordinate;
import org.junit.Assert;
import org.junit.Test;

import static com.fundynamic.d2tm.game.entities.entitiesdata.EntitiesData.LIGHT_FACTORY;
import static com.fundynamic.d2tm.game.entities.entitiesdata.EntitiesData.WINDTRAP;
import static org.junit.Assert.*;


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
        assertEquals(startingCredits + 50, player.getAnimatedCredits());
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

    @Test
    public void powerProductionAndConsuming() {
        Structure windtrap = makeStructure(player, MapCoordinate.create(12, 12), WINDTRAP);

        Assert.assertEquals(1, player.aliveEntities());
        Assert.assertEquals(200, player.getTotalPowerProduced());

        // Take damage
        windtrap.takeDamage(100, null);

        Assert.assertEquals(150, player.getTotalPowerProduced());
        Assert.assertEquals(150, player.getPowerBalance());
        Assert.assertFalse(player.isLowPower());

        // Add a few more structures that consumes power
        int amountOfStructuresToMake = 4;
        for (int i = 0; i < amountOfStructuresToMake; i++) {
            makeStructure(player, MapCoordinate.create(15, 12 + (i * 2)), LIGHT_FACTORY); // consumes 30 power
        }

        // should still have 30 power left
        Assert.assertEquals(30, player.getPowerBalance());

        makeStructure(player, MapCoordinate.create(15, 20), LIGHT_FACTORY); // .. 150

        // should still have 0 power left
        Assert.assertEquals(0, player.getPowerBalance());
        Assert.assertFalse(player.isLowPower());

        makeStructure(player, MapCoordinate.create(15, 22), LIGHT_FACTORY);

        Assert.assertEquals(-30, player.getPowerBalance());
        Assert.assertTrue(player.isLowPower());
    }

    @Test
    public void gotEnoughPowerAfterDestructionOfStructureThatConsumedTooMuchPower() {
        Structure windtrap = makeStructure(player, MapCoordinate.create(12, 12), WINDTRAP);

        Assert.assertEquals(1, player.aliveEntities());
        Assert.assertEquals(200, player.getTotalPowerProduced());

        // Take damage
        windtrap.takeDamage(100, null);
        Assert.assertEquals(150, player.getTotalPowerProduced());
        Assert.assertEquals(150, player.getPowerBalance());
        Assert.assertFalse(player.isLowPower());

        int amountOfStructuresToMake = 5; // 5X30 = 150
        for (int i = 0; i < amountOfStructuresToMake; i++) {
            makeStructure(player, MapCoordinate.create(15, 12 + (i * 2)), LIGHT_FACTORY); // consumes 30 power
        }

        // should still have 0 power left
        Assert.assertEquals(0, player.getPowerBalance());
        Assert.assertFalse(player.isLowPower());

        Structure toBeDestroyed = makeStructure(player, MapCoordinate.create(25, 23), LIGHT_FACTORY);
        toBeDestroyed.takeDamage(toBeDestroyed.getHitPoints(), null);

        Assert.assertEquals(0, player.getPowerBalance());
        Assert.assertFalse(player.isLowPower());
    }
}