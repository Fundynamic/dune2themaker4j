package com.fundynamic.d2tm.game.entities.entitiesdata.ini;

import org.ini4j.Profile;

import static com.fundynamic.d2tm.game.entities.entitiesdata.EntitiesDataReader.*;

/**
 * Object representation of a WEAPON entry in the INI file.
 */
public class IniDataWeapon {

    public int widthInPixels;
    public int heightInPixels;
    public String explosionId;
    public float moveSpeed;
    public int damage;
    public int facings;
    public String image;
    public String soundId;

    public IniDataWeapon() {
    }

    public IniDataWeapon(Profile.Section struct) {
        this.image = struct.get(INI_KEYWORD_IMAGE, String.class);
        this.soundId = struct.get(INI_KEYWORD_SOUND, String.class);
        this.widthInPixels = struct.get(INI_KEYWORD_WIDTH, Integer.class);
        this.heightInPixels = struct.get(INI_KEYWORD_HEIGHT, Integer.class);
        this.explosionId = struct.get(INI_KEYWORD_EXPLOSION, String.class);
        this.moveSpeed = struct.get(INI_KEYWORD_MOVE_SPEED, Float.class);
        this.damage = struct.get(INI_KEYWORD_DAMAGE, Integer.class);
        this.facings = struct.get(INI_KEYWORD_FACINGS, Integer.class);
    }
}
