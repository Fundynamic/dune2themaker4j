package com.fundynamic.dune2themaker.dune.terrain;

public enum TerrainType {
	SAND(0),
	ROCK(1),
	SAND_HILL(2),
	SPICE(3),
	MOUNTAIN(4),
	SPICE_HILL(5),
	CONCRETE(6);

	private final int typeAsInteger;

	TerrainType(int typeAsInteger) {
		this.typeAsInteger = typeAsInteger;
	}

	public static TerrainType getByValue(int typeAsInteger) {
		return TerrainType.SAND;
	}
}
