package com.fundynamic.d2tm.game.terrain;

import com.fundynamic.d2tm.game.map.Cell;

public interface TerrainFactory {

    Terrain create(int terrainType, Cell cell);

    Terrain createEmptyTerrain();
}
