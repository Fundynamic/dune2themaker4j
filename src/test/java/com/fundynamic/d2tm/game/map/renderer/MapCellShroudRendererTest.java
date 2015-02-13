package com.fundynamic.d2tm.game.map.renderer;

import junit.framework.Assert;
import org.junit.Test;

import static com.fundynamic.d2tm.game.map.renderer.MapCellShroudRenderer.getFacing;
import static com.fundynamic.d2tm.graphics.ShroudFacing.*;

public class MapCellShroudRendererTest {

    @Test
    public void MIDDLE() throws Exception {
        Assert.assertEquals(MIDDLE, getFacing(true, true, true, true));
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
    public void TOP_RIGHT() throws Exception {
        Assert.assertEquals(TOP_RIGHT, getFacing(true, true, false, false));
    }

    @Test
    public void RIGHT() throws Exception {
        Assert.assertEquals(RIGHT, getFacing(false, true, false, false));
    }

    @Test
    public void RIGHT_BOTTOM() throws Exception {
        Assert.assertEquals(RIGHT_BOTTOM, getFacing(false, true, true, false));
    }

    @Test
    public void BOTTOM() throws Exception {
        Assert.assertEquals(BOTTOM, getFacing(false, false, true, false));
    }

    @Test
    public void BOTTOM_LEFT() throws Exception {
        Assert.assertEquals(BOTTOM_LEFT, getFacing(false, false, true, true));
    }

    @Test
    public void LEFT() throws Exception {
        Assert.assertEquals(LEFT, getFacing(false, false, false, true));
    }

    @Test
    public void TOP_BOTTOM_LEFT() throws Exception {
        Assert.assertEquals(TOP_BOTTOM_LEFT, getFacing(true, false, true, true));
    }

    @Test
    public void TOP_RIGHT_LEFT() throws Exception {
        Assert.assertEquals(TOP_RIGHT_LEFT, getFacing(true, true, false, true));
    }

    @Test
    public void TOP_RIGHT_BOTTOM() throws Exception {
        Assert.assertEquals(TOP_RIGHT_BOTTOM, getFacing(true, true, true, false));
    }

    @Test
    public void RIGHT_BOTTOM_LEFT() throws Exception {
        Assert.assertEquals(RIGHT_BOTTOM_LEFT, getFacing(false, true, true, true));
    }

    @Test
    public void TOP_BOTTOM() throws Exception {
        Assert.assertEquals(TOP_BOTTOM, getFacing(true, false, true, false));
    }

    @Test
    public void RIGHT_LEFT() throws Exception {
        Assert.assertEquals(RIGHT_LEFT, getFacing(false, true, false, true));
    }

    @Test
    public void returnsNullWhenNoShroudAround() throws Exception {
        Assert.assertNull(getFacing(false, false, false, false));
    }
}