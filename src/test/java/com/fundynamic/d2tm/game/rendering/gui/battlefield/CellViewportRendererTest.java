package com.fundynamic.d2tm.game.rendering.gui.battlefield;

import com.fundynamic.d2tm.math.Rectangle;
import com.fundynamic.d2tm.math.Vector2D;
import org.junit.Test;

import static com.fundynamic.d2tm.game.map.Cell.TILE_SIZE;
import static org.junit.Assert.*;

public class CellViewportRendererTest {

    @Test
    public void getVisibleCell1() throws Exception {
        testVisibleCellRange(
            new Vector2D(TILE_SIZE, TILE_SIZE),
            new Vector2D(0, 0),
            new Rectangle(0, 0, new Vector2D(1, 1)));
    }

    // viewport exactly 1 cell large, exactly one cell, horizontal movement

    @Test
    public void getVisibleCell2() throws Exception {
        testVisibleCellRange(
                new Vector2D(TILE_SIZE, TILE_SIZE),
                new Vector2D(1, 0),
                new Rectangle(0, 0, new Vector2D(2, 1)));
    }

    @Test
    public void getVisibleCell3() throws Exception {
        testVisibleCellRange(
                new Vector2D(TILE_SIZE, TILE_SIZE),
                new Vector2D(TILE_SIZE - 1, 0),
                new Rectangle(0, 0, new Vector2D(2, 1)));
    }

    @Test
    public void getVisibleCell4() throws Exception {
        testVisibleCellRange(
                new Vector2D(TILE_SIZE, TILE_SIZE),
                new Vector2D(TILE_SIZE, 0),
                new Rectangle(1, 0, new Vector2D(1, 1)));
    }

    // viewport exactly 1 cell large, vertical movement

    @Test
    public void getVisibleCell5() throws Exception {
        testVisibleCellRange(
                new Vector2D(TILE_SIZE, TILE_SIZE),
                new Vector2D(0, 1),
                new Rectangle(0, 0, new Vector2D(1, 2)));
    }

    @Test
    public void getVisibleCell6() throws Exception {
        testVisibleCellRange(
                new Vector2D(TILE_SIZE, TILE_SIZE),
                new Vector2D(0, TILE_SIZE - 1),
                new Rectangle(0, 0, new Vector2D(1, 2)));
    }

    @Test
    public void getVisibleCell7() throws Exception {
        testVisibleCellRange(
                new Vector2D(TILE_SIZE, TILE_SIZE),
                new Vector2D(0, TILE_SIZE),
                new Rectangle(0, 1, new Vector2D(1, 1)));
    }

    // viewport 1 cell + 1px large, horizontal movement
    @Test
    public void getVisibleCell8() throws Exception {
        testVisibleCellRange(
                new Vector2D(TILE_SIZE + 1, TILE_SIZE),
                new Vector2D(0, 0),
                new Rectangle(0, 0, new Vector2D(2, 1)));
    }

    @Test
    public void getVisibleCell9() throws Exception {
        testVisibleCellRange(
                new Vector2D(TILE_SIZE + 1, TILE_SIZE),
                new Vector2D(1, 0),
                new Rectangle(0, 0, new Vector2D(2, 1)));
    }

    @Test
    public void getVisibleCell10() throws Exception {
        testVisibleCellRange(
                new Vector2D(TILE_SIZE + 1, TILE_SIZE),
                new Vector2D(TILE_SIZE -1, 0),
                new Rectangle(0, 0, new Vector2D(2, 1)));
    }

    @Test
    public void getVisibleCell11() throws Exception {
        testVisibleCellRange(
                new Vector2D(TILE_SIZE + 1, TILE_SIZE),
                new Vector2D(TILE_SIZE, 0),
                new Rectangle(1, 0, new Vector2D(2, 1)));
    }

    // viewport 1 cell + 1px large, horizontal movement
    @Test
    public void getVisibleCell12() throws Exception {
        testVisibleCellRange(
                new Vector2D(TILE_SIZE + 2, TILE_SIZE),
                new Vector2D(0, 0),
                new Rectangle(0, 0, new Vector2D(2, 1)));
    }

    @Test
    public void getVisibleCell13() throws Exception {
        testVisibleCellRange(
                new Vector2D(TILE_SIZE + 2, TILE_SIZE),
                new Vector2D(1, 0),
                new Rectangle(0, 0, new Vector2D(2, 1)));
    }

    @Test
    public void getVisibleCell14() throws Exception {
        testVisibleCellRange(
                new Vector2D(TILE_SIZE + 2, TILE_SIZE),
                new Vector2D(TILE_SIZE - 2, 0),
                new Rectangle(0, 0, new Vector2D(2, 1)));
    }

    @Test
    public void getVisibleCell15() throws Exception {
        testVisibleCellRange(
                new Vector2D(TILE_SIZE + 2, TILE_SIZE),
                new Vector2D(TILE_SIZE - 1, 0),
                new Rectangle(0, 0, new Vector2D(3, 1)));
    }

    @Test
    public void getVisibleCell16() throws Exception {
        testVisibleCellRange(
                new Vector2D(TILE_SIZE + 2, TILE_SIZE),
                new Vector2D(TILE_SIZE, 0),
                new Rectangle(1, 0, new Vector2D(2, 1)));
    }

    // viewport 1.5 cell large, vertical movement
    @Test
    public void getVisibleCell17() throws Exception {
        testVisibleCellRange(
                new Vector2D(TILE_SIZE, TILE_SIZE + 2),
                new Vector2D(0, 0),
                new Rectangle(0, 0, new Vector2D(1, 2)));
    }

    @Test
    public void getVisibleCell18() throws Exception {
        testVisibleCellRange(
                new Vector2D(TILE_SIZE, TILE_SIZE + 2),
                new Vector2D(0, 1),
                new Rectangle(0, 0, new Vector2D(1, 2)));
    }

    @Test
    public void getVisibleCell19() throws Exception {
        testVisibleCellRange(
                new Vector2D(TILE_SIZE, TILE_SIZE + 2),
                new Vector2D(0, TILE_SIZE - 2),
                new Rectangle(0, 0, new Vector2D(1, 2)));
    }

    @Test
    public void getVisibleCell20() throws Exception {
        testVisibleCellRange(
                new Vector2D(TILE_SIZE, TILE_SIZE + 2),
                new Vector2D(0, TILE_SIZE - 1),
                new Rectangle(0, 0, new Vector2D(1, 3)));
    }

    @Test
    public void getVisibleCell21() throws Exception {
        testVisibleCellRange(
                new Vector2D(TILE_SIZE, TILE_SIZE + 2),
                new Vector2D(0, TILE_SIZE),
                new Rectangle(0, 1, new Vector2D(1, 2)));
    }

    // viewport 1.5 cell large, horizontal and vertical movement

    @Test
    public void getVisibleCell22() throws Exception {
        testVisibleCellRange(
                new Vector2D(TILE_SIZE + 2, TILE_SIZE + 2),
                new Vector2D(TILE_SIZE - 1, TILE_SIZE - 1),
                new Rectangle(0, 0, new Vector2D(3, 3)));
    }

    private void testVisibleCellRange(Vector2D windowDimensions, Vector2D viewingVector, Rectangle expectedVisibleCellRange) {
        CellViewportRenderer cellViewportRenderer = new CellViewportRenderer(null, windowDimensions);
        Rectangle visibleCellRange = cellViewportRenderer.getVisibleCellRange(viewingVector);
        assertEquals(expectedVisibleCellRange, visibleCellRange);
    }
}