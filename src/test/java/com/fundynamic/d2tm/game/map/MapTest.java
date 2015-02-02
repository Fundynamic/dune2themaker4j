package com.fundynamic.d2tm.game.map;

import com.fundynamic.d2tm.game.terrain.Terrain;
import com.fundynamic.d2tm.game.terrain.TerrainFactory;
import com.fundynamic.d2tm.graphics.Shroud;
import junit.framework.Assert;
import org.junit.Test;
import org.mockito.Mockito;
import org.newdawn.slick.SlickException;

public class MapTest {

    @Test
    public void returnsDimensionsInPixels() throws SlickException {
        TerrainFactory terrainFactory = Mockito.mock(TerrainFactory.class);
        Mockito.doReturn(Mockito.mock(Terrain.class)).when(terrainFactory).createEmptyTerrain();
        Shroud shroud = Mockito.mock(Shroud.class);

        int mapWidth = 10;
        int mapHeight = 20;
        int tileWidth = 16;
        int tileHeight = 24;
        Map map = new Map(terrainFactory, shroud, mapWidth, mapHeight, tileWidth, tileHeight);

        Assert.assertEquals(mapWidth * tileWidth, map.getWidthInPixels());
        Assert.assertEquals(mapHeight * tileHeight, map.getHeightInPixels());
    }
}