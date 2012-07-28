package com.fundynamic.dune2themaker.dune.terrain;

import com.fundynamic.dune2themaker.game.Cell;
import com.fundynamic.dune2themaker.game.terrain.Harvestable;

public class SpiceHill implements Terrain, Harvestable {

	private Cell cell;

	private int spice;

	public SpiceHill(Cell cell, int spice) {
		this.cell = cell;
		this.spice = spice;
	}

	public void harvest(int spice) {
		this.spice -= spice;
		if (this.spice <= 0) {
			cell.changeTerrain(new Spice(cell, 100));
		}
	}

	public int getRowOnSpriteSheet() {
		return 0;
	}
}
