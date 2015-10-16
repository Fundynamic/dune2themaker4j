package com.fundynamic.d2tm.game.entities;


import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

import java.util.HashMap;

public class EntitiesData {

    // units
    public static int TRIKE = 0;
    public static int QUAD = 1;

    // structures
    public static int CONSTRUCTION_YARD = 0;
    public static int REFINERY = 1;

    // projectiles
    public static int ROCKET = 0;
    public static int BULLET = 1;

    // explosions
    public static final int NO_EXPLOSION = -1;
    public static int EXPLOSION_NORMAL = 0;
    public static int EXPLOSION_SMALL_UNIT = 1;
    public static int EXPLOSION_SMALL_BULLET = 2;

    private HashMap<String, EntityData> entitiesData;

    public EntitiesData() {
        entitiesData = new HashMap<>();
    }

    public void createProjectile(int id, String pathToImage, int widthInPixels, int heightInPixels, int explosionId, float moveSpeed, int damage, int facings) throws SlickException {
        EntityData entity = createEntity(id, pathToImage, widthInPixels, heightInPixels, EntityType.PROJECTILE, -1, moveSpeed, -1);
        entity.damage = damage;
        entity.explosionId = explosionId;
        entity.setFacingsAndCalculateChops(facings);
    }

    public void createParticle(int id, String pathToImage, int widthInPixels, int heightInPixels, float framesPerSecond) throws SlickException {
        EntityData entity = createEntity(id, pathToImage, widthInPixels, heightInPixels, EntityType.PARTICLE, -1, -1, -1);
        entity.animationSpeed = framesPerSecond;
    }

    public void createStructure(int id, String pathToImage, int widthInPixels, int heightInPixels, int sight, int hitPoints, int explosionId) throws SlickException {
        EntityData entity = createEntity(id, pathToImage, widthInPixels, heightInPixels, EntityType.STRUCTURE, sight, 0F, hitPoints);
        entity.explosionId = explosionId;
    }

    public void createUnit(int id, String pathToImage, int widthInPixels, int heightInPixels, int sight, float moveSpeed, int hitPoints, int weaponId, int explosionId) throws SlickException {
        EntityData entity = createEntity(id, pathToImage, widthInPixels, heightInPixels, EntityType.UNIT, sight, moveSpeed, hitPoints);
        entity.weaponId = weaponId;
        entity.explosionId = explosionId;
    }

    private EntityData createEntity(int id, String pathToImage, int widthInPixels, int heightInPixels, EntityType entityType, int sight, float moveSpeed, int hitPoints) throws SlickException {
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


    public EntityData getEntityData(EntityType entityType, int id) {
        EntityData entityData = entitiesData.get(constructKey(entityType, id));
        if (entityData == null) throw new EntityNotFoundException("Entity not found for entityType " + entityType + " and ID " + id + ". Known entities are:\n" + entitiesData);
        return entityData;
    }

    private boolean tryGetEntityData(EntityType entityType, int id) {
        try {
            getEntityData(entityType, id);
            return true;
        } catch (EntityNotFoundException e) {
            return false;
        }
    }

    public String constructKey(EntityType entityType, int id) {
        return entityType.toString() + "-" + id;
    }

    public void clear() {
        this.entitiesData.clear();
    }

    public boolean isEmpty() {
        return this.entitiesData.isEmpty();
    }
}
