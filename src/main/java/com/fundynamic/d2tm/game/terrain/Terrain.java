package com.fundynamic.d2tm.game.terrain;

import com.fundynamic.d2tm.game.map.Map;
import org.newdawn.slick.Image;

public interface Terrain {

    Image getTileImage();

    Terrain setFacing(Map.TerrainFacing terrainFacing);

    boolean isSame(Terrain terrain);

}
