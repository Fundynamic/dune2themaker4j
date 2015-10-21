package com.fundynamic.d2tm.game.entities.units;

import com.fundynamic.d2tm.math.Vector2D;
import org.junit.Test;

import static com.fundynamic.d2tm.game.entities.units.UnitFacings.*;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;


public class UnitFacingsTest {

    Vector2D unitAbsoluteMapCoordinates = Vector2D.create(32, 32);

    //////////////////////////////////
    // Determine facing from A to B

    @Test
    public void determinesFacingRightDown() {
        Vector2D coordinatesToFaceTo = unitAbsoluteMapCoordinates.add(Vector2D.create(1, 1));
        assertEquals(RIGHT_DOWN, determine(unitAbsoluteMapCoordinates, coordinatesToFaceTo));
    }

    @Test
    public void determinesFacingLeftDown() {
        Vector2D coordinatesToFaceTo = unitAbsoluteMapCoordinates.add(Vector2D.create(-1, 1));
        assertEquals(UnitFacings.LEFT_DOWN, determine(unitAbsoluteMapCoordinates, coordinatesToFaceTo));
    }

    @Test
    public void determinesFacingRightUp() {
        Vector2D coordinatesToFaceTo = unitAbsoluteMapCoordinates.add(Vector2D.create(1, -1));
        assertEquals(RIGHT_UP, determine(unitAbsoluteMapCoordinates, coordinatesToFaceTo));
    }

    @Test
    public void determinesFacingLeftUp() {
        Vector2D coordinatesToFaceTo = unitAbsoluteMapCoordinates.add(Vector2D.create(-1, -1));
        assertEquals(LEFT_UP, determine(unitAbsoluteMapCoordinates, coordinatesToFaceTo));
    }

    @Test
    public void determinesFacingUp() {
        Vector2D coordinatesToFaceTo = unitAbsoluteMapCoordinates.add(Vector2D.create(0, -1));
        assertEquals(UP, determine(unitAbsoluteMapCoordinates, coordinatesToFaceTo));
    }

    @Test
    public void determinesFacingDown() {
        Vector2D coordinatesToFaceTo = unitAbsoluteMapCoordinates.add(Vector2D.create(0, 1));
        assertEquals(UnitFacings.DOWN, determine(unitAbsoluteMapCoordinates, coordinatesToFaceTo));
    }

    @Test
    public void determinesFacingLeft() {
        Vector2D coordinatesToFaceTo = unitAbsoluteMapCoordinates.add(Vector2D.create(-1, 0));
        assertEquals(LEFT, determine(unitAbsoluteMapCoordinates, coordinatesToFaceTo));
    }

    @Test
    public void determinesFacingRight() {
        Vector2D coordinatesToFaceTo = unitAbsoluteMapCoordinates.add(Vector2D.create(1, 0));
        assertEquals(RIGHT, determine(unitAbsoluteMapCoordinates, coordinatesToFaceTo));
    }


    ////////////////////////////////////////////////
    // Facing logic

    @Test
    public void determineNextFacing() {
        assertThat(nextFacing(UP, RIGHT), is(RIGHT_UP));
        assertThat(nextFacing(UP, RIGHT_DOWN), is(RIGHT_UP));
        assertThat(nextFacing(UP, LEFT), is(LEFT_UP));
        assertThat(nextFacing(RIGHT_DOWN, LEFT_UP), is(RIGHT));

        // these force wrapping around on facing indexes, ie RIGHT to RIGHT_DOWN:
        assertThat(nextFacing(RIGHT, DOWN), is(RIGHT_DOWN));
        assertThat(nextFacing(RIGHT_DOWN, RIGHT_UP), is(RIGHT));

        assertThat(nextFacing(UP, LEFT), is(LEFT_UP));
    }
}