package com.fundynamic.d2tm.game.terrain.impl;

import com.fundynamic.d2tm.game.map.Cell;
import com.fundynamic.d2tm.game.terrain.Harvestable;
import com.fundynamic.d2tm.game.terrain.Terrain;
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

    @Override
    public int getTerrainType() {
        return TERRAIN_SPICE;
    }

    @Override
    public boolean isSame(Terrain terrain) {
        if (terrain.getTerrainType() == TERRAIN_SPICE_HILL) return true;
        return super.isSame(terrain);
    }

    public int harvest(int spice) {
        this.spice -= spice;
        if (this.spice <= 0) {
            cell.changeTerrain(new Sand(theme)); // <-- this is odd!?
            cell.smoothSurroundingCells();
//			Spice terrain = (Spice)terrains.getType(Spice.class, this.facing);
//			terrain.setAmount
//			cell.changeTerrain(terrain);
        }
        return spice;
    }

}
