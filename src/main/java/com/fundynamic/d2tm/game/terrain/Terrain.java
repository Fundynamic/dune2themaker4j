package com.fundynamic.d2tm.game.terrain;

import com.fundynamic.d2tm.graphics.TerrainFacing;
import org.newdawn.slick.Image;

public interface Terrain {

    Image getTileImage();

    Terrain setFacing(TerrainFacing terrainFacing);

    boolean isSame(Terrain terrain);

}
