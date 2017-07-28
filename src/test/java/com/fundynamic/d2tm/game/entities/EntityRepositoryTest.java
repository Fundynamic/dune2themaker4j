package com.fundynamic.d2tm.game.entities;

import com.fundynamic.d2tm.game.AbstractD2TMTest;
import com.fundynamic.d2tm.game.entities.entitiesdata.EntitiesData;
import com.fundynamic.d2tm.game.entities.superpowers.SuperPower;
import com.fundynamic.d2tm.game.entities.units.Unit;
import com.fundynamic.d2tm.game.rendering.gui.battlefield.Recolorer;
import com.fundynamic.d2tm.game.types.EntityData;
import com.fundynamic.d2tm.math.Coordinate;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.internal.util.collections.Sets;
import org.newdawn.slick.SlickException;

import static org.hamcrest.collection.IsEmptyCollection.empty;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

public class EntityRepositoryTest extends AbstractD2TMTest {

    @Test
    public void findsUnitAtVector() throws SlickException {
        Unit unit = makeUnit(player, Coordinate.create(100, 100), EntitiesData.QUAD);

        // find at same position
        EntitiesSet entities = entityRepository.findAliveEntitiesOfTypeAtVector(Coordinate.create(100, 100), EntityType.UNIT);
        assertThat(entities.hasAny(), is(true));
        assertThat((Unit) entities.getFirst(), is(unit));

        // do not find anything at a bit more upwards
        entities = entityRepository.findAliveEntitiesOfTypeAtVector(Coordinate.create(99, 99), EntityType.UNIT);
        assertThat(entities, is(empty()));

        // find entity at its right-bottom dimension
        entities = entityRepository.findAliveEntitiesOfTypeAtVector(Coordinate.create(131, 131), EntityType.UNIT);
        assertThat(entities.hasAny(), is(true));
        assertThat((Unit) entities.getFirst(), is(unit));

        // do not find anything at a bit more upwards
        entities = entityRepository.findAliveEntitiesOfTypeAtVector(Coordinate.create(132, 132), EntityType.UNIT);
        assertThat(entities, is(empty()));
    }

    @Test
    public void findAliveEntitiesAtMultipleCoordinates() throws SlickException {
        Unit unit1 = makeUnit(player, Coordinate.create(32, 32), EntitiesData.QUAD);
        Unit unit2 = makeUnit(player, Coordinate.create(64, 32), EntitiesData.QUAD);


        // Single coordinate passed, expected unit1
        EntitiesSet destructibleEntities = entityRepository.findDestructibleEntities(Coordinate.create(32, 32));

        assertThat(destructibleEntities.hasAny(), is(true));
        assertThat(destructibleEntities.size(), is(1));
        assertThat(destructibleEntities.contains(unit1), is(true));

        // Single coordinate passed, as Set
        destructibleEntities = entityRepository.findDestructibleEntities(Sets.newSet(Coordinate.create(32, 32)));

        assertThat(destructibleEntities.hasAny(), is(true));
        assertThat(destructibleEntities.size(), is(1));
        assertThat(destructibleEntities.contains(unit1), is(true));

        // Two coordinate passed, as Set, only one coordinate would yield result
        destructibleEntities = entityRepository.findDestructibleEntities(Sets.newSet(Coordinate.create(32, 32), Coordinate.create(320, 320)));

        assertThat(destructibleEntities.hasAny(), is(true));
        assertThat(destructibleEntities.size(), is(1));
        assertThat(destructibleEntities.contains(unit1), is(true));

        // Two coordinate passed, as Set, two entities should be found
        destructibleEntities = entityRepository.findDestructibleEntities(Sets.newSet(Coordinate.create(32, 32), Coordinate.create(64, 32)));

        assertThat(destructibleEntities.hasAny(), is(true));
        assertThat(destructibleEntities.size(), is(2));
        assertThat(destructibleEntities.contains(unit1), is(true));
        assertThat(destructibleEntities.contains(unit2), is(true));
    }


    @Test (expected = IllegalArgumentException.class)
    public void throwsExceptionWhenTryingToCreateExplosionOutOfNonParticle() {
        EntityData entityData = entitiesData.getEntityData(EntityType.UNIT, EntitiesData.QUAD);
        entityRepository.placeExplosion(Coordinate.create(0, 0), entityData, player);
    }

    @Test (expected = EntityNotFoundException.class)
    public void placeOnMapThrowsEntityNotFoundExceptionWhenAskingForUnknownEntity() {
        entityRepository.placeOnMap(Coordinate.create(0, 0), EntityType.UNIT, "93232", player);
    }

    @Test
    public void removeEntities() {
        makeUnit(player);
    }

    @Test
    public void placeOnMapPutsUnitOnMap() throws SlickException {
        entityRepository.placeOnMap(Coordinate.create(10, 11), EntityType.UNIT, EntitiesData.QUAD, player);

        EntitiesSet entitiesSet = entityRepository.getEntitiesSet();
        assertThat(entitiesSet.size(), is(1));

        Entity first = entitiesSet.getFirst();

        assertThat(first.getCoordinate(), is(Coordinate.create(10, 11)));
    }

    @Test
    public void placeOnMapPutsStructureOnMap() throws SlickException {
        entityRepository.placeOnMap(Coordinate.create(21, 23), EntityType.STRUCTURE, EntitiesData.CONSTRUCTION_YARD, player);

        EntitiesSet entitiesSet = entityRepository.getEntitiesSet();
        assertThat(entitiesSet.size(), is(1));

        Entity first = entitiesSet.getFirst();

        assertThat(first.getCoordinate(), is(Coordinate.create(21, 23)));
    }

    @Test
    public void placeParticleOnMap() {
        entityRepository.placeOnMap(Coordinate.create(21, 23), EntityType.PARTICLE, EntitiesData.EXPLOSION_SMALL_UNIT, player);

        EntitiesSet entitiesSet = entityRepository.getEntitiesSet();
        assertThat(entitiesSet.size(), is(1));

        Entity first = entitiesSet.getFirst();

        assertThat(first.getCoordinate(), is(Coordinate.create(21, 23)));
    }

    @Test(expected = IllegalArgumentException.class)
    public void creatingEntityRepositoryWithoutEntitiesDataThrowsException() throws SlickException {
        new EntityRepository(map, new Recolorer(), new EntitiesData());
    }

    @Test
    public void spawnSuperPower() {
        EntityData deathhand = entityRepository.getEntityData(EntityType.SUPERPOWER, "DEATHHAND");
        SuperPower superPower = entityRepository.spawnSuperPower(Coordinate.create(322, 123), deathhand, player, Coordinate.zero());

        // expect location of super power to be 0,0, since it won't matter anyway.
        // the super power is a script
        assertThat(superPower.getCoordinate(), is(Coordinate.zero()));
    }

    @Test
    public void createSuperWeaponUsingPlaceOnMapThrowsException() {
        try {
            entityRepository.placeOnMap(Coordinate.zero(), EntityType.SUPERPOWER, "DEATHHAND", player);
            fail("Expected exception to be thrown");
        } catch (IllegalArgumentException e) {
            Assert.assertEquals("Don't use placeOnMap, but use method spawnSuperPower method instead.", e.getMessage());
        }
    }
}