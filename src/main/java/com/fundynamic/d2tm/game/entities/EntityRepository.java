package com.fundynamic.d2tm.game.entities;


import com.fundynamic.d2tm.Game;
import com.fundynamic.d2tm.game.entities.particle.Particle;
import com.fundynamic.d2tm.game.entities.predicates.PredicateBuilder;
import com.fundynamic.d2tm.game.entities.projectiles.Projectile;
import com.fundynamic.d2tm.game.entities.structures.Structure;
import com.fundynamic.d2tm.game.entities.units.Unit;
import com.fundynamic.d2tm.game.map.Map;
import com.fundynamic.d2tm.game.rendering.Recolorer;
import com.fundynamic.d2tm.math.Coordinate;
import com.fundynamic.d2tm.math.Vector2D;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.SpriteSheet;

import java.util.Set;

public class EntityRepository {

    private final Map map;

    private final Recolorer recolorer;

    private EntitiesData entitiesData;
    private Entity lastCreatedEntity;

    private EntitiesSet entitiesSet;

    public EntityRepository(Map map, Recolorer recolorer, EntitiesData entitiesData) throws SlickException {
        if (entitiesData.isEmpty()) {
            throw new IllegalArgumentException("EntitiesData may not be empty");
        }
        this.map = map;
        this.recolorer = recolorer;
        this.entitiesData = entitiesData;
        this.entitiesSet = new EntitiesSet();
    }

    public Unit placeUnitOnMap(Coordinate coordinate, String id, Player player) {
        return (Unit) placeOnMap(coordinate, EntityType.UNIT, id, player);
    }

    public Structure placeStructureOnMap(Coordinate coordinate, String id, Player player) {
        return (Structure) placeOnMap(coordinate, EntityType.STRUCTURE, id, player);
    }

    /**
     * This is the same logic as explodeAt(), only instead of assuming the origin is an entity, we assume the
     * cell is the origin. Used for spawning explosions of structures (which are cell based).
     * 
     * @param coordinate
     * @param explosionId
     * @param player
     */
    public void explodeAtCoordinate(Coordinate coordinate, String explosionId, Player player) {
        if (EntitiesData.UNKNOWN.equals(explosionId)) return;

        EntityData particle = entitiesData.getParticle(explosionId);

        // The top left map coordinate is based on Game.TILE_SIZE tiles. So assumes at top-left of tile
        // depending on the particle that will be created, we need to correct this so the explosion appears
        // centered in that cell.
        //
        // Meaning, when the particle dimensions are < Game.TILE_SIZE then something needs to be added to the x and y
        // of the coordinate. When the particle dimensions are > Game.TILE_SIZE then we need to substract.
        placeExplosionCenteredAt(coordinate, player, Game.TILE_SIZE, Game.TILE_SIZE, particle);
    }

    /**
     * This spawns an explosion from the origin entityData, ie, this could be a projectile about to explode.
     * To make sure the center of the projectile corresponds with the center of the explosion we need to know about the
     * width and height of the to-be-spawned explosion and correct its coordinates.
     *
     * @param coordinate
     * @param origin
     * @param player
     */
    public void explodeAt(Coordinate coordinate, EntityData origin, Player player) {
        if (!origin.hasExplosionId()) return;
        EntityData particle = entitiesData.getParticle(origin.explosionId);
        placeExplosionCenteredAt(coordinate, player, origin.getWidth(), origin.getHeight(), particle);
    }

    public void placeExplosionWithCenterAt(Coordinate centerCoordinate, Player player, String explosionId) {
        EntityData particle = entitiesData.getParticle(explosionId);
        Coordinate topLeft = centerCoordinate.min(particle.getHalfSize());
        placeExplosion(topLeft, particle, player);
    }

    public void placeExplosionCenteredAt(Coordinate topLeftCoordinate, Player player, int originWidth, int originHeight, EntityData particle) {
        // this compensates based on it comes from, so the center of the explosion is the same center
        // of the original center.
        float correctedX = (originWidth - particle.getWidth()) / 2;
        float correctedY = (originHeight - particle.getHeight()) / 2;
        Coordinate correctedCoordinate = topLeftCoordinate.add(correctedX, correctedY);
        placeExplosion(correctedCoordinate, particle, player);
    }

    public void placeExplosion(Coordinate coordinate, EntityData particle, Player player) {
        if (particle.type != EntityType.PARTICLE) {
            throw new IllegalArgumentException("Cannot explode type that is " + particle.type + ", it must be of entity type " + EntityType.PARTICLE);
        }
        placeOnMap(coordinate, particle, player);
    }

    public Entity placeOnMap(Coordinate coordinate, EntityType entityType, String id, Player player) {
        EntityData entityData = entitiesData.getEntityData(entityType, id);
        return placeOnMap(coordinate, entityData, player);
    }

    public Entity placeOnMap(Coordinate coordinate, EntityData entityData, Player player) {
        Entity createdEntity;
        Image originalImage = entityData.image;

        if (entityData.isTypeStructure()) {
            Image recoloredImage = recolorer.recolorToFactionColor(originalImage, player.getFactionColor());
            createdEntity = new Structure(coordinate, recoloredImage, player, entityData, this);
            return addEntityToList(map.revealShroudFor(createdEntity));
        } else if (entityData.isTypeUnit()) {
            Image recoloredImage = recolorer.recolorToFactionColor(originalImage, player.getFactionColor());
            SpriteSheet spriteSheet = makeSpriteSheet(entityData, recoloredImage);
            createdEntity = new Unit(map, coordinate, spriteSheet, player, entityData, this);
            return addEntityToList(map.revealShroudFor(createdEntity));
        } else if (entityData.isTypeProjectile()) {
            SpriteSheet spriteSheet = makeSpriteSheet(entityData, originalImage);
            createdEntity = new Projectile(coordinate, spriteSheet, player, entityData, this);
            return addEntityToList(map.revealShroudFor(createdEntity));
        }  else if (entityData.isTypeParticle()) {
            SpriteSheet spriteSheet = makeSpriteSheet(entityData, originalImage);
            createdEntity = new Particle(coordinate, spriteSheet, entityData, this);
            return addEntityToList(map.revealShroudFor(createdEntity));
        } else {
            throw new IllegalArgumentException("Unknown type " + entityData.type);
        }
    }

    public SpriteSheet makeSpriteSheet(EntityData entityData, Image recoloredImage) {
        return new SpriteSheet(recoloredImage, entityData.getWidth(), entityData.getHeight());
    }

    public Entity addEntityToList(Entity entity) {
        lastCreatedEntity = entity;
        entitiesSet.add(entity);
        return entity;
    }

    public void removeEntities(Predicate predicate) {
        Set<Entity> entitiesToRemove = filter(predicate);
        if (entitiesToRemove.size() == 0) return;
        System.out.println("Size of all entities: " + entitiesSet.size());
        System.out.println("Removing following entities: " + entitiesToRemove);

        for (Entity entity : entitiesToRemove) {
            entity.removeFromPlayerSet(entity);
            entitiesSet.remove(entity);
        }

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
    public EntitiesSet findEntitiesOfTypeWithinRectangle(Rectangle rectangle, EntityType entityType) {
        return filter(
                Predicate.builder().
                        ofType(entityType).
                        withinArea(rectangle)
        );
    }

    /**
     * Slow way of filtering within area!
     *
     * @param rectangle
     * @return
     */
    public EntitiesSet findEntitiesWithinRectangle(Rectangle rectangle) {
        return filter(
                Predicate.builder().
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

    public EntitiesSet findEntitiesOfTypeAtVector(Vector2D absoluteMapCoordinates, EntityType... types) {
        return filter(
                Predicate.builder().
                        ofTypes(types).
                        vectorWithin(absoluteMapCoordinates)
        );
    }

    public EntitiesSet findEntitiesAtVector(Vector2D absoluteMapCoordinates) {
        return filter(
                Predicate.builder().
                        vectorWithin(absoluteMapCoordinates)
        );
    }

    public EntityData getEntityData(EntityType entityType, String id) {
        return entitiesData.getEntityData(entityType, id);
    }

    public int getEntitiesCount() {
        return entitiesSet.size();
    }

    public Entity getLastCreatedEntity() {
        return lastCreatedEntity;
    }

    public Projectile placeProjectile(Coordinate coordinate, String id, Player player) {
        return (Projectile) placeOnMap(coordinate, EntityType.PROJECTILE, id, player);
    }
}
