package com.fundynamic.d2tm.math;

import org.junit.Assert;
import org.junit.Test;

import static com.fundynamic.d2tm.game.map.Cell.TILE_SIZE;


public class MapCoordinateTest {

    @Test
    public void transformsMapCoordinateToCoordinate() {
        MapCoordinate mapCoordinate = MapCoordinate.create(32, 30);
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

    public void assertAbsoluteToMapConversion(int absoluteX, int absoluteY, float mapCoordinateX, float mapCoordinateY) {
        Coordinate coordinate = Coordinate.create(absoluteX, absoluteY);
        MapCoordinate mapCoordinate = coordinate.toMapCoordinate();
        Assert.assertEquals(mapCoordinate.getX(), mapCoordinateX, 0.00000000000001F);
        Assert.assertEquals(mapCoordinate.getY(), mapCoordinateY, 0.00000000000001F);
    }

}