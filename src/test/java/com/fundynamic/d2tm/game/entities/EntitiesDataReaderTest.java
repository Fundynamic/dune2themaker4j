package com.fundynamic.d2tm.game.entities;

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
    public void readsStructureFromIniFile() {
        readFromTestRulesIni();
        EntityData constyard = entitiesData.getEntityData(EntityType.STRUCTURE, "CONSTYARD");
        assertThat(constyard, is(not(nullValue())));
        assertThat(constyard.hitPoints, is(230));
        assertThat(constyard.image, is(not(nullValue())));
        assertThat(constyard.width, is(64));
        assertThat(constyard.height, is(64));
        assertThat(constyard.sight, is(4));
        assertThat(constyard.explosionId, is("BOOM"));
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
        assertThat(quad.width, is(32));
        assertThat(quad.height, is(32));
        assertThat(quad.sight, is(7));
        assertThat(quad.explosionId, is("BOOM"));
        assertThat(quad.weaponId, is("RIFLE"));
    }

    @Test
    public void readsWeaponsFromIniFile() {
        readFromTestRulesIni();
        EntityData rifle = entitiesData.getEntityData(EntityType.PROJECTILE, "RIFLE");
        assertThat(rifle, is(not(nullValue())));
        assertThat(rifle.image, is(not(nullValue())));
        assertThat(rifle.width, is(6));
        assertThat(rifle.height, is(6));
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
        assertThat(boom.width, is(48));
        assertThat(boom.height, is(48));
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