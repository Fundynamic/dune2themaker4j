package com.fundynamic.d2tm.game.entities;


import com.fundynamic.d2tm.Game;
import com.fundynamic.d2tm.game.entities.particle.Particle;
import com.fundynamic.d2tm.game.entities.predicates.PredicateBuilder;
import com.fundynamic.d2tm.game.entities.projectiles.Projectile;
import com.fundynamic.d2tm.game.entities.structures.Structure;
import com.fundynamic.d2tm.game.entities.units.Unit;
import com.fundynamic.d2tm.game.map.Map;
import com.fundynamic.d2tm.game.rendering.Recolorer;
import com.fundynamic.d2tm.math.Vector2D;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.SpriteSheet;

import java.util.Set;

public class EntityRepository {

    private final Map map;

    private final Recolorer recolorer;

    private EntitiesData entitiesData;

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

    public Unit placeUnitOnMap(Vector2D absoluteMapCoordinate, String id, Player player) {
        return (Unit) placeOnMap(absoluteMapCoordinate, EntityType.UNIT, id, player);
    }

    public void placeStructureOnMap(Vector2D topLeftMapCoordinate, String id, Player player) {
        placeOnMap(topLeftMapCoordinate, EntityType.STRUCTURE, id, player);
    }

    public void explodeAt(Vector2D topLeftAbsoluteMapCoordinate, String explosionId, Player player) {
        if ("UNKNOWN".equals(explosionId)) return;

        EntityData particle = entitiesData.getEntityData(EntityType.PARTICLE, explosionId);

        // The top left map coordinate is based on Game.TILE_SIZE tiles. So assumes at top-left of tile
        // depending on the particle that will be created, we need to correct this so the explosion appears
        // centered in that cell.
        //
        // Meaning, when the particle dimensions are < Game.TILE_SIZE then something needs to be added to the x and y
        // of the absoluteCoordinates. When the particle dimensions are > Game.TILE_SIZE then we need to substract.
        int correctedX = (Game.TILE_SIZE - particle.width) / 2;
        int correctedY = (Game.TILE_SIZE - particle.height) / 2;
        Vector2D correctedCoordinate = topLeftAbsoluteMapCoordinate.add(correctedX, correctedY);

        placeOnMap(correctedCoordinate, particle, player);
    }

    public Entity placeOnMap(Vector2D topLeftAbsoluteMapCoordinate, EntityType entityType, String id, Player player) {
        EntityData entityData = entitiesData.getEntityData(entityType, id);
        return placeOnMap(topLeftAbsoluteMapCoordinate, entityData, player);
    }

    public Entity placeOnMap(Vector2D absoluteMapCoordinates, EntityData entityData, Player player) {
        System.out.println("Placing " + entityData + " on map at " + absoluteMapCoordinates + " for " + player);
        Entity createdEntity;
        Image originalImage = entityData.image;
        Image recoloredImage = originalImage;
        SpriteSheet spriteSheet;

        switch (entityData.type) {
            case STRUCTURE:
                recoloredImage = recolorer.recolorToFactionColor(originalImage, player.getFactionColor());
                createdEntity = new Structure(absoluteMapCoordinates, recoloredImage, player, entityData, this);
                addEntityToList(map.placeStructure((Structure) createdEntity));
                break;
            case UNIT:
                recoloredImage = recolorer.recolorToFactionColor(originalImage, player.getFactionColor());
                spriteSheet = new SpriteSheet(recoloredImage, entityData.width, entityData.height);
                createdEntity = new Unit(map, absoluteMapCoordinates, spriteSheet, player, entityData, this);
                addEntityToList(map.placeUnit((Unit) createdEntity));
                break;
            case PROJECTILE:
                spriteSheet = new SpriteSheet(recoloredImage, entityData.width, entityData.height);
                createdEntity = new Projectile(absoluteMapCoordinates, spriteSheet, player, entityData, this);
                addEntityToList(map.placeProjectile((Projectile) createdEntity));
                break;
            case PARTICLE:
                spriteSheet = new SpriteSheet(recoloredImage, entityData.width, entityData.height);
                createdEntity = new Particle(absoluteMapCoordinates, spriteSheet, entityData, this);
                addEntityToList(createdEntity);
                break;
            default:
                throw new IllegalArgumentException("Unknown type " + entityData.type);
        }
        return createdEntity;
    }

    public void addEntityToList(Entity entity) {
        entitiesSet.add(entity);
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

    public void removeAllEntityData() {
        entitiesData.clear();
    }

    public EntityData getEntityData(EntityType entityType, String id) {
        return entitiesData.getEntityData(entityType, id);
    }

    public int getEntitiesCount() {
        return entitiesSet.size();
    }
}
