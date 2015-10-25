package com.fundynamic.d2tm.math;


import com.fundynamic.d2tm.Game;

/**
 *
 * This represents an absolute coordinate (Vector2D). To transform coordinates into Map coordinates (cell based) use the
 * toMapCoordinate method.
 *
 */
public class Coordinate extends Vector2D {

    public static Coordinate create(float x, float y) {
        return new Coordinate(x, y);
    }

    public Coordinate(Vector2D vec) {
        this(vec.getX(), vec.getY());
    }

    public Coordinate(float x, float y) {
        super(x, y);
    }

    public MapCoordinate toMapCoordinate() {
        return MapCoordinate.create(getX() / Game.TILE_SIZE, getY() / Game.TILE_SIZE);
    }

    public Coordinate add(float correctX, float correctY) {
        return new Coordinate(super.add(correctX, correctY));
    }

    public Coordinate add(Vector2D vec) {
        return new Coordinate(super.add(vec));
    }

    public Coordinate min(Vector2D vec) {
        return new Coordinate(super.min(vec));
    }
}
