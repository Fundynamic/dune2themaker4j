package com.fundynamic.d2tm.game.map;

import junit.framework.Assert;
import org.junit.Test;

import static com.fundynamic.d2tm.game.map.ShroudFacingDeterminer.getFacing;
import static com.fundynamic.d2tm.graphics.ShroudFacing.*;

public class ShroudFacingDeterminerTest {

    @Test
    public void FULL_is_null() throws Exception {
        Assert.assertTrue(getFacing(true, true, true, true) != FULL);
        Assert.assertEquals(null, getFacing(true, true, true, true)); // instead of FULL
    }

    @Test
    public void TOP_LEFT() throws Exception {
        Assert.assertEquals(TOP_LEFT, getFacing(true, false, false, true));
    }

    @Test
    public void TOP() throws Exception {
        Assert.assertEquals(TOP, getFacing(true, false, false, false));
    }

    @Test
    public void RIGHT_BOTTOM_LEFT() throws Exception {
        Assert.assertEquals(RIGHT_BOTTOM_LEFT, getFacing(false, true, true, true));
    }


    @Test
    public void returnsNullWhenNoShroudAround() throws Exception {
        Assert.assertNull(getFacing(false, false, false, false));
    }

}