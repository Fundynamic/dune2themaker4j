package com.fundynamic.d2tm.game.map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

@RunWith(MockitoJUnitRunner.class)
public class MapCellTest {

    @Mock
    private Map map;

    @Test
    public void sameLocation() {
        MapCell mapCell = CellFactory.makeCell(10, 10);
        MapCell other = CellFactory.makeCell(10, 10);
        assertTrue(mapCell.isAtSameLocationAs(other));
    }

    @Test
    public void notOnSameLocationWhenCoordinatesDoNotMatch() {
        MapCell mapCell = CellFactory.makeCell(10, 10);
        MapCell other = CellFactory.makeCell(10, 11);
        assertFalse(mapCell.isAtSameLocationAs(other));
    }

    @Test
    public void notOnSameLocationWhenOtherIsNull() {
        MapCell mapCell = CellFactory.makeCell(10, 10);
        assertFalse(mapCell.isAtSameLocationAs(null));
    }

}