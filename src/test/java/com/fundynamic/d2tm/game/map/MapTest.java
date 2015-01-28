package com.fundynamic.d2tm.game.map;

import com.fundynamic.d2tm.game.terrain.Terrain;
import com.fundynamic.d2tm.game.terrain.TerrainFactory;
import junit.framework.Assert;
import org.junit.Test;
import org.mockito.Mockito;
import org.newdawn.slick.SlickException;

public class MapTest {

    @Test
    public void returnsDimensionsInPixels() throws SlickException {
        TerrainFactory terrainFactory = Mockito.mock(TerrainFactory.class);
        Mockito.doReturn(Mockito.mock(Terrain.class)).when(terrainFactory).createEmptyTerrain();

        int mapWidth = 10;
        int mapHeight = 20;
        int tileWidth = 16;
        int tileHeight = 24;
        Map map = new Map(terrainFactory, mapWidth, mapHeight, tileWidth, tileHeight);

        Assert.assertEquals(mapWidth * tileWidth, map.getWidthInPixels());
        Assert.assertEquals(mapHeight * tileHeight, map.getHeightInPixels());
    }
}