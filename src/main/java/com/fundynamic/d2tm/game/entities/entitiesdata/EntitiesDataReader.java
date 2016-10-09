package com.fundynamic.d2tm.game.entities.entitiesdata;


import com.fundynamic.d2tm.game.entities.entitiesdata.ini.IniDataStructure;
import com.fundynamic.d2tm.game.entities.entitiesdata.ini.IniDataUnit;
import org.ini4j.Ini;
import org.ini4j.Profile;
import org.newdawn.slick.SlickException;

import java.io.IOException;
import java.io.InputStream;

/**
 * Reads the 'rules.ini' file and extracts an EntitiesData data structure.
 */
public class EntitiesDataReader {

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

    public EntitiesData fromRulesIni() {
        return fromResource(getClass().getResourceAsStream("/rules.ini"));
    }

    public EntitiesData fromResource(InputStream inputStream) {
        try {
            if (inputStream == null) throw new IllegalArgumentException("Unable to read from null stream");
            EntitiesData entitiesData = createNewEntitiesData();

            Ini ini = new Ini(inputStream);
            readWeapons(entitiesData, ini);
            readExplosions(entitiesData, ini);
            readStructures(entitiesData, ini);
            readUnits(entitiesData, ini);

            return entitiesData;
        } catch (IOException | SlickException e) {
            throw new IllegalStateException("Unable to read rules.ini", e);
        }
    }

    private void readWeapons(EntitiesData entitiesData, Ini ini) throws SlickException {
        Profile.Section weapons = ini.get("WEAPONS");
        String[] strings = weapons.childrenNames();
        for (String id : strings) {
            Profile.Section struct = weapons.getChild(id);
            entitiesData.addProjectile(id,
                    struct.get(INI_KEYWORD_IMAGE, String.class),
                    struct.get(INI_KEYWORD_WIDTH, Integer.class),
                    struct.get(INI_KEYWORD_HEIGHT, Integer.class),
                    struct.get(INI_KEYWORD_EXPLOSION, String.class),
                    struct.get(INI_KEYWORD_MOVE_SPEED, Float.class),
                    struct.get(INI_KEYWORD_DAMAGE, Integer.class),
                    struct.get(INI_KEYWORD_FACINGS, Integer.class));
        }

    }

    public void readStructures(EntitiesData entitiesData, Ini ini) throws SlickException {
        Profile.Section structures = ini.get("STRUCTURES");
        String[] strings = structures.childrenNames();
        for (String id : strings) {
            Profile.Section struct = structures.getChild(id);
            entitiesData.addStructure(
                    id,
                    new IniDataStructure(struct)
            );
        }
    }

    public void readUnits(EntitiesData entitiesData, Ini ini) throws SlickException {
        Profile.Section units = ini.get("UNITS");
        String[] strings = units.childrenNames();
        for (String id : strings) {
            Profile.Section struct = units.getChild(id);
            entitiesData.addUnit(
                    id,
                    new IniDataUnit(struct)
            );
        }
    }

    public void readExplosions(EntitiesData entitiesData, Ini ini) throws SlickException {
        Profile.Section explosions = ini.get("EXPLOSIONS");
        String[] strings = explosions.childrenNames();
        for (String id : strings) {
            Profile.Section struct = explosions.getChild(id);
            entitiesData.addParticle(id,
                    struct.get(INI_KEYWORD_IMAGE, String.class),
                    struct.get(INI_KEYWORD_WIDTH, Integer.class),
                    struct.get(INI_KEYWORD_HEIGHT, Integer.class),
                    struct.get(INI_KEYWORD_FPS, Float.class),
                    struct.get(INI_KEYWORD_RECOLOR, Boolean.class, false));
        }
    }

    public EntitiesData createNewEntitiesData() {
        return new EntitiesData();
    }

}
