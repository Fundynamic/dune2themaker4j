package com.fundynamic.d2tm.game.map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

@RunWith(MockitoJUnitRunner.class)
public class CellTest {

    @Mock
    private Map map;

    @Test (expected = IllegalArgumentException.class)
    public void terrainConstructorThrowsIllegalArgumentExceptionWhenArgumentIsNull() {
        new Cell(map, null, 0, 0);
    }

    // TODO: add more test cases for constructor (out of bounds on map etc)

    @Test
    public void sameLocation() {
        Cell mapCell = CellFactory.makeCell(10, 10);
        Cell other = CellFactory.makeCell(10, 10);
        assertTrue(mapCell.isAtSameLocationAs(other));
    }

    @Test
    public void notOnSameLocationWhenCoordinatesDoNotMatch() {
        Cell mapCell = CellFactory.makeCell(10, 10);
        Cell other = CellFactory.makeCell(10, 11);
        assertFalse(mapCell.isAtSameLocationAs(other));
    }

    @Test
    public void notOnSameLocationWhenOtherIsNull() {
        Cell mapCell = CellFactory.makeCell(10, 10);
        assertFalse(mapCell.isAtSameLocationAs(null));
    }

}