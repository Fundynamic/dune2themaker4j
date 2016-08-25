package com.fundynamic.d2tm.game.entities;


import com.fundynamic.d2tm.Game;
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
        int widthInCells = widthInPixels / Game.TILE_SIZE;
        int heightInCells = heightInPixels / Game.TILE_SIZE;
        int hitPoints = 150;
        String idOfEntity = "1";
        int sight = 2;
        float animationSpeed = 0.4F;
        float moveSpeed = 1.0F;
        float turnSpeed = 2.0F;
        float turnSpeedCannon = 2.1F;
        float attackRate = 2.2F;
        float attackRange = 82F;
        String weaponId = "UNKNOWN";
        String explosionId = "UNKNOWN";
        entitiesData.addUnit(idOfEntity, "quad.png", "barrel.png", widthInPixels, heightInPixels, sight, animationSpeed, moveSpeed, turnSpeed, turnSpeedCannon, attackRate, attackRange, hitPoints, weaponId, explosionId);

        EntityData data = entitiesData.getEntityData(EntityType.UNIT, idOfEntity);

        assertEquals(EntityType.UNIT, data.type);
        assertEquals(widthInPixels, data.getWidth());
        assertEquals(heightInPixels, data.getHeight());
        assertEquals(widthInCells, data.getWidthInCells());
        assertEquals(heightInCells, data.getHeightInCells());
        assertEquals(sight, data.sight);
        assertThat(animationSpeed, is(data.animationSpeed));
        assertThat(moveSpeed, is(data.moveSpeed));
        assertThat(turnSpeed, is(data.turnSpeed));
        assertThat(turnSpeedCannon, is(data.turnSpeedCannon));
        assertThat(attackRate, is(data.attackRate));
        assertEquals(hitPoints, data.hitPoints);
        assertEquals(explosionId, data.explosionId);
        assertEquals(weaponId, data.weaponId);
    }

    @Test (expected = IllegalArgumentException.class)
    public void createUnitWithDuplicateIdThrowsIllegalArgumentException() throws SlickException {
        String idOfEntity = "1";
        entitiesData.addUnit(idOfEntity, "quad.png", "barrel.png", 32, 32, 2, 0.4F, 1.0F, 1.0F, 1.2F, 1.1F, 2.2F, 100, "0", "1"); // success!
        entitiesData.addUnit(idOfEntity, "this is irrelevant", "barrel.png", 32, 32, 3, 0.4F, 1.0F, 1.0F, 1.2F, 1.1F, 3.2F, 100, "0", "1"); // boom!
    }

    @Test (expected = EntityNotFoundException.class)
    public void getEntityDataThrowsEntityNotFoundExceptionWhenAskingForUnknownEntity() {
        entitiesData.getEntityData(EntityType.UNIT, "0");
    }

    @Test
    public void createStructureCreatesStructureData() throws SlickException {
        int widthInPixels = 64;
        int heightInPixels = 64;
        int widthInCells = widthInPixels / Game.TILE_SIZE;
        int heightInCells = heightInPixels / Game.TILE_SIZE;
        int hitPoints = 1000;
        String idOfEntity = "1";
        int sight = 3;
        String explosionId = "UNKNOWN";

        // add
        entitiesData.addStructure(idOfEntity, "constyard.png", widthInPixels, heightInPixels, sight, 1000, explosionId, "icon_constyard.bmp");

        // get & assert
        EntityData data = entitiesData.getEntityData(EntityType.STRUCTURE, idOfEntity);

        assertEquals(EntityType.STRUCTURE, data.type);
        assertEquals(widthInPixels, data.getWidth());
        assertEquals(heightInPixels, data.getHeight());
        assertEquals(widthInCells, data.getWidthInCells());
        assertEquals(heightInCells, data.getHeightInCells());
        assertEquals(sight, data.sight);
        assertEquals(hitPoints, data.hitPoints);
        assertEquals(explosionId, data.explosionId);
    }

    @Test (expected = IllegalArgumentException.class)
    public void createStructureWithDuplicateIdThrowsIllegalArgumentException() throws SlickException {
        String idOfEntity = "1";
        entitiesData.addStructure(idOfEntity, "constyard.png", 32, 32, 2, 1000, "1", "icon_constyard.bmp"); // success!
        entitiesData.addStructure(idOfEntity, "this is irrelevant", 32, 32, 3, 1000, "1", "icon_constyard.bmp"); // boom!
    }


}
