package com.fundynamic.d2tm.game.entities;

import com.fundynamic.d2tm.game.entities.structures.Structure;
import com.fundynamic.d2tm.game.entities.units.Unit;
import com.fundynamic.d2tm.game.map.Map;
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

import static junit.framework.Assert.assertEquals;
import static org.mockito.Mockito.mock;

@RunWith(MockitoJUnitRunner.class)
public class EntityRepositoryTest {

    @Mock
    public Map map;
    private EntityRepository entityRepository;

    @Before
    public void setUp() throws SlickException {
        entityRepository = new EntityRepository(map, new HashMap<String, EntityRepository.EntityData>()) {
            @Override
            protected Image loadImage(String pathToImage) throws SlickException {
                return mock(Image.class);
            }
        };
    }

    @Test
    public void createUnitCreatesUnitData() throws SlickException {
        int widthInPixels = 32;
        int heightInPixels = 32;
        int idOfEntity = 1;
        entityRepository.createUnit(idOfEntity, "quad.png", widthInPixels, heightInPixels);

        EntityRepository.EntityData data = entityRepository.getEntityData(EntityRepository.EntityType.UNIT, idOfEntity);

        assertEquals(EntityRepository.EntityType.UNIT, data.type);
        assertEquals(widthInPixels, data.width);
        assertEquals(heightInPixels, data.height);
    }

    @Test (expected = IllegalArgumentException.class)
    public void createUnitWithDuplicateIdThrowsIllegalArgumentException() throws SlickException {
        int idOfEntity = 1;
        entityRepository.createUnit(idOfEntity, "quad.png", 32, 32); // success!
        entityRepository.createUnit(idOfEntity, "this is irrelevant", 32, 32); // boom!
    }

    @Test (expected = EntityNotFoundException.class)
    public void getEntityDataThrowsEntityNotFoundExceptionWhenAskingForUnknownEntity() {
        entityRepository.getEntityData(EntityRepository.EntityType.UNIT, 0);
    }

    @Test
    public void createStructureCreatesStructureData() throws SlickException {
        int widthInPixels = 64;
        int heightInPixels = 64;
        int idOfEntity = 1;
        entityRepository.createStructure(idOfEntity, "constyard.png", widthInPixels, heightInPixels);

        EntityRepository.EntityData data = entityRepository.getEntityData(EntityRepository.EntityType.STRUCTURE, idOfEntity);

        assertEquals(EntityRepository.EntityType.STRUCTURE, data.type);
        assertEquals(widthInPixels, data.width);
        assertEquals(heightInPixels, data.height);
    }

    @Test (expected = EntityNotFoundException.class)
    public void placeOnMapThrowsEntityNotFoundExceptionWhenAskingForUnknownEntity() {
        entityRepository.placeOnMap(Vector2D.zero(), EntityRepository.EntityType.UNIT, 0);
    }

    @Test
    public void placeOnMapPutsUnitOnMap() throws SlickException {
        entityRepository.createUnit(0, "quad.png", 32, 32);

        entityRepository.placeOnMap(Vector2D.create(10, 11), EntityRepository.EntityType.UNIT, 0);

        ArgumentCaptor<Unit> argument = ArgumentCaptor.forClass(Unit.class);
        Mockito.verify(map).placeUnit(argument.capture());
        Unit unitToPlace = argument.getValue();

        assertEquals(Vector2D.create(10, 11), unitToPlace.getMapCoordinates());
        assertEquals(1, entityRepository.getEntities().size());
    }

    @Test
    public void placeOnMapPutsStructureOnMap() throws SlickException {
        entityRepository.createStructure(0, "constyard.png", 32, 32);

        entityRepository.placeOnMap(Vector2D.create(21, 23), EntityRepository.EntityType.STRUCTURE, 0);

        ArgumentCaptor<Structure> argument = ArgumentCaptor.forClass(Structure.class);
        Mockito.verify(map).placeStructure(argument.capture());
        Structure structureToPlace = argument.getValue();

        assertEquals(Vector2D.create(21, 23), structureToPlace.getMapCoordinates());
        assertEquals(1, entityRepository.getEntities().size());
    }

}