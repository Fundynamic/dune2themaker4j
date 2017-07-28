package com.fundynamic.d2tm.math;

import org.junit.Assert;
import org.junit.Test;

import java.util.HashSet;
import java.util.Set;

import static com.fundynamic.d2tm.game.map.Cell.TILE_SIZE;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;


public class CoordinateTest {

    @Test
    public void canTransformToMapCoordinate() {
        Coordinate coordinate = Coordinate.create(64, 64);
        MapCoordinate mapCoordinate = coordinate.toMapCoordinate();
        assertThat(mapCoordinate.getXAsInt(), is(64 / TILE_SIZE));
        assertThat(mapCoordinate.getYAsInt(), is(64 / TILE_SIZE));
    }

    @Test
    public void areEqual() {
        Coordinate coord1 = new Coordinate(10F, 10F);
        Coordinate coord2 = new Coordinate(10F, 10F);
        Coordinate coord3 = new Coordinate(11F, 10F);
        Assert.assertEquals(coord1, coord2);
        Assert.assertNotEquals(coord1, coord3);
    }

    @Test
    public void foo() {
        Set<Coordinate> coordinates = new HashSet<>();
        coordinates.add(new Coordinate(10, 10));
        coordinates.add(new Coordinate(11, 11));
        Assert.assertEquals(coordinates.size(),2);
        coordinates.add(new Coordinate(11, 11));
        Assert.assertEquals(coordinates.size(),2);
    }

}