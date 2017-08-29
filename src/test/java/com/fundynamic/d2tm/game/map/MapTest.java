package com.fundynamic.d2tm.game.map;

import com.fundynamic.d2tm.game.AbstractD2TMTest;
import com.fundynamic.d2tm.game.terrain.impl.Rock;
import com.fundynamic.d2tm.game.terrain.impl.Sand;
import com.fundynamic.d2tm.game.terrain.impl.Spice;
import com.fundynamic.d2tm.graphics.Theme;
import com.fundynamic.d2tm.math.Coordinate;
import com.fundynamic.d2tm.math.MapCoordinate;
import org.junit.Before;
import org.junit.Test;
import org.newdawn.slick.SlickException;

import static com.fundynamic.d2tm.game.map.Cell.TILE_SIZE;
import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;

public class MapTest extends AbstractD2TMTest {

    private static int MAP_WIDTH = 10;
    private static int MAP_HEIGHT = 20;

    @Before
    public void setUp() throws SlickException {
        super.setUp();
        map = makeMap(MAP_WIDTH, MAP_HEIGHT);

        map.getCell(0, 0).changeTerrain(new Sand());
        map.getCell(5, 5).changeTerrain(new Spice(mock(Theme.class), null, 0));
        map.getCell(MAP_WIDTH + 1, MAP_HEIGHT + 1).changeTerrain(new Rock()); // because of the invisible border

        entityRepository = makeTestableEntityRepository(map, entitiesData);
    }

    @Test
    public void isWithinPlayableMapBoundaries() {
        // smoke test
        assertTrue(map.isWithinPlayableMapBoundaries(MapCoordinate.create(3,3)));

        // 0,0 is just over the edge (on top, and on the left)
        assertFalse(map.isWithinPlayableMapBoundaries(MapCoordinate.create(0,0)));

        // x is within boundaries, but y is still over the top
        assertFalse(map.isWithinPlayableMapBoundaries(MapCoordinate.create(1,0)));

        // 1,1 is the upmost topleft coordinate
        assertTrue(map.isWithinPlayableMapBoundaries(MapCoordinate.create(1,1)));

        // going over the edge at the right
        assertFalse(map.isWithinPlayableMapBoundaries(MapCoordinate.create(MAP_WIDTH + 1,1)));

        // going over the edge at the bottom
        assertFalse(map.isWithinPlayableMapBoundaries(MapCoordinate.create(1,MAP_HEIGHT + 1)));

        // going over the edge at the right and bottom
        assertFalse(map.isWithinPlayableMapBoundaries(MapCoordinate.create(MAP_WIDTH + 1,MAP_HEIGHT + 1)));

        // this is the bottom-right corner
        assertTrue(map.isWithinPlayableMapBoundaries(MapCoordinate.create(MAP_WIDTH,MAP_HEIGHT)));
    }

    @Test
    public void returnsDimensionsInPixels() throws SlickException {
        assertEquals(MAP_WIDTH * TILE_SIZE, map.getWidthInPixels());
        assertEquals(MAP_HEIGHT * TILE_SIZE, map.getHeightInPixels());
    }

    @Test
    public void getCellProtectedReturnsUpperLeftValidCellWhenGoingOutOfBoundsUpperLeft() throws SlickException {
        Cell protectedCell = map.getCellProtected(-1, -1);
        assertTrue(protectedCell.getTerrain() instanceof Sand);
    }

    @Test
    public void getCellProtectedReturnsLowerRightValidCellWhenGoingOutOfBoundsLowerRight() throws SlickException {
        Cell protectedCell = map.getCellProtected(MAP_WIDTH + 2, MAP_HEIGHT + 2); // go over the invisible border
        assertTrue(protectedCell.getTerrain() instanceof Rock);
    }

    @Test
    public void getCellProtectedSmokeTest() throws SlickException {
        Cell protectedCell = map.getCellProtected(5, 5);
        assertTrue(protectedCell.getTerrain() instanceof Spice);
    }

    @Test (expected = ArrayIndexOutOfBoundsException.class)
    public void getCellThrowsOutOfBoundsWhenGoingOutOfBoundsUpperLeft() throws SlickException {
        map.getCell(-1, -1);
    }

    @Test (expected = ArrayIndexOutOfBoundsException.class)
    public void getCellThrowsOutOfBoundsWhenGoingOutOfBoundsLowerRight() throws SlickException {
        map.getCell(MAP_WIDTH + 2, MAP_HEIGHT + 2); // go over the invisible border
    }

    @Test
    public void returnsCellForPixelCoordinatesTopLeftOfMapTopLeftOfCell() {
        Cell cell = map.getCellByAbsoluteMapCoordinates(Coordinate.create(0, 0));
        assertTrue(cell.getTerrain() instanceof Sand);
    }

    @Test
    public void returnsCellForPixelCoordinatesTopLeftOfMapMiddleOfCell() {
        Cell cell = map.getCellByAbsoluteMapCoordinates(Coordinate.create(TILE_SIZE / 2, TILE_SIZE / 2));
        assertTrue(cell.getTerrain() instanceof Sand);
    }

    @Test
    public void returnsCellForPixelCoordinatesMiddleOfMapBottomRightOfCell() {
        // at 5,5 (see setup) is Spice
        int pixelX = TILE_SIZE * 5;
        int pixelY = TILE_SIZE * 5;

        // keep just within the cell
        pixelX += (TILE_SIZE - 1);
        pixelY += (TILE_SIZE - 1);

        Cell cell = map.getCellByAbsoluteMapCoordinates(Coordinate.create(pixelX, pixelY));
        assertTrue(cell.getTerrain() instanceof Spice);
    }

}