package com.fundynamic.d2tm.game.map;

import com.fundynamic.d2tm.game.structures.Structure;
import com.fundynamic.d2tm.game.terrain.Terrain;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

public class Cell {

    private Structure structure;

    private Terrain terrain;
    private boolean shrouded;
    private boolean topLeftOfStructure;

    public Cell(Terrain terrain) {
        if (terrain == null) throw new IllegalArgumentException("Terrain argument may not be null");
        this.terrain = terrain;
        this.shrouded = true;
        this.structure = null;
        this.topLeftOfStructure = false;
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

    public Structure getStructure() {
        return structure;
    }

    public Cell setStructure(Structure structure) {
        this.structure = structure;
        return this;
    }

    public boolean hasStructure(Structure selectedStructure) {
        if (this.structure == null) return false;
        return this.structure == selectedStructure;
    }

    public void setTopLeftOfStructure(boolean topLeftOfStructure) {
        this.topLeftOfStructure = topLeftOfStructure;
    }

    public boolean isTopLeftOfStructure() {
        return topLeftOfStructure;
    }
}
