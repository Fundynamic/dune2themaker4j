package com.fundynamic.dune2themaker.game.map;

import com.fundynamic.dune2themaker.game.terrain.TerrainFacing;
import junit.framework.Assert;
import org.junit.Test;

public class FacingDeterminerTest {

    @Test
    public void returnsMiddleWhenNoSameTypeOfNeighbours() throws Exception {
        FacingDeterminer determiner = makeFacingDeterminer();
        TerrainFacing facing = determiner.getFacing();
        Assert.assertEquals(TerrainFacing.MIDDLE, facing);
    }

    @Test
    public void returnsTopWhenDifferentTypeAbove() throws Exception {
        FacingDeterminer determiner = makeFacingDeterminer();
        determiner.setRightSame(true);
        determiner.setBottomSame(true);
        determiner.setLeftSame(true);
        TerrainFacing facing = determiner.getFacing();
        Assert.assertEquals(TerrainFacing.TOP, facing);
    }

    @Test
    public void returnsRightWhenDifferentTypeRight() throws Exception {
        FacingDeterminer determiner = makeFacingDeterminer();
        determiner.setTopSame(true);
        determiner.setBottomSame(true);
        determiner.setLeftSame(true);
        TerrainFacing facing = determiner.getFacing();
        Assert.assertEquals(TerrainFacing.RIGHT, facing);
    }

    @Test
    public void returnsBottomWhenDifferentTypeBottom() throws Exception {
        FacingDeterminer determiner = makeFacingDeterminer();
        determiner.setTopSame(true);
        determiner.setRightSame(true);
        determiner.setLeftSame(true);
        TerrainFacing facing = determiner.getFacing();
        Assert.assertEquals(TerrainFacing.BOTTOM, facing);
    }

    @Test
    public void returnsLeftWhenDifferentTypeLeft() throws Exception {
        FacingDeterminer determiner = makeFacingDeterminer();
        determiner.setTopSame(true);
        determiner.setRightSame(true);
        determiner.setBottomSame(true);
        TerrainFacing facing = determiner.getFacing();
        Assert.assertEquals(TerrainFacing.LEFT, facing);
    }

    @Test
    public void returnsTopRightWhenSameTypeBottomAndLeft() throws Exception {
        FacingDeterminer determiner = makeFacingDeterminer();
        determiner.setBottomSame(true);
        determiner.setLeftSame(true);
        TerrainFacing facing = determiner.getFacing();
        Assert.assertEquals(TerrainFacing.TOP_RIGHT, facing);
    }

    @Test
    public void returnsFullWhenAllSameTypeOfNeighbours() throws Exception {
        FacingDeterminer determiner = makeFacingDeterminer();
        determiner.setTopSame(true);
        determiner.setRightSame(true);
        determiner.setBottomSame(true);
        determiner.setLeftSame(true);
        TerrainFacing facing = determiner.getFacing();
        Assert.assertEquals(TerrainFacing.FULL, facing);
    }

    private FacingDeterminer makeFacingDeterminer() {
        return new FacingDeterminer();
    }

}
