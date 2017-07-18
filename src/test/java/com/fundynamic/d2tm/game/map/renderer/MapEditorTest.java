package com.fundynamic.d2tm.game.map.renderer;

import com.fundynamic.d2tm.Game;
import com.fundynamic.d2tm.game.map.Map;
import com.fundynamic.d2tm.game.map.MapEditor;
import com.fundynamic.d2tm.game.terrain.TerrainFactory;
import com.fundynamic.d2tm.game.terrain.impl.DuneTerrain;
import com.fundynamic.d2tm.game.terrain.impl.DuneTerrainFactory;
import com.fundynamic.d2tm.graphics.Shroud;
import com.fundynamic.d2tm.graphics.Theme;
import org.junit.Before;
import org.junit.Test;
import org.newdawn.slick.Image;

import static com.fundynamic.d2tm.game.map.Cell.TILE_SIZE;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;

public class MapEditorTest {

    private Shroud shroud;
    private MapEditor mapEditor;

    @Before
    public void setUp() {
        TerrainFactory terrainFactory = new DuneTerrainFactory(new Theme(mock(Image.class), TILE_SIZE));
        shroud = new Shroud(mock(Image.class), TILE_SIZE);
        mapEditor = new MapEditor(terrainFactory);
    }

    @Test
    public void createMapOfCorrectDimensions() {
        Map map = mapEditor.create(shroud, 3, 3, DuneTerrain.TERRAIN_ROCK);

        assertThat(map.getHeight(), is(3));
        assertThat(map.getWidth(), is(3));
    }

    @Test
    public void createMapWithExpectedTerrain() {
        Map map = mapEditor.create(shroud, 3, 3, DuneTerrain.TERRAIN_ROCK);

        assertThat(map.getTerrainMap(), is(
                "RRR\n" +
                "RRR\n" +
                "RRR\n"
        ));
    }

    @Test
    public void mountainInCenterOf3By3MapHasFullRockCellsAroundIt() {
        Map map = mapEditor.create(shroud, 3, 3, DuneTerrain.TERRAIN_ROCK);

        mapEditor.putTerrainOnCell(map, 2, 2, DuneTerrain.TERRAIN_MOUNTAIN);

        assertThat(map.getTerrainMap(), is(
                "RRR\n" +
                "RMR\n" +
                "RRR\n"
        ));

        mapEditor.smooth(map);

        assertThat(map.getTerrainFacing(2, 2), is(MapEditor.TerrainFacing.SINGLE));

        // full rock facing entirely

        // left side
        assertThat(map.getTerrainFacing(1, 1), is(MapEditor.TerrainFacing.FULL));
        assertThat(map.getTerrainFacing(1, 2), is(MapEditor.TerrainFacing.FULL));
        assertThat(map.getTerrainFacing(1, 3), is(MapEditor.TerrainFacing.FULL));

        // top and bottom
        assertThat(map.getTerrainFacing(2, 1), is(MapEditor.TerrainFacing.FULL));
        assertThat(map.getTerrainFacing(2, 3), is(MapEditor.TerrainFacing.FULL));

        // right side
        assertThat(map.getTerrainFacing(3, 1), is(MapEditor.TerrainFacing.FULL));
        assertThat(map.getTerrainFacing(3, 2), is(MapEditor.TerrainFacing.FULL));
        assertThat(map.getTerrainFacing(3, 3), is(MapEditor.TerrainFacing.FULL));
    }

    @Test
    public void spiceHillInCenterOf3By3MapHasFullSpiceCellsAroundIt() {
        Map map = mapEditor.create(shroud, 3, 3, DuneTerrain.TERRAIN_SPICE);

        mapEditor.putTerrainOnCell(map, 2, 2, DuneTerrain.TERRAIN_SPICE_HILL);

        assertThat(map.getTerrainMap(), is(
                "###\n" +
                "#H#\n" +
                "###\n"
        ));

        mapEditor.smooth(map);

        assertThat(map.getTerrainFacing(2, 2), is(MapEditor.TerrainFacing.SINGLE));

        // full rock facing entirely

        // left side
        assertThat(map.getTerrainFacing(1, 1), is(MapEditor.TerrainFacing.FULL));
        assertThat(map.getTerrainFacing(1, 2), is(MapEditor.TerrainFacing.FULL));
        assertThat(map.getTerrainFacing(1, 3), is(MapEditor.TerrainFacing.FULL));

        // top and bottom
        assertThat(map.getTerrainFacing(2, 1), is(MapEditor.TerrainFacing.FULL));
        assertThat(map.getTerrainFacing(2, 3), is(MapEditor.TerrainFacing.FULL));

        // right side
        assertThat(map.getTerrainFacing(3, 1), is(MapEditor.TerrainFacing.FULL));
        assertThat(map.getTerrainFacing(3, 2), is(MapEditor.TerrainFacing.FULL));
        assertThat(map.getTerrainFacing(3, 3), is(MapEditor.TerrainFacing.FULL));
    }

    @Test
    public void canGenerateRandomMap() {
        int width = 33;
        int height = 63;
        Map map = mapEditor.generateRandom(shroud, width, height);
        assertThat(map.getWidth(), is(width));
        assertThat(map.getHeight(), is(height));
    }

    @Test
    public void returnsMiddleWhenNoSameTypeOfNeighbours() throws Exception {
        assertEquals(MapEditor.TerrainFacing.SINGLE, MapEditor.getFacing(false, false, false, false));
    }

    @Test
    public void returnsTopWhenDifferentTypeAbove() throws Exception {
        assertEquals(MapEditor.TerrainFacing.TOP, MapEditor.getFacing(false, true, true, true));
    }

    @Test
    public void returnsRightWhenDifferentTypeRight() throws Exception {
        assertEquals(MapEditor.TerrainFacing.RIGHT, MapEditor.getFacing(true, false, true, true));
    }

    @Test
    public void returnsBottomWhenDifferentTypeBottom() throws Exception {
        assertEquals(MapEditor.TerrainFacing.BOTTOM, MapEditor.getFacing(true, true, false, true));
    }

    @Test
    public void returnsLeftWhenDifferentTypeLeft() throws Exception {
        assertEquals(MapEditor.TerrainFacing.LEFT, MapEditor.getFacing(true, true, true, false));
    }

    @Test
    public void returnsTopRightWhenSameTypeBottomAndLeft() throws Exception {
        assertEquals(MapEditor.TerrainFacing.TOP_RIGHT, MapEditor.getFacing(false, false, true, true));
    }

    @Test
    public void returnsFullWhenAllSameTypeOfNeighbours() throws Exception {
        assertEquals(MapEditor.TerrainFacing.FULL, MapEditor.getFacing(true, true, true, true));
    }

}