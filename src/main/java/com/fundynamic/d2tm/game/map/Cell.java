package com.fundynamic.d2tm.game.map;

import com.fundynamic.d2tm.game.terrain.Terrain;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

public class Cell {

    private Terrain terrain;

    public Cell(Terrain terrain) {
        if (terrain == null) throw new IllegalArgumentException("Terrain argument may not be null");
        this.terrain = terrain;
    }

    public void changeTerrain(Terrain terrain) {
        this.terrain = terrain;
    }

    public Image getTileImage() throws SlickException {
        return terrain.getTileImage();
    }

    public Terrain getTerrain() {
        return terrain;
    }

    public boolean isSameTerrain(Terrain terrain) {
        return this.terrain.isSame(terrain);
    }
}
