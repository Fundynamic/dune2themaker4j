package com.fundynamic.d2tm.game.map.renderer;

import com.fundynamic.d2tm.game.map.MapEditor;
import junit.framework.Assert;
import org.junit.Test;

public class TerrainFacingDeterminerTest {

    @Test
    public void returnsMiddleWhenNoSameTypeOfNeighbours() throws Exception {
        Assert.assertEquals(MapEditor.TerrainFacing.MIDDLE, MapEditor.getFacing(false, false, false, false));
    }

    @Test
    public void returnsTopWhenDifferentTypeAbove() throws Exception {
        Assert.assertEquals(MapEditor.TerrainFacing.TOP, MapEditor.getFacing(false, true, true, true));
    }

    @Test
    public void returnsRightWhenDifferentTypeRight() throws Exception {
        Assert.assertEquals(MapEditor.TerrainFacing.RIGHT, MapEditor.getFacing(true, false, true, true));
    }

    @Test
    public void returnsBottomWhenDifferentTypeBottom() throws Exception {
        Assert.assertEquals(MapEditor.TerrainFacing.BOTTOM, MapEditor.getFacing(true, true, false, true));
    }

    @Test
    public void returnsLeftWhenDifferentTypeLeft() throws Exception {
        Assert.assertEquals(MapEditor.TerrainFacing.LEFT, MapEditor.getFacing(true, true, true, false));
    }

    @Test
    public void returnsTopRightWhenSameTypeBottomAndLeft() throws Exception {
        Assert.assertEquals(MapEditor.TerrainFacing.TOP_RIGHT, MapEditor.getFacing(false, false, true, true));
    }

    @Test
    public void returnsFullWhenAllSameTypeOfNeighbours() throws Exception {
        Assert.assertEquals(MapEditor.TerrainFacing.FULL, MapEditor.getFacing(true, true, true, true));
    }

}