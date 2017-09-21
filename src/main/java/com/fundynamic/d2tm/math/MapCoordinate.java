package com.fundynamic.d2tm.math;


import com.fundynamic.d2tm.utils.StringUtils;

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

    public MapCoordinate min(Vector2D other) {
        return new MapCoordinate(super.min(other));
    }

    public MapCoordinate add(float correctX, float correctY) {
        return add(correctX, correctY);
    }

    public boolean isAdjacentTo(MapCoordinate mapCoordinate) {
        if (mapCoordinate == null) return false;
        return distanceInMapCoords(mapCoordinate) == 1;
    }

    /**
     * This gives a 'distance' within map coordinates. You can visualise this as 'the longest axis diff wins'.
     * Meaning, a coordinate at 10,10 compared to 12,10 has a distance of 2. But so is 10,10 to 12,9.
     *
     * You might expect this to be a distance of 2,4... because the Y axis is also changed, yet this
     * returns 2 'cells' distance.
     *
     * @param mapCoordinate
     * @return
     */
    public int distanceInMapCoords(MapCoordinate mapCoordinate) {
        return (int) distance(mapCoordinate);
    }

    /**
     * Generate a vector with random (int) coordinates. Where X will be between minX and given maxX and Y will be between
     * minY and maxY.
     *
     * @param minX
     * @param maxX
     * @param minY
     * @param maxY
     * @return new Vector instance
     */
    public static MapCoordinate random(int minX, int maxX, int minY, int maxY) {
        return MapCoordinate.create(Random.getRandomBetween(minX, maxX), Random.getRandomBetween(minY, maxY));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        if (o instanceof MapCoordinate) {
            MapCoordinate mapCoordinate = (MapCoordinate)o;
            return mapCoordinate.getXAsInt() == getXAsInt() && mapCoordinate.getYAsInt() == getYAsInt();
        }
        return super.equals(o);
    }

    @Override
    public String toString() {
        return "MapCoordinate{" + this.getXAsInt() + "," + getYAsInt() + "}";
    }

    public static MapCoordinate fromString(String inputString) {
        if (StringUtils.isEmpty(inputString)) return null;
        String[] parts = inputString.split(",");
        if (parts.length != 2) return null;

        try {
            int x = Integer.parseInt(parts[0]);
            int y = Integer.parseInt(parts[1]);

            return MapCoordinate.create(x, y);
        } catch (NumberFormatException e) {
            return null;
        }
    }
}
