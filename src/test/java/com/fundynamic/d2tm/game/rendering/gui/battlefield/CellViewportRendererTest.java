package com.fundynamic.d2tm.game.rendering.gui.battlefield;

import com.fundynamic.d2tm.math.Rectangle;
import com.fundynamic.d2tm.math.Vector2D;
import org.junit.Test;

import static com.fundynamic.d2tm.game.map.Cell.TILE_SIZE;
import static org.junit.Assert.*;

public class CellViewportRendererTest {

    @Test
    public void getViewport1() throws Exception {
        testVisibleCellRange(
            new Vector2D(TILE_SIZE, TILE_SIZE),
            new Vector2D(0, 0),
            new Rectangle(0, 0, new Vector2D(1, 1)));
    }

    // viewport exactly 1 cell large, exactly one cell, horizontal movement

    @Test
    public void getViewport2() throws Exception {
        testVisibleCellRange(
                new Vector2D(TILE_SIZE, TILE_SIZE),
                new Vector2D(1, 0),
                new Rectangle(0, 0, new Vector2D(2, 1)));
    }

    @Test
    public void getViewport3() throws Exception {
        testVisibleCellRange(
                new Vector2D(TILE_SIZE, TILE_SIZE),
                new Vector2D(TILE_SIZE - 1, 0),
                new Rectangle(0, 0, new Vector2D(2, 1)));
    }

    @Test
    public void getViewport4() throws Exception {
        testVisibleCellRange(
                new Vector2D(TILE_SIZE, TILE_SIZE),
                new Vector2D(TILE_SIZE, 0),
                new Rectangle(1, 0, new Vector2D(1, 1)));
    }

    // viewport exactly 1 cell large, vertical movement

    @Test
    public void getViewport5() throws Exception {
        testVisibleCellRange(
                new Vector2D(TILE_SIZE, TILE_SIZE),
                new Vector2D(0, 1),
                new Rectangle(0, 0, new Vector2D(1, 2)));
    }

    @Test
    public void getViewport6() throws Exception {
        testVisibleCellRange(
                new Vector2D(TILE_SIZE, TILE_SIZE),
                new Vector2D(0, TILE_SIZE - 1),
                new Rectangle(0, 0, new Vector2D(1, 2)));
    }

    @Test
    public void getViewport7() throws Exception {
        testVisibleCellRange(
                new Vector2D(TILE_SIZE, TILE_SIZE),
                new Vector2D(0, TILE_SIZE),
                new Rectangle(0, 1, new Vector2D(1, 1)));
    }

    // viewport 1 cell + 1px large, horizontal movement
    @Test
    public void getViewport8() throws Exception {
        testVisibleCellRange(
                new Vector2D(TILE_SIZE + 1, TILE_SIZE),
                new Vector2D(0, 0),
                new Rectangle(0, 0, new Vector2D(2, 1)));
    }

    @Test
    public void getViewport9() throws Exception {
        testVisibleCellRange(
                new Vector2D(TILE_SIZE + 1, TILE_SIZE),
                new Vector2D(1, 0),
                new Rectangle(0, 0, new Vector2D(2, 1)));
    }

    @Test
    public void getViewport10() throws Exception {
        testVisibleCellRange(
                new Vector2D(TILE_SIZE + 1, TILE_SIZE),
                new Vector2D(TILE_SIZE -1, 0),
                new Rectangle(0, 0, new Vector2D(2, 1)));
    }

    @Test
    public void getViewport11() throws Exception {
        testVisibleCellRange(
                new Vector2D(TILE_SIZE + 1, TILE_SIZE),
                new Vector2D(TILE_SIZE, 0),
                new Rectangle(1, 0, new Vector2D(2, 1)));
    }

    // viewport 1 cell + 1px large, horizontal movement
    @Test
    public void getViewport12() throws Exception {
        testVisibleCellRange(
                new Vector2D(TILE_SIZE + 2, TILE_SIZE),
                new Vector2D(0, 0),
                new Rectangle(0, 0, new Vector2D(2, 1)));
    }

    @Test
    public void getViewport13() throws Exception {
        testVisibleCellRange(
                new Vector2D(TILE_SIZE + 2, TILE_SIZE),
                new Vector2D(1, 0),
                new Rectangle(0, 0, new Vector2D(2, 1)));
    }

    @Test
    public void getViewport14() throws Exception {
        testVisibleCellRange(
                new Vector2D(TILE_SIZE + 2, TILE_SIZE),
                new Vector2D(TILE_SIZE - 2, 0),
                new Rectangle(0, 0, new Vector2D(2, 1)));
    }

    @Test
    public void getViewport15() throws Exception {
        testVisibleCellRange(
                new Vector2D(TILE_SIZE + 2, TILE_SIZE),
                new Vector2D(TILE_SIZE - 1, 0),
                new Rectangle(0, 0, new Vector2D(3, 1)));
    }

    @Test
    public void getViewport16() throws Exception {
        testVisibleCellRange(
                new Vector2D(TILE_SIZE + 2, TILE_SIZE),
                new Vector2D(TILE_SIZE, 0),
                new Rectangle(1, 0, new Vector2D(2, 1)));
    }

    // viewport 1.5 cell large, vertical movement
    @Test
    public void getViewport17() throws Exception {
        testVisibleCellRange(
                new Vector2D(TILE_SIZE, TILE_SIZE + 2),
                new Vector2D(0, 0),
                new Rectangle(0, 0, new Vector2D(1, 2)));
    }

    @Test
    public void getViewport18() throws Exception {
        testVisibleCellRange(
                new Vector2D(TILE_SIZE, TILE_SIZE + 2),
                new Vector2D(0, 1),
                new Rectangle(0, 0, new Vector2D(1, 2)));
    }

    @Test
    public void getViewport19() throws Exception {
        testVisibleCellRange(
                new Vector2D(TILE_SIZE, TILE_SIZE + 2),
                new Vector2D(0, TILE_SIZE - 2),
                new Rectangle(0, 0, new Vector2D(1, 2)));
    }

    @Test
    public void getViewport20() throws Exception {
        testVisibleCellRange(
                new Vector2D(TILE_SIZE, TILE_SIZE + 2),
                new Vector2D(0, TILE_SIZE - 1),
                new Rectangle(0, 0, new Vector2D(1, 3)));
    }

    @Test
    public void getViewport21() throws Exception {
        testVisibleCellRange(
                new Vector2D(TILE_SIZE, TILE_SIZE + 2),
                new Vector2D(0, TILE_SIZE),
                new Rectangle(0, 1, new Vector2D(1, 2)));
    }

    // viewport 1.5 cell large, horizontal and vertical movement

    @Test
    public void getViewport22() throws Exception {
        testVisibleCellRange(
                new Vector2D(TILE_SIZE + 2, TILE_SIZE + 2),
                new Vector2D(TILE_SIZE - 1, TILE_SIZE - 1),
                new Rectangle(0, 0, new Vector2D(3, 3)));
    }

    private void testVisibleCellRange(Vector2D windowDimensions, Vector2D viewingVector, Rectangle expectedVisibleCellRange) {
        CellViewportRenderer cellViewportRenderer = new CellViewportRenderer(null, windowDimensions);
        Rectangle visibleCellRange = cellViewportRenderer.getViewport(viewingVector);
        assertEquals(expectedVisibleCellRange, visibleCellRange);
    }
}