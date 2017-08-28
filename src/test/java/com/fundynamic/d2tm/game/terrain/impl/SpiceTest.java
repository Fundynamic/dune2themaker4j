package com.fundynamic.d2tm.game.terrain.impl;

import com.fundynamic.d2tm.game.map.Cell;
import com.fundynamic.d2tm.game.map.Map;
import com.fundynamic.d2tm.graphics.Theme;
import org.junit.Assert;
import org.junit.Test;

import static org.mockito.Mockito.mock;

public class SpiceTest {

    @Test
    public void harvestReturnsExpectedHarvestedAmounts() {
        Cell cell = new Cell(mock(Map.class), EmptyTerrain.instance(), 10, 10); // make sure we are way out of range due structure width/height

        Spice spice = new Spice(mock(Theme.class), cell, 100);
        cell.changeTerrain(spice);

        Assert.assertEquals(50, spice.harvest(50), 0.0001f); // 100 spice, harvest 50, so expect 50 are harvested

        Assert.assertEquals(40, spice.harvest(40), 0.0001f); // 50 spice remaining, harvest 40, expect 40 harvested

        Assert.assertTrue(cell.isHarvestable());

        Assert.assertEquals(10, spice.harvest(50),0.0001f); // 10 spice remaining, harvest 50, expect 10 harvested

        Assert.assertFalse(cell.isHarvestable());
    }
}