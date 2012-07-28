package com.fundynamic.dune2themaker;

import com.fundynamic.dune2themaker.game.Cell;
import com.fundynamic.dune2themaker.game.TerrainFactory;
import com.fundynamic.dune2themaker.game.terrain.Terrain;
import com.fundynamic.dune2themaker.terrain.Concrete;
import com.fundynamic.dune2themaker.terrain.Mountain;
import com.fundynamic.dune2themaker.terrain.Rock;
import com.fundynamic.dune2themaker.terrain.Sand;
import com.fundynamic.dune2themaker.terrain.SandHill;
import com.fundynamic.dune2themaker.terrain.Spice;
import com.fundynamic.dune2themaker.terrain.SpiceHill;

public class DuneTerrainFactory implements TerrainFactory {

	public static final int TERRAIN_SAND = 0;
	public static final int TERRAIN_ROCK = 1;
	public static final int TERRAIN_SAND_HILL = 2;
	public static final int TERRAIN_SPICE = 3;
	public static final int TERRAIN_MOUNTAIN = 4;
	public static final int TERRAIN_SPICE_HILL = 5;
	public static final int CONCRETE = 6;

	private final Theme theme;

	public DuneTerrainFactory(Theme theme) {
		this.theme = theme;
	}

	public Terrain create(int terrainType, Cell cell) {
		switch (terrainType) {
			case TERRAIN_SAND:
				return new Sand(theme);
			case TERRAIN_ROCK:
				return new Rock(theme);
			case TERRAIN_SAND_HILL:
				return new SandHill(theme);
			case TERRAIN_SPICE:
				return new Spice(theme, cell, 100);
			case TERRAIN_MOUNTAIN:
				return new Mountain(theme);
			case TERRAIN_SPICE_HILL:
				return new SpiceHill(theme, cell, 100);
			case CONCRETE:
				return new Concrete(theme);
			default:
				throw new IndexOutOfBoundsException("Invalid value for terrainType: " + terrainType);
		}
	}
}
