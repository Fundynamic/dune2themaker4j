package com.fundynamic.d2tm.game.entities;

import com.fundynamic.d2tm.game.AbstractD2TMTest;
import com.fundynamic.d2tm.game.entities.entitiesdata.EntitiesData;
import com.fundynamic.d2tm.game.entities.units.Unit;
import com.fundynamic.d2tm.game.rendering.gui.battlefield.Recolorer;
import com.fundynamic.d2tm.math.Coordinate;
import com.fundynamic.d2tm.math.Vector2D;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;
import org.newdawn.slick.SlickException;

import static org.hamcrest.collection.IsEmptyCollection.empty;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

@RunWith(MockitoJUnitRunner.class)
public class EntityRepositoryTest extends AbstractD2TMTest {

    @Test
    public void findsUnitAtVector() throws SlickException {
        Unit unit = makeUnit(player, Coordinate.create(100, 100), EntitiesData.QUAD);

        // find at same position
        EntitiesSet entities = entityRepository.findEntitiesOfTypeAtVector(Vector2D.create(100, 100), EntityType.UNIT);
        assertThat(entities.hasAny(), is(true));
        assertThat((Unit) entities.getFirst(), is(unit));

        // do not find anything at a bit more upwards
        entities = entityRepository.findEntitiesOfTypeAtVector(Vector2D.create(99, 99), EntityType.UNIT);
        assertThat(entities, is(empty()));

        // find entity at its right-bottom dimension
        entities = entityRepository.findEntitiesOfTypeAtVector(Vector2D.create(131, 131), EntityType.UNIT);
        assertThat(entities.hasAny(), is(true));
        assertThat((Unit) entities.getFirst(), is(unit));

        // do not find anything at a bit more upwards
        entities = entityRepository.findEntitiesOfTypeAtVector(Vector2D.create(132, 132), EntityType.UNIT);
        assertThat(entities, is(empty()));
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

        assertThat(first.getCoordinate(), is(Vector2D.create(10, 11)));
    }

    @Test
    public void placeOnMapPutsStructureOnMap() throws SlickException {
        entityRepository.placeOnMap(Coordinate.create(21, 23), EntityType.STRUCTURE, EntitiesData.CONSTRUCTION_YARD, player);

        EntitiesSet entitiesSet = entityRepository.getEntitiesSet();
        assertThat(entitiesSet.size(), is(1));

        Entity first = entitiesSet.getFirst();

        assertThat(first.getCoordinate(), is(Vector2D.create(21, 23)));
    }

    @Test
    public void placeParticleOnMap() {
        entityRepository.placeOnMap(Coordinate.create(21, 23), EntityType.PARTICLE, EntitiesData.EXPLOSION_SMALL_UNIT, player);

        EntitiesSet entitiesSet = entityRepository.getEntitiesSet();
        assertThat(entitiesSet.size(), is(1));

        Entity first = entitiesSet.getFirst();

        assertThat(first.getCoordinate(), is(Vector2D.create(21, 23)));
    }

    @Test(expected = IllegalArgumentException.class)
    public void creatingEntityRepositoryWithoutEntitiesDataThrowsException() throws SlickException {
        new EntityRepository(map, new Recolorer(), new EntitiesData());
    }

}