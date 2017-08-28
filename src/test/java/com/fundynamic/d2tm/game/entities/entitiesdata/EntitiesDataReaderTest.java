package com.fundynamic.d2tm.game.entities.entitiesdata;

import com.fundynamic.d2tm.Game;
import com.fundynamic.d2tm.game.entities.EntityType;
import com.fundynamic.d2tm.game.entities.entitybuilders.EntityBuilderType;
import com.fundynamic.d2tm.game.types.EntityData;
import com.fundynamic.d2tm.game.types.SoundData;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.newdawn.slick.SlickException;

import java.util.List;

import static com.fundynamic.d2tm.game.AbstractD2TMTest.makeEntitiesDataReader;
import static com.fundynamic.d2tm.game.map.Cell.HALF_TILE;
import static com.fundynamic.d2tm.game.map.Cell.TILE_SIZE;
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
        if (Game.RECORDING_VIDEO) {
            Assert.fail("You cannot run this test with RECORDING_VIDEO to true, because it messes up with build times.");
        }
    }

    public void readFromTestRulesIni() {
        entitiesData = entitiesDataReader.fromResource(getClass().getResourceAsStream("/test-rules.ini"));
    }

    @Test
    public void readsSoundsFromIniFile() {
        readFromTestRulesIni();

        List<SoundData> sounds = entitiesData.getSounds();

        Assert.assertEquals(sounds.size(), 1);

        SoundData soundData = sounds.get(0);
        Assert.assertNotNull(soundData.sound);
    }

    @Test
    public void readsConstYardFromIniFile() {
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
        assertThat(constyard.isRefinery, is(false));

        // 1 extra tile range is added by the EntitiesData class (while it is '2' in the test-rules.ini!)
        // therefor we do times 3!
        float value = ((TILE_SIZE) * 3) + HALF_TILE; // we can do half-tile because it is a 64x64 structure
        assertThat(constyard.buildRange, is(value)); // calculated by entitiesData class
    }

    @Test
    public void readsRefineryFromIniFile() {
        readFromTestRulesIni();

        EntityData refinery = entitiesData.getEntityData(EntityType.STRUCTURE, EntitiesData.REFINERY);
        assertThat(refinery, is(not(nullValue())));
        assertThat(refinery.hitPoints, is(1500));
        assertThat(refinery.image, is(not(nullValue())));
        assertThat(refinery.getWidth(), is(96));
        assertThat(refinery.getHeight(), is(64));
        assertThat(refinery.sight, is(5));
        assertThat(refinery.explosionId, is("BOOM"));
        assertThat(refinery.buildIcon, is(not(nullValue())));
        assertThat(refinery.entityBuilderType, is(EntityBuilderType.NONE));
        assertThat(refinery.buildCost, is(1000));
        assertThat(refinery.isRefinery, is(true));
        assertThat(refinery.onPlacementSpawnUnitId, is("QUAD"));
        assertThat(refinery.powerProduction, is(0));
        assertThat(refinery.powerConsumption, is(10));
    }

    @Test
    public void readsBuildingStructureFromIniFileThatConstructsUnits() {
        readFromTestRulesIni();

        EntityData lightfctry = entitiesData.getEntityData(EntityType.STRUCTURE, EntitiesData.LIGHT_FACTORY);
        assertThat(lightfctry.entityBuilderType, is(EntityBuilderType.UNITS));
    }

    @Test
    public void readsWindtrapFromIniFile() {
        readFromTestRulesIni();

        EntityData windtrap = entitiesData.getEntityData(EntityType.STRUCTURE, EntitiesData.WINDTRAP);
        assertThat(windtrap, is(not(nullValue())));
        assertThat(windtrap.hitPoints, is(283));
        assertThat(windtrap.image, is(not(nullValue())));
        assertThat(windtrap.type, is(EntityType.STRUCTURE));
        assertThat(windtrap.getWidth(), is(64));
        assertThat(windtrap.getHeight(), is(64));
        assertThat(windtrap.sight, is(4));
        assertThat(windtrap.explosionId, is("BOOM"));
        assertThat(windtrap.buildIcon, is(not(nullValue())));
        assertThat(windtrap.entityBuilderType, is(EntityBuilderType.NONE));
        assertThat(windtrap.buildTimeInSeconds, is(5.0f));
        assertThat(windtrap.buildCost, is(250));
        assertThat(windtrap.powerProduction, is(200));
        assertThat(windtrap.powerConsumption, is(0));
    }

    @Test
    public void readsSuperPowerFromIniFile() {
        readFromTestRulesIni();

        EntityData deathhand = entitiesData.getEntityData(EntityType.SUPERPOWER, EntitiesData.DEATHHAND);

        assertThat(deathhand, is(not(nullValue())));
        assertThat(deathhand.type, is(EntityType.SUPERPOWER));
        assertThat(deathhand.buildIcon, is(not(nullValue())));
        assertThat(deathhand.entityBuilderType, is(EntityBuilderType.NONE));
        assertThat(deathhand.buildTimeInSeconds, is(5.0f));
        assertThat(deathhand.buildCost, is(0));
        assertThat(deathhand.weaponId, is("RIFLE"));
        assertThat(deathhand.hasSound(), is(false));
        assertThat(deathhand.hasExplosionId(), is(true));
        assertThat(deathhand.explosionId, is("BOOM"));
    }

    @Test
    public void readsUnitFromIniFile() {
        readFromTestRulesIni();

        EntityData quad = entitiesData.getEntityData(EntityType.UNIT, EntitiesData.QUAD);
        assertThat(quad, is(not(nullValue())));
        assertThat(quad.image, is(not(nullValue())));
        assertThat(quad.type, is(EntityType.UNIT));
        assertThat(quad.hitPoints, is(434));
        assertThat(quad.moveSpeed, is(32.0F));
        assertThat(quad.turnSpeed, is(10.0F));
        assertThat(quad.attackRate, is(2.3F));
        assertThat(quad.attackRange, is(96F));
        assertThat(quad.getWidth(), is(32));
        assertThat(quad.getHeight(), is(32));
        assertThat(quad.sight, is(7));
        assertThat(quad.explosionId, is("BOOM"));
        assertThat(quad.weaponId, is("RIFLE"));
        assertThat(quad.buildTimeInSeconds, is(7.0f));
        assertThat(quad.buildCost, is(200));
        assertThat(quad.isHarvester, is(false));
        assertThat(quad.harvestCapacity, is(0));
        assertThat(quad.depositSpeed, is(0F));
    }

    @Test
    public void readsHarvesterFromIniFile() {
        readFromTestRulesIni();

        EntityData harvester = entitiesData.getEntityData(EntityType.UNIT, EntitiesData.HARVESTER);
        assertThat(harvester, is(not(nullValue())));
        assertThat(harvester.image, is(not(nullValue())));
        assertThat(harvester.type, is(EntityType.UNIT));
        assertThat(harvester.hitPoints, is(75));
        assertThat(harvester.moveSpeed, is(32.0F));
        assertThat(harvester.turnSpeed, is(10.0F));
        assertThat(harvester.getWidth(), is(80));
        assertThat(harvester.getHeight(), is(52));
        assertThat(harvester.sight, is(4));
        assertThat(harvester.explosionId, is("BOOM"));
        assertThat(harvester.buildCost, is(50));
        assertThat(harvester.isHarvester, is(true));
        assertThat(harvester.harvestCapacity, is(800));
        assertThat(harvester.depositSpeed, is(15F)); // deposit total capacity in 15 seconds
        assertThat(harvester.getDepositSpeed(), is(53.333333333F)); // 800 / 15
        assertThat(harvester.harvestSpeed, is(30F)); // harvester should harvest to full in 30 seconds (given it would be done on one cell)
        assertThat(harvester.getHarvestSpeed(), is(26.666666667F)); // 800 / 30
    }

    @Test
    public void readsWeaponsFromIniFile() {
        readFromTestRulesIni();

        EntityData rifle = entitiesData.getEntityData(EntityType.PROJECTILE, "RIFLE");
        assertThat(rifle, is(not(nullValue())));
        assertThat(rifle.image, is(not(nullValue())));
        assertThat(rifle.type, is(EntityType.PROJECTILE));
        assertThat(rifle.getWidth(), is(6));
        assertThat(rifle.getHeight(), is(6));
        assertThat(rifle.hasExplosionId(), is(true));
        assertThat(rifle.explosionId, is("BOOM"));
        assertThat(rifle.moveSpeed, is(160f));
        assertThat(rifle.damage, is(28));
        assertThat(rifle.hasSound(), is(true));
        assertThat(rifle.maxAscensionHeight, is(83));
        assertThat(rifle.maxAscensionAtFlightPercentage, is(0.23F));
        assertThat(rifle.startToDescendPercentage, is(0.87F));
    }

    @Test
    public void readsExplosionFromIniFile() {
        readFromTestRulesIni();

        EntityData boom = entitiesData.getEntityData(EntityType.PARTICLE, "BOOM");
        assertThat(boom, is(not(nullValue())));
        assertThat(boom.type, is(EntityType.PARTICLE));
        assertThat(boom.image, is(not(nullValue())));
        assertThat(boom.getWidth(), is(48));
        assertThat(boom.getHeight(), is(48));
        assertThat(boom.hasSound(), is(true));
    }

    @Test
    public void throwsExceptionWhenWeaponIdForUnitDoesNotExist() {
        try {
            entitiesData = entitiesDataReader.fromResource(getClass().getResourceAsStream("/test-rules-with-wrong-weaponid-unit.ini"));
            fail("Expected to have thrown an illegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertThat(e.getMessage(), is("entity QUAD property [Weapon] refers to non-existing [WEAPONS/THIS_ID_DOES_NOT_EXIST]"));
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