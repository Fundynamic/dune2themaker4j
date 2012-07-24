package com.fundynamic.dune2themaker.dune;

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
			case 0:
				return new Sand();
			case 1:
				return new Rock();
			case 2:
				return new SandHill();
			case 3:
				return new Spice(cell, 100);
			case 4:
				return new Mountain();
			case 5:
				return new SpiceHill(cell, 100);
			case 6:
				return new Concrete();
			default:
				throw new IndexOutOfBoundsException("Invalid value for terrainType: " + terrainType);
		}
	}

	public Image getImage(Theme theme) throws SlickException {
		return theme.getTileImage(terrain, null);
	}
}
