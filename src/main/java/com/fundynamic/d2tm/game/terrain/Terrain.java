package com.fundynamic.d2tm.game.terrain;

import com.fundynamic.d2tm.game.map.MapEditor;
import org.newdawn.slick.Image;

public interface Terrain {

    Image getTileImage();

    Terrain setFacing(MapEditor.TerrainFacing terrainFacing);

    boolean isSame(Terrain terrain);

    int getTerrainType();

    MapEditor.TerrainFacing getTerrainFacing();

}
