package com.fundynamic.d2tm.game.map;

import com.fundynamic.d2tm.graphics.TerrainFacing;
import junit.framework.Assert;
import org.junit.Test;

public class TerrainFacingDeterminerTest {

    @Test
    public void returnsMiddleWhenNoSameTypeOfNeighbours() throws Exception {
        TerrainFacingDeterminer determiner = makeFacingDeterminer();
        TerrainFacing facing = determiner.getFacing();
        Assert.assertEquals(TerrainFacing.MIDDLE, facing);
    }

    @Test
    public void returnsTopWhenDifferentTypeAbove() throws Exception {
        TerrainFacingDeterminer determiner = makeFacingDeterminer();
        determiner.setRightSame(true);
        determiner.setBottomSame(true);
        determiner.setLeftSame(true);
        TerrainFacing facing = determiner.getFacing();
        Assert.assertEquals(TerrainFacing.TOP, facing);
    }

    @Test
    public void returnsRightWhenDifferentTypeRight() throws Exception {
        TerrainFacingDeterminer determiner = makeFacingDeterminer();
        determiner.setTopSame(true);
        determiner.setBottomSame(true);
        determiner.setLeftSame(true);
        TerrainFacing facing = determiner.getFacing();
        Assert.assertEquals(TerrainFacing.RIGHT, facing);
    }

    @Test
    public void returnsBottomWhenDifferentTypeBottom() throws Exception {
        TerrainFacingDeterminer determiner = makeFacingDeterminer();
        determiner.setTopSame(true);
        determiner.setRightSame(true);
        determiner.setLeftSame(true);
        TerrainFacing facing = determiner.getFacing();
        Assert.assertEquals(TerrainFacing.BOTTOM, facing);
    }

    @Test
    public void returnsLeftWhenDifferentTypeLeft() throws Exception {
        TerrainFacingDeterminer determiner = makeFacingDeterminer();
        determiner.setTopSame(true);
        determiner.setRightSame(true);
        determiner.setBottomSame(true);
        TerrainFacing facing = determiner.getFacing();
        Assert.assertEquals(TerrainFacing.LEFT, facing);
    }

    @Test
    public void returnsTopRightWhenSameTypeBottomAndLeft() throws Exception {
        TerrainFacingDeterminer determiner = makeFacingDeterminer();
        determiner.setBottomSame(true);
        determiner.setLeftSame(true);
        TerrainFacing facing = determiner.getFacing();
        Assert.assertEquals(TerrainFacing.TOP_RIGHT, facing);
    }

    @Test
    public void returnsFullWhenAllSameTypeOfNeighbours() throws Exception {
        TerrainFacingDeterminer determiner = makeFacingDeterminer();
        determiner.setTopSame(true);
        determiner.setRightSame(true);
        determiner.setBottomSame(true);
        determiner.setLeftSame(true);
        TerrainFacing facing = determiner.getFacing();
        Assert.assertEquals(TerrainFacing.FULL, facing);
    }

    private TerrainFacingDeterminer makeFacingDeterminer() {
        return new TerrainFacingDeterminer();
    }

}