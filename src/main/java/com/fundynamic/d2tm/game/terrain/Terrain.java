package com.fundynamic.d2tm.game.terrain;

import com.fundynamic.d2tm.game.map.renderer.TerrainFacingDeterminer;
import org.newdawn.slick.Image;

public interface Terrain {

    Image getTileImage();

    Terrain setFacing(TerrainFacingDeterminer.TerrainFacing terrainFacing);

    boolean isSame(Terrain terrain);

}
