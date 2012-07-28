package com.fundynamic.dune2themaker.game;

import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import com.fundynamic.dune2themaker.dune.terrain.Concrete;
import com.fundynamic.dune2themaker.dune.terrain.Mountain;
import com.fundynamic.dune2themaker.dune.terrain.Rock;
import com.fundynamic.dune2themaker.dune.terrain.Sand;
import com.fundynamic.dune2themaker.dune.terrain.SandHill;
import com.fundynamic.dune2themaker.dune.terrain.Spice;
import com.fundynamic.dune2themaker.dune.terrain.SpiceHill;
import com.fundynamic.dune2themaker.dune.terrain.Terrain;

public class Cell {

	public static final int TERRAIN_ROCK = 0;
	public static final int TERRAIN_SAND = 1;
	public static final int TERRAIN_SANDHILL = 2;
	public static final int TERRAIN_SPICE = 3;
	public static final int TERRAIN_MOUNTAIN = 4;
	public static final int TERRAIN_SPICEHILL = 5;
	public static final int CONCRETE = 6;

	private Terrain terrain;

	private Cell() {
	}

	public void changeTerrain(Terrain terrain) {
		this.terrain = terrain;
	}

	public static Cell create(int terrainType) {
		Cell cell = new Cell();
		cell.changeTerrain(createTerrain(terrainType, cell));
		return cell;
	}

	private static Terrain createTerrain(int terrainType, Cell cell) {
		switch (terrainType) {
			case TERRAIN_SAND:
				return new Sand();
			case TERRAIN_ROCK :
				return new Rock();
			case TERRAIN_SANDHILL:
				return new SandHill();
			case TERRAIN_SPICE:
				return new Spice(cell, 100);
			case TERRAIN_MOUNTAIN:
				return new Mountain();
			case TERRAIN_SPICEHILL:
				return new SpiceHill(cell, 100);
			case CONCRETE:
				return new Concrete();
			default:
				throw new IndexOutOfBoundsException("Invalid value for terrainType: " + terrainType);
		}
	}

	public Image getImage(Theme theme) throws SlickException {
		return theme.getTileImage(terrain, null);
	}
}
