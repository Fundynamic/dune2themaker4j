package com.fundynamic.d2tm.game.entities.units;

import com.fundynamic.d2tm.math.Vector2D;
import org.junit.Test;

import static com.fundynamic.d2tm.game.entities.units.UnitFacings.*;
import static com.fundynamic.d2tm.math.Vector2D.create;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;


public class UnitFacingsTest {

    private Vector2D unitAbsoluteMapCoordinates = create(32, 32);

    //////////////////////////////////
    // Determine facing from A to B

    @Test
    public void returnsRightWhenUnableToFigureOutWhereToFaceTo() {
        assertEquals(UnitFacings.RIGHT, determine(unitAbsoluteMapCoordinates, unitAbsoluteMapCoordinates));
    }

    @Test
    public void determinesFacingRightDown() {
        Vector2D coordinatesToFaceTo = unitAbsoluteMapCoordinates.add(create(1, 1));
        assertEquals(RIGHT_DOWN, determine(unitAbsoluteMapCoordinates, coordinatesToFaceTo));
    }

    @Test
    public void determinesFacingLeftDown() {
        Vector2D coordinatesToFaceTo = unitAbsoluteMapCoordinates.add(create(-1, 1));
        assertEquals(UnitFacings.LEFT_DOWN, determine(unitAbsoluteMapCoordinates, coordinatesToFaceTo));
    }

    @Test
    public void determinesFacingRightUp() {
        Vector2D coordinatesToFaceTo = unitAbsoluteMapCoordinates.add(create(1, -1));
        assertEquals(RIGHT_UP, determine(unitAbsoluteMapCoordinates, coordinatesToFaceTo));
    }

    @Test
    public void determinesFacingLeftUp() {
        Vector2D coordinatesToFaceTo = unitAbsoluteMapCoordinates.add(create(-1, -1));
        assertEquals(LEFT_UP, determine(unitAbsoluteMapCoordinates, coordinatesToFaceTo));
    }

    @Test
    public void determinesFacingUp() {
        Vector2D coordinatesToFaceTo = unitAbsoluteMapCoordinates.add(create(0, -1));
        assertEquals(UP, determine(unitAbsoluteMapCoordinates, coordinatesToFaceTo));
    }

    @Test
    public void determinesFacingDown() {
        Vector2D coordinatesToFaceTo = unitAbsoluteMapCoordinates.add(create(0, 1));
        assertEquals(UnitFacings.DOWN, determine(unitAbsoluteMapCoordinates, coordinatesToFaceTo));
    }

    @Test
    public void determinesFacingLeft() {
        Vector2D coordinatesToFaceTo = unitAbsoluteMapCoordinates.add(create(-1, 0));
        assertEquals(LEFT, determine(unitAbsoluteMapCoordinates, coordinatesToFaceTo));
    }

    @Test
    public void determinesFacingRight() {
        Vector2D coordinatesToFaceTo = unitAbsoluteMapCoordinates.add(create(1, 0));
        assertEquals(RIGHT, determine(unitAbsoluteMapCoordinates, coordinatesToFaceTo));
    }


    ////////////////////////////////////////////////
    // Facing logic - turning

    @Test
    public void determineNextFacingFromCurrentToDesired() {
        assertThat(nextFacing(UP.getValue(), RIGHT.getValue()), is(RIGHT_UP.getValue()));
        assertThat(nextFacing(UP.getValue(), RIGHT_DOWN.getValue()), is(RIGHT_UP.getValue()));
        assertThat(nextFacing(UP.getValue(), LEFT.getValue()), is(LEFT_UP.getValue()));
        assertThat(nextFacing(RIGHT_DOWN.getValue(), LEFT_UP.getValue()), is(RIGHT.getValue()));

        // these force wrapping around on facing indexes, ie RIGHT to RIGHT_DOWN:
        assertThat(nextFacing(RIGHT.getValue(), DOWN.getValue()), is(RIGHT_DOWN.getValue()));
        assertThat(nextFacing(RIGHT_DOWN.getValue(), RIGHT_UP.getValue()), is(RIGHT.getValue()));

        assertThat(nextFacing(UP.getValue(), LEFT.getValue()), is(LEFT_UP.getValue()));
    }

    ////////////////////////////////////////////////
    // Facing logic - determine desired facing based on 2 vectors

    @Test
    public void determineDesiredFacingBasedOnVectors() {
        Vector2D center = create(32, 32);
        assertThat(getFacing(center, create(64, 64)), is(RIGHT_DOWN));
        assertThat(getFacing(center, create(64, 32)), is(RIGHT));
        assertThat(getFacing(center, create(64, 0)), is(RIGHT_UP));

        assertThat(getFacing(center, create(32, 64)), is(DOWN));
        assertThat(getFacing(center, create(32, 0)), is(UP));

        assertThat(getFacing(center, create(0, 64)), is(LEFT_DOWN));
        assertThat(getFacing(center, create(0, 32)), is(LEFT));
        assertThat(getFacing(center, create(0, 0)), is(LEFT_UP));
    }
}