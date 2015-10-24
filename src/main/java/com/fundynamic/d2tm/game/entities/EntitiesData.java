package com.fundynamic.d2tm.game.entities;


import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

import java.util.HashMap;

public class EntitiesData {

    public static final String UNKNOWN = "UNKNOWN";

    // units
    public static String TRIKE = "TRIKE";
    public static String QUAD = "QUAD";

    // structures
    public static String CONSTRUCTION_YARD = "CONSTYARD";
    public static String REFINERY = "REFINERY";

    // projectiles
    public static String BULLET = "RIFLE";

    // explosions
    public static String EXPLOSION_SMALL_UNIT = "WHEELED";

    private HashMap<String, EntityData> entitiesData;

    public EntitiesData() {
        entitiesData = new HashMap<>();
    }

    public void addProjectile(String id, String pathToImage, int widthInPixels, int heightInPixels, String explosionId, float moveSpeed, int damage, int facings) throws SlickException {
        EntityData entity = createEntity(id, pathToImage, widthInPixels, heightInPixels, EntityType.PROJECTILE, -1, moveSpeed, -1);
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
    public void addParticle(String id, String pathToImage, int widthInPixels, int heightInPixels, float framesPerSecond) throws SlickException {
        EntityData entity = createEntity(id, pathToImage, widthInPixels, heightInPixels, EntityType.PARTICLE, -1, -1, -1);
        entity.animationSpeed = framesPerSecond;
    }

    /**
     * Create and add structure to this collection.
     *
     * @param id
     * @param pathToImage
     * @param widthInPixels
     * @param heightInPixels
     * @param sight
     * @param hitPoints
     * @param explosionId
     * @throws SlickException
     */
    public void addStructure(String id, String pathToImage, int widthInPixels, int heightInPixels, int sight, int hitPoints, String explosionId) throws SlickException {
        EntityData entity = createEntity(id, pathToImage, widthInPixels, heightInPixels, EntityType.STRUCTURE, sight, 0F, hitPoints);

        if (!idProvided(explosionId)) {
            if (!tryGetEntityData(EntityType.PARTICLE, explosionId)) {
                throw new IllegalArgumentException("structure " + id + " [explosion] refers to non-existing [EXPLOSIONS/" + explosionId + "]");
            }
            entity.explosionId = explosionId;
        }
    }

    public void addUnit(String id, String pathToImage, int widthInPixels, int heightInPixels, int sight, float moveSpeed, float turnSpeed, float attackRate, float attackRange, int hitPoints, String weaponId, String explosionId) throws SlickException {
        EntityData entity = createEntity(id, pathToImage, widthInPixels, heightInPixels, EntityType.UNIT, sight, moveSpeed, hitPoints);

        entity.attackRate = attackRate;
        entity.attackRange = attackRange;
        entity.turnSpeed = turnSpeed;
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

    private EntityData createEntity(String id, String pathToImage, int widthInPixels, int heightInPixels, EntityType entityType, int sight, float moveSpeed, int hitPoints) throws SlickException {
        if (tryGetEntityData(entityType, id)) {
            throw new IllegalArgumentException("Entity of type " + entityType + " already exists with id " + id + ". Known entities are:\n" + entitiesData);
        }
        EntityData entityData = new EntityData();
        entityData.image = loadImage(pathToImage);
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
//            System.out.println(e);
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
