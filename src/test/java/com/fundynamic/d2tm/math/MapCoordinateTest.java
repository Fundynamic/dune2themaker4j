package com.fundynamic.d2tm.math;

import org.junit.Assert;
import org.junit.Test;

import static com.fundynamic.d2tm.game.map.Cell.TILE_SIZE;
import static com.fundynamic.d2tm.math.MapCoordinate.create;


public class MapCoordinateTest {

    @Test
    public void transformsMapCoordinateToCoordinate() {
        MapCoordinate mapCoordinate = create(32, 30);
        Coordinate coordinate = mapCoordinate.toCoordinate();
        Assert.assertEquals(coordinate.getXAsInt(), 32 * TILE_SIZE);
        Assert.assertEquals(coordinate.getYAsInt(), 30 * TILE_SIZE);
    }

    @Test
    public void transformsCoordinateToMapCoordinate() {
        assertAbsoluteToMapConversion(32, 32, 1.0F, 1.0F);
        assertAbsoluteToMapConversion(40, 40, 1.0F, 1.0F); // loss of precision here
        assertAbsoluteToMapConversion(48, 48, 2.0F, 2.0F); // because 1,5
    }

    @Test
    public void isAdjacentTo() {
        int x = 10;
        int y = 10;
        MapCoordinate mapCoordinate = create(x, y);

        // left column
        Assert.assertTrue(mapCoordinate.isAdjacentTo(create(x - 1, y)));
        Assert.assertTrue(mapCoordinate.isAdjacentTo(create(x - 1, y + 1)));
        Assert.assertTrue(mapCoordinate.isAdjacentTo(create(x - 1, y - 1)));

        // left & right on same x axis
        Assert.assertTrue(mapCoordinate.isAdjacentTo(create(x, y - 1)));
        Assert.assertTrue(mapCoordinate.isAdjacentTo(create(x, y + 1)));

        // right column
        Assert.assertTrue(mapCoordinate.isAdjacentTo(create(x + 1, y - 1)));
        Assert.assertTrue(mapCoordinate.isAdjacentTo(create(x + 1, y)));
        Assert.assertTrue(mapCoordinate.isAdjacentTo(create(x + 1, y + 1)));

        // invalid cases
        Assert.assertFalse(mapCoordinate.isAdjacentTo(create(x + 2, y + 2)));
        Assert.assertFalse(mapCoordinate.isAdjacentTo(null));
    }

    @Test
    public void distanceInMapCoords() {
        MapCoordinate start = create(10, 10);

        Assert.assertEquals(start.distanceInMapCoords(create(10, 10)), 0);
        Assert.assertEquals(start.distanceInMapCoords(create(11, 11)), 1);
        Assert.assertEquals(start.distanceInMapCoords(create(9, 9)), 1);
        Assert.assertEquals(start.distanceInMapCoords(create(9, 10)), 1);
        Assert.assertEquals(start.distanceInMapCoords(create(12, 11)), 2);
    }

    public void assertAbsoluteToMapConversion(int absoluteX, int absoluteY, float mapCoordinateX, float mapCoordinateY) {
        Coordinate coordinate = Coordinate.create(absoluteX, absoluteY);
        MapCoordinate mapCoordinate = coordinate.toMapCoordinate();
        Assert.assertEquals(mapCoordinate.getX(), mapCoordinateX, 0.00000000000001F);
        Assert.assertEquals(mapCoordinate.getY(), mapCoordinateY, 0.00000000000001F);
    }

}