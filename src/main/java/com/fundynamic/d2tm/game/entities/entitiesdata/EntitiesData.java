package com.fundynamic.d2tm.game.entities.entitiesdata;


import com.fundynamic.d2tm.game.entities.entitiesdata.ini.IniDataStructure;
import com.fundynamic.d2tm.game.entities.entitybuilders.EntityBuilderType;
import com.fundynamic.d2tm.game.entities.EntityData;
import com.fundynamic.d2tm.game.entities.EntityNotFoundException;
import com.fundynamic.d2tm.game.entities.EntityType;
import com.fundynamic.d2tm.utils.StringUtils;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

import java.util.HashMap;

public class EntitiesData {

    public static final String UNKNOWN = "UNKNOWN";

    // units

    /**
     * ID of TRIKE unit
     */
    public static String TRIKE = "TRIKE";

    /**
     * ID of QUAD unit
     */
    public static String QUAD = "QUAD";

    /**
     * ID of TANK unit
     */
    public static String TANK = "TANK";

    public static String SOLDIER = "SOLDIER";
    public static String INFANTRY = "INFANTRY";

    // structures
    public static String CONSTRUCTION_YARD = "CONSTYARD";
    public static String REFINERY = "REFINERY";
    public static String WINDTRAP = "WINDTRAP";

    // projectiles
    public static String BULLET = "RIFLE";

    /**
     * ID of large rocket
     */
    public static final String LARGE_ROCKET = "LARGE_ROCKET";

    // explosions
    public static String EXPLOSION_SMALL_UNIT = "WHEELED";

    private HashMap<String, EntityData> entitiesData;

    public EntitiesData() {
        entitiesData = new HashMap<>();
    }

    public void addProjectile(String id, String pathToImage, int widthInPixels, int heightInPixels, String explosionId, float moveSpeed, int damage, int facings) throws SlickException {
        EntityData entity = createEntity(id, pathToImage, null, widthInPixels, heightInPixels, EntityType.PROJECTILE, -1, moveSpeed, -1);
        entity.damage = damage;
        entity.explosionId = explosionId;
        entity.setFacingsAndCalculateChops(facings);
    }

    /**
     * Create and add particle to this collection.
     *
     * @param id
     * @param pathToImage
     * @param widthInPixels
     * @param heightInPixels
     * @param framesPerSecond
     * @throws SlickException
     */
    public void addParticle(String id, String pathToImage, int widthInPixels, int heightInPixels, float framesPerSecond, boolean recolor) throws SlickException {
        EntityData entity = createEntity(id, pathToImage, null, widthInPixels, heightInPixels, EntityType.PARTICLE, -1, -1, -1);
        entity.animationSpeed = framesPerSecond;
        entity.recolor = recolor;
    }

    /**
     * Create and add a Structure EntityData to this collection.
     *
     * @throws SlickException
     */
    public EntityData addStructure(IniDataStructure iniDataStructure) throws SlickException {
        EntityData entityData = createEntity(
                iniDataStructure.id,
                iniDataStructure.image,
                null,
                iniDataStructure.width,
                iniDataStructure.height,
                EntityType.STRUCTURE,
                iniDataStructure.sight,
                0F,
                iniDataStructure.hitpoints
        );

        entityData.entityBuilderType = iniDataStructure.getEntityBuilderType();
        entityData.buildTimeInSeconds = iniDataStructure.buildTimeInSeconds;

        if (!idProvided(iniDataStructure.explosion)) {
            if (!tryGetEntityData(EntityType.PARTICLE, iniDataStructure.explosion)) {
                throw new IllegalArgumentException("structure " + iniDataStructure.id + " [explosion] refers to non-existing [EXPLOSIONS/" + iniDataStructure.explosion + "]");
            }
            entityData.explosionId = iniDataStructure.explosion;
        }
        entityData.buildIcon = loadImage(iniDataStructure.buildIcon);

        return entityData;
    }

    public void addUnit(String id, String pathToImage, String pathToBarrelImage, int widthInPixels, int heightInPixels, int sight, float animationSpeed, float moveSpeed, float turnSpeed, float turnSpeedCannon, float attackRate, float attackRange, int hitPoints, String weaponId, String explosionId) throws SlickException {
        EntityData entity = createEntity(id, pathToImage, pathToBarrelImage, widthInPixels, heightInPixels, EntityType.UNIT, sight, moveSpeed, hitPoints);

        entity.attackRate = attackRate;
        entity.attackRange = attackRange;
        entity.turnSpeed = turnSpeed;
        entity.turnSpeedCannon = turnSpeedCannon;
        entity.animationSpeed = animationSpeed;

        if (!idProvided(weaponId)) {
            if (!tryGetEntityData(EntityType.PROJECTILE, weaponId)) {
                throw new IllegalArgumentException("unit " + id + " [weapon] refers to non-existing [WEAPONS/" + weaponId + "]");
            }
            entity.weaponId = weaponId;
        }

        if (!idProvided(explosionId)) {
            if (!tryGetEntityData(EntityType.PARTICLE, explosionId)) {
                throw new IllegalArgumentException("unit " + id + " [explosion] refers to non-existing [EXPLOSIONS/" + explosionId + "]");
            }
            entity.explosionId = explosionId;
        }
    }

    public boolean idProvided(String weaponId) {
        return UNKNOWN.equals(weaponId);
    }

    private EntityData createEntity(String id, String pathToImage, String pathToBarrelImage, int widthInPixels, int heightInPixels, EntityType entityType, int sight, float moveSpeed, int hitPoints) throws SlickException {
        if (tryGetEntityData(entityType, id)) {
            throw new IllegalArgumentException("Entity of type " + entityType + " already exists with id " + id + ". Known entities are:\n" + entitiesData);
        }
        EntityData entityData = new EntityData();
        entityData.image = loadImage(pathToImage);
        entityData.barrelImage = loadImage(pathToBarrelImage);
        entityData.setWidth(widthInPixels);
        entityData.setHeight(heightInPixels);
        entityData.type = entityType;
        entityData.sight = sight;
        entityData.moveSpeed = moveSpeed;
        entityData.hitPoints = hitPoints;
        entityData.key = EntityData.constructKey(entityType, id);
        entitiesData.put(entityData.key, entityData);
        return entityData;
    }

    protected Image loadImage(String pathToImage) throws SlickException {
        if (StringUtils.isEmpty(pathToImage)) {
            return null;
        }
        return createSlickImage(pathToImage);
    }

    protected Image createSlickImage(String pathToImage) throws SlickException {
        return new Image(pathToImage);
    }

    public EntityData getParticle(String id) {
        return getEntityData(EntityType.PARTICLE, id);
    }

    public EntityData getEntityData(EntityType entityType, String id) {
        return getEntityData(EntityData.constructKey(entityType, id));
    }

    public EntityData getEntityData(String key) {
        EntityData entityData = entitiesData.get(key);
        if (entityData == null) throw new EntityNotFoundException("Entity not found for key " + key + ". Known entities are:\n" + toString());
        return entityData;
    }

    private boolean tryGetEntityData(EntityType entityType, String id) {
        try {
            getEntityData(entityType, id);
            return true;
        } catch (EntityNotFoundException e) {
//            System.err.println(e);
            return false;
        }
    }

    public void clear() {
        this.entitiesData.clear();
    }

    public boolean isEmpty() {
        return this.entitiesData.isEmpty();
    }

    @Override
    public String toString() {
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.
                append("--- EntitiesData ---\n");
        for (String key : entitiesData.keySet()) {
            stringBuffer.append(key + "=" + entitiesData.get(key) + "\n");
        }
        stringBuffer.append("--- EntitiesData ---\n");
        return stringBuffer.toString();
    }
}
