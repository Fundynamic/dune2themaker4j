package com.fundynamic.d2tm.game.entities.entitiesdata;


import com.fundynamic.d2tm.Game;
import com.fundynamic.d2tm.game.AbstractD2TMTest;
import com.fundynamic.d2tm.game.entities.EntityData;
import com.fundynamic.d2tm.game.entities.EntityNotFoundException;
import com.fundynamic.d2tm.game.entities.EntityType;
import com.fundynamic.d2tm.game.entities.entitiesdata.ini.IniDataStructure;
import com.fundynamic.d2tm.game.entities.entitiesdata.ini.IniDataUnit;
import com.fundynamic.d2tm.game.entities.entitybuilders.EntityBuilderType;
import org.junit.Before;
import org.junit.Test;
import org.newdawn.slick.SlickException;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
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

        IniDataUnit iniDataUnit = new IniDataUnit();
        iniDataUnit.explosionId = explosionId;
        iniDataUnit.pathToImage = "quad.png";
        iniDataUnit.pathToBarrelImage = "barrel.png";
        iniDataUnit.buildIcon = "buildicon.png";
        iniDataUnit.width = widthInPixels;
        iniDataUnit.height = heightInPixels;
        iniDataUnit.sight = sight;
        iniDataUnit.animationSpeed = animationSpeed;
        iniDataUnit.moveSpeed = moveSpeed;
        iniDataUnit.turnSpeed = turnSpeed;
        iniDataUnit.turnSpeedCannon = turnSpeedCannon;
        iniDataUnit.attackRange = attackRange;
        iniDataUnit.attackRate = attackRate;
        iniDataUnit.weaponId = weaponId;
        iniDataUnit.explosionId = explosionId;
        iniDataUnit.hitpoints = hitPoints;
        iniDataUnit.buildTimeInSeconds = 1.0F;

        entitiesData.addUnit(idOfEntity, iniDataUnit);

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
        assertEquals(1.0F, data.buildTimeInSeconds, 0.1F);
        assertNotNull(data.buildIcon);
    }

    @Test (expected = IllegalArgumentException.class)
    public void createUnitWithDuplicateIdThrowsIllegalArgumentException() throws SlickException {
        String idOfEntity = "1";
        entitiesData.addUnit(idOfEntity, new IniDataUnit()); // success!
        entitiesData.addUnit(idOfEntity, new IniDataUnit()); // boom!
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
        String entityBuilderType = "";
        String idOfEntity = "1";
        int sight = 3;
        String explosionId = "UNKNOWN";



        IniDataStructure iniDataStructure = new IniDataStructure();

        iniDataStructure.image = "constyard.png";
        iniDataStructure.width = widthInPixels;
        iniDataStructure.height = heightInPixels;
        iniDataStructure.sight = sight;
        iniDataStructure.hitpoints = 1000;
        iniDataStructure.explosion = explosionId;
        iniDataStructure.buildIcon = "icon_constyard.bmp";
        iniDataStructure.entityBuilderType = entityBuilderType;
        iniDataStructure.buildTimeInSeconds = 1.0F;
        iniDataStructure.buildList = "WINDTRAP,REFINERY";

        // add
        entitiesData.addStructure(
                idOfEntity,
                iniDataStructure
        );

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
        assertEquals(EntityBuilderType.NONE, data.entityBuilderType);
        assertEquals(1.0F, data.buildTimeInSeconds, 0.1F);
        assertEquals("WINDTRAP,REFINERY", data.buildList);
        assertNotNull(data.buildIcon);
    }

    @Test (expected = IllegalArgumentException.class)
    public void createStructureWithDuplicateIdThrowsIllegalArgumentException() throws SlickException {
        String idOfEntity = "1";
        entitiesData.addStructure(idOfEntity, new IniDataStructure()); // success!
        entitiesData.addStructure(idOfEntity, new IniDataStructure()); // boom!
    }


}
