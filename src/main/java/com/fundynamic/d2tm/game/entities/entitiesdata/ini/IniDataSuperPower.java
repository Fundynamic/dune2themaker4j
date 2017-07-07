package com.fundynamic.d2tm.game.entities.entitiesdata.ini;

import com.fundynamic.d2tm.Game;
import org.ini4j.Profile;

import static com.fundynamic.d2tm.game.entities.entitiesdata.EntitiesDataReader.*;


public class IniDataSuperPower {

    public String image;
    public int width;
    public int height;
    public String buildIcon;
    public float buildTimeInSeconds;
    public int buildCost;
    public int facings;

    public IniDataSuperPower(Profile.Section struct) {
        this.buildIcon = struct.get(INI_KEYWORD_BUILD_ICON, String.class, null);
        this.buildCost = struct.get(INI_KEYWORD_BUILD_COST, Integer.class, 0);
        this.width = struct.get(INI_KEYWORD_WIDTH, Integer.class);
        this.height = struct.get(INI_KEYWORD_HEIGHT, Integer.class);
        this.image = struct.get(INI_KEYWORD_IMAGE, String.class, null);
        this.facings = struct.get(INI_KEYWORD_FACINGS, Integer.class, 0);

        if (Game.RECORDING_VIDEO) {
            this.buildTimeInSeconds = 1;
        } else {
            this.buildTimeInSeconds = struct.get(INI_KEYWORD_BUILD_TIME, Float.class, 0F);
        }
    }

}
