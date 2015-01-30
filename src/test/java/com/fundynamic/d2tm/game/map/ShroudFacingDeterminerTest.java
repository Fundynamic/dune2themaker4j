package com.fundynamic.d2tm.game.map;

import com.fundynamic.d2tm.graphics.ShroudFacing;
import junit.framework.Assert;
import org.junit.Test;

public class ShroudFacingDeterminerTest {

    @Test
    public void RIGHT_BOTTOM_LEFT() throws Exception {
        ShroudFacingDeterminer determiner = makeFacingDeterminer();
        determiner.setRightShrouded(true);
        determiner.setBottomShrouded(true);
        determiner.setLeftShrouded(true);
        Assert.assertEquals(ShroudFacing.RIGHT_BOTTOM_LEFT, determiner.getFacing());
    }

    @Test
    public void returnsNullWhenNoShroudAround() throws Exception {
        ShroudFacingDeterminer determiner = makeFacingDeterminer();
        determiner.setRightShrouded(false);
        determiner.setBottomShrouded(false);
        determiner.setLeftShrouded(false);
        determiner.setTopShrouded(false);
        Assert.assertNull(determiner.getFacing());
    }

    private ShroudFacingDeterminer makeFacingDeterminer() {
        return new ShroudFacingDeterminer();
    }

}