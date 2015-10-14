package com.fundynamic.d2tm.game.map;

import com.fundynamic.d2tm.game.AbstractD2TMTest;
import com.fundynamic.d2tm.game.terrain.Terrain;
import org.junit.Test;
import org.mockito.Mockito;

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

}