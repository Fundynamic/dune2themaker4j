package com.fundynamic.d2tm.game.terrain.impl;

import com.fundynamic.d2tm.game.map.Cell;
import com.fundynamic.d2tm.game.terrain.Terrain;
import com.fundynamic.d2tm.game.terrain.TerrainFactory;
import com.fundynamic.d2tm.graphics.Theme;
import com.fundynamic.d2tm.math.Random;

public class DuneTerrainFactory implements TerrainFactory {

    private final Theme theme;

    public DuneTerrainFactory(Theme theme) {
        this.theme = theme;
    }

    public Terrain create(int terrainType, Cell cell) {
        switch (terrainType) {
            case DuneTerrain.TERRAIN_SAND:
                return new Sand(theme);
            case DuneTerrain.TERRAIN_ROCK:
                return new Rock(theme);
            case DuneTerrain.TERRAIN_SAND_HILL:
                return new SandHill(theme);
            case DuneTerrain.TERRAIN_SPICE:
                return new Spice(theme, cell, getSpiceAmount());
            case DuneTerrain.TERRAIN_MOUNTAIN:
                return new Mountain(theme);
            case DuneTerrain.TERRAIN_SPICE_HILL:
                return new SpiceHill(theme, cell, getSpiceHillAmount());
            case DuneTerrain.CONCRETE:
                return new ConcreteSlab(theme);
            default:
                throw new IndexOutOfBoundsException("Invalid value for terrainType: " + terrainType);
        }
    }

    public int getSpiceHillAmount() {
        return Random.getRandomBetween(350, 500);
    }

    public int getSpiceAmount() {
        return Random.getRandomBetween(150, 250);
    }

}
