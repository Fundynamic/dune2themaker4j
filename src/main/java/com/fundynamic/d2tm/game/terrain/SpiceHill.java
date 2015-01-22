package com.fundynamic.d2tm.game.terrain;

import com.fundynamic.d2tm.game.map.Cell;
import com.fundynamic.d2tm.graphics.Theme;

public class SpiceHill extends DuneTerrain implements Harvestable {

    private final Theme theme;
    private Cell cell;
    private int spice;

    public SpiceHill(Theme theme, Cell cell, int spice) {
        super(theme);
        this.theme = theme;
        this.cell = cell;
        this.spice = spice;
    }

    @Override
    protected int getTerrainType() {
        return DuneTerrain.TERRAIN_SPICE_HILL;
    }

    public void harvest(int spice) {
        this.spice -= spice;
        if (this.spice <= 0) {
            cell.changeTerrain(new Spice(theme, cell, 100));
        }
    }

}
