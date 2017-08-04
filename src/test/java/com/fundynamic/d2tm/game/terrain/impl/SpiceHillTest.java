package com.fundynamic.d2tm.game.terrain.impl;

import com.fundynamic.d2tm.game.map.Cell;
import com.fundynamic.d2tm.game.map.Map;
import com.fundynamic.d2tm.graphics.Theme;
import org.junit.Assert;
import org.junit.Test;

import static org.mockito.Mockito.mock;

public class SpiceHillTest {

    @Test
    public void harvestReturnsExpectedHarvestedAmounts() {
        Cell cell = new Cell(mock(Map.class), EmptyTerrain.instance(), 10, 10); // make sure we are way out of range due structure width/height

        SpiceHill spiceHill = new SpiceHill(mock(Theme.class), cell, 100);
        cell.changeTerrain(spiceHill);

        Assert.assertEquals(50, spiceHill.harvest(50)); // 100 spice, harvest 50, so expect 50 are harvested

        Assert.assertEquals(40, spiceHill.harvest(40)); // 50 spice remaining, harvest 40, expect 40 harvested

        Assert.assertTrue(cell.isHarvestable());

        Assert.assertEquals(10, spiceHill.harvest(50)); // 10 spice remaining, harvest 50, expect 10 harvested

        // expect still to be harvestable, but now a different terrain type
        Assert.assertTrue(cell.isHarvestable());
    }
}