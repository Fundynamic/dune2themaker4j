package com.fundynamic.dune2themaker.dune.terrain;

import com.fundynamic.dune2themaker.game.Cell;
import com.fundynamic.dune2themaker.game.terrain.Harvestable;

public class Spice implements Terrain, Harvestable {

	private Cell cell;

	private int spice;

	public Spice(Cell cell, int spice) {
		this.cell = cell;
		this.spice = spice;
	}

	public int getRowOnSpriteSheet() {
		return 3;
	}

	public void harvest(int spice) {
		this.spice -= spice;
		if (this.spice <= 0) {
			cell.changeTerrain(new Sand());
		}
	}

}
