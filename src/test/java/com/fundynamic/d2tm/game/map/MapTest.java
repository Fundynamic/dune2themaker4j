package com.fundynamic.d2tm.game.map;

import com.fundynamic.d2tm.game.entities.Entity;
import com.fundynamic.d2tm.game.entities.Player;
import com.fundynamic.d2tm.game.entities.structures.Structure;
import com.fundynamic.d2tm.game.entities.units.Unit;
import com.fundynamic.d2tm.game.terrain.Terrain;
import com.fundynamic.d2tm.game.terrain.TerrainFactory;
import com.fundynamic.d2tm.game.terrain.impl.Rock;
import com.fundynamic.d2tm.game.terrain.impl.Sand;
import com.fundynamic.d2tm.game.terrain.impl.Spice;
import com.fundynamic.d2tm.graphics.Shroud;
import com.fundynamic.d2tm.math.Vector2D;
import junit.framework.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

import static org.mockito.Mockito.mock;

@RunWith(MockitoJUnitRunner.class)
public class MapTest {

    private static int MAP_WIDTH = 10;
    private static int MAP_HEIGHT = 20;

    @Mock
    private TerrainFactory terrainFactory;

    @Mock
    private Shroud shroud;

    @Mock
    private Player player;

    private Map map;

    @Before
    public void setUp() throws Exception {
        Mockito.doReturn(mock(Terrain.class)).when(terrainFactory).createEmptyTerrain();
        map = new Map(terrainFactory, shroud, MAP_WIDTH, MAP_HEIGHT);

        map.getCell(0, 0).changeTerrain(mock(Sand.class));
        map.getCell(5, 5).changeTerrain(mock(Spice.class));
        map.getCell(MAP_WIDTH + 1, MAP_HEIGHT + 1).changeTerrain(mock(Rock.class)); // because of the invisible border
    }

    @Test
    public void returnsDimensionsInPixels() throws SlickException {
        int tileWidth = 16;
        int tileHeight = 24;
        Assert.assertEquals(MAP_WIDTH * tileWidth, map.getWidthInPixels(tileWidth));
        Assert.assertEquals(MAP_HEIGHT * tileHeight, map.getHeightInPixels(tileHeight));
    }

    @Test
    public void getCellProtectedReturnsUpperLeftValidCellWhenGoingOutOfBoundsUpperLeft() throws SlickException {
        Cell protectedCell = map.getCellProtected(-1, -1);
        Assert.assertTrue(protectedCell.getTerrain() instanceof Sand);
    }

    @Test
    public void getCellProtectedReturnsLowerRightValidCellWhenGoingOutOfBoundsLowerRight() throws SlickException {
        Cell protectedCell = map.getCellProtected(MAP_WIDTH + 2, MAP_HEIGHT + 2); // go over the invisible border
        Assert.assertTrue(protectedCell.getTerrain() instanceof Rock);
    }

    @Test
    public void getCellProtectedSmokeTest() throws SlickException {
        Cell protectedCell = map.getCellProtected(5, 5);
        Assert.assertTrue(protectedCell.getTerrain() instanceof Spice);
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
        Cell cell = map.getCellByAbsolutePixelCoordinates(0, 0);
        Assert.assertTrue(cell.getTerrain() instanceof Sand);
    }

    @Test
    public void returnsCellForPixelCoordinatesTopLeftOfMapMiddleOfCell() {
        int TILE_SIZE = 32;
        Cell cell = map.getCellByAbsolutePixelCoordinates(TILE_SIZE / 2, TILE_SIZE / 2);
        Assert.assertTrue(cell.getTerrain() instanceof Sand);
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

        Cell cell = map.getCellByAbsolutePixelCoordinates(pixelX, pixelY);
        Assert.assertTrue(cell.getTerrain() instanceof Spice);
    }

    @Test
    public void placeStructureOfOneByOneOnMap() {
        int TILE_SIZE = 32;
        int SIGHT = 2;
        Structure turret = new Structure(Vector2D.create(5, 5), mock(Image.class), TILE_SIZE, TILE_SIZE, SIGHT, player);
        map.placeStructure(turret);
        Entity entity = map.getCell(Vector2D.create(5, 5)).getEntity();
        Assert.assertSame(turret, entity);

        // make sure it does not expand beyond
        Assert.assertNull(map.getCell(Vector2D.create(6, 5)).getEntity()); // 1 too much to the top right
        Assert.assertNull(map.getCell(Vector2D.create(4, 5)).getEntity()); // 1 too much to the top left
        Assert.assertNull(map.getCell(Vector2D.create(6, 6)).getEntity()); // 1 too much to the bottom right
        Assert.assertNull(map.getCell(Vector2D.create(4, 6)).getEntity()); // 1 too much to the bottom left
    }

    @Test
    public void placeStructureOfThreeByTwoByOneOnMap() {
        int TILE_SIZE = 32;
        int SIGHT = 2;

        Structure refinery = new Structure(Vector2D.create(5, 5), mock(Image.class), TILE_SIZE * 3, TILE_SIZE * 2, SIGHT, player);
        map.placeStructure(refinery);

        Assert.assertSame(refinery, map.getCell(Vector2D.create(5, 5)).getEntity()); // top left
        Assert.assertSame(refinery, map.getCell(Vector2D.create(6, 5)).getEntity()); // top middle
        Assert.assertSame(refinery, map.getCell(Vector2D.create(7, 5)).getEntity()); // top right
        Assert.assertSame(refinery, map.getCell(Vector2D.create(5, 6)).getEntity()); // bottom left
        Assert.assertSame(refinery, map.getCell(Vector2D.create(6, 6)).getEntity()); // bottom middle
        Assert.assertSame(refinery, map.getCell(Vector2D.create(7, 6)).getEntity()); // bottom right

        // make sure it does not expand beyond
        Assert.assertNull(map.getCell(Vector2D.create(8, 5)).getEntity()); // 1 too much to the top right
        Assert.assertNull(map.getCell(Vector2D.create(4, 5)).getEntity()); // 1 too much to the top left
        Assert.assertNull(map.getCell(Vector2D.create(8, 6)).getEntity()); // 1 too much to the bottom right
        Assert.assertNull(map.getCell(Vector2D.create(4, 6)).getEntity()); // 1 too much to the bottom left

        Assert.assertNull("structure expanded too far up", map.getCell(Vector2D.create(5, 4)).getEntity()); // 1 too much upwards
        Assert.assertNull("structure expanded too far down", map.getCell(Vector2D.create(5, 7)).getEntity()); // 1 too much downwards
    }

    @Test
    public void placeUnit() {
        int TILE_SIZE = 32;
        int SIGHT = 2;
        Unit quad = new Unit(map, Vector2D.create(5, 5), mock(Image.class), TILE_SIZE, TILE_SIZE, SIGHT, player);
        map.placeUnit(quad);
        
        Entity entity = map.getCell(Vector2D.create(5, 5)).getEntity();
        Assert.assertSame(quad, entity);

        // make sure it does not expand beyond
        Assert.assertNull(map.getCell(Vector2D.create(6, 5)).getEntity()); // 1 too much to the top right
        Assert.assertNull(map.getCell(Vector2D.create(4, 5)).getEntity()); // 1 too much to the top left
        Assert.assertNull(map.getCell(Vector2D.create(6, 6)).getEntity()); // 1 too much to the bottom right
        Assert.assertNull(map.getCell(Vector2D.create(4, 6)).getEntity()); // 1 too much to the bottom left
    }
}