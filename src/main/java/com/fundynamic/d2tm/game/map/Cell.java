package com.fundynamic.d2tm.game.map;

import com.fundynamic.d2tm.game.terrain.Terrain;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

public class Cell {


    private Terrain terrain;
    private boolean shrouded;
    private MapEntity mapEntity;

    protected Cell(Terrain terrain) {
        if (terrain == null) throw new IllegalArgumentException("Terrain argument may not be null");
        this.terrain = terrain;
        this.shrouded = true;
        this.mapEntity = null;
    }

    // TODO: test this constructor?
    protected Cell(Cell other) {
        if (other == null)
            throw new IllegalArgumentException("argument for copy constructor may not be null (cannot copy from NULL)");
        this.terrain = other.getTerrain();
        this.shrouded = other.isShrouded();
        this.mapEntity = other.getMapEntity();
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

    public MapEntity getMapEntity() {
        return mapEntity;
    }

    public void setMapEntity(MapEntity mapEntity) {
        if (this.mapEntity != null) {
            throw new IllegalArgumentException("Cannot place mapEntity (" + mapEntity + ") because mapEntity already exists: " + this.mapEntity);
        }
        this.mapEntity = mapEntity;
    }
}
