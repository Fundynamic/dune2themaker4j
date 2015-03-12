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

    // TODO: for now the cell has a direct link to an entity, this *will* become obselete and thus removed.
    private Entity entity;

    private Vector2D position;

    public Cell(Map map, Terrain terrain, int x, int y) {
        if (terrain == null) throw new IllegalArgumentException("Terrain argument may not be null");
        if (map == null) throw new IllegalArgumentException("Map argument may not be null");
        if (x < 0 || y < 0) throw new OutOfMapBoundsException("x may ot be lower than 0, for given x, y: " + x + "," + y);
        this.terrain = terrain;
        this.map = map;
        this.x = x;
        this.y = y;
        this.entity = null;
        this.position = new Vector2D(x, y);
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

    public Entity getEntity() {
        return entity;
    }

    public void removeEntity() {
        this.entity = null;
    }

    public void setEntity(Entity entity) {
        if (this.entity != null) {
            throw new CellAlreadyOccupiedException("Cannot place Entity (" + entity + ") on cell because Entity already present: " + this.entity);
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

    public Vector2D getPosition() {
        return position;
    }
}
