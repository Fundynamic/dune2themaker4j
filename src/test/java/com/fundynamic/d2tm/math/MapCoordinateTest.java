package com.fundynamic.d2tm.math;

import org.junit.Test;


public class MapCoordinateTest {


    @Test
    public void canTransformToCoordinate() {
        MapCoordinate mapCoordinate = MapCoordinate.create(2, 3);
        Coordinate coordinate = mapCoordinate.toCoordinate();
    }
}