package com.fundynamic.d2tm.game.map.renderer;

import org.junit.Assert;
import org.junit.Test;

import static com.fundynamic.d2tm.game.rendering.CellShroudRenderer.ShroudFacing.*;
import static com.fundynamic.d2tm.game.rendering.CellShroudRenderer.getFacing;
import static org.junit.Assert.assertEquals;

public class CellShroudRendererTest {

    @Test
    public void middle() throws Exception {
        assertEquals(MIDDLE, getFacing(true, true, true, true));
    }

    @Test
    public void top_left() throws Exception {
        assertEquals(TOP_LEFT, getFacing(true, false, false, true));
    }

    @Test
    public void top() throws Exception {
        assertEquals(TOP, getFacing(true, false, false, false));
    }

    @Test
    public void top_right() throws Exception {
        assertEquals(TOP_RIGHT, getFacing(true, true, false, false));
    }

    @Test
    public void right() throws Exception {
        assertEquals(RIGHT, getFacing(false, true, false, false));
    }

    @Test
    public void right_bottom() throws Exception {
        assertEquals(RIGHT_BOTTOM, getFacing(false, true, true, false));
    }

    @Test
    public void bottom() throws Exception {
        assertEquals(BOTTOM, getFacing(false, false, true, false));
    }

    @Test
    public void bottom_left() throws Exception {
        assertEquals(BOTTOM_LEFT, getFacing(false, false, true, true));
    }

    @Test
    public void left() throws Exception {
        assertEquals(LEFT, getFacing(false, false, false, true));
    }

    @Test
    public void top_bottom_left() throws Exception {
        assertEquals(TOP_BOTTOM_LEFT, getFacing(true, false, true, true));
    }

    @Test
    public void top_right_left() throws Exception {
        assertEquals(TOP_RIGHT_LEFT, getFacing(true, true, false, true));
    }

    @Test
    public void top_right_bottom() throws Exception {
        assertEquals(TOP_RIGHT_BOTTOM, getFacing(true, true, true, false));
    }

    @Test
    public void right_bottom_left() throws Exception {
        assertEquals(RIGHT_BOTTOM_LEFT, getFacing(false, true, true, true));
    }

    @Test
    public void top_bottom() throws Exception {
        assertEquals(TOP_BOTTOM, getFacing(true, false, true, false));
    }

    @Test
    public void right_left() throws Exception {
        assertEquals(RIGHT_LEFT, getFacing(false, true, false, true));
    }

    @Test
    public void returnsNullWhenNoShroudAround() throws Exception {
        Assert.assertNull(getFacing(false, false, false, false));
    }
}