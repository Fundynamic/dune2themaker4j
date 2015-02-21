package com.fundynamic.d2tm.game.map;

import com.fundynamic.d2tm.game.entities.Entity;
import com.fundynamic.d2tm.game.terrain.Terrain;
import com.fundynamic.d2tm.math.Vector2D;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

public class Cell {

    private final Map map;
    private final int x;
    private final int y;

    private Terrain terrain;
    private boolean shrouded;

    // TODO: for now the cell has a direct link to an entity, this *will* become obselete and thus removed.
    private Entity entity;

    public Cell(Map map, Terrain terrain, int x, int y) {
        if (terrain == null) throw new IllegalArgumentException("Terrain argument may not be null");
        if (map == null) throw new IllegalArgumentException("Map argument may not be null");
        this.terrain = terrain;
        this.map = map;
        this.x = x;
        this.y = y;
        this.shrouded = true;
        this.entity = null;
    }

    public Cell createCopy() {
        Cell copy = new Cell(map, terrain, x, y);
        copy.shrouded = shrouded;
        copy.entity = entity;
        return copy;
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

    public Entity getEntity() {
        return entity;
    }

    public void setEntity(Entity entity) {
        if (this.entity != null) {
            throw new IllegalArgumentException("Cannot place Entity (" + entity + ") on cell because Entity already present: " + this.entity);
        }
        this.entity = entity;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public Cell getCellAbove() {
        return map.getCell(this.x, this.y - 1);
    }

    public Cell getCellLeft() {
        return map.getCell(this.x - 1, this.y);
    }

    public Cell getCellBeneath() {
        return map.getCell(this.x, this.y + 1);
    }

    public Cell getCellRight() {
        return map.getCell(this.x + 1, this.y);
    }

    public Vector2D getCoordinatesAsVector2D() {
        return Vector2D.create(x, y);
    }

    public boolean isAtSameLocationAs(Cell other) {
        if (other == null) return false;
        return this.x == other.getX() && y == other.getY();
    }
}
