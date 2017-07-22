package com.fundynamic.d2tm.math;


import static com.fundynamic.d2tm.game.map.Cell.TILE_SIZE;

/**
 *
 * This represents a map (cell) coordinate. To transform to absolute Coordinate, use toCoordinate.
 *
 * Map coordinates force input to be casted to int (for a while), so that all details get lost.
 * Ie, an absolute coordinate 40, 40 becomes (40/32=1,25 -> 1) , thus Map coordinate 1,1. Going back to coordinates will
 * return 32, 32. You have lost precision there. This may come in handy when you want to 'snap' some pixels to map coordinates.
 *
 */
public class MapCoordinate extends Vector2D {

    public static MapCoordinate create(float x, float y) {
        return new MapCoordinate(Math.round(x), Math.round(y));
    }

    public MapCoordinate(Vector2D vec) {
        this(vec.getX(), vec.getY());
    }

    public MapCoordinate(float x, float y) {
        super(Math.round(x), Math.round(y));
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
