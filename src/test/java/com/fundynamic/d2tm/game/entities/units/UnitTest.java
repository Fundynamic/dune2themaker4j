package com.fundynamic.d2tm.game.entities.units;

import com.fundynamic.d2tm.game.entities.Player;
import com.fundynamic.d2tm.game.map.Map;
import com.fundynamic.d2tm.math.Vector2D;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.newdawn.slick.SpriteSheet;

import static junit.framework.Assert.assertEquals;

@RunWith(MockitoJUnitRunner.class)
public class UnitTest {

    @Mock
    private Map map;

    @Mock
    private SpriteSheet spriteSheet;

    @Mock
    private Player player;

    private Unit unit;
    private Vector2D unitMapCoordinates;

    @Before
    public void setUp() {
        unitMapCoordinates = Vector2D.create(10, 10);
        unit = makeUnit(UnitFacings.RIGHT);
    }

    private Unit makeUnit(UnitFacings facing) {
        return new Unit(map, unitMapCoordinates, spriteSheet, 32, 32, player, 10, facing.getValue(), unitMapCoordinates, unitMapCoordinates, Vector2D.zero());
    }

    @Test
    public void returnsCurrentFacingWhenNothingChanged() {
        assertEquals(UnitFacings.RIGHT, unit.determineFacingFor(unit.getMapCoordinates()));
    }

    @Test
    public void determinesFacingRightDown() {
        Vector2D coordinatesToFaceTo = unitMapCoordinates.add(Vector2D.create(1, 1));
        assertEquals(UnitFacings.RIGHT_DOWN, unit.determineFacingFor(coordinatesToFaceTo));
    }

    @Test
    public void determinesFacingLeftDown() {
        Vector2D coordinatesToFaceTo = unitMapCoordinates.add(Vector2D.create(-1, 1));
        assertEquals(UnitFacings.LEFT_DOWN, unit.determineFacingFor(coordinatesToFaceTo));
    }

    @Test
    public void determinesFacingRightUp() {
        Vector2D coordinatesToFaceTo = unitMapCoordinates.add(Vector2D.create(1, -1));
        assertEquals(UnitFacings.RIGHT_UP, unit.determineFacingFor(coordinatesToFaceTo));
    }

    @Test
    public void determinesFacingLeftUp() {
        Vector2D coordinatesToFaceTo = unitMapCoordinates.add(Vector2D.create(-1, -1));
        assertEquals(UnitFacings.LEFT_UP, unit.determineFacingFor(coordinatesToFaceTo));
    }

    @Test
    public void determinesFacingUp() {
        Vector2D coordinatesToFaceTo = unitMapCoordinates.add(Vector2D.create(0, -1));
        assertEquals(UnitFacings.UP, unit.determineFacingFor(coordinatesToFaceTo));
    }

    @Test
    public void determinesFacingDown() {
        Vector2D coordinatesToFaceTo = unitMapCoordinates.add(Vector2D.create(0, 1));
        assertEquals(UnitFacings.DOWN, unit.determineFacingFor(coordinatesToFaceTo));
    }

    @Test
    public void determinesFacingLeft() {
        Vector2D coordinatesToFaceTo = unitMapCoordinates.add(Vector2D.create(-1, 0));
        assertEquals(UnitFacings.LEFT, unit.determineFacingFor(coordinatesToFaceTo));
    }

    @Test
    public void determinesFacingRight() {
        unit = makeUnit(UnitFacings.LEFT);
        Vector2D coordinatesToFaceTo = unitMapCoordinates.add(Vector2D.create(1, 0));
        assertEquals(UnitFacings.RIGHT, unit.determineFacingFor(coordinatesToFaceTo));
    }

}