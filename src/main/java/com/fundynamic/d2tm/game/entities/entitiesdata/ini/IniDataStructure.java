package com.fundynamic.d2tm.game.entities.entitiesdata.ini;

import com.fundynamic.d2tm.game.entities.entitiesdata.EntitiesData;
import com.fundynamic.d2tm.game.entities.entitybuilders.EntityBuilderType;
import com.fundynamic.d2tm.utils.StringUtils;
import org.ini4j.Profile;

import static com.fundynamic.d2tm.game.entities.entitiesdata.EntitiesDataReader.*;

/**
 * Object representation of a STRUCTURE entry in the INI file.
 */
public class IniDataStructure {

    public String image;
    public int width;
    public int height;
    public int sight;
    public int hitpoints;
    public int buildRangeInTiles;
    public String explosion;
    public String buildIcon;
    public String entityBuilderType;
    public float buildTimeInSeconds;

    public String buildList;

    public IniDataStructure() {
    }

    public IniDataStructure(Profile.Section struct) {
        this.image = struct.get(INI_KEYWORD_IMAGE, String.class, null);
        this.width = struct.get(INI_KEYWORD_WIDTH, Integer.class);
        this.height = struct.get(INI_KEYWORD_HEIGHT, Integer.class);
        this.sight = struct.get(INI_KEYWORD_SIGHT, Integer.class);
        this.hitpoints = struct.get(INI_KEYWORD_HIT_POINTS, Integer.class);
        this.explosion = struct.get(INI_KEYWORD_EXPLOSION, String.class, EntitiesData.UNKNOWN);
        this.buildIcon = struct.get(INI_KEYWORD_BUILD_ICON, String.class, null);
        this.entityBuilderType = struct.get(INI_KEYWORD_BUILDS, String.class, "");
        this.buildTimeInSeconds = struct.get(INI_KEYWORD_BUILD_TIME, Float.class, 0F);
        this.buildList = struct.get(INI_KEYWORD_BUILD_LIST, String.class, "");
        this.buildRangeInTiles = struct.get(INI_KEYWORD_BUILD_RANGE, Integer.class, 0);
    }

    public EntityBuilderType getEntityBuilderType() {
        try {
            if (StringUtils.isEmpty(entityBuilderType)) {
                return EntityBuilderType.NONE;
            }
            return EntityBuilderType.valueOf(entityBuilderType);
        } catch (Exception e) {
            System.err.println("Unable to convert 'builds' property with value [" + entityBuilderType + "] into enum EntityBuilderType. Valid values are " + EntityBuilderType.getValues());
            return EntityBuilderType.NONE;
        }
    }
}
