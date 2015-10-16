package com.fundynamic.d2tm.game.entities;

import com.fundynamic.d2tm.game.AbstractD2TMTest;
import com.fundynamic.d2tm.game.entities.units.Unit;
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

    public static final int UNIT_FIRST_ID = 0;

    @Test
    public void findsUnitAtVector() throws SlickException {
        Unit unit = makeUnit(player, 100, Vector2D.create(100, 100));

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

    @Test (expected = EntityNotFoundException.class)
    public void placeOnMapThrowsEntityNotFoundExceptionWhenAskingForUnknownEntity() {
        entityRepository.placeOnMap(Vector2D.zero(), EntityType.UNIT, 93232, player);
    }

    // TODO: This test name is now a bit weird, and we really need to clean up our tests suite, for proper setup etc.
    // TODO: See placeOnMapPutsStructureOnMap
    @Test
    public void placeOnMapPutsUnitOnMap() throws SlickException {
//        entitiesData.createUnit(0, "quad.png", 32, 32, 2, 1.0F, 200, 0, 1);

        entityRepository.placeOnMap(Vector2D.create(10, 11), EntityType.UNIT, 0, player);

        EntitiesSet entitiesSet = entityRepository.getEntitiesSet();
        assertThat(entitiesSet.size(), is(1));

        Entity first = entitiesSet.getFirst();

        assertThat(first.getAbsoluteCoordinates(), is(Vector2D.create(10, 11)));
    }

    // TODO: This test name is now a bit weird, and we really need to clean up our tests suite, for proper setup etc.
    @Test
    public void placeOnMapPutsStructureOnMap() throws SlickException {
//        entitiesData.createStructure(0, "constyard.png", 32, 32, 2, 1000, 1);

        entityRepository.placeOnMap(Vector2D.create(21, 23), EntityType.STRUCTURE, 0, player);

        EntitiesSet entitiesSet = entityRepository.getEntitiesSet();
        assertThat(entitiesSet.size(), is(1));

        Entity first = entitiesSet.getFirst();

        assertThat(first.getAbsoluteCoordinates(), is(Vector2D.create(21, 23)));
    }

//    public static Unit createUnit(EntityRepository entityRepository, Vector2D mapCoordinates, Player player) throws SlickException {
//        return (Unit) entityRepository.placeOnMap(mapCoordinates, EntityType.UNIT, EntityRepository.QUAD, player);
//    }
//

}