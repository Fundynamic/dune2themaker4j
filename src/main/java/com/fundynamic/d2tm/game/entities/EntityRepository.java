package com.fundynamic.d2tm.game.entities;


import com.fundynamic.d2tm.game.entities.particle.Particle;
import com.fundynamic.d2tm.game.entities.predicates.PredicateBuilder;
import com.fundynamic.d2tm.game.entities.projectiles.Projectile;
import com.fundynamic.d2tm.game.entities.structures.Structure;
import com.fundynamic.d2tm.game.entities.units.Unit;
import com.fundynamic.d2tm.game.map.CellAlreadyOccupiedException;
import com.fundynamic.d2tm.game.map.Map;
import com.fundynamic.d2tm.game.rendering.Recolorer;
import com.fundynamic.d2tm.math.Vector2D;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.SpriteSheet;

import java.util.HashMap;
import java.util.Set;

public class EntityRepository {

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

    private final Map map;

    private final Recolorer recolorer;

    private HashMap<String, EntityData> entitiesData;

    private EntitiesSet entitiesSet;

    public EntityRepository(Map map, Recolorer recolorer) throws SlickException {
        this(map, recolorer, new HashMap<String, EntityData>());
        init();
    }

    public void init() throws SlickException {
        createParticle(EXPLOSION_NORMAL, "explosions/explosion_3.png", 48, 48, 5f);
        createParticle(EXPLOSION_SMALL_UNIT, "explosions/small_unit_explosion.png", 48, 48, 3f);
        createParticle(EXPLOSION_SMALL_BULLET, "explosions/small_bullet_explosion.png", 32, 32, 3f);

        createProjectile(ROCKET, "projectiles/LargeRocket.png", 48, 48, EXPLOSION_NORMAL, 160f, 200, 16);
        createProjectile(BULLET, "projectiles/SmallBullet.png", 6, 6, EXPLOSION_SMALL_BULLET, 160f, 15, 0);

        createUnit(QUAD, "units/quad.png", 32, 32, 3, 1.5F, 200, BULLET, EXPLOSION_SMALL_UNIT);
        createUnit(TRIKE, "units/trike.png", 28, 26, 4, 2.5F, 150, BULLET, EXPLOSION_SMALL_UNIT);

        createStructure(CONSTRUCTION_YARD, "structures/2x2_constyard.png", 64, 64, 5, 1000, EXPLOSION_NORMAL);
        createStructure(REFINERY, "structures/3x2_refinery.png", 96, 64, 5, 1500, EXPLOSION_NORMAL);
    }

    public EntityRepository(Map map, Recolorer recolorer, HashMap<String, EntityData> entitiesData) throws SlickException {
        this.map = map;
        this.recolorer = recolorer;
        this.entitiesData = entitiesData;
        this.entitiesSet = new EntitiesSet();
    }

    public void placeUnitOnMap(Vector2D mapCoordinate, int id, Player player) {
        placeOnMap(mapCoordinate, EntityType.UNIT, id, player);
    }

    public void placeStructureOnMap(Vector2D topLeftMapCoordinate, int id, Player player) {
        placeOnMap(topLeftMapCoordinate, EntityType.STRUCTURE, id, player);
    }

    public Entity placeOnMap(Vector2D topLeftMapCoordinate, EntityType entityType, int id, Player player) {
        EntityData entityData = getEntityData(entityType, id);
        return placeOnMap(topLeftMapCoordinate, entityData, player);
    }

    public Entity placeOnMap(Vector2D mapCoordinate, EntityData entityData, Player player) {
        System.out.println("Placing " + entityData + " on map at " + mapCoordinate + " for " + player);
        try {
            Entity createdEntity;
            Image originalImage = entityData.image;
            Image recoloredImage = originalImage;
            SpriteSheet spriteSheet;

            switch (entityData.type) {
                case STRUCTURE:
                    recoloredImage = recolorer.recolorToFactionColor(originalImage, player.getFactionColor());
                    createdEntity = new Structure(mapCoordinate, recoloredImage, player, entityData, this);
                    entitiesSet.add(map.placeStructure((Structure) createdEntity));
                    break;
                case UNIT:
                    recoloredImage = recolorer.recolorToFactionColor(originalImage, player.getFactionColor());
                    createdEntity = new Unit(map, mapCoordinate, recoloredImage, player, entityData, this);
                    entitiesSet.add(map.placeUnit((Unit) createdEntity));
                    break;
                case PROJECTILE:
                    spriteSheet = new SpriteSheet(recoloredImage, entityData.width, entityData.height);
                    createdEntity = new Projectile(map, mapCoordinate, spriteSheet, entityData.sight, player, entityData, this);
                    entitiesSet.add(map.placeProjectile((Projectile) createdEntity));
                    break;
                case PARTICLE:
                    spriteSheet = new SpriteSheet(recoloredImage, entityData.width, entityData.height);
                    createdEntity = new Particle(mapCoordinate, spriteSheet, entityData, this);
                    entitiesSet.add(createdEntity);
                    break;
                default:
                    throw new IllegalArgumentException("Unknown type " + entityData.type);
            }
            return createdEntity;
        } catch (CellAlreadyOccupiedException e) {
            throw new UnableToPlaceEntityOnMapException(e);
        }
    }

    public void createUnit(int id, String pathToImage, int widthInPixels, int heightInPixels, int sight, float moveSpeed, int hitPoints, int weaponId, int explosionId) throws SlickException {
        EntityData entity = createEntity(id, pathToImage, widthInPixels, heightInPixels, EntityType.UNIT, sight, moveSpeed, hitPoints);
        entity.weaponId = weaponId;
        entity.explosionId = explosionId;
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

    public void removeEntities(Predicate predicate) {
        Set<Entity> entitiesToRemove = filter(predicate);
        if (entitiesToRemove.size() == 0) return;
        System.out.println("Size of all entities: " + entitiesSet.size());
        System.out.println("Removing following entities: " + entitiesToRemove);

        for (Entity entity : entitiesToRemove) {
            map.removeEntity(entity);
            entity.removeFromPlayerSet(entity);
            entitiesSet.remove(entity);
        }

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

    public EntitiesSet getEntitiesSet() {
        return entitiesSet;
    }

    public EntitiesSet filter(PredicateBuilder predicateBuilder) {
        return entitiesSet.filter(predicateBuilder);
    }

    public EntitiesSet filter(Predicate<Entity> predicate) {
        return entitiesSet.filter(predicate);
    }

    public Set<Entity> findMovableWithinRectangleForPlayer(Player player, Rectangle rectangle) {
        return filter(
                Predicate.builder().
                        selectableMovableForPlayer(player).
                        withinArea(rectangle)
        );
    }

    /**
     * Slow way of filtering within area!
     *
     * @param rectangle
     * @return
     */
    public Set<Entity> findEntitiesOfTypeWithinRectangle(Rectangle rectangle, EntityType entityType) {
        return filter(
                Predicate.builder().
                        ofType(entityType).
                        withinArea(rectangle)
        );
    }

    public EntitiesSet allProjectiles() {
        return ofType(EntityType.PROJECTILE);
    }

    public EntitiesSet ofType(EntityType entityType) {
        return filter(
                Predicate.builder().
                        ofType(entityType)
        );
    }

    public EntitiesSet allUnits() {
        return ofType(EntityType.UNIT);
    }

}
