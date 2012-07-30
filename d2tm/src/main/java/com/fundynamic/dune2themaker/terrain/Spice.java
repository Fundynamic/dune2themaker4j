package com.fundynamic.dune2themaker.terrain;

import org.newdawn.slick.Image;
import com.fundynamic.dune2themaker.DuneTerrainFactory;
import com.fundynamic.dune2themaker.Theme;
import com.fundynamic.dune2themaker.game.Cell;
import com.fundynamic.dune2themaker.game.terrain.Harvestable;
import com.fundynamic.dune2themaker.game.terrain.Terrain;

public class Spice implements Terrain, Harvestable {

	private final Theme theme;

	private Cell cell;

	private int spice;
	private final Image tileImage;

	public Spice(Theme theme, Cell cell, int spice, Image tileImage) {
		this.theme = theme;
		this.cell = cell;
		this.spice = spice;
		this.tileImage = tileImage;
	}

	public Image getTileImage() {
		return tileImage;
	}

	public void harvest(int spice) {
		this.spice -= spice;
		if (this.spice <= 0) {
			cell.changeTerrain(new Sand(this.theme.getTileImage(DuneTerrainFactory.TERRAIN_SAND, null))); // <-- this is odd!?
//			Spice terrain = (Spice)terrains.getType(Spice.class, this.facing);
//			terrain.setAmount
//			cell.changeTerrain(terrain);
		}
	}

}
