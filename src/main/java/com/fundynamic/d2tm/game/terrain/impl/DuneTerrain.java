package com.fundynamic.d2tm.game.terrain.impl;

import com.fundynamic.d2tm.game.map.MapEditor;
import com.fundynamic.d2tm.game.terrain.Terrain;
import com.fundynamic.d2tm.graphics.Theme;
import org.newdawn.slick.Image;

public abstract class DuneTerrain implements Terrain {

    private final Theme theme;
    private Image tileImage;
    private MapEditor.TerrainFacing terrainFacing;
    private boolean produceTileImage = true;

    public static final int TERRAIN_SAND = 0;
    public static final int TERRAIN_SAND_HILL = 2;
    public static final int TERRAIN_ROCK = 1;
    public static final int TERRAIN_SPICE = 3;
    public static final int TERRAIN_MOUNTAIN = 4;
    public static final int TERRAIN_SPICE_HILL = 5;
    public static final int CONCRETE = 6;


    public DuneTerrain(Theme theme) {
        this.theme = theme;
        this.terrainFacing = MapEditor.TerrainFacing.FULL;
    }

    public DuneTerrain setFacing(MapEditor.TerrainFacing terrainFacing) {
        this.terrainFacing = terrainFacing;
        this.produceTileImage = true;
        return this;
    }

    public Image getTileImage() {
        if (produceTileImage) {
            this.tileImage = makeTileImage(terrainFacing);
            this.produceTileImage = false;
        }
        return tileImage;
    }

    public MapEditor.TerrainFacing getTerrainFacing() {
        return terrainFacing;
    }

    public boolean isSame(Terrain terrain) {
        if (terrain instanceof EmptyTerrain) return true; // WEIRD!?
        return this.getClass().equals(terrain.getClass());
    }

    private Image makeTileImage(MapEditor.TerrainFacing terrainFacing) {
        return theme.getFacingTile(getTerrainType(), terrainFacing);
    }
}
