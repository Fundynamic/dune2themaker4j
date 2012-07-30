package com.fundynamic.dune2themaker.game;

import com.fundynamic.dune2themaker.game.terrain.Terrain;
import com.fundynamic.dune2themaker.game.terrain.TerrainFacing;

public interface TerrainFactory {

	Terrain create(int terrainType, Cell cell);

}
