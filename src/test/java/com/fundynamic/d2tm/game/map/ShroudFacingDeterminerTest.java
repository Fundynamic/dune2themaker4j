package com.fundynamic.d2tm.game.map;

import com.fundynamic.d2tm.graphics.ShroudFacing;
import junit.framework.Assert;
import org.junit.Test;

import static com.fundynamic.d2tm.game.map.ShroudFacingDeterminer.getFacing;

public class ShroudFacingDeterminerTest {

    @Test
    public void RIGHT_BOTTOM_LEFT() throws Exception {
        Assert.assertEquals(ShroudFacing.RIGHT_BOTTOM_LEFT, getFacing(false, true, true, true));
    }

    @Test
    public void TOP_LEFT() throws Exception {
        Assert.assertEquals(ShroudFacing.TOP_LEFT, getFacing(true, false, false, true));
    }

    @Test
    public void returnsNullWhenNoShroudAround() throws Exception {
        Assert.assertNull(getFacing(false, false, false, false));
    }

}