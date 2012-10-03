package com.fundynamic.dune2themaker;

import com.fundynamic.dune2themaker.game.map.Cell;
import com.fundynamic.dune2themaker.game.TerrainFactory;
import com.fundynamic.dune2themaker.game.terrain.EmptyTerrain;
import com.fundynamic.dune2themaker.game.terrain.Terrain;
import com.fundynamic.dune2themaker.game.terrain.TerrainFacing;
import com.fundynamic.dune2themaker.terrain.*;

public class DuneTerrainFactory implements TerrainFactory {

	private final Theme theme;

	public DuneTerrainFactory(Theme theme) {
		this.theme = theme;
	}

	public Terrain create(int terrainType, Cell cell) {
		switch (terrainType) {
			case DuneTerrain.TERRAIN_SAND:
				return new Sand(theme);
			case DuneTerrain.TERRAIN_ROCK:
				return new Rock(theme);
			case DuneTerrain.TERRAIN_SAND_HILL:
				return new SandHill(theme);
			case DuneTerrain.TERRAIN_SPICE:
				return new Spice(theme, cell, 100);
			case DuneTerrain.TERRAIN_MOUNTAIN:
				return new Mountain(theme);
			case DuneTerrain.TERRAIN_SPICE_HILL:
				return new SpiceHill(theme, cell, 100);
			case DuneTerrain.CONCRETE:
				return new Concrete(theme);
			default:
				throw new IndexOutOfBoundsException("Invalid value for terrainType: " + terrainType);
		}
	}

	public Terrain createEmptyTerrain() {
		return new EmptyTerrain();
	}
}
