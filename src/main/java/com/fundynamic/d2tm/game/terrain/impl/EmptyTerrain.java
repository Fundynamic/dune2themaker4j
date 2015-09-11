package com.fundynamic.d2tm.game.terrain.impl;

import com.fundynamic.d2tm.game.map.MapEditor;
import com.fundynamic.d2tm.game.terrain.Terrain;
import org.newdawn.slick.Color;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;


public class EmptyTerrain implements Terrain {

    private Image image;
    private int tileWidth;
    private int tileHeight;
    private static Image blackImage = null;

    public EmptyTerrain(int tileWidth, int tileHeight) {
        this.tileHeight = tileHeight;
        this.tileWidth = tileWidth;
    }

    // Used for testing
    public EmptyTerrain(Image image) {
        this.image = image;
    }

    public Image getTileImage() {
        try {
            if (blackImage == null) {
                blackImage = new Image(tileWidth, tileHeight);
                blackImage.getGraphics().setColor(Color.black);
                blackImage.getGraphics().fillRect(0, 0, tileWidth, tileHeight);
            }
            this.image = blackImage;
        } catch (SlickException e) {
            throw new IllegalStateException("Cannot create empty image of 32x32");
        }
        return image;
    }

    @Override
    public Terrain setFacing(MapEditor.TerrainFacing terrainFacing) {
        return this;
    }

    public boolean isSame(Terrain terrain) {
        if (terrain == null) throw new IllegalArgumentException("Terrain argument may not be null");
        return true;
    }

    @Override
    public int getTerrainType() {
        return -1;
    }

    @Override
    public MapEditor.TerrainFacing getTerrainFacing() {
        return MapEditor.TerrainFacing.FULL;
    }

}