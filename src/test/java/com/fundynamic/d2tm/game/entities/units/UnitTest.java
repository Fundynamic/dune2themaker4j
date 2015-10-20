package com.fundynamic.d2tm.game.entities.units;

import com.fundynamic.d2tm.game.AbstractD2TMTest;
import com.fundynamic.d2tm.game.behaviors.FadingSelection;
import com.fundynamic.d2tm.math.Vector2D;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.SpriteSheet;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class UnitTest extends AbstractD2TMTest {

    private Unit unit;

    private Vector2D unitAbsoluteMapCoordinates;

    @Before
    public void setUp() throws SlickException {
        super.setUp();
        unitAbsoluteMapCoordinates = Vector2D.create(10, 10).scale(TILE_SIZE);
        unit = makeUnit(UnitFacings.RIGHT, unitAbsoluteMapCoordinates);
    }

    @Test
    public void returnsRightWhenUnableToFigureOutWhereToFaceTo() {
        assertEquals(UnitFacings.RIGHT, unit.determineFacingFor(unit.getAbsoluteCoordinates()));
    }



    @Test
    public void rendersUnitOnExpectedCoordinates() {
        int offsetX = 5;
        int offsetY = 6;
        Vector2D offset = Vector2D.create(offsetX, offsetY);

        Unit unit = makeUnit(UnitFacings.DOWN, unitAbsoluteMapCoordinates, offset, 100);

        // TODO: Resolve this quirky thing, because we pass here the coordinates to draw
        // but isn't that basically the unit coordinates * tile size!?
        int drawX = 10;
        int drawY = 12;

        unit.render(graphics, drawX, drawY);

        int expectedDrawX = drawX + offsetX;
        int expectedDrawY = drawY + offsetY;

        verify(graphics).drawImage((Image) anyObject(), eq((float) expectedDrawX), eq((float) expectedDrawY));
    }

    @Test
    public void aliveUnitUpdateCycleOfUnitThatIsNotSelected() {
        FadingSelection fadingSelection = mock(FadingSelection.class);
        Unit unit = makeUnit(UnitFacings.DOWN, unitAbsoluteMapCoordinates);
        unit.setFadingSelection(fadingSelection);

        int deltaInMs = 1;
        unit.update(deltaInMs);

        verify(fadingSelection, times(1)).update(deltaInMs);
    }

    @Test
    public void deadUnitUpdateCycle() {
        FadingSelection fadingSelection = mock(FadingSelection.class);
        int hitPoints = 100;
        Unit unit = makeUnit(UnitFacings.DOWN, unitAbsoluteMapCoordinates, Vector2D.zero(), hitPoints);
        unit.setFadingSelection(fadingSelection);

        unit.takeDamage(hitPoints);

        unit.update(1);

        verifyZeroInteractions(fadingSelection);
    }

    @Test
    public void verifyUnitMovesToDesiredCellItWantsToMoveToDownRightCell() {
        Unit unit = makeUnit(UnitFacings.DOWN, unitAbsoluteMapCoordinates);

        Vector2D mapCoordinateToMoveTo = unitAbsoluteMapCoordinates.add(Vector2D.create(32, 32)); // move to right-down
        unit.moveTo(mapCoordinateToMoveTo); // translate to absolute coordinates

        assertThat(unit.getAbsoluteCoordinates(), is(unitAbsoluteMapCoordinates));

        // update 32 'ticks'
        for (int tick = 0; tick < 32; tick++) {
            unit.update(1);
        }

        // the unit is about to fully move onto new cell
        assertThat(unit.getAbsoluteCoordinates(), is(unitAbsoluteMapCoordinates));
        assertThat(unit.getOffset(), is(Vector2D.create(31, 31)));

        // one more time
        unit.update(1);

        assertThat(unit.getAbsoluteCoordinates(), is(mapCoordinateToMoveTo));
        assertThat(unit.getOffset(), is(Vector2D.create(0, 0)));
    }

    @Test
    public void verifyUnitMovesToDesiredCellItWantsToMoveToUpperLeftCell() {
        Unit unit = makeUnit(UnitFacings.DOWN, unitAbsoluteMapCoordinates);

        Vector2D mapCoordinateToMoveTo = unitAbsoluteMapCoordinates.min(Vector2D.create(32, 32)); // move to left-up
        unit.moveTo(mapCoordinateToMoveTo); // move to left-up

        assertThat(unit.getAbsoluteCoordinates(), is(unitAbsoluteMapCoordinates));

        // update 32 'ticks'
        for (int tick = 0; tick < 32; tick++) {
            unit.update(1);
        }

        // the unit is about to move, so do not expect it has been moved yet
        assertThat(unit.getAbsoluteCoordinates(), is(unitAbsoluteMapCoordinates));
        assertThat(unit.getOffset(), is(Vector2D.create(-31, -31)));

        // one more time
        unit.update(1);

        assertThat(unit.getAbsoluteCoordinates(), is(mapCoordinateToMoveTo));
        assertThat(unit.getOffset(), is(Vector2D.create(0, 0)));
    }


    public static SpriteSheet makeSpriteSheet() {
        SpriteSheet spriteSheet = mock(SpriteSheet.class);
        Image image = mock(Image.class);

        when(spriteSheet.getSprite(anyInt(), anyInt())).thenReturn(image);
        return spriteSheet;
    }

}