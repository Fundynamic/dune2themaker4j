package com.fundynamic.d2tm.math;


import static com.fundynamic.d2tm.game.map.Cell.TILE_SIZE;

/**
 *
 * This represents a map (cell) coordinate. To transform to absolute Coordinate, use toCoordinate.
 *
 */
public class MapCoordinate extends Vector2D {

    public static MapCoordinate create(float x, float y) {
        return new MapCoordinate(x, y);
    }

    public MapCoordinate(Vector2D vec) {
        this(vec.getX(), vec.getY());
    }

    public MapCoordinate(float x, float y) {
        super(x, y);
    }

    public Coordinate toCoordinate() {
        return Coordinate.create(getX() * TILE_SIZE, getY() * TILE_SIZE);
    }

    public MapCoordinate add(Vector2D other) {
        return new MapCoordinate(super.add(other));
    }

    public MapCoordinate add(float correctX, float correctY) {
        return add(correctX, correctY);
    }
}
