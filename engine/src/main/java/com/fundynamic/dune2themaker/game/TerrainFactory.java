package com.fundynamic.dune2themaker.game;

import com.fundynamic.dune2themaker.game.terrain.Terrain;

public interface TerrainFactory {

	Terrain create(int terrainType, Cell cell);

}
