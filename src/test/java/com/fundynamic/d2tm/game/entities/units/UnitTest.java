package com.fundynamic.d2tm.game.entities.units;

import com.fundynamic.d2tm.game.AbstractD2TMTest;
import com.fundynamic.d2tm.game.behaviors.FadingSelection;
import com.fundynamic.d2tm.game.entities.EntityData;
import com.fundynamic.d2tm.game.entities.EntityRepository;
import com.fundynamic.d2tm.game.entities.EntityRepositoryTest;
import com.fundynamic.d2tm.game.entities.Player;
import com.fundynamic.d2tm.game.map.Map;
import com.fundynamic.d2tm.game.map.MapTest;
import com.fundynamic.d2tm.graphics.Shroud;
import com.fundynamic.d2tm.math.Vector2D;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.newdawn.slick.Graphics;
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

    @Mock
    private SpriteSheet spriteSheet;

    @Mock
    private Player player;

    @Mock
    private FadingSelection fadingSelection;

    private EntityRepository entityRepository;

    private Unit unit;
    private Vector2D unitAbsoluteMapCoordinates;

    @Before
    public void setUp() throws SlickException {
        super.setUp();
        int TILE_SIZE = 32;
        unitAbsoluteMapCoordinates = Vector2D.create(10, 10).scale(TILE_SIZE);
        entityRepository = EntityRepositoryTest.makeTestableEntityRepository(map);
        unit = makeUnit(UnitFacings.RIGHT, entityRepository);
    }

    public Unit makeUnit(UnitFacings facing, EntityRepository entityRepository) {
        return makeUnit(facing, Vector2D.zero(), 100, entityRepository);
    }

    public Unit makeUnit(UnitFacings facing, int hitPoints, EntityRepository entityRepository) {
        return makeUnit(facing, Vector2D.zero(), hitPoints, entityRepository);
    }

    public Unit makeUnit(UnitFacings facing, Vector2D offset, int hitPoints, EntityRepository entityRepository) {
        EntityData entityData = new EntityData(32, 32, 10);
        return new Unit(map, unitAbsoluteMapCoordinates, spriteSheet, player, entityData, facing.getValue(), unitAbsoluteMapCoordinates, unitAbsoluteMapCoordinates, offset, hitPoints, fadingSelection, entityRepository) {
            @Override
            public boolean isDestroyed() {
                // we do this so that we do not have to deal with spawning explosions (which is done in the
                // update method)
                return super.hitPointBasedDestructibility.hasDied();
            }
        };
    }

    @Test
    public void returnsCurrentFacingWhenNothingChanged() {
        assertEquals(UnitFacings.RIGHT, unit.determineFacingFor(unit.getAbsoluteMapCoordinates()));
    }

    @Test
    public void determinesFacingRightDown() {
        Vector2D coordinatesToFaceTo = unitAbsoluteMapCoordinates.add(Vector2D.create(1, 1));
        assertEquals(UnitFacings.RIGHT_DOWN, unit.determineFacingFor(coordinatesToFaceTo));
    }

    @Test
    public void determinesFacingLeftDown() {
        Vector2D coordinatesToFaceTo = unitAbsoluteMapCoordinates.add(Vector2D.create(-1, 1));
        assertEquals(UnitFacings.LEFT_DOWN, unit.determineFacingFor(coordinatesToFaceTo));
    }

    @Test
    public void determinesFacingRightUp() {
        Vector2D coordinatesToFaceTo = unitAbsoluteMapCoordinates.add(Vector2D.create(1, -1));
        assertEquals(UnitFacings.RIGHT_UP, unit.determineFacingFor(coordinatesToFaceTo));
    }

    @Test
    public void determinesFacingLeftUp() {
        Vector2D coordinatesToFaceTo = unitAbsoluteMapCoordinates.add(Vector2D.create(-1, -1));
        assertEquals(UnitFacings.LEFT_UP, unit.determineFacingFor(coordinatesToFaceTo));
    }

    @Test
    public void determinesFacingUp() {
        Vector2D coordinatesToFaceTo = unitAbsoluteMapCoordinates.add(Vector2D.create(0, -1));
        assertEquals(UnitFacings.UP, unit.determineFacingFor(coordinatesToFaceTo));
    }

    @Test
    public void determinesFacingDown() {
        Vector2D coordinatesToFaceTo = unitAbsoluteMapCoordinates.add(Vector2D.create(0, 1));
        assertEquals(UnitFacings.DOWN, unit.determineFacingFor(coordinatesToFaceTo));
    }

    @Test
    public void determinesFacingLeft() {
        Vector2D coordinatesToFaceTo = unitAbsoluteMapCoordinates.add(Vector2D.create(-1, 0));
        assertEquals(UnitFacings.LEFT, unit.determineFacingFor(coordinatesToFaceTo));
    }

    @Test
    public void determinesFacingRight() {
        unit = makeUnit(UnitFacings.LEFT, entityRepository);
        Vector2D coordinatesToFaceTo = unitAbsoluteMapCoordinates.add(Vector2D.create(1, 0));
        assertEquals(UnitFacings.RIGHT, unit.determineFacingFor(coordinatesToFaceTo));
    }

    @Test
    public void rendersUnitOnExpectedCoordinates() {
        int offsetX = 5;
        int offsetY = 6;
        Vector2D offset = Vector2D.create(offsetX, offsetY);

        Unit unit = makeUnit(UnitFacings.DOWN, offset, 100, entityRepository);
        Graphics graphics = mock(Graphics.class);

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
        Unit unit = makeUnit(UnitFacings.DOWN, entityRepository);

        int deltaInMs = 1;
        unit.update(deltaInMs);

        verify(fadingSelection, times(1)).update(deltaInMs);
    }

    @Test
    public void deadUnitUpdateCycle() {
        int hitPoints = 100;
        Unit unit = makeUnit(UnitFacings.DOWN, hitPoints, entityRepository);
        unit.takeDamage(hitPoints);

        unit.update(1);

        verifyZeroInteractions(fadingSelection);
    }

    @Test
    public void verifyUnitMovesToDesiredCellItWantsToMoveToDownRightCell() {
        Unit unit = makeUnit(UnitFacings.DOWN, entityRepository);

        Vector2D mapCoordinateToMoveTo = unitAbsoluteMapCoordinates.add(Vector2D.create(32, 32)); // move to right-down
        unit.moveTo(mapCoordinateToMoveTo); // translate to absolute coordinates

        assertThat(unit.getAbsoluteMapCoordinates(), is(unitAbsoluteMapCoordinates));

        // update 32 'ticks'
        for (int tick = 0; tick < 32; tick++) {
            unit.update(1);
        }

        // the unit is about to fully move onto new cell
        assertThat(unit.getAbsoluteMapCoordinates(), is(unitAbsoluteMapCoordinates));
        assertThat(unit.getOffset(), is(Vector2D.create(31, 31)));

        // one more time
        unit.update(1);

        assertThat(unit.getAbsoluteMapCoordinates(), is(mapCoordinateToMoveTo));
        assertThat(unit.getOffset(), is(Vector2D.create(0, 0)));
    }

    @Test
    public void verifyUnitMovesToDesiredCellItWantsToMoveToUpperLeftCell() {
        Unit unit = makeUnit(UnitFacings.DOWN, entityRepository);

        Vector2D mapCoordinateToMoveTo = unitAbsoluteMapCoordinates.min(Vector2D.create(32, 32)); // move to left-up
        unit.moveTo(mapCoordinateToMoveTo); // move to left-up

        assertThat(unit.getAbsoluteMapCoordinates(), is(unitAbsoluteMapCoordinates));

        // update 32 'ticks'
        for (int tick = 0; tick < 32; tick++) {
            unit.update(1);
        }

        // the unit is about to move, so do not expect it has been moved yet
        assertThat(unit.getAbsoluteMapCoordinates(), is(unitAbsoluteMapCoordinates));
        assertThat(unit.getOffset(), is(Vector2D.create(-31, -31)));

        // one more time
        unit.update(1);

        assertThat(unit.getAbsoluteMapCoordinates(), is(mapCoordinateToMoveTo));
        assertThat(unit.getOffset(), is(Vector2D.create(0, 0)));
    }


    /**
     * Replace with com.fundynamic.d2tm.game.entities.EntityRepositoryTest#createUnit(EntityRepository, Vector2D, Player) ?
     * @return
     */
    public static Unit makeUnit(Map map, Player player, Vector2D mapCoordinates) throws SlickException {
        EntityRepository entityRepository = EntityRepositoryTest.makeTestableEntityRepository(map);
        return makeUnit(map, player, mapCoordinates, entityRepository);
    }
    /**
     * Replace with com.fundynamic.d2tm.game.entities.EntityRepositoryTest#createUnit(EntityRepository, Vector2D, Player) ?
     * @param map
     * @param player
     * @param mapCoordinates
     * @return
     */
    public static Unit makeUnit(Map map, Player player, Vector2D mapCoordinates, EntityRepository entityRepository) throws SlickException {
        Unit unit = entityRepository.placeUnitOnMap(mapCoordinates, EntityRepositoryTest.UNIT_FIRST_ID, player);
        map.placeUnit(unit);
        return unit;
    }

    public static SpriteSheet makeSpriteSheet() {
        SpriteSheet spriteSheet = mock(SpriteSheet.class);
        Image image = mock(Image.class);

        when(spriteSheet.getSprite(anyInt(), anyInt())).thenReturn(image);
        return spriteSheet;
    }

}