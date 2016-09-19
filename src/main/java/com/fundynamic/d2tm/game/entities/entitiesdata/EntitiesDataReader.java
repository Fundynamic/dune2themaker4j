package com.fundynamic.d2tm.game.entities.entitiesdata;


import com.fundynamic.d2tm.game.entities.entitiesdata.ini.IniDataStructure;
import org.ini4j.Ini;
import org.ini4j.Profile;
import org.newdawn.slick.SlickException;

import java.io.IOException;
import java.io.InputStream;

/**
 * Reads the 'rules.ini' file and extracts an EntitiesData data structure.
 */
public class EntitiesDataReader {

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
                    struct.get("image", String.class),
                    struct.get("width", Integer.class),
                    struct.get("height", Integer.class),
                    struct.get("explosion", String.class),
                    struct.get("movespeed", Float.class),
                    struct.get("damage", Integer.class),
                    struct.get("facings", Integer.class));
        }

    }

    public void readStructures(EntitiesData entitiesData, Ini ini) throws SlickException {
        Profile.Section structures = ini.get("STRUCTURES");
        String[] strings = structures.childrenNames();
        for (String id : strings) {
            Profile.Section struct = structures.getChild(id);
            entitiesData.addStructure(
                    new IniDataStructure(
                        id,
                        struct.get("image", String.class, null),
                        struct.get("width", Integer.class),
                        struct.get("height", Integer.class),
                        struct.get("sight", Integer.class),
                        struct.get("hitpoints", Integer.class),
                        struct.get("explosion", String.class, EntitiesData.UNKNOWN),
                        struct.get("buildIcon", String.class, null),
                        struct.get("builds", String.class, ""),
                        struct.get("buildtime", Float.class, 0F)
                    )
            );
        }
    }

    public void readUnits(EntitiesData entitiesData, Ini ini) throws SlickException {
        Profile.Section units = ini.get("UNITS");
        String[] strings = units.childrenNames();
        for (String id : strings) {
            Profile.Section struct = units.getChild(id);
            entitiesData.addUnit(id,
                    struct.get("image", String.class, "no-image-provided"),
                    struct.get("barrel", String.class, null),
                    struct.get("width", Integer.class, 1),
                    struct.get("height", Integer.class, 1),
                    struct.get("sight", Integer.class, 1),
                    struct.get("fps", Float.class, 0f),
                    struct.get("movespeed", Float.class, 0f),
                    struct.get("turnspeed", Float.class, 0f),
                    struct.get("turnspeedcannon", Float.class, 0f),
                    struct.get("attackrate", Float.class, 0f),
                    struct.get("attackrange", Float.class, 0f),
                    struct.get("hitpoints", Integer.class, 0),
                    struct.get("weapon", String.class, EntitiesData.UNKNOWN),
                    struct.get("explosion", String.class));
        }
    }

    public void readExplosions(EntitiesData entitiesData, Ini ini) throws SlickException {
        Profile.Section explosions = ini.get("EXPLOSIONS");
        String[] strings = explosions.childrenNames();
        for (String id : strings) {
            Profile.Section struct = explosions.getChild(id);
            entitiesData.addParticle(id,
                    struct.get("image", String.class),
                    struct.get("width", Integer.class),
                    struct.get("height", Integer.class),
                    struct.get("fps", Float.class),
                    struct.get("recolor", Boolean.class, false));
        }
    }

    public EntitiesData createNewEntitiesData() {
        return new EntitiesData();
    }

}
