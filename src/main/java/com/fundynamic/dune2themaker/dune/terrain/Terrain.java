package com.fundynamic.dune2themaker.dune.terrain;

public interface Terrain {

	Terrain ROCK = new Rock();
	Terrain SAND = new Sand();

	int getRowOnSpriteSheet();

}

