package com.fundynamic.d2tm.game.entities.entitiesdata.ini;

import com.fundynamic.d2tm.Game;
import com.fundynamic.d2tm.game.entities.entitiesdata.EntitiesData;
import org.ini4j.Profile;

import static com.fundynamic.d2tm.game.entities.entitiesdata.EntitiesDataReader.*;

/**
 * Object representation of a UNIT entry in the INI file.
 */
public class IniDataUnit {


    public String pathToImage;
    public String pathToBarrelImage;
    public int width;
    public int height;
    public float animationSpeed;

    // movement of unit and its (optional) barrel/gun
    public float moveSpeed;
    public float turnSpeed;
    public float turnSpeedCannon;

    // what weapon to fire, how far it can reach and how often it fires
    public String weaponId;
    public float attackRate;
    public float attackRange;

    // sight and hitPoints
    public int sight;
    public int hitPoints;

    // Explosion to render when dead
    public String explosionId;

    // related to building this unit
    public String buildIcon;
    public int buildCost;
    public float buildTimeInSeconds;

    // harvester related properties
    public boolean harvester;
    public int harvestCapacity;
    public float depositSpeed;
    public float harvestSpeed;

    public IniDataUnit() {
    }

    public IniDataUnit(Profile.Section struct) {
        this.harvestSpeed = struct.get(INI_KEYWORD_HARVEST_SPEED, Float.class, 0f);
        this.depositSpeed = struct.get(INI_KEYWORD_DEPOSIT_SPEED, Float.class, 0f);
        this.harvestCapacity = struct.get(INI_KEYWORD_HARVEST_CAPACITY, Integer.class, 0);
        this.harvester = struct.get(INI_KEYWORD_HARVESTER, Boolean.class, false);
        this.pathToImage = struct.get(INI_KEYWORD_IMAGE, String.class, "no-image-provided");
        this.pathToBarrelImage = struct.get(INI_KEYWORD_BARREL, String.class, null);
        this.width = struct.get(INI_KEYWORD_WIDTH, Integer.class, 1);
        this.height = struct.get(INI_KEYWORD_HEIGHT, Integer.class, 1);
        this.sight = struct.get(INI_KEYWORD_SIGHT, Integer.class, 1);
        this.animationSpeed = struct.get(INI_KEYWORD_FPS, Float.class, 0f);
        this.moveSpeed = struct.get(INI_KEYWORD_MOVE_SPEED, Float.class, 0f);
        this.turnSpeed = struct.get(INI_KEYWORD_TURN_SPEED, Float.class, 0f);
        this.turnSpeedCannon = struct.get(INI_KEYWORD_TURN_SPEED_CANNON, Float.class, 0f);
        this.attackRate = struct.get(INI_KEYWORD_ATTACK_RATE, Float.class, 0f);
        this.attackRange = struct.get(INI_KEYWORD_ATTACK_RANGE, Float.class, 0f);
        this.hitPoints = struct.get(INI_KEYWORD_HIT_POINTS, Integer.class, 0);
        this.weaponId = struct.get(INI_KEYWORD_WEAPON, String.class, EntitiesData.UNKNOWN);
        this.explosionId = struct.get(INI_KEYWORD_EXPLOSION, String.class);
        this.buildIcon = struct.get(INI_KEYWORD_BUILD_ICON, String.class, null);
        this.buildTimeInSeconds = struct.get(INI_KEYWORD_BUILD_TIME, Float.class, 0F);
        this.buildCost = struct.get(INI_KEYWORD_BUILD_COST, Integer.class, 0);

        if (Game.RECORDING_VIDEO) {
            this.buildTimeInSeconds *= 0.1f;
            this.moveSpeed *= 2;
            this.turnSpeed *= 2;
            this.turnSpeedCannon *= 2;
        }
    }

}
