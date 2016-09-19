package com.fundynamic.d2tm.game.entities.entitiesdata.ini;

import com.fundynamic.d2tm.game.entities.entitybuilders.EntityBuilderType;
import com.fundynamic.d2tm.utils.StringUtils;

/**
 * Object representation of a STRUCTURE entry in the INI file.
 */
public class IniDataStructure {

    public IniDataStructure(String id, String image, int width, int height, int sight, int hitpoints, String explosion, String buildIcon, String entityBuilderType, float buildTimeInSeconds, String buildList) {
        this.id = id;
        this.image = image;
        this.width = width;
        this.height = height;
        this.sight = sight;
        this.hitpoints = hitpoints;
        this.explosion = explosion;
        this.buildIcon = buildIcon;
        this.entityBuilderType = entityBuilderType;
        this.buildTimeInSeconds = buildTimeInSeconds;
        this.buildList = buildList;
    }

    public String id;
    public String image;
    public int width;
    public int height;
    public int sight;
    public int hitpoints;
    public String explosion;
    public String buildIcon;
    public String entityBuilderType;
    public float buildTimeInSeconds;
    public String buildList;

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
