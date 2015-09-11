package com.fundynamic.d2tm.game.map;

import com.fundynamic.d2tm.game.entities.Entity;
import com.fundynamic.d2tm.game.entities.EntityData;
import com.fundynamic.d2tm.game.entities.Player;
import com.fundynamic.d2tm.game.entities.structures.Structure;
import com.fundynamic.d2tm.game.entities.units.Unit;
import com.fundynamic.d2tm.game.terrain.impl.Rock;
import com.fundynamic.d2tm.game.terrain.impl.Sand;
import com.fundynamic.d2tm.game.terrain.impl.Spice;
import com.fundynamic.d2tm.graphics.Shroud;
import com.fundynamic.d2tm.math.Vector2D;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;

@RunWith(MockitoJUnitRunner.class)
public class MapTest {

    private static int MAP_WIDTH = 10;
    private static int MAP_HEIGHT = 20;

    @Mock
    private Player player;

    private Map map;

    @Before
    public void setUp() throws Exception {
        map = new Map(new Shroud(), MAP_WIDTH, MAP_HEIGHT);

        map.getCell(0, 0).changeTerrain(new Sand());
        map.getCell(5, 5).changeTerrain(new Spice());
        map.getCell(MAP_WIDTH + 1, MAP_HEIGHT + 1).changeTerrain(new Rock()); // because of the invisible border
    }

    @Test
    public void returnsDimensionsInPixels() throws SlickException {
        int tileWidth = 16;
        int tileHeight = 24;
        assertEquals(MAP_WIDTH * tileWidth, map.getWidthInPixels(tileWidth));
        assertEquals(MAP_HEIGHT * tileHeight, map.getHeightInPixels(tileHeight));
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
        Cell cell = map.getCellByAbsolutePixelCoordinates(Vector2D.create(0, 0));
        assertTrue(cell.getTerrain() instanceof Sand);
    }

    @Test
    public void returnsCellForPixelCoordinatesTopLeftOfMapMiddleOfCell() {
        int TILE_SIZE = 32;
        Cell cell = map.getCellByAbsolutePixelCoordinates(Vector2D.create(TILE_SIZE / 2, TILE_SIZE / 2));
        assertTrue(cell.getTerrain() instanceof Sand);
    }

    @Test
    public void returnsCellForPixelCoordinatesMiddleOfMapBottomRightOfCell() {
        int TILE_SIZE = 32;

        // at 5,5 (see setup) is Spice
        int pixelX = TILE_SIZE * 5;
        int pixelY = TILE_SIZE * 5;

        // keep just within the cell
        pixelX += (TILE_SIZE - 1);
        pixelY += (TILE_SIZE - 1);

        Cell cell = map.getCellByAbsolutePixelCoordinates(Vector2D.create(pixelX, pixelY));
        assertTrue(cell.getTerrain() instanceof Spice);
    }

    @Test
    public void placeStructureOfOneByOneOnMap() {
        int TILE_SIZE = 32;
        int SIGHT = 2;
        Structure turret = new Structure(Vector2D.create(5, 5), mock(Image.class), player, new EntityData(TILE_SIZE, TILE_SIZE, SIGHT));
        map.placeStructure(turret);
        Entity entity = map.getCell(Vector2D.create(5, 5)).getEntity();
        assertSame(turret, entity);

        // make sure it does not expand beyond
        assertNull(map.getCell(Vector2D.create(6, 5)).getEntity()); // 1 too much to the top right
        assertNull(map.getCell(Vector2D.create(4, 5)).getEntity()); // 1 too much to the top left
        assertNull(map.getCell(Vector2D.create(6, 6)).getEntity()); // 1 too much to the bottom right
        assertNull(map.getCell(Vector2D.create(4, 6)).getEntity()); // 1 too much to the bottom left
    }

    @Test
    public void placeStructureOfThreeByTwoByOneOnMap() {
        int TILE_SIZE = 32;
        int SIGHT = 2;

        Structure refinery = new Structure(Vector2D.create(5, 5), mock(Image.class), player, new EntityData(TILE_SIZE * 3, TILE_SIZE * 2, SIGHT));
        map.placeStructure(refinery);

        assertSame(refinery, map.getCell(Vector2D.create(5, 5)).getEntity()); // top left
        assertSame(refinery, map.getCell(Vector2D.create(6, 5)).getEntity()); // top middle
        assertSame(refinery, map.getCell(Vector2D.create(7, 5)).getEntity()); // top right
        assertSame(refinery, map.getCell(Vector2D.create(5, 6)).getEntity()); // bottom left
        assertSame(refinery, map.getCell(Vector2D.create(6, 6)).getEntity()); // bottom middle
        assertSame(refinery, map.getCell(Vector2D.create(7, 6)).getEntity()); // bottom right

        // make sure it does not expand beyond
        assertNull(map.getCell(Vector2D.create(8, 5)).getEntity()); // 1 too much to the top right
        assertNull(map.getCell(Vector2D.create(4, 5)).getEntity()); // 1 too much to the top left
        assertNull(map.getCell(Vector2D.create(8, 6)).getEntity()); // 1 too much to the bottom right
        assertNull(map.getCell(Vector2D.create(4, 6)).getEntity()); // 1 too much to the bottom left

        assertNull("structure expanded too far up", map.getCell(Vector2D.create(5, 4)).getEntity()); // 1 too much upwards
        assertNull("structure expanded too far down", map.getCell(Vector2D.create(5, 7)).getEntity()); // 1 too much downwards
    }

    @Test
    public void placeUnit() {
        int TILE_SIZE = 32;
        int SIGHT = 2;
        Unit quad = new Unit(map, Vector2D.create(5, 5), mock(Image.class), player, new EntityData(TILE_SIZE, TILE_SIZE, SIGHT, 1.0f));
        map.placeUnit(quad);
        
        Entity entity = map.getCell(Vector2D.create(5, 5)).getEntity();
        assertSame(quad, entity);

        // make sure it does not expand beyond
        assertNull(map.getCell(Vector2D.create(6, 5)).getEntity()); // 1 too much to the top right
        assertNull(map.getCell(Vector2D.create(4, 5)).getEntity()); // 1 too much to the top left
        assertNull(map.getCell(Vector2D.create(6, 6)).getEntity()); // 1 too much to the bottom right
        assertNull(map.getCell(Vector2D.create(4, 6)).getEntity()); // 1 too much to the bottom left
    }
}