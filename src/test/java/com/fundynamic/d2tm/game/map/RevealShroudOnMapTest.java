package com.fundynamic.d2tm.game.map;


import com.fundynamic.d2tm.game.entities.Player;
import com.fundynamic.d2tm.game.rendering.Recolorer;
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
    private Player player;

    @Before
    public void setUp() throws SlickException {
        Mockito.doReturn(mock(Terrain.class)).when(terrainFactory).createEmptyTerrain();
        player = new Player("John Doe", Recolorer.FactionColor.RED);
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
                map.getAsciiShroudMap(player));
    }

    @Test
    public void fullyRevealedMapIsAllDots() {
        for (int y = 0; y < map.getHeight(); y++) {
            for (int x = 0; x < map.getWidth(); x++) {
                map.revealShroudFor(x, y, player);
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
                map.getAsciiShroudMap(player));
    }

    @Test
    public void revealShroudForWithOnlyXAndYRevealsOneCellOnly() {
        map.revealShroudFor(5, 5, player); // this is 0 based, so it is the 6th row!
        Assert.assertEquals(
                "##########\n" + // 1
                "##########\n" + // 2
                "##########\n" + // 3
                "##########\n" + // 4
                "##########\n" + // 5
                "#####.####\n" + // 6
                "##########\n" + // 7
                "##########\n" + // 8
                "##########\n" + // 9
                "##########\n",  // 10
                map.getAsciiShroudMap(player));
    }

    @Test
    public void revealShroudUsingSightValueOfZeroDoesNotRevealAnyShroud() {
        int sightRange = 0;
        map.revealShroudFor(5, 5, sightRange, player); // this is 0 based, so it is the 6th row!
        Assert.assertEquals(
                "##########\n" + // 1
                "##########\n" + // 2
                "##########\n" + // 3
                "##########\n" + // 4
                "##########\n" + // 5
                "##########\n" + // 6
                "##########\n" + // 7
                "##########\n" + // 8
                "##########\n" + // 9
                "##########\n",  // 10
                map.getAsciiShroudMap(player));
    }

    @Test
    public void revealShroudUsingSightValueOfOneRevealsOneCell() {
        int sightRange = 1;
        map.revealShroudFor(5, 5, sightRange, player); // this is 0 based, so it is the 6th row!
        Assert.assertEquals(
                "##########\n" + // 1
                "##########\n" + // 2
                "##########\n" + // 3
                "##########\n" + // 4
                "##########\n" + // 5
                "#####.####\n" + // 6
                "##########\n" + // 7
                "##########\n" + // 8
                "##########\n" + // 9
                "##########\n",  // 10
                map.getAsciiShroudMap(player));
    }

    @Test
    public void revealShroudUsingSightValueOfTwoRevealsOneCell() {
        int sightRange = 2;
        map.revealShroudFor(5, 5, sightRange, player); // this is 0 based, so it is the 6th row!
        Assert.assertEquals(
                "##########\n" + // 1
                "##########\n" + // 2
                "##########\n" + // 3
                "##########\n" + // 4
                "####...###\n" + // 5
                "####...###\n" + // 6
                "####...###\n" + // 7
                "##########\n" + // 8
                "##########\n" + // 9
                "##########\n",  // 10
                map.getAsciiShroudMap(player));
    }

    @Test
    public void revealShroudUsingSightValueOfThreeRevealsOneCell() {
        int sightRange = 3;
        map.revealShroudFor(5, 5, sightRange, player); // this is 0 based, so it is the 6th row!
        Assert.assertEquals(
                "##########\n" + // 1
                "##########\n" + // 2
                "##########\n" + // 3
                "####...###\n" + // 4
                "###.....##\n" + // 5
                "###.....##\n" + // 6
                "###.....##\n" + // 7
                "####...###\n" + // 8
                "##########\n" + // 9
                "##########\n",  // 10
                map.getAsciiShroudMap(player));
    }

    @Test
    public void revealShroudUsingSightValueOfFourRevealsOneCell() {
        int sightRange = 4;
        map.revealShroudFor(5, 5, sightRange, player); // this is 0 based, so it is the 6th row!

        Assert.assertEquals(
                "##########\n" +
                "##########\n" +
                "###.....##\n" +
                "##.......#\n" +
                "##.......#\n" +
                "##.......#\n" +
                "##.......#\n" +
                "##.......#\n" +
                "###.....##\n" +
                "##########\n",
                map.getAsciiShroudMap(player));
    }

}
