package com.fundynamic.d2tm.game.terrain.impl;

import com.fundynamic.d2tm.game.map.Cell;
import com.fundynamic.d2tm.game.terrain.Harvestable;
import com.fundynamic.d2tm.graphics.Theme;

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

    public Spice() {
        super(null);
        this.theme = null;
    }

    @Override
    protected int getTerrainType() {
        return TERRAIN_SPICE;
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
