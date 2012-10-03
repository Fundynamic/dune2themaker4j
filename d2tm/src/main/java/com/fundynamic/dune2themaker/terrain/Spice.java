package com.fundynamic.dune2themaker.terrain;

import org.newdawn.slick.Image;
import com.fundynamic.dune2themaker.DuneTerrainFactory;
import com.fundynamic.dune2themaker.Theme;
import com.fundynamic.dune2themaker.game.map.Cell;
import com.fundynamic.dune2themaker.game.terrain.Harvestable;

public class Spice extends DuneTerrain implements Harvestable {

	private final Theme theme;
	private Cell cell;
	private int spice;

	public Spice(Theme theme, Cell cell, int spice) {
		super(theme);
		this.theme = theme;
		this.cell = cell;
		this.spice = spice;
	}

    @Override
    protected int getTerrainType() {
        return DuneTerrain.TERRAIN_SPICE;
    }

	public void harvest(int spice) {
		this.spice -= spice;
		if (this.spice <= 0) {
			cell.changeTerrain(new Sand(theme)); // <-- this is odd!?
//			Spice terrain = (Spice)terrains.getType(Spice.class, this.facing);
//			terrain.setAmount
//			cell.changeTerrain(terrain);
		}
	}

}
