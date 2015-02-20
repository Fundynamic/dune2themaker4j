package com.fundynamic.d2tm.game.map;

import com.fundynamic.d2tm.game.structures.Structure;
import com.fundynamic.d2tm.game.terrain.Terrain;
import com.fundynamic.d2tm.game.units.Unit;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

public class Cell {

    private Structure structure;
    private Unit unit;

    private Terrain terrain;
    private boolean shrouded;

    protected Cell(Terrain terrain) {
        if (terrain == null) throw new IllegalArgumentException("Terrain argument may not be null");
        this.terrain = terrain;
        this.shrouded = true;
        this.structure = null;
        this.unit = null;
    }

    // TODO: test this constructor?
    protected Cell(Cell other) {
        if (other == null)
            throw new IllegalArgumentException("argument for copy constructor may not be null (cannot copy from NULL)");
        this.terrain = other.getTerrain();
        this.shrouded = other.isShrouded();
        this.structure = other.getStructure();
        this.unit = other.getUnit();
    }

    public static Cell withTerrain(Terrain terrain) {
        return new Cell(terrain);
    }

    public static Cell copy(Cell other) {
        return new Cell(other);
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

    public void setUnit(Unit unit) {
        this.unit = unit;
    }

    public Unit getUnit() {
        return unit;
    }
}
