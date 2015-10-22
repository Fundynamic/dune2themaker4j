package com.fundynamic.d2tm.game.entities;


import com.fundynamic.d2tm.game.AbstractD2TMTest;
import org.junit.Before;
import org.junit.Test;
import org.newdawn.slick.SlickException;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

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
        String idOfEntity = "1";
        int sight = 2;
        float moveSpeed = 1.0F;
        float turnSpeed = 2.0F;
        String weaponId = "UNKNOWN";
        String explosionId = "UNKNOWN";
        entitiesData.addUnit(idOfEntity, "quad.png", widthInPixels, heightInPixels, sight, moveSpeed, turnSpeed, hitPoints, weaponId, explosionId);

        EntityData data = entitiesData.getEntityData(EntityType.UNIT, idOfEntity);

        assertEquals(EntityType.UNIT, data.type);
        assertEquals(widthInPixels, data.width);
        assertEquals(heightInPixels, data.height);
        assertEquals(sight, data.sight);
        assertThat(moveSpeed, is(data.moveSpeed));
        assertThat(turnSpeed, is(data.turnSpeed));
        assertEquals(hitPoints, data.hitPoints);
        assertEquals(explosionId, data.explosionId);
        assertEquals(weaponId, data.weaponId);
    }

    @Test (expected = IllegalArgumentException.class)
    public void createUnitWithDuplicateIdThrowsIllegalArgumentException() throws SlickException {
        String idOfEntity = "1";
        entitiesData.addUnit(idOfEntity, "quad.png", 32, 32, 2, 1.0F, 1.0F, 100, "0", "1"); // success!
        entitiesData.addUnit(idOfEntity, "this is irrelevant", 32, 32, 3, 1.0F, 1.0F, 100, "0", "1"); // boom!
    }

    @Test (expected = EntityNotFoundException.class)
    public void getEntityDataThrowsEntityNotFoundExceptionWhenAskingForUnknownEntity() {
        entitiesData.getEntityData(EntityType.UNIT, "0");
    }

    @Test
    public void createStructureCreatesStructureData() throws SlickException {
        int widthInPixels = 64;
        int heightInPixels = 64;
        int hitPoints = 1000;
        String idOfEntity = "1";
        int sight = 3;
        String explosionId = "UNKNOWN";
        entitiesData.addStructure(idOfEntity, "constyard.png", widthInPixels, heightInPixels, sight, 1000, explosionId);

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
        String idOfEntity = "1";
        entitiesData.addStructure(idOfEntity, "constyard.png", 32, 32, 2, 1000, "1"); // success!
        entitiesData.addStructure(idOfEntity, "this is irrelevant", 32, 32, 3, 1000, "1"); // boom!
    }


}
