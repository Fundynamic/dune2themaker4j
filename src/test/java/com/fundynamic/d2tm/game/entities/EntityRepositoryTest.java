package com.fundynamic.d2tm.game.entities;

import com.fundynamic.d2tm.game.AbstractD2TMTest;
import com.fundynamic.d2tm.game.entities.units.Unit;
import com.fundynamic.d2tm.game.map.Map;
import com.fundynamic.d2tm.game.rendering.Recolorer;
import com.fundynamic.d2tm.math.Vector2D;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

import java.util.HashMap;

import static org.hamcrest.collection.IsEmptyCollection.empty;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class EntityRepositoryTest extends AbstractD2TMTest {

    public static final int UNIT_FIRST_ID = 0;

    @Before
    public void setUp() throws SlickException {
        super.setUp();
        // remove all data because the tests assume an empty state
        entityRepository.removeAllEntityData();
    }

    @Test
    public void createUnitCreatesUnitData() throws SlickException {
        int widthInPixels = 32;
        int heightInPixels = 32;
        int hitPoints = 150;
        int idOfEntity = 1;
        int sight = 2;
        float moveSpeed = 1.0F;
        int weaponId = 214;
        int explosionId = 23;
        entityRepository.createUnit(idOfEntity, "quad.png", widthInPixels, heightInPixels, sight, moveSpeed, hitPoints, weaponId, explosionId);

        EntityData data = entityRepository.getEntityData(EntityType.UNIT, idOfEntity);

        assertEquals(EntityType.UNIT, data.type);
        assertEquals(widthInPixels, data.width);
        assertEquals(heightInPixels, data.height);
        assertEquals(sight, data.sight);
        assertEquals(hitPoints, data.hitPoints);
        assertEquals(explosionId, data.explosionId);
        assertEquals(weaponId, data.weaponId);
    }

    @Test (expected = IllegalArgumentException.class)
    public void createUnitWithDuplicateIdThrowsIllegalArgumentException() throws SlickException {
        int idOfEntity = 1;
        entityRepository.createUnit(idOfEntity, "quad.png", 32, 32, 2, 1.0F, 100, 0, 1); // success!
        entityRepository.createUnit(idOfEntity, "this is irrelevant", 32, 32, 3, 1.0F, 100, 0, 1); // boom!
    }

    @Test (expected = EntityNotFoundException.class)
    public void getEntityDataThrowsEntityNotFoundExceptionWhenAskingForUnknownEntity() {
        entityRepository.getEntityData(EntityType.UNIT, 0);
    }

    @Test
    public void createStructureCreatesStructureData() throws SlickException {
        int widthInPixels = 64;
        int heightInPixels = 64;
        int hitPoints = 1000;
        int idOfEntity = 1;
        int sight = 3;
        int explosionId = 24;
        entityRepository.createStructure(idOfEntity, "constyard.png", widthInPixels, heightInPixels, sight, 1000, explosionId);

        EntityData data = entityRepository.getEntityData(EntityType.STRUCTURE, idOfEntity);

        assertEquals(EntityType.STRUCTURE, data.type);
        assertEquals(widthInPixels, data.width);
        assertEquals(heightInPixels, data.height);
        assertEquals(sight, data.sight);
        assertEquals(hitPoints, data.hitPoints);
        assertEquals(explosionId, data.explosionId);
    }

    @Test
    public void findsUnitAtVector() throws SlickException {
        Map map = makeMap(64, 64);
        entityRepository = makeTestableEntityRepository(map);

        Unit unit = createUnit(entityRepository, Vector2D.create(100, 100), player);

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
    public void createStructureWithDuplicateIdThrowsIllegalArgumentException() throws SlickException {
        int idOfEntity = 1;
        entityRepository.createStructure(idOfEntity, "constyard.png", 32, 32, 2, 1000, 1); // success!
        entityRepository.createStructure(idOfEntity, "this is irrelevant", 32, 32, 3, 1000, 1); // boom!
    }

    @Test (expected = EntityNotFoundException.class)
    public void placeOnMapThrowsEntityNotFoundExceptionWhenAskingForUnknownEntity() {
        entityRepository.placeOnMap(Vector2D.zero(), EntityType.UNIT, 0, player);
    }

    // TODO: This test name is now a bit weird, and we really need to clean up our tests suite, for proper setup etc.
    // TODO: See placeOnMapPutsStructureOnMap
    @Test
    public void placeOnMapPutsUnitOnMap() throws SlickException {
        entityRepository.createUnit(0, "quad.png", 32, 32, 2, 1.0F, 200, 0, 1);

        entityRepository.placeOnMap(Vector2D.create(10, 11), EntityType.UNIT, 0, player);

        EntitiesSet entitiesSet = entityRepository.getEntitiesSet();
        assertThat(entitiesSet.size(), is(1));

        Entity first = entitiesSet.getFirst();

        assertThat(first.getAbsoluteMapCoordinates(), is(Vector2D.create(10, 11)));
    }

    // TODO: This test name is now a bit weird, and we really need to clean up our tests suite, for proper setup etc.
    @Test
    public void placeOnMapPutsStructureOnMap() throws SlickException {
        entityRepository.createStructure(0, "constyard.png", 32, 32, 2, 1000, 1);

        entityRepository.placeOnMap(Vector2D.create(21, 23), EntityType.STRUCTURE, 0, player);

        EntitiesSet entitiesSet = entityRepository.getEntitiesSet();
        assertThat(entitiesSet.size(), is(1));

        Entity first = entitiesSet.getFirst();

        assertThat(first.getAbsoluteMapCoordinates(), is(Vector2D.create(21, 23)));
    }

    public static Unit createUnit(EntityRepository entityRepository, Vector2D mapCoordinates, Player player) throws SlickException {
        return (Unit) entityRepository.placeOnMap(mapCoordinates, EntityType.UNIT, EntityRepository.QUAD, player);
    }

    public static EntityRepository makeTestableEntityRepositoryWithMockedMap() throws SlickException {
        return makeTestableEntityRepository(mock(Map.class));
    }

    public static EntityRepository makeTestableEntityRepository(Map map) throws SlickException {
        TestableEntityRepository entityRepository = makeEmptyTestableEntityRepository(map);
        entityRepository.init();
        return entityRepository;
    }

    public static TestableEntityRepository makeEmptyTestableEntityRepository(final Map map) throws SlickException {
        Image image = mock(Image.class);
        Recolorer recolorer = mock(Recolorer.class);
        when(recolorer.recolorToFactionColor(any(Image.class), any(Recolorer.FactionColor.class))).thenReturn(image);
        return new TestableEntityRepository(map, recolorer, new HashMap<String, EntityData>()) {
            @Override
            protected Image loadImage(String pathToImage) throws SlickException {
                return mock(Image.class);
            }
        };
    }

}