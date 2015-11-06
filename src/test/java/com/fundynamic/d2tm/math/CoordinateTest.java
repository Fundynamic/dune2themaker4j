package com.fundynamic.d2tm.math;

import com.fundynamic.d2tm.Game;
import org.junit.Test;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;


public class CoordinateTest {

    @Test
    public void canTransformToMapCoordinate() {
        Coordinate coordinate = Coordinate.create(64, 64);
        MapCoordinate mapCoordinate = coordinate.toMapCoordinate();
        assertThat(mapCoordinate.getXAsInt(), is(64 / Game.TILE_SIZE));
        assertThat(mapCoordinate.getYAsInt(), is(64 / Game.TILE_SIZE));
    }

}