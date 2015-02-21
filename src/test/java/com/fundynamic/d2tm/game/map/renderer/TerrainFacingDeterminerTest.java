package com.fundynamic.d2tm.game.map.renderer;

import com.fundynamic.d2tm.game.map.Map;
import junit.framework.Assert;
import org.junit.Test;

public class TerrainFacingDeterminerTest {

    @Test
    public void returnsMiddleWhenNoSameTypeOfNeighbours() throws Exception {
        Assert.assertEquals(Map.TerrainFacing.MIDDLE, Map.getFacing(false, false, false, false));
    }

    @Test
    public void returnsTopWhenDifferentTypeAbove() throws Exception {
        Assert.assertEquals(Map.TerrainFacing.TOP, Map.getFacing(false, true, true, true));
    }

    @Test
    public void returnsRightWhenDifferentTypeRight() throws Exception {
        Assert.assertEquals(Map.TerrainFacing.RIGHT, Map.getFacing(true, false, true, true));
    }

    @Test
    public void returnsBottomWhenDifferentTypeBottom() throws Exception {
        Assert.assertEquals(Map.TerrainFacing.BOTTOM, Map.getFacing(true, true, false, true));
    }

    @Test
    public void returnsLeftWhenDifferentTypeLeft() throws Exception {
        Assert.assertEquals(Map.TerrainFacing.LEFT, Map.getFacing(true, true, true, false));
    }

    @Test
    public void returnsTopRightWhenSameTypeBottomAndLeft() throws Exception {
        Assert.assertEquals(Map.TerrainFacing.TOP_RIGHT, Map.getFacing(false, false, true, true));
    }

    @Test
    public void returnsFullWhenAllSameTypeOfNeighbours() throws Exception {
        Assert.assertEquals(Map.TerrainFacing.FULL, Map.getFacing(true, true, true, true));
    }

}