package com.fundynamic.d2tm.game.entities.units;

import com.fundynamic.d2tm.game.AbstractD2TMTest;
import com.fundynamic.d2tm.game.behaviors.FadingSelection;
import com.fundynamic.d2tm.game.behaviors.HitPointBasedDestructibility;
import com.fundynamic.d2tm.game.entities.*;
import com.fundynamic.d2tm.game.entities.entitiesdata.EntitiesData;
import com.fundynamic.d2tm.game.entities.projectiles.Projectile;
import com.fundynamic.d2tm.game.entities.structures.Structure;
import com.fundynamic.d2tm.game.entities.units.states.GoalResolverState;
import com.fundynamic.d2tm.game.entities.units.states.MoveToCellState;
import com.fundynamic.d2tm.game.entities.units.states.TurnBodyTowardsState;
import com.fundynamic.d2tm.game.map.MapEditor;
import com.fundynamic.d2tm.game.rendering.gui.battlefield.RenderQueue;
import com.fundynamic.d2tm.game.terrain.impl.DuneTerrain;
import com.fundynamic.d2tm.game.terrain.impl.DuneTerrainFactory;
import com.fundynamic.d2tm.game.types.EntityData;
import com.fundynamic.d2tm.graphics.Theme;
import com.fundynamic.d2tm.math.Coordinate;
import com.fundynamic.d2tm.math.MapCoordinate;
import com.fundynamic.d2tm.math.Vector2D;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.newdawn.slick.SlickException;

import java.util.List;

import static com.fundynamic.d2tm.game.map.Cell.TILE_SIZE;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.*;

public class UnitTest extends AbstractD2TMTest {

    private Coordinate unitAbsoluteMapCoordinates;

    @Before
    public void setUp() throws SlickException {
        super.setUp();
        unitAbsoluteMapCoordinates = MapCoordinate.create(10, 10).toCoordinate();
    }

    @Test
    public void takingDamageReducesHitpoints() {
        Unit unit = makeUnit(player);
        int hitPoints = unit.getHitPoints();

        int damageInHitpoints = 5;
        unit.takeDamage(damageInHitpoints, null);

        assertEquals(hitPoints - damageInHitpoints, unit.getHitPoints());
    }

    // FLAKY TEST: Sometimes fails probably because 'random cell to move to' is the same as it was now...
    @Test
    public void cpuUnitMovesRandomlyAroundWhenTakingDamageFromUnknownEntity() {
        Unit cpuUnit = makeUnit(cpu, MapCoordinate.create(2, 2), EntitiesData.QUAD);
        assertFalse(cpuUnit.shouldMove());

        cpuUnit.takeDamage(1, null); // null means unknown entity

        assertTrue(cpuUnit.shouldMove());
    }

    @Test
    @Ignore("This is for now no longer applicable")
    public void cpuUnitAttacksBackTheEntityItTookDamageFrom() {
        Unit humanUnit = makeUnit(player);

        Unit unit = makeUnit(cpu, MapCoordinate.create(2, 2), EntitiesData.QUAD);
        assertFalse(unit.shouldAttack());

        unit.takeDamage(1, humanUnit); // takes damage from the humanUnit
        updateUnitTimesHundredMilis(unit, 2);

        assertEquals(humanUnit, unit.getEntityToAttack());
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

        unit.update(1); // dying
        unit.update(1); // dead

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
        // start at 320, 320
        Unit unit = makeUnit(UnitFacings.DOWN, unitAbsoluteMapCoordinates);

        // move to right-down (352, 352)
        // this requires 1 'turn' and 1 cell 'move'
        Coordinate mapCoordinateToMoveTo = unitAbsoluteMapCoordinates.add(Vector2D.create(TILE_SIZE, TILE_SIZE));
        unit.moveTo(mapCoordinateToMoveTo); // translate to absolute coordinates

        assertThat(unit.getCoordinate(), is(unitAbsoluteMapCoordinates));

        // expect unit to be in goal resolver state
        assertThat(unit.getState(), instanceOf(GoalResolverState.class));
        updateUnitTimesHundredMilis(unit, 1);

        // goal resolver will move into
        assertThat(unit.getState(), instanceOf(MoveToCellState.class));
        updateUnitTimesHundredMilis(unit, 1);

        // the unit will be turning around
        assertThat(unit.getState(), instanceOf(TurnBodyTowardsState.class));
        updateUnitTimesHundredMilis(unit, 2);

        // done turning around
        assertThat(unit.getState(), instanceOf(MoveToCellState.class));

        // update 1 second, thus 10X100ms
        updateUnitTimesHundredMilis(unit, 10);

        // at destination
        assertThat(unit.getCoordinate(), is(mapCoordinateToMoveTo));
        assertThat(unit.getOffset(), is(Vector2D.create(0, 0)));
    }

    @Test
    public void verifyUnitMovesToDesiredCellItWantsToMoveToUpperLeftCell() {
        // start at 320, 320
        Unit unit = makeUnit(UnitFacings.UP, unitAbsoluteMapCoordinates);

        // move to left-up
        // 288, 288
        Coordinate mapCoordinateToMoveTo = unitAbsoluteMapCoordinates.min(Vector2D.create(TILE_SIZE, TILE_SIZE));
        unit.moveTo(mapCoordinateToMoveTo);

        assertThat(unit.getCoordinate(), is(unitAbsoluteMapCoordinates));

        // expect unit to be in goal resolver state
        assertThat(unit.getState(), instanceOf(GoalResolverState.class));
        updateUnitTimesHundredMilis(unit, 1);

        // goal resolver will move into
        assertThat(unit.getState(), instanceOf(MoveToCellState.class));
        updateUnitTimesHundredMilis(unit, 1);

        // the unit will be turning around
        assertThat(unit.getState(), instanceOf(TurnBodyTowardsState.class));
        updateUnitTimesHundredMilis(unit, 2);

        // done turning around
        assertThat(unit.getState(), instanceOf(MoveToCellState.class));

        // update 1 second, thus 10X100ms
        updateUnitTimesHundredMilis(unit, 10);

        // at destination
        assertThat(unit.getCoordinate(), is(mapCoordinateToMoveTo));
        assertThat(unit.getOffset(), is(Vector2D.create(0, 0)));
    }

    @Test
    public void firesProjectileWhenAttackingUnitInRange() {
        Player cpu = new Player("cpu", Faction.BLUE);
        Unit playerQuad = makeUnit(player, MapCoordinate.create(1, 1), "QUAD");
        playerQuad.setFacing(UnitFacings.RIGHT_DOWN.getValue()); // looking at the unit below...

        Unit cpuQuad = makeUnit(cpu, MapCoordinate.create(2, 2), "QUAD");

        // order to attack, it is in range, and already facing it.
        playerQuad.attack(cpuQuad);

        // update, 1 second passed. rules.ini file should have at least attack rate for QUAD < 1 second
        // so it fires a projectile...
        updateUnitTimesHundredMilis(playerQuad, 10);

        Entity lastCreatedEntity = entityRepository.getLastCreatedEntity();
        assertThat(lastCreatedEntity.getEntityType(), is(EntityType.PROJECTILE));
        Projectile projectile = (Projectile) lastCreatedEntity;

        EntitiesSet entitiesAtVector = entityRepository.findAliveEntitiesOfTypeAtVector(projectile.getTarget(), EntityType.UNIT);
        Unit first = (Unit) entitiesAtVector.getFirst();
        assertThat(first, equalTo(cpuQuad));
    }

    @Test
    public void selectedUnitPutsFadingSelectionAndHealthBarOnRenderQueue() {
        MapCoordinate mapCoordinate = MapCoordinate.create(2, 2);
        Coordinate unitCoordinate = mapCoordinate.toCoordinate();
        Unit unit = makeUnit(player, mapCoordinate, EntitiesData.QUAD);
        unit.select();

        Vector2D viewportVec = Vector2D.create(32, 32);
        RenderQueue renderQueue = new RenderQueue(viewportVec);
        unit.enrichRenderQueue(renderQueue);

        List<RenderQueue.ThingToRender> thingsToRender = renderQueue.getThingsToRender(RenderQueue.ENTITY_GUI_LAYER);
        assertThat(thingsToRender.size(), is(2));

        RenderQueue.ThingToRender first = thingsToRender.get(0);
        assertThat(first.renderQueueEnrichable, is(instanceOf(HitPointBasedDestructibility.class)));
        assertThat(first.screenX, is(unitCoordinate.getXAsInt() - viewportVec.getXAsInt())); // unitX - viewportVecX
        assertThat(first.screenY, is(unitCoordinate.getYAsInt() - viewportVec.getYAsInt())); // unitY - viewportVecY

        RenderQueue.ThingToRender second = thingsToRender.get(1);
        assertThat(second.renderQueueEnrichable, is(instanceOf(FadingSelection.class)));
        assertThat(second.screenX, is(unitCoordinate.getXAsInt() - viewportVec.getXAsInt())); // unitX - viewportVecX
        assertThat(second.screenY, is(unitCoordinate.getYAsInt() - viewportVec.getYAsInt())); // unitY - viewportVecY
    }

    @Test
    public void canHarvestWhenHarvesterAndOnHarvestableCell() {
        EntityData harvesterEntityData = getEntitiesData().getEntityData(EntityType.UNIT, EntitiesData.HARVESTER);
        assertThat(harvesterEntityData.isHarvester, is(true));

        // make an all 'spice' map, with more resources than the harvester capacity
        MapEditor mapEditor = new MapEditor(new DuneTerrainFactory(mock(Theme.class)) {
            @Override
            public int getSpiceAmount() {
                return harvesterEntityData.harvestCapacity + 1;
            }
        });
        mapEditor.fillMapWithTerrain(map, DuneTerrain.TERRAIN_SPICE);


        Unit unit = makeUnit(player, MapCoordinate.create(2, 2), EntitiesData.HARVESTER);

        assertThat(unit.canHarvest(), is(true));
        assertThat(unit.isDoneHarvesting(), is(false));

        // harvest
        unit.harvest(harvesterEntityData.harvestCapacity);

        // still spice remaining, but the harvester is full
        // so: can? yes, isDoneHarvesting? yes
        assertThat(unit.canHarvest(), is(true));
        assertThat(unit.isDoneHarvesting(), is(true));
    }

    @Test
    public void canHarvestIsFalseWhenNotHarvester() {
        // make an all spice map
        MapEditor mapEditor = new MapEditor(new DuneTerrainFactory(mock(Theme.class)));
        mapEditor.fillMapWithTerrain(map, DuneTerrain.TERRAIN_SPICE);

        Unit unit = makeUnit(player, MapCoordinate.create(2, 2), EntitiesData.QUAD);
        assertThat(unit.canHarvest(), is(false));
    }
    
    @Test
    public void returnToRefineryThrowsIllegalArgumentExceptionWhenArgumentIsNotARefinery() {
        try {
            Unit unit = makeUnit(player, MapCoordinate.create(2, 2), EntitiesData.HARVESTER);
            Structure constYard = makeStructure(player, MapCoordinate.create(10, 10), EntitiesData.CONSTRUCTION_YARD);
            unit.returnToRefinery(constYard);
            fail("Expected illegal argument exception");
        } catch (IllegalArgumentException iae) {
            assertEquals("Can only return to refinery type of entity", iae.getMessage());
        }
    }

    @Test
    public void returnToRefineryThrowsIllegalArgumentExceptionWhenUnitIsNotAHarvesterType() {
        try {
            Unit unit = makeUnit(player, MapCoordinate.create(2, 2), EntitiesData.QUAD);
            Structure refinery = makeStructure(player, MapCoordinate.create(10, 10), EntitiesData.REFINERY);
            unit.returnToRefinery(refinery);
            fail("Expected illegal state exception");
        } catch (IllegalStateException ise) {
            assertEquals("Only harvesters can return to a refinery", ise.getMessage());
        }
    }

    @Test
    public void firstHarvesterReturnsToRefinery() {
        Unit unit = makeUnit(player, MapCoordinate.create(2, 2), EntitiesData.HARVESTER);
        Structure refinery = makeStructure(player, MapCoordinate.create(10, 10), EntitiesData.REFINERY);

        // Act
        unit.returnToRefinery(refinery);

        Entity who = EnterStructureIntent.instance.getEnterIntentFrom(refinery);
        assertSame(who, unit);

        assertTrue(unit.getState() instanceof GoalResolverState);
        assertEquals(unit.getTarget(), refinery.getCoordinate());
    }

    @Test
    public void secondHarvesterMovesToRefinery() {
        Unit harvester1 = makeUnit(player, MapCoordinate.create(2, 2), EntitiesData.HARVESTER);
        Unit harvester2 = makeUnit(player, MapCoordinate.create(3, 2), EntitiesData.HARVESTER);
        Structure refinery = makeStructure(player, MapCoordinate.create(10, 10), EntitiesData.REFINERY);

        // Act
        harvester2.returnToRefinery(refinery);
        harvester1.returnToRefinery(refinery);

        Entity who = EnterStructureIntent.instance.getEnterIntentFrom(refinery);
        assertSame(who, harvester2);
    }

    @Test
    public void isCellPassableForMeIsTrueWhenNoOtherUnitIsPresent() {
        Unit harvester = makeUnit(player, MapCoordinate.create(2, 2), EntitiesData.HARVESTER);
        assertTrue(harvester.isCellPassableForMe(MapCoordinate.create(3, 3)));
    }

    @Test
    public void isCellPassableForMeIsFalseWhenOtherEntityOccupiesCell() {
        Unit harvester1 = makeUnit(player, MapCoordinate.create(2, 2), EntitiesData.HARVESTER);
        makeStructure(player, MapCoordinate.create(3, 3), EntitiesData.REFINERY);
        assertFalse(harvester1.isCellPassableForMe(MapCoordinate.create(3, 3)));
    }

    @Test
    public void isCellPassableForMeIsTrueWhenEntityThatOccupiesCellIsTheEntityToReturnTo() {
        Unit harvester = makeUnit(player, MapCoordinate.create(2, 2), EntitiesData.HARVESTER);
        Structure refinery = makeStructure(player, MapCoordinate.create(3, 3), EntitiesData.REFINERY);
        harvester.returnToRefinery(refinery);
        assertTrue(harvester.isCellPassableForMe(MapCoordinate.create(3, 3)));
    }

    public static void updateUnitTimesHundredMilis(Unit unit, int times) {
        for (int i = 0; i < times; i++) {
            unit.update(0.1f);
        }
    }

}