package com.fundynamic.d2tm.game.map;

import com.fundynamic.d2tm.game.terrain.Terrain;
import com.fundynamic.d2tm.game.terrain.TerrainFactory;
import com.fundynamic.d2tm.game.terrain.impl.Rock;
import com.fundynamic.d2tm.game.terrain.impl.Sand;
import com.fundynamic.d2tm.game.terrain.impl.Spice;
import com.fundynamic.d2tm.graphics.Shroud;
import junit.framework.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
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
        Cell cell = map.getCellByPixelsCoordinates(0, 0);
        Assert.assertTrue(cell.getTerrain() instanceof Sand);
    }

    @Test
    public void returnsCellForPixelCoordinatesTopLeftOfMapMiddleOfCell() {
        int TILE_SIZE = 32;
        Cell cell = map.getCellByPixelsCoordinates(TILE_SIZE / 2, TILE_SIZE / 2);
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

        Cell cell = map.getCellByPixelsCoordinates(pixelX, pixelY);
        Assert.assertTrue(cell.getTerrain() instanceof Spice);
    }

}