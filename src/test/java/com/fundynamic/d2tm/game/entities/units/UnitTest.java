package com.fundynamic.d2tm.game.entities.units;

import com.fundynamic.d2tm.game.AbstractD2TMTest;
import com.fundynamic.d2tm.game.behaviors.FadingSelection;
import com.fundynamic.d2tm.game.behaviors.HitPointBasedDestructibility;
import com.fundynamic.d2tm.game.entities.EntitiesSet;
import com.fundynamic.d2tm.game.entities.Entity;
import com.fundynamic.d2tm.game.entities.EntityType;
import com.fundynamic.d2tm.game.entities.Player;
import com.fundynamic.d2tm.game.entities.projectiles.Projectile;
import com.fundynamic.d2tm.game.rendering.gui.battlefield.Recolorer;
import com.fundynamic.d2tm.game.rendering.gui.battlefield.RenderQueue;
import com.fundynamic.d2tm.math.Coordinate;
import com.fundynamic.d2tm.math.MapCoordinate;
import com.fundynamic.d2tm.math.Vector2D;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.SpriteSheet;

import java.util.List;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class UnitTest extends AbstractD2TMTest {

    private Coordinate unitAbsoluteMapCoordinates;

    @Before
    public void setUp() throws SlickException {
        super.setUp();
        unitAbsoluteMapCoordinates = MapCoordinate.create(10, 10).toCoordinate();
    }

    @Test
    public void rendersUnitOnExpectedCoordinates() {
        int offsetX = 5;
        int offsetY = 6;
        Vector2D offset = Vector2D.create(offsetX, offsetY);

        Unit unit = makeUnit(UnitFacings.DOWN, unitAbsoluteMapCoordinates, offset);

        // TODO: Resolve this quirky thing, because we pass here the coordinates to draw
        // but isn't that basically the unit coordinates * tile size!?
        int drawX = 10;
        int drawY = 12;

        unit.render(graphics, drawX, drawY);

        int expectedDrawX = drawX + offsetX;
        int expectedDrawY = drawY + offsetY;

        verify(graphics, times(2)).drawImage(anyObject(), eq((float) expectedDrawX), eq((float) expectedDrawY));
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
    public void whenUnitDiesItSpawnsAnExplosion() {
        Unit unit = makeUnit(player);
        unit.takeDamage(unit.getHitPoints(), null);

        unit.update(1);

        Entity lastCreatedEntity = entityRepository.getLastCreatedEntity();
        assertThat(lastCreatedEntity.getEntityType(), is(EntityType.PARTICLE));
        assertThat(lastCreatedEntity.getEntityData().key, is(unit.getEntityData().getExplosionIdKey()));

        // once dead, we don't expect any interactions
        FadingSelection fadingSelection = mock(FadingSelection.class);
        unit.setFadingSelection(fadingSelection);
        unit.update(1);
        verifyZeroInteractions(fadingSelection);
    }

    @Test
    public void verifyUnitMovesToDesiredCellItWantsToMoveToDownRightCell() {
        Unit unit = makeUnit(UnitFacings.DOWN, unitAbsoluteMapCoordinates);

        Vector2D mapCoordinateToMoveTo = unitAbsoluteMapCoordinates.add(Vector2D.create(32, 32)); // move to right-down
        unit.moveTo(mapCoordinateToMoveTo); // translate to absolute coordinates

        assertThat(unit.getCoordinate(), is(unitAbsoluteMapCoordinates));

        unit.update(0.5F); // decide next cell
        unit.update(0.5F); // start turning

        // a QUAD moves 2 squares for 1 second (see rules.ini)
        unit.update(0.5F);

        assertThat(unit.getCoordinate(), is(mapCoordinateToMoveTo));
        assertThat(unit.getOffset(), is(Vector2D.create(0, 0)));
    }

    @Test
    public void firesProjectileWhenAttackingUnitInRange() {
        Player cpu = new Player("cpu", Recolorer.FactionColor.BLUE);
        Unit playerQuad = makeUnit(player, new Coordinate(32, 32), "QUAD");
        playerQuad.setFacing(UnitFacings.RIGHT_DOWN.getValue()); // looking at the unit below...

        Unit cpuQuad = makeUnit(cpu, new Coordinate(64, 64), "QUAD");

        // order to attack, it is in range, and already facing it.
        playerQuad.attack(cpuQuad);

        // update, 1 second passed. rules.ini file should have at least attack rate for QUAD < 1 second
        // so it fires a projectile...
        playerQuad.update(1);

        Entity lastCreatedEntity = entityRepository.getLastCreatedEntity();
        assertThat(lastCreatedEntity.getEntityType(), is(EntityType.PROJECTILE));
        Projectile projectile = (Projectile) lastCreatedEntity;

        EntitiesSet entitiesAtVector = entityRepository.findEntitiesOfTypeAtVector(projectile.getTarget(), EntityType.UNIT);
        Unit first = (Unit) entitiesAtVector.getFirst();
        assertThat(first, equalTo(cpuQuad));
    }

    @Test
    public void verifyUnitMovesToDesiredCellItWantsToMoveToUpperLeftCell() {
        Unit unit = makeUnit(UnitFacings.UP, unitAbsoluteMapCoordinates);

        Vector2D mapCoordinateToMoveTo = unitAbsoluteMapCoordinates.min(Vector2D.create(32, 32)); // move to left-up
        unit.moveTo(mapCoordinateToMoveTo); // move to left-up

        assertThat(unit.getCoordinate(), is(unitAbsoluteMapCoordinates));

        // for facing, coming from UP to LEFT_UP requires a 1 step
        unit.update(1); // first decide which movecell, etc

        // we take 0.5F because turnSpeed is 2 'turns' per second. So half a second is 1 facing per update
        unit.update(0.5F); // turn towards

        // a QUAD moves 2 squares for 1 second (see rules.ini)
        unit.update(0.5F);

        assertThat(unit.getCoordinate(), is(mapCoordinateToMoveTo));
        assertThat(unit.getOffset(), is(Vector2D.create(0, 0)));
    }

    @Test
    public void selectedUnitPutsFadingSelectionAndHealthBarOnRenderQueue() {
        Unit unit = makeUnit(player, Coordinate.create(48, 48), "QUAD");
        unit.select();

        Vector2D viewportVec = Vector2D.create(32, 32);
        RenderQueue renderQueue = new RenderQueue(viewportVec);
        unit.enrichRenderQueue(renderQueue);

        List<RenderQueue.ThingToRender> thingsToRender = renderQueue.getThingsToRender(RenderQueue.ENTITY_GUI_LAYER);
        assertThat(thingsToRender.size(), is(2));

        RenderQueue.ThingToRender first = thingsToRender.get(0);
        assertThat(first.renderQueueEnrichable, is(instanceOf(HitPointBasedDestructibility.class)));
        assertThat(first.screenX, is(16)); // unitX - viewportVecX
        assertThat(first.screenY, is(16)); // unitY - viewportVecY

        RenderQueue.ThingToRender second = thingsToRender.get(1);
        assertThat(second.renderQueueEnrichable, is(instanceOf(FadingSelection.class)));
        assertThat(second.screenX, is(16)); // unitX - viewportVecX
        assertThat(second.screenY, is(16)); // unitY - viewportVecY
    }

    public static SpriteSheet makeSpriteSheet() {
        SpriteSheet spriteSheet = mock(SpriteSheet.class);
        Image image = mock(Image.class);

        when(spriteSheet.getSprite(anyInt(), anyInt())).thenReturn(image);
        return spriteSheet;
    }

}