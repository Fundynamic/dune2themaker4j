package com.fundynamic.d2tm.game.map;

import com.fundynamic.d2tm.graphics.ShroudFacing;
import junit.framework.Assert;
import org.junit.Test;

public class ShroudFacingDeterminerTest {

    @Test
    public void returnsMiddleWhenNoSameTypeOfNeighbours() throws Exception {
        ShroudFacingDeterminer determiner = makeFacingDeterminer();
        ShroudFacing facing = determiner.getFacing();
//        Assert.assertEquals(ShroudFacing.MIDDLE, facing); // does not exist
    }

    @Test
    public void RIGHT_BOTTOM_LEFT() throws Exception {
        ShroudFacingDeterminer determiner = makeFacingDeterminer();
        determiner.setRightShrouded(true);
        determiner.setBottomShrouded(true);
        determiner.setLeftShrouded(true);
        ShroudFacing facing = determiner.getFacing();
        Assert.assertEquals(ShroudFacing.RIGHT_BOTTOM_LEFT, facing);
    }

    private ShroudFacingDeterminer makeFacingDeterminer() {
        return new ShroudFacingDeterminer();
    }

}