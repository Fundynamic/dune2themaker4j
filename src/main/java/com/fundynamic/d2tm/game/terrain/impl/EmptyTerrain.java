package com.fundynamic.d2tm.game.terrain.impl;

import com.fundynamic.d2tm.game.terrain.Terrain;
import com.fundynamic.d2tm.graphics.TerrainFacing;
import org.newdawn.slick.Color;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;


public class EmptyTerrain implements Terrain {

    private final Image image;
    private static Image blackImage = null;

    public EmptyTerrain(int tileWidth, int tileHeight) {
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
    }

    public EmptyTerrain(Image image) {
        this.image = image;
    }

    public Image getTileImage() {
        return image;
    }

    @Override
    public Terrain setFacing(TerrainFacing terrainFacing) {
        return this;
    }

    public boolean isSame(Terrain terrain) {
        if (terrain == null) throw new IllegalArgumentException("Terrain argument may not be null");
        return true;
    }

    public static Builder builder() {
        return new Builder();
    }
    public static class Builder {
        private EmptyTerrain instance;

        public Builder() {
            instance = new EmptyTerrain(null);
        }

        public EmptyTerrain setFacing(TerrainFacing terrainFacing) {
            instance.setFacing(terrainFacing);
            return instance;
        }

        public EmptyTerrain getInstance() {
            return instance;
        }
    }
}