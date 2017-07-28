package com.fundynamic.d2tm.game.entities.entitiesdata.ini;

import com.fundynamic.d2tm.game.entities.entitiesdata.EntitiesData;
import org.ini4j.Profile;

import static com.fundynamic.d2tm.game.entities.entitiesdata.EntitiesDataReader.*;

/**
 * Object representation of an EXPLOSION entry in the INI file.
 */
public class IniDataExplosion {

    public int widthInPixels;
    public int heightInPixels;
    public String image;
    public String soundId;

    public float framesPerSecond;
    public boolean recolor;

    public IniDataExplosion() {
    }

    public IniDataExplosion(Profile.Section struct) {
        this.image = struct.get(INI_KEYWORD_IMAGE, String.class);
        this.soundId = struct.get(INI_KEYWORD_SOUND, String.class, EntitiesData.UNKNOWN);
        this.widthInPixels = struct.get(INI_KEYWORD_WIDTH, Integer.class);
        this.heightInPixels = struct.get(INI_KEYWORD_HEIGHT, Integer.class);
        this.framesPerSecond = struct.get(INI_KEYWORD_FPS, Float.class);
        this.recolor = struct.get(INI_KEYWORD_RECOLOR, Boolean.class, false);
    }
}
