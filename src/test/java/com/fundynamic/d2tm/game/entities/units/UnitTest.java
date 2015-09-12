package com.fundynamic.d2tm.game.entities.units;

import com.fundynamic.d2tm.game.entities.Player;
import com.fundynamic.d2tm.game.map.Map;
import com.fundynamic.d2tm.math.Vector2D;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SpriteSheet;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Matchers.eq;

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

    public Unit makeUnit(UnitFacings facing) {
        return makeUnit(facing, Vector2D.zero());
    }

    public Unit makeUnit(UnitFacings facing, Vector2D offset) {
        return new Unit(map, unitMapCoordinates, spriteSheet, 32, 32, player, 10, facing.getValue(), unitMapCoordinates, unitMapCoordinates, offset);
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

    @Test
    public void rendersUnitOnExpectedCoordinates() {
        int offsetX = 5;
        int offsetY = 6;
        Vector2D offset = Vector2D.create(offsetX, offsetY);

        Unit unit = makeUnit(UnitFacings.DOWN, offset);
        Graphics graphics = Mockito.mock(Graphics.class);

        // TODO: Resolve this quirky thing, because we pass here the coordinates to draw
        // but isn't that basically the unit coordinates * tile size!?
        int drawX = 10;
        int drawY = 12;

        unit.render(graphics, drawX, drawY);

        Mockito.verify(graphics).drawImage((Image) anyObject(), eq((float)drawX + offsetX), eq((float)drawY + offsetY));
    }

}