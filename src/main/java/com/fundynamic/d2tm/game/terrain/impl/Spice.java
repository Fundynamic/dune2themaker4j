package com.fundynamic.d2tm.game.terrain.impl;

import com.fundynamic.d2tm.game.map.Cell;
import com.fundynamic.d2tm.game.terrain.Harvestable;
import com.fundynamic.d2tm.game.terrain.Terrain;
import com.fundynamic.d2tm.graphics.Theme;
import org.newdawn.slick.Color;

public class Spice extends DuneTerrain implements Harvestable {

    private static final Color terrainColor = new Color(185, 98, 37);

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
    public Color getTerrainColor() {
        return terrainColor;
    }

    @Override
    public boolean isSame(Terrain terrain) {
        if (terrain.getTerrainType() == TERRAIN_SPICE_HILL) return true;
        return super.isSame(terrain);
    }

    public float harvest(float amount) {
        if (spice > amount) {
            spice -= amount;
            return amount;
        }
        int remaining = spice;
        spice = 0;
        cell.changeTerrain(new Sand(theme)); // <-- this is odd!?
        cell.smoothSurroundingCells();
        return remaining;
    }

}
