package com.fundynamic.d2tm.math;


import static com.fundynamic.d2tm.game.map.Cell.HALF_TILE;
import static com.fundynamic.d2tm.game.map.Cell.TILE_SIZE;

/**
 *
 * This represents an absolute coordinate (Vector2D). To transform coordinates into Map coordinates (cell based) use the
 * toMapCoordinate method.
 *
 */
public class Coordinate extends Vector2D {

    private MapCoordinate mapCoordinate;

    public static Coordinate create(float x, float y) {
        return new Coordinate(x, y);
    }

    public static Coordinate zero() {
        return create(0,0);
    }

    public Coordinate(Vector2D vec) {
        this(vec.getX(), vec.getY());
    }

    public Coordinate(float x, float y) {
        super(x, y);
    }

    public MapCoordinate toMapCoordinate() {
        if (mapCoordinate == null) {
            mapCoordinate = MapCoordinate.create(getX() / TILE_SIZE, getY() / TILE_SIZE);
        }
        return mapCoordinate;
    }

    public Coordinate add(Vector2D vec) {
        return new Coordinate(super.add(vec));
    }

    public Coordinate min(Vector2D otherVector) {
        return new Coordinate(super.min(otherVector));
    }

    public Coordinate addHalfTile() {
        return new Coordinate(getX() + HALF_TILE, getYAsInt() + HALF_TILE);
    }

    public static Coordinate create(Vector2D vector2D) {
        return new Coordinate(vector2D.getX(), vector2D.getY());
    }
}
