package com.fundynamic.d2tm.game.entities.entitiesdata;


import com.fundynamic.d2tm.game.entities.entitiesdata.ini.*;
import org.ini4j.Ini;
import org.ini4j.Profile;
import org.newdawn.slick.SlickException;

import java.io.IOException;
import java.io.InputStream;

/**
 * Reads the 'rules.ini' file and creates a EntitiesData data structure out of it. Used by the
 * {@link com.fundynamic.d2tm.game.entities.EntityRepository} for bringing the entities into life.
 */
public class EntitiesDataReader { // TODO: Rename to INIEntitiesDataReader? (allow other formats?)

    public static final String INI_KEYWORD_ON_PLACEMENT_SPAWN = "OnPlacementSpawn";
    public static final String INI_KEYWORD_HARVEST_SPEED = "HarvestSpeed";
    public static final String INI_KEYWORD_DEPOSIT_SPEED = "DepositSpeed";
    public static final String INI_KEYWORD_HARVEST_CAPACITY = "HarvestCapacity";
    public static final String INI_KEYWORD_HARVESTER = "Harvester";
    public static final String INI_KEYWORD_REFINERY = "Refinery";
    public static final String INI_KEYWORD_IMAGE = "Image";
    public static final String INI_KEYWORD_WIDTH = "Width";
    public static final String INI_KEYWORD_HEIGHT = "Height";
    public static final String INI_KEYWORD_EXPLOSION = "Explosion";
    public static final String INI_KEYWORD_MOVE_SPEED = "MoveSpeed";
    public static final String INI_KEYWORD_TURN_SPEED = "TurnSpeed";
    public static final String INI_KEYWORD_TURN_SPEED_CANNON = "TurnSpeedCannon";
    public static final String INI_KEYWORD_ATTACK_RATE = "AttackRate";
    public static final String INI_KEYWORD_ATTACK_RANGE = "AttackRange";
    public static final String INI_KEYWORD_WEAPON = "Weapon";
    public static final String INI_KEYWORD_DAMAGE = "Damage";
    public static final String INI_KEYWORD_FACINGS = "Facings";
    public static final String INI_KEYWORD_SIGHT = "Sight";
    public static final String INI_KEYWORD_HIT_POINTS = "HitPoints";
    public static final String INI_KEYWORD_BUILD_ICON = "BuildIcon";
    public static final String INI_KEYWORD_BUILDS = "Builds";
    public static final String INI_KEYWORD_BUILD_TIME = "BuildTime";
    public static final String INI_KEYWORD_BUILD_RANGE = "BuildRange";
    public static final String INI_KEYWORD_BUILD_COST = "BuildCost";
    public static final String INI_KEYWORD_BUILD_LIST = "BuildList";
    public static final String INI_KEYWORD_FPS = "Fps";
    public static final String INI_KEYWORD_RECOLOR = "Recolor";
    public static final String INI_KEYWORD_BARREL = "Barrel";
    public static final String INI_KEYWORD_FILE = "File";
    public static final String INI_KEYWORD_SOUND = "Sound";
    public static final String INI_KEYWORD_ASCEND_TO = "AscendTo";
    public static final String INI_KEYWORD_ASCEND_AT = "AscendAt";
    public static final String INI_KEYWORD_DESCEND_AT = "DescendAt";

    public EntitiesData fromRulesIni() {
        return fromResource(getClass().getResourceAsStream("/rules.ini"));
    }

    public EntitiesData fromResource(InputStream inputStream) {
        try {
            if (inputStream == null) throw new IllegalArgumentException("Unable to read from null stream");
            EntitiesData entitiesData = createNewEntitiesData();

            Ini ini = new Ini(inputStream);
            readSounds(entitiesData, ini);
            readWeapons(entitiesData, ini);
            readExplosions(entitiesData, ini);
            readSuperPowers(entitiesData, ini);
            readUnits(entitiesData, ini);
            readStructures(entitiesData, ini);

            return entitiesData;
        } catch (IOException | SlickException e) {
            throw new IllegalStateException("Unable to read rules.ini", e);
        }
    }

    private void readSounds(EntitiesData entitiesData, Ini ini) throws SlickException {
        Profile.Section sounds = ini.get("SOUNDS");
        if (sounds == null) return;
        String[] strings = sounds.childrenNames();
        for (String id : strings) {
            Profile.Section struct = sounds.getChild(id);
            entitiesData.addSound(id, struct.get(INI_KEYWORD_FILE, String.class));
        }
    }

    private void readWeapons(EntitiesData entitiesData, Ini ini) throws SlickException {
        Profile.Section weapons = ini.get("WEAPONS");
        String[] strings = weapons.childrenNames();
        for (String id : strings) {
            Profile.Section struct = weapons.getChild(id);
            entitiesData.addProjectile(id, new IniDataWeapon(struct));
        }
    }

    private void readSuperPowers(EntitiesData entitiesData, Ini ini) throws SlickException {
        Profile.Section superpowers = ini.get("SUPERPOWERS");
        if (superpowers == null) return;
        String[] strings = superpowers.childrenNames();
        for (String id : strings) {
            Profile.Section struct = superpowers.getChild(id);
            entitiesData.addSuperPower(id, new IniDataSuperPower(struct));
        }
    }

    public void readStructures(EntitiesData entitiesData, Ini ini) throws SlickException {
        Profile.Section structures = ini.get("STRUCTURES");
        String[] strings = structures.childrenNames();
        for (String id : strings) {
            Profile.Section struct = structures.getChild(id);
            entitiesData.addStructure(id, new IniDataStructure(struct)
            );
        }
    }

    public void readUnits(EntitiesData entitiesData, Ini ini) throws SlickException {
        Profile.Section units = ini.get("UNITS");
        String[] strings = units.childrenNames();
        for (String id : strings) {
            Profile.Section struct = units.getChild(id);
            entitiesData.addUnit(id, new IniDataUnit(struct)
            );
        }
    }

    public void readExplosions(EntitiesData entitiesData, Ini ini) throws SlickException {
        Profile.Section explosions = ini.get("EXPLOSIONS");
        String[] strings = explosions.childrenNames();
        for (String id : strings) {
            Profile.Section struct = explosions.getChild(id);
            entitiesData.addParticle(id, new IniDataExplosion(struct));
        }
    }

    public EntitiesData createNewEntitiesData() {
        return new EntitiesData();
    }

}
