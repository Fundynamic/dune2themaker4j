package com.fundynamic.d2tm.game.entities.units;

import com.fundynamic.d2tm.game.behaviors.FadingSelection;
import com.fundynamic.d2tm.game.entities.Player;
import com.fundynamic.d2tm.game.map.Map;
import com.fundynamic.d2tm.game.map.MapEditor;
import com.fundynamic.d2tm.graphics.Shroud;
import com.fundynamic.d2tm.math.Vector2D;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.SpriteSheet;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;

@RunWith(MockitoJUnitRunner.class)
public class UnitTest {

    private Map map;

    @Mock
    private SpriteSheet spriteSheet;

    @Mock
    private Player player;

    @Mock
    private FadingSelection fadingSelection;

    private Unit unit;
    private Vector2D unitMapCoordinates;

    @Before
    public void setUp() throws SlickException {
        int TILE_SIZE = 32;
        int mapWidth = 64;
        int mapHeight = 64;
        map = new Map(new Shroud(Mockito.mock(Image.class), TILE_SIZE, TILE_SIZE), mapWidth, mapHeight);
        unitMapCoordinates = Vector2D.create(10, 10);
        unit = makeUnit(UnitFacings.RIGHT);
    }

    public Unit makeUnit(UnitFacings facing) {
        return makeUnit(facing, Vector2D.zero(), 100);
    }

    public Unit makeUnit(UnitFacings facing, int hitPoints) {
        return makeUnit(facing, Vector2D.zero(), hitPoints);
    }

    public Unit makeUnit(UnitFacings facing, Vector2D offset, int hitPoints) {
        return new Unit(map, unitMapCoordinates, spriteSheet, player, 10, facing.getValue(), unitMapCoordinates, unitMapCoordinates, offset, hitPoints, fadingSelection);
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

        Unit unit = makeUnit(UnitFacings.DOWN, offset, 100);
        Graphics graphics = Mockito.mock(Graphics.class);

        // TODO: Resolve this quirky thing, because we pass here the coordinates to draw
        // but isn't that basically the unit coordinates * tile size!?
        int drawX = 10;
        int drawY = 12;

        unit.render(graphics, drawX, drawY);

        int expectedDrawX = drawX + offsetX;
        int expectedDrawY = drawY + offsetY;

        verify(graphics).drawImage((Image) anyObject(), eq((float)expectedDrawX), eq((float)expectedDrawY));

        verify(fadingSelection, times(1)).render(eq(graphics), eq(expectedDrawX), eq(expectedDrawY));
    }

    @Test
    public void aliveUnitUpdateCycleOfUnitThatHasNothingToDo() {
        Unit unit = makeUnit(UnitFacings.DOWN);

        int deltaInMs = 1;
        unit.update(deltaInMs);

        verify(fadingSelection, times(1)).update(deltaInMs);
    }

    @Test
    public void deadUnitUpdateCycle() {
        int hitPoints = 100;
        Unit unit = makeUnit(UnitFacings.DOWN, hitPoints);
        unit.takeDamage(hitPoints);

        unit.update(1);

        verifyZeroInteractions(fadingSelection);
    }

    @Test
    public void verifyUnitMovesToDesiredCellItWantsToMoveToDownRightCell() {
        Unit unit = makeUnit(UnitFacings.DOWN);

        unit.moveTo(Vector2D.create(11, 11)); // move to right-down

        assertEquals(unit.getMapCoordinates(), Vector2D.create(10, 10));

        // update 32 'ticks'
        for (int tick = 0; tick < 32; tick++) {
            unit.update(1);
        }

        // the unit is about to move, so do not expect it has been moved yet
        assertEquals(unit.getMapCoordinates(), Vector2D.create(10, 10));
        assertEquals(unit.getOffset(), Vector2D.create(31, 31));

        // one more time
        unit.update(1);

        assertEquals(unit.getMapCoordinates(), Vector2D.create(11, 11));
        assertEquals(unit.getOffset(), Vector2D.create(0, 0));
    }

    @Test
    public void verifyUnitMovesToDesiredCellItWantsToMoveToUpperLeftCell() {
        Unit unit = makeUnit(UnitFacings.DOWN);

        unit.moveTo(Vector2D.create(9, 9)); // move to left-up

        assertEquals(unit.getMapCoordinates(), Vector2D.create(10, 10));

        // update 32 'ticks'
        for (int tick = 0; tick < 32; tick++) {
            unit.update(1);
        }

        // the unit is about to move, so do not expect it has been moved yet
        assertEquals(unit.getMapCoordinates(), Vector2D.create(10, 10));
        assertEquals(unit.getOffset(), Vector2D.create(-31, -31));

        // one more time
        unit.update(1);

        assertEquals(unit.getMapCoordinates(), Vector2D.create(9, 9));
        assertEquals(unit.getOffset(), Vector2D.create(0, 0));
    }
}