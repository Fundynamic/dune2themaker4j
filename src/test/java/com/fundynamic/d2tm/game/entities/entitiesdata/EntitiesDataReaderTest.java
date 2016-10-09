package com.fundynamic.d2tm.game.entities.entitiesdata;

import com.fundynamic.d2tm.Game;
import com.fundynamic.d2tm.game.entities.EntityData;
import com.fundynamic.d2tm.game.entities.EntityType;
import com.fundynamic.d2tm.game.entities.entitybuilders.EntityBuilderType;
import org.junit.Before;
import org.junit.Test;
import org.newdawn.slick.SlickException;

import static com.fundynamic.d2tm.game.AbstractD2TMTest.makeEntitiesDataReader;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.nullValue;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;


public class EntitiesDataReaderTest {

    private EntitiesData entitiesData;
    private EntitiesDataReader entitiesDataReader;

    @Before
    public void setUp() throws SlickException {
        entitiesDataReader = makeEntitiesDataReader();
    }

    public void readFromTestRulesIni() {
        entitiesData = entitiesDataReader.fromResource(getClass().getResourceAsStream("/test-rules.ini"));
    }

    @Test
    public void readsBuildingStructureFromIniFile() {
        readFromTestRulesIni();

        EntityData constyard = entitiesData.getEntityData(EntityType.STRUCTURE, EntitiesData.CONSTRUCTION_YARD);
        assertThat(constyard, is(not(nullValue())));
        assertThat(constyard.hitPoints, is(230));
        assertThat(constyard.image, is(not(nullValue())));
        assertThat(constyard.getWidth(), is(64));
        assertThat(constyard.getHeight(), is(64));
        assertThat(constyard.sight, is(4));
        assertThat(constyard.explosionId, is("BOOM"));
        assertThat(constyard.buildIcon, is(not(nullValue())));
        assertThat(constyard.entityBuilderType, is(EntityBuilderType.STRUCTURES));
        assertThat(constyard.buildCost, is(1000));

        // 1 extra tile range is added by the EntitiesData class (while it is '2' in the test-rules.ini!)
        // therefor we do times 3!
        float value = ((Game.TILE_SIZE) * 3) + Game.HALF_TILE; // we can do half-tile because it is a 64x64 structure
        assertThat(constyard.buildRange, is(value)); // calculated by entitiesData class
    }

    @Test
    public void readsBuildingStructureFromIniFileThatConstructsUnits() {
        readFromTestRulesIni();

        EntityData lightfctry = entitiesData.getEntityData(EntityType.STRUCTURE, EntitiesData.LIGHT_FACTORY);
        assertThat(lightfctry.entityBuilderType, is(EntityBuilderType.UNITS));
    }

    @Test
    public void readsSimpleStructureFromIniFile() {
        readFromTestRulesIni();

        EntityData constyard = entitiesData.getEntityData(EntityType.STRUCTURE, EntitiesData.WINDTRAP);
        assertThat(constyard, is(not(nullValue())));
        assertThat(constyard.hitPoints, is(283));
        assertThat(constyard.image, is(not(nullValue())));
        assertThat(constyard.getWidth(), is(64));
        assertThat(constyard.getHeight(), is(64));
        assertThat(constyard.sight, is(4));
        assertThat(constyard.explosionId, is("BOOM"));
        assertThat(constyard.buildIcon, is(not(nullValue())));
        assertThat(constyard.entityBuilderType, is(EntityBuilderType.NONE));
        assertThat(constyard.buildTimeInSeconds, is(5.0f));
        assertThat(constyard.buildCost, is(250));
    }

    @Test
    public void readsUnitFromIniFile() {
        readFromTestRulesIni();

        EntityData quad = entitiesData.getEntityData(EntityType.UNIT, "QUAD");
        assertThat(quad, is(not(nullValue())));
        assertThat(quad.image, is(not(nullValue())));
        assertThat(quad.hitPoints, is(434));
        assertThat(quad.moveSpeed, is(1.5F));
        assertThat(quad.turnSpeed, is(0.75F));
        assertThat(quad.attackRate, is(2.3F));
        assertThat(quad.attackRange, is(96F));
        assertThat(quad.getWidth(), is(32));
        assertThat(quad.getHeight(), is(32));
        assertThat(quad.sight, is(7));
        assertThat(quad.explosionId, is("BOOM"));
        assertThat(quad.weaponId, is("RIFLE"));
        assertThat(quad.buildTimeInSeconds, is(7.0f));
        assertThat(quad.buildCost, is(200));
    }

    @Test
    public void readsWeaponsFromIniFile() {
        readFromTestRulesIni();

        EntityData rifle = entitiesData.getEntityData(EntityType.PROJECTILE, "RIFLE");
        assertThat(rifle, is(not(nullValue())));
        assertThat(rifle.image, is(not(nullValue())));
        assertThat(rifle.getWidth(), is(6));
        assertThat(rifle.getHeight(), is(6));
        assertThat(rifle.explosionId, is("BOOM"));
        assertThat(rifle.moveSpeed, is(160f));
        assertThat(rifle.damage, is(28));
    }

    @Test
    public void readsExplosionFromIniFile() {
        readFromTestRulesIni();

        EntityData boom = entitiesData.getEntityData(EntityType.PARTICLE, "BOOM");
        assertThat(boom, is(not(nullValue())));
        assertThat(boom.image, is(not(nullValue())));
        assertThat(boom.getWidth(), is(48));
        assertThat(boom.getHeight(), is(48));
    }

    @Test
    public void throwsExceptionWhenWeaponIdForUnitDoesNotExist() {
        try {
            entitiesData = entitiesDataReader.fromResource(getClass().getResourceAsStream("/test-rules-with-wrong-weaponid-unit.ini"));
            fail("Expected to have thrown an illegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertThat(e.getMessage(), is("unit QUAD [weapon] refers to non-existing [WEAPONS/THIS_ID_DOES_NOT_EXIST]"));
        }
    }

    @Test
    public void acceptsNoExplosionId() {
        entitiesData = entitiesDataReader.fromResource(getClass().getResourceAsStream("/test-rules-with-empty-weaponid-unit.ini"));
    }

    @Test
    public void throwsExceptionWhenExplosionIdForUnitDoesNotExist() {
        try {
            entitiesData = entitiesDataReader.fromResource(getClass().getResourceAsStream("/test-rules-with-wrong-explosionid-unit.ini"));
            fail("Expected to have thrown an illegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertThat(e.getMessage(), is("unit QUAD [explosion] refers to non-existing [EXPLOSIONS/THIS_EXPLOSION_DOES_NOT_EXIST]"));
        }
    }

    @Test
    public void throwsExceptionWhenExplosionIdForStructureDoesNotExist() {
        try {
            entitiesData = entitiesDataReader.fromResource(getClass().getResourceAsStream("/test-rules-with-wrong-explosionid-structure.ini"));
            fail("Expected to have thrown an illegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertThat(e.getMessage(), is("structure CONSTYARD [explosion] refers to non-existing [EXPLOSIONS/THIS_EXPLOSION_DOES_NOT_EXIST]"));
        }
    }

}