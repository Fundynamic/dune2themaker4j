package com.fundynamic.d2tm.game.map.renderer;

import com.fundynamic.d2tm.game.map.Map;
import com.fundynamic.d2tm.game.map.MapEditor;
import com.fundynamic.d2tm.game.terrain.TerrainFactory;
import com.fundynamic.d2tm.game.terrain.impl.DuneTerrain;
import com.fundynamic.d2tm.game.terrain.impl.Rock;
import com.fundynamic.d2tm.graphics.Shroud;
import com.fundynamic.d2tm.graphics.Theme;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.mockito.Mockito;
import org.newdawn.slick.Image;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;

public class MapEditorTest {


    // TODO: given a map full of rcok
    // place a mountain on it. The rock should be a full-rock still!
    @Test
    @Ignore // this makes painly clear how aweful the architecture is now. The rendering is tightly coupled to the map data
    public void createMapOfCorrectDimensions() {
        TerrainFactory terrainFactory = Mockito.mock(TerrainFactory.class);
        Theme theme = new Theme(Mockito.mock(Image.class), 32, 32);
        Rock rock = new Rock(theme);
        when(terrainFactory.createEmptyTerrain()).thenReturn(rock);

        Shroud shroud = Mockito.mock(Shroud.class);
        MapEditor mapEditor = new MapEditor(terrainFactory);
        Map map = mapEditor.create(shroud, 3, 3, DuneTerrain.TERRAIN_ROCK);

        assertThat(map.getHeight(), is(3));
    }


    @Test
    public void returnsMiddleWhenNoSameTypeOfNeighbours() throws Exception {
        Assert.assertEquals(MapEditor.TerrainFacing.MIDDLE, MapEditor.getFacing(false, false, false, false));
    }

    @Test
    public void returnsTopWhenDifferentTypeAbove() throws Exception {
        Assert.assertEquals(MapEditor.TerrainFacing.TOP, MapEditor.getFacing(false, true, true, true));
    }

    @Test
    public void returnsRightWhenDifferentTypeRight() throws Exception {
        Assert.assertEquals(MapEditor.TerrainFacing.RIGHT, MapEditor.getFacing(true, false, true, true));
    }

    @Test
    public void returnsBottomWhenDifferentTypeBottom() throws Exception {
        Assert.assertEquals(MapEditor.TerrainFacing.BOTTOM, MapEditor.getFacing(true, true, false, true));
    }

    @Test
    public void returnsLeftWhenDifferentTypeLeft() throws Exception {
        Assert.assertEquals(MapEditor.TerrainFacing.LEFT, MapEditor.getFacing(true, true, true, false));
    }

    @Test
    public void returnsTopRightWhenSameTypeBottomAndLeft() throws Exception {
        Assert.assertEquals(MapEditor.TerrainFacing.TOP_RIGHT, MapEditor.getFacing(false, false, true, true));
    }

    @Test
    public void returnsFullWhenAllSameTypeOfNeighbours() throws Exception {
        Assert.assertEquals(MapEditor.TerrainFacing.FULL, MapEditor.getFacing(true, true, true, true));
    }

}