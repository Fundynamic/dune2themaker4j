package com.fundynamic.d2tm.game.entities.units;

import com.fundynamic.d2tm.math.Vector2D;
import org.junit.Test;

import static org.junit.Assert.*;


public class UnitFacingsTest {

    Vector2D unitAbsoluteMapCoordinates = Vector2D.create(32, 32);

    @Test
    public void determinesFacingRightDown() {
        Vector2D coordinatesToFaceTo = unitAbsoluteMapCoordinates.add(Vector2D.create(1, 1));
        assertEquals(UnitFacings.RIGHT_DOWN, UnitFacings.determine(unitAbsoluteMapCoordinates, coordinatesToFaceTo));
    }

    @Test
    public void determinesFacingLeftDown() {
        Vector2D coordinatesToFaceTo = unitAbsoluteMapCoordinates.add(Vector2D.create(-1, 1));
        assertEquals(UnitFacings.LEFT_DOWN, UnitFacings.determine(unitAbsoluteMapCoordinates, coordinatesToFaceTo));
    }

    @Test
    public void determinesFacingRightUp() {
        Vector2D coordinatesToFaceTo = unitAbsoluteMapCoordinates.add(Vector2D.create(1, -1));
        assertEquals(UnitFacings.RIGHT_UP, UnitFacings.determine(unitAbsoluteMapCoordinates, coordinatesToFaceTo));
    }

    @Test
    public void determinesFacingLeftUp() {
        Vector2D coordinatesToFaceTo = unitAbsoluteMapCoordinates.add(Vector2D.create(-1, -1));
        assertEquals(UnitFacings.LEFT_UP, UnitFacings.determine(unitAbsoluteMapCoordinates, coordinatesToFaceTo));
    }

    @Test
    public void determinesFacingUp() {
        Vector2D coordinatesToFaceTo = unitAbsoluteMapCoordinates.add(Vector2D.create(0, -1));
        assertEquals(UnitFacings.UP, UnitFacings.determine(unitAbsoluteMapCoordinates, coordinatesToFaceTo));
    }

    @Test
    public void determinesFacingDown() {
        Vector2D coordinatesToFaceTo = unitAbsoluteMapCoordinates.add(Vector2D.create(0, 1));
        assertEquals(UnitFacings.DOWN, UnitFacings.determine(unitAbsoluteMapCoordinates, coordinatesToFaceTo));
    }

    @Test
    public void determinesFacingLeft() {
        Vector2D coordinatesToFaceTo = unitAbsoluteMapCoordinates.add(Vector2D.create(-1, 0));
        assertEquals(UnitFacings.LEFT, UnitFacings.determine(unitAbsoluteMapCoordinates, coordinatesToFaceTo));
    }

    @Test
    public void determinesFacingRight() {
        Vector2D coordinatesToFaceTo = unitAbsoluteMapCoordinates.add(Vector2D.create(1, 0));
        assertEquals(UnitFacings.RIGHT, UnitFacings.determine(unitAbsoluteMapCoordinates, coordinatesToFaceTo));
    }

}