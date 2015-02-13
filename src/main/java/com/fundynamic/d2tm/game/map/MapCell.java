package com.fundynamic.d2tm.game.map;

import com.fundynamic.d2tm.game.math.Vector2D;
import com.fundynamic.d2tm.game.terrain.Terrain;

/**
 * Is a cell with coordinates
 */
public class MapCell extends Cell {

    private final Map map;
    private final int x;
    private final int y;

    public MapCell(Map map, int x, int y) {
        super(map.getCell(x, y));
        this.map = map;
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public MapCell getCellAbove() {
        return new MapCell(map, this.x, this.y - 1);
    }

    public Cell getCellLeft() {
        return new MapCell(map, this.x - 1, this.y);
    }

    public Cell getCellBeneath() {
        return new MapCell(map, this.x, this.y + 1);
    }

    public Cell getCellRight() {
        return new MapCell(map, this.x + 1, this.y);
    }

    public Vector2D getCoordinatesAsVector2D() {
        return Vector2D.create(x, y);
    }

    public boolean isAtSameLocationAs(MapCell other) {
        if (other == null) return false;
        return this.x == other.getX() && y == other.getY();
    }
}
