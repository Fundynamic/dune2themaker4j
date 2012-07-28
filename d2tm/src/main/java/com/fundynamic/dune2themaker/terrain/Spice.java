package com.fundynamic.dune2themaker.terrain;

import org.newdawn.slick.Image;
import com.fundynamic.dune2themaker.Theme;
import com.fundynamic.dune2themaker.game.Cell;
import com.fundynamic.dune2themaker.game.terrain.Harvestable;
import com.fundynamic.dune2themaker.game.terrain.Terrain;

public class Spice implements Terrain, Harvestable {

	private final Theme theme;

	private Cell cell;

	private int spice;

	public Spice(Theme theme, Cell cell, int spice) {
		this.theme = theme;
		this.cell = cell;
		this.spice = spice;
	}

	public int getRowOnSpriteSheet() {
		return 3;
	}

	public Image getTileImage() {
		return theme.getTileImage(this, null);
	}

	public void harvest(int spice) {
		this.spice -= spice;
		if (this.spice <= 0) {
			cell.changeTerrain(new Sand(theme));
		}
	}

}
