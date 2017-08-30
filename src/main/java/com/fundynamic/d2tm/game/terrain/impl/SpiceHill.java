package com.fundynamic.d2tm.game.terrain.impl;

import com.fundynamic.d2tm.game.map.Cell;
import com.fundynamic.d2tm.game.terrain.Harvestable;
import com.fundynamic.d2tm.graphics.Theme;
import org.newdawn.slick.Color;

public class SpiceHill extends DuneTerrain implements Harvestable {

    private static final Color terrainColor = new Color(170, 75, 25);

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
    public int getTerrainType() {
        return TERRAIN_SPICE_HILL;
    }

    @Override
    public Color getTerrainColor() {
        return terrainColor;
    }

    public float harvest(float amount) {
        if (spice > amount) {
            spice -= amount;
            return amount;
        }
        int remaining = spice;
        spice = 0;
        cell.changeTerrain(new Spice(theme, cell, 100));
        cell.smoothSurroundingCells();
        return remaining;
    }
}
