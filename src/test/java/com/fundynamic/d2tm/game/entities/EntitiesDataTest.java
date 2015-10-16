package com.fundynamic.d2tm.game.entities;


import com.fundynamic.d2tm.game.AbstractD2TMTest;
import org.junit.Before;
import org.junit.Test;
import org.newdawn.slick.SlickException;

import static org.junit.Assert.assertEquals;

public class EntitiesDataTest extends AbstractD2TMTest {

    @Before
    public void setUp() throws SlickException {
        super.setUp();
        entitiesData.clear();
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
        entitiesData.createUnit(idOfEntity, "quad.png", widthInPixels, heightInPixels, sight, moveSpeed, hitPoints, weaponId, explosionId);

        EntityData data = entitiesData.getEntityData(EntityType.UNIT, idOfEntity);

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
        entitiesData.createUnit(idOfEntity, "quad.png", 32, 32, 2, 1.0F, 100, 0, 1); // success!
        entitiesData.createUnit(idOfEntity, "this is irrelevant", 32, 32, 3, 1.0F, 100, 0, 1); // boom!
    }

    @Test (expected = EntityNotFoundException.class)
    public void getEntityDataThrowsEntityNotFoundExceptionWhenAskingForUnknownEntity() {
        entitiesData.getEntityData(EntityType.UNIT, 0);
    }

    @Test
    public void createStructureCreatesStructureData() throws SlickException {
        int widthInPixels = 64;
        int heightInPixels = 64;
        int hitPoints = 1000;
        int idOfEntity = 1;
        int sight = 3;
        int explosionId = 24;
        entitiesData.createStructure(idOfEntity, "constyard.png", widthInPixels, heightInPixels, sight, 1000, explosionId);

        EntityData data = entitiesData.getEntityData(EntityType.STRUCTURE, idOfEntity);

        assertEquals(EntityType.STRUCTURE, data.type);
        assertEquals(widthInPixels, data.width);
        assertEquals(heightInPixels, data.height);
        assertEquals(sight, data.sight);
        assertEquals(hitPoints, data.hitPoints);
        assertEquals(explosionId, data.explosionId);
    }

    @Test (expected = IllegalArgumentException.class)
    public void createStructureWithDuplicateIdThrowsIllegalArgumentException() throws SlickException {
        int idOfEntity = 1;
        entitiesData.createStructure(idOfEntity, "constyard.png", 32, 32, 2, 1000, 1); // success!
        entitiesData.createStructure(idOfEntity, "this is irrelevant", 32, 32, 3, 1000, 1); // boom!
    }


}
