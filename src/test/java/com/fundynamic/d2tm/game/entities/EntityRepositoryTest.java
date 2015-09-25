package com.fundynamic.d2tm.game.entities;

import com.fundynamic.d2tm.game.entities.structures.Structure;
import com.fundynamic.d2tm.game.entities.units.Unit;
import com.fundynamic.d2tm.game.map.Map;
import com.fundynamic.d2tm.game.rendering.Recolorer;
import com.fundynamic.d2tm.math.Vector2D;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

import java.util.HashMap;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class EntityRepositoryTest {

    @Mock
    public Map map;

    @Mock
    public Player player;

    private EntityRepository entityRepository;

    @Before
    public void setUp() throws SlickException {
        entityRepository = makeEmptyTestableEntityRepository(map);
    }

    @Test
    public void createUnitCreatesUnitData() throws SlickException {
        int widthInPixels = 32;
        int heightInPixels = 32;
        int hitPoints = 150;
        int idOfEntity = 1;
        int sight = 2;
        float moveSpeed = 1.0F;
        entityRepository.createUnit(idOfEntity, "quad.png", widthInPixels, heightInPixels, sight, moveSpeed, hitPoints);

        EntityData data = entityRepository.getEntityData(EntityType.UNIT, idOfEntity);

        assertEquals(EntityType.UNIT, data.type);
        assertEquals(widthInPixels, data.width);
        assertEquals(heightInPixels, data.height);
        assertEquals(sight, data.sight);
        assertEquals(hitPoints, data.hitPoints);
    }

    @Test (expected = IllegalArgumentException.class)
    public void createUnitWithDuplicateIdThrowsIllegalArgumentException() throws SlickException {
        int idOfEntity = 1;
        entityRepository.createUnit(idOfEntity, "quad.png", 32, 32, 2, 1.0F, 100); // success!
        entityRepository.createUnit(idOfEntity, "this is irrelevant", 32, 32, 3, 1.0F, 100); // boom!
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
        entityRepository.createStructure(idOfEntity, "constyard.png", widthInPixels, heightInPixels, sight, 1000);

        EntityData data = entityRepository.getEntityData(EntityType.STRUCTURE, idOfEntity);

        assertEquals(EntityType.STRUCTURE, data.type);
        assertEquals(widthInPixels, data.width);
        assertEquals(heightInPixels, data.height);
        assertEquals(sight, data.sight);
        assertEquals(hitPoints, data.hitPoints);
    }


    @Test (expected = IllegalArgumentException.class)
    public void createStructureWithDuplicateIdThrowsIllegalArgumentException() throws SlickException {
        int idOfEntity = 1;
        entityRepository.createStructure(idOfEntity, "constyard.png", 32, 32, 2, 1000); // success!
        entityRepository.createStructure(idOfEntity, "this is irrelevant", 32, 32, 3, 1000); // boom!
    }

    @Test (expected = EntityNotFoundException.class)
    public void placeOnMapThrowsEntityNotFoundExceptionWhenAskingForUnknownEntity() {
        entityRepository.placeOnMap(Vector2D.zero(), EntityType.UNIT, 0, player);
    }

    @Test
    public void placeOnMapPutsUnitOnMap() throws SlickException {
        entityRepository.createUnit(0, "quad.png", 32, 32, 2, 1.0F, 200);

        entityRepository.placeOnMap(Vector2D.create(10, 11), EntityType.UNIT, 0, player);

        ArgumentCaptor<Unit> argument = ArgumentCaptor.forClass(Unit.class);
        Mockito.verify(map).placeUnit(argument.capture());
        Unit unitToPlace = argument.getValue();

        assertEquals(Vector2D.create(10, 11), unitToPlace.getMapCoordinates());
        assertEquals(1, entityRepository.getEntitiesSet().size());
    }

    @Test
    public void placeOnMapPutsStructureOnMap() throws SlickException {
        entityRepository.createStructure(0, "constyard.png", 32, 32, 2, 1000);

        entityRepository.placeOnMap(Vector2D.create(21, 23), EntityType.STRUCTURE, 0, player);

        ArgumentCaptor<Structure> argument = ArgumentCaptor.forClass(Structure.class);
        Mockito.verify(map).placeStructure(argument.capture());
        Structure structureToPlace = argument.getValue();

        assertEquals(Vector2D.create(21, 23), structureToPlace.getMapCoordinates());
        assertEquals(1, entityRepository.getEntitiesSet().size());
    }

    public static Unit createUnit(EntityRepository entityRepository, Vector2D mapCoordinates, Player player) throws SlickException {
        return (Unit) entityRepository.placeOnMap(mapCoordinates, EntityType.UNIT, 1, player);
    }

    public static EntityRepository makeTestableEntityRepositoryWithMockedMap() throws SlickException {
        return makeTestableEntityRepository(mock(Map.class));
    }

    public static EntityRepository makeTestableEntityRepository(Map map) throws SlickException {
        TestableEntityRepository entityRepository = makeEmptyTestableEntityRepository(map);

        entityRepository.createStructure(0, "constyard.png", 32, 32, 2, 1000);
        entityRepository.createUnit(1, "quad.png", 32, 32, 2, 1.0F, 100);

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