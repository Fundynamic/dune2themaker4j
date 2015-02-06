package com.fundynamic.d2tm.game.map.renderer;

import com.fundynamic.d2tm.graphics.TerrainFacing;
import junit.framework.Assert;
import org.junit.Test;

public class TerrainFacingDeterminerTest {

    @Test
    public void returnsMiddleWhenNoSameTypeOfNeighbours() throws Exception {
        Assert.assertEquals(TerrainFacing.MIDDLE, TerrainFacingDeterminer.getFacing(false, false, false, false));
    }

    @Test
    public void returnsTopWhenDifferentTypeAbove() throws Exception {
        Assert.assertEquals(TerrainFacing.TOP, TerrainFacingDeterminer.getFacing(false, true, true, true));
    }

    @Test
    public void returnsRightWhenDifferentTypeRight() throws Exception {
        Assert.assertEquals(TerrainFacing.RIGHT, TerrainFacingDeterminer.getFacing(true, false, true, true));
    }

    @Test
    public void returnsBottomWhenDifferentTypeBottom() throws Exception {
        Assert.assertEquals(TerrainFacing.BOTTOM, TerrainFacingDeterminer.getFacing(true, true, false, true));
    }

    @Test
    public void returnsLeftWhenDifferentTypeLeft() throws Exception {
        Assert.assertEquals(TerrainFacing.LEFT, TerrainFacingDeterminer.getFacing(true, true, true, false));
    }

    @Test
    public void returnsTopRightWhenSameTypeBottomAndLeft() throws Exception {
        Assert.assertEquals(TerrainFacing.TOP_RIGHT, TerrainFacingDeterminer.getFacing(false, false, true, true));
    }

    @Test
    public void returnsFullWhenAllSameTypeOfNeighbours() throws Exception {
        Assert.assertEquals(TerrainFacing.FULL, TerrainFacingDeterminer.getFacing(true, true, true, true));
    }

}