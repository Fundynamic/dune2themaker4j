package com.fundynamic.d2tm.game.entities;


import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

import java.util.HashMap;

public class EntitiesData {

    // units
    public static String TRIKE = "TRIKE";
    public static String QUAD = "QUAD";

    // structures
    public static String CONSTRUCTION_YARD = "CONSTYARD";
    public static String REFINERY = "REFINERY";

    // projectiles
    public static String ROCKET = "ROCKET";
    public static String BULLET = "RIFLE";

    // explosions
    public static String EXPLOSION_NORMAL = "NORMAL";
    public static String EXPLOSION_SMALL_UNIT = "WHEELED";
    public static String EXPLOSION_SMALL_BULLET = "RIFLEHIT";

    private HashMap<String, EntityData> entitiesData;

    public EntitiesData() {
        entitiesData = new HashMap<>();
    }

    public void createProjectile(String id, String pathToImage, int widthInPixels, int heightInPixels, String explosionId, float moveSpeed, int damage, int facings) throws SlickException {
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
        entity.explosionId = explosionId;
    }

    public void createUnit(String id, String pathToImage, int widthInPixels, int heightInPixels, int sight, float moveSpeed, int hitPoints, String weaponId, String explosionId) throws SlickException {
        EntityData entity = createEntity(id, pathToImage, widthInPixels, heightInPixels, EntityType.UNIT, sight, moveSpeed, hitPoints);
        entity.weaponId = weaponId;
        entity.explosionId = explosionId;
    }

    private EntityData createEntity(String id, String pathToImage, int widthInPixels, int heightInPixels, EntityType entityType, int sight, float moveSpeed, int hitPoints) throws SlickException {
        if (tryGetEntityData(entityType, id)) {
            throw new IllegalArgumentException("Entity of type " + entityType + " already exists with id " + id + ". Known entities are:\n" + entitiesData);
        }
        EntityData entityData = new EntityData();
        entityData.image = loadImage(pathToImage);
        entityData.width = widthInPixels;
        entityData.height = heightInPixels;
        entityData.type = entityType;
        entityData.sight = sight;
        entityData.moveSpeed = moveSpeed;
        entityData.hitPoints = hitPoints;
        entitiesData.put(constructKey(entityType, id), entityData);
        return entityData;
    }

    protected Image loadImage(String pathToImage) throws SlickException {
        return new Image(pathToImage);
    }


    public EntityData getEntityData(EntityType entityType, String id) {
        String key = constructKey(entityType, id);
        EntityData entityData = entitiesData.get(key);
        if (entityData == null) throw new EntityNotFoundException("Entity not found for key " + key + ". Known entities are:\n" + toString());
        return entityData;
    }

    private boolean tryGetEntityData(EntityType entityType, String id) {
        try {
            getEntityData(entityType, id);
            return true;
        } catch (EntityNotFoundException e) {
            return false;
        }
    }

    public String constructKey(EntityType entityType, String id) {
        return entityType.toString() + "-" + id;
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
