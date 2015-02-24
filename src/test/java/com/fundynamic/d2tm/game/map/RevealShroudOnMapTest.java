package com.fundynamic.d2tm.game.map;


import com.fundynamic.d2tm.game.terrain.Terrain;
import com.fundynamic.d2tm.game.terrain.TerrainFactory;
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
public class RevealShroudOnMapTest {

    public static int MAP_WIDTH = 10;
    public static int MAP_HEIGHT = 10;


    @Mock
    private TerrainFactory terrainFactory;

    @Mock
    private Shroud shroud;

    private Map map;

    @Before
    public void setUp() throws SlickException {
        Mockito.doReturn(mock(Terrain.class)).when(terrainFactory).createEmptyTerrain();
        map = new Map(terrainFactory, shroud, MAP_WIDTH, MAP_HEIGHT);
    }

    @Test
    public void newMapIsInitiallyTotallyShroudedGivesAllHashes() {
        Assert.assertEquals(
                "##########\n" +
                "##########\n" +
                "##########\n" +
                "##########\n" +
                "##########\n" +
                "##########\n" +
                "##########\n" +
                "##########\n" +
                "##########\n" +
                "##########\n",
                map.getAsciiShroudMap());
    }

    @Test
    public void fullyRevealedMapIsAllDots() {
        for (int y = 0; y < map.getHeight(); y++) {
            for (int x = 0; x < map.getWidth(); x++) {
                map.revealShroudFor(x, y);
            }
        }
        Assert.assertEquals(
                "..........\n" +
                "..........\n" +
                "..........\n" +
                "..........\n" +
                "..........\n" +
                "..........\n" +
                "..........\n" +
                "..........\n" +
                "..........\n" +
                "..........\n",
                map.getAsciiShroudMap());
    }

}
