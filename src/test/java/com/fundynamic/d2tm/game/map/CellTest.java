package com.fundynamic.d2tm.game.map;

import com.fundynamic.d2tm.game.AbstractD2TMTest;
import com.fundynamic.d2tm.game.terrain.Terrain;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.List;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class CellTest extends AbstractD2TMTest {

    @Test (expected = IllegalArgumentException.class)
    public void terrainConstructorThrowsIllegalArgumentExceptionWhenArgumentIsNull() {
        new Cell(map, null, 0, 0);
    }

    @Test (expected = OutOfMapBoundsException.class)
    public void throwsOutOfMapBoundsExceptionWhenXIsLowerThanZero() {
        new Cell(map, Mockito.mock(Terrain.class), -1, 0);
    }

    @Test (expected = OutOfMapBoundsException.class)
    public void throwsOutOfMapBoundsExceptionWhenYIsLowerThanZero() {
        new Cell(map, Mockito.mock(Terrain.class), 0, -1);
    }

    @Test
    public void sameLocation() {
        Cell mapCell = makeCell(10, 10);
        Cell other = makeCell(10, 10);
        assertTrue(mapCell.isAtSameLocationAs(other));
    }

    @Test
    public void notOnSameLocationWhenCoordinatesDoNotMatch() {
        Cell mapCell = makeCell(10, 10);
        Cell other = makeCell(10, 11);
        assertFalse(mapCell.isAtSameLocationAs(other));
    }

    @Test
    public void notOnSameLocationWhenOtherIsNull() {
        Cell mapCell = makeCell(10, 10);
        assertFalse(mapCell.isAtSameLocationAs(null));
    }

    @Test
    public void getSurroundingCellsNotNearEdges() {
        Cell cell = makeCell(10, 10); // not near any edge
        List<Cell> surroundingCells = cell.getSurroundingCells();
        Assert.assertEquals(8, surroundingCells.size());
    }

    @Test
    public void getSurroundingCellsNearTop() {
        Cell cell = makeCell(10, 0); // cannot go higher than top, so missing 3 cells
        List<Cell> surroundingCells = cell.getSurroundingCells();
        Assert.assertEquals(5, surroundingCells.size());
    }

    @Test
    public void getSurroundingCellsNearLeftBorder() {
        Cell cell = makeCell(0, 10); // cannot go higher than top, so missing 3 cells
        List<Cell> surroundingCells = cell.getSurroundingCells();
        Assert.assertEquals(5, surroundingCells.size());
    }

    @Test
    public void getSurroundingCellsNearRightBorder() {
        // This is weird!?
        Cell cell = makeCell(map.getWidth() + 1, 10);
        List<Cell> surroundingCells = cell.getSurroundingCells();
        Assert.assertEquals(5, surroundingCells.size());
    }

    @Test
    public void getSurroundingCellsNearBottom() {
        //!?
        Cell cell = makeCell(10, map.getHeight() + 1); // cannot go lower
        List<Cell> surroundingCells = cell.getSurroundingCells();
        Assert.assertEquals(5, surroundingCells.size());
    }

    @Test
    public void getSurroundingCellsNearUpperLeft() {
        //!?
        Cell cell = makeCell(0, 0);
        List<Cell> surroundingCells = cell.getSurroundingCells();
        Assert.assertEquals(3, surroundingCells.size());
    }

    @Test
    public void getSurroundingCellsNearUpperRight() {
        //!?
        Cell cell = makeCell(map.getWidth() + 1, 0);
        List<Cell> surroundingCells = cell.getSurroundingCells();
        Assert.assertEquals(3, surroundingCells.size());
    }

    @Test
    public void getSurroundingCellsNearLowerLeft() {
        //!?
        Cell cell = makeCell(0, map.getHeight() + 1);
        List<Cell> surroundingCells = cell.getSurroundingCells();
        Assert.assertEquals(3, surroundingCells.size());
    }

    @Test
    public void getSurroundingCellsNearLowerRight() {
        //!?
        Cell cell = makeCell(map.getWidth() + 1, map.getHeight() + 1);
        List<Cell> surroundingCells = cell.getSurroundingCells();
        Assert.assertEquals(3, surroundingCells.size());
    }

}