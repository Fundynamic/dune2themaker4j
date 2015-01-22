package com.fundynamic.d2tm.game;

import com.fundynamic.d2tm.game.map.Cell;
import com.fundynamic.d2tm.game.terrain.Terrain;

public interface TerrainFactory {

    Terrain create(int terrainType, Cell cell);

    Terrain createEmptyTerrain();
}
