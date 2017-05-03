package com.fundynamic.d2tm.game.entities.entitiesdata.ini;

import com.fundynamic.d2tm.Game;
import org.ini4j.Profile;

import static com.fundynamic.d2tm.game.entities.entitiesdata.EntitiesDataReader.INI_KEYWORD_BUILD_COST;
import static com.fundynamic.d2tm.game.entities.entitiesdata.EntitiesDataReader.INI_KEYWORD_BUILD_ICON;
import static com.fundynamic.d2tm.game.entities.entitiesdata.EntitiesDataReader.INI_KEYWORD_BUILD_TIME;


public class IniDataSuperPower {

    public String buildIcon;
    public float buildTimeInSeconds;
    public int buildCost;

    public IniDataSuperPower(Profile.Section struct) {
        this.buildIcon = struct.get(INI_KEYWORD_BUILD_ICON, String.class, null);
        this.buildCost = struct.get(INI_KEYWORD_BUILD_COST, Integer.class, 0);
        if (Game.RECORDING_VIDEO) {
            this.buildTimeInSeconds = 1;
        } else {
            this.buildTimeInSeconds = struct.get(INI_KEYWORD_BUILD_TIME, Float.class, 0F);
        }
    }

}
