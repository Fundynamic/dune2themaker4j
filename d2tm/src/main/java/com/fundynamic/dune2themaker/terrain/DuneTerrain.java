package com.fundynamic.dune2themaker.terrain;

import com.fundynamic.dune2themaker.Theme;
import org.newdawn.slick.Image;
import com.fundynamic.dune2themaker.game.terrain.EmptyTerrain;
import com.fundynamic.dune2themaker.game.terrain.Terrain;
import com.fundynamic.dune2themaker.game.terrain.TerrainFacing;

public abstract class DuneTerrain implements Terrain {

	private final Theme theme;
    private Image tileImage;

    public static final int TERRAIN_SAND = 0;
    public static final int TERRAIN_SAND_HILL = 2;
    public static final int TERRAIN_ROCK = 1;
    public static final int TERRAIN_SPICE = 3;
    public static final int TERRAIN_MOUNTAIN = 4;
    public static final int TERRAIN_SPICE_HILL = 5;
    public static final int CONCRETE = 6;


	public DuneTerrain(Theme theme) {
		this.theme = theme;
        this.tileImage = makeTileImage(TerrainFacing.FULL);
	}

    protected abstract int getTerrainType();

    public void setFacing(TerrainFacing terrainFacing) {
        this.tileImage = makeTileImage(terrainFacing);
    }

    public Image getTileImage() {
        return tileImage;
    }

	public boolean isSame(Terrain terrain) {
		if (terrain instanceof EmptyTerrain) return true;
		return this.getClass().equals(terrain.getClass());
	}

    private Image makeTileImage(TerrainFacing terrainFacing) {
        return theme.getTileImage(getTerrainType(), terrainFacing);
    }
}
