package com.fundynamic.d2tm.game.map;

import com.fundynamic.d2tm.game.structures.ConstructionYard;
import com.fundynamic.d2tm.game.terrain.Terrain;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

public class Cell {

    private ConstructionYard constructionYard;

    private Terrain terrain;
    private boolean shrouded;

    public Cell(Terrain terrain) {
        if (terrain == null) throw new IllegalArgumentException("Terrain argument may not be null");
        this.terrain = terrain;
        this.shrouded = true;
        this.constructionYard = null;
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

    public boolean isShrouded() {
        return shrouded;
    }

    public void setShrouded(boolean shrouded) {
        this.shrouded = shrouded;
    }

    public ConstructionYard getConstructionYard() {
        return constructionYard;
    }

    public void setConstructionYard(ConstructionYard constructionYard) {
        this.constructionYard = constructionYard;
    }
}
