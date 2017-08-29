package com.fundynamic.d2tm.game.entities;


import com.fundynamic.d2tm.game.behaviors.FadingSelectionCentered;
import com.fundynamic.d2tm.game.behaviors.HitPointBasedDestructibilityCentered;
import com.fundynamic.d2tm.game.entities.entitiesdata.EntitiesData;
import com.fundynamic.d2tm.game.entities.particle.Particle;
import com.fundynamic.d2tm.game.entities.predicates.PredicateBuilder;
import com.fundynamic.d2tm.game.entities.projectiles.Projectile;
import com.fundynamic.d2tm.game.entities.structures.Structure;
import com.fundynamic.d2tm.game.entities.superpowers.SuperPower;
import com.fundynamic.d2tm.game.entities.units.NullRenderQueueEnrichableWithFacingLogic;
import com.fundynamic.d2tm.game.entities.units.RenderQueueEnrichableWithFacingLogic;
import com.fundynamic.d2tm.game.entities.units.Unit;
import com.fundynamic.d2tm.game.map.Cell;
import com.fundynamic.d2tm.game.map.Map;
import com.fundynamic.d2tm.game.rendering.gui.battlefield.Recolorer;
import com.fundynamic.d2tm.game.types.EntityData;
import com.fundynamic.d2tm.math.Coordinate;
import com.fundynamic.d2tm.math.MapCoordinate;
import com.fundynamic.d2tm.math.Rectangle;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.SpriteSheet;

import java.util.Set;

/**
 * <h1>General purpose</h1>
 * <p>
 *     This class is responsible for the <em>creation</em> of entities as well as the <em>removal</em> of entities and
 *     can be considered as the <em><b>global game state of the battlefield</b></em>.
 * </p>
 * <p>
 *     An entity repository contains all {@link EntitiesData} (blue-prints/types of all entities) and is able to construct
 *     new {@link Entity}'s on the provided within its internal {@link EntitiesSet}.
 * </p>
 * <p>
 *     This set is used by (for instance) the {@link com.fundynamic.d2tm.game.rendering.gui.battlefield.BattleField}.
 * </p>
 */
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

    public Unit placeUnitOnMap(MapCoordinate coordinate, String id, Player player) {
        return (Unit) placeOnMap(coordinate.toCoordinate(), EntityType.UNIT, id, player);
    }

    public Structure placeStructureOnMap(MapCoordinate coordinate, String id, Player player) {
        return (Structure) placeOnMap(coordinate.toCoordinate(), EntityType.STRUCTURE, id, player);
    }

    /**
     * This spawns an explosion from the origin entityData, ie, this could be a projectile about to explode.
     *
     * @param centerCoordinate of explosion
     * @param origin the entityData that spawns the explosion
     * @param player
     */
    public void explodeAt(Coordinate centerCoordinate, EntityData origin, Player player) {
        if (!origin.hasExplosionId()) return;
        placeExplosionWithCenterAt(centerCoordinate, player, origin.explosionId);
    }

    public void placeExplosionWithCenterAt(Coordinate centerCoordinate, Player player, String explosionId) {
        EntityData particle = entitiesData.getParticle(explosionId);
        Coordinate topLeft = centerCoordinate.min(particle.halfDimensions());
        placeExplosion(topLeft, particle, player);
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

    public Entity placeOnMap(Coordinate coordinate, EntityType entityType, String id, Entity origin) {
        EntityData entityData = entitiesData.getEntityData(entityType, id);
        Entity entity = placeOnMap(coordinate, entityData, origin.player);
        return entity.setOrigin(origin);
    }

    /**
     * Return a passableResult object for a given entity and a mapCoordinate it intents to do move to.
     * This is not taking special unit logic into consideration.
     * @param entity
     * @param intendedMapCoordinatesToMoveTo
     * @return
     */
    public PassableResult isPassable(Entity entity, MapCoordinate intendedMapCoordinatesToMoveTo) {
        Coordinate absoluteMapCoordinates = intendedMapCoordinatesToMoveTo.toCoordinate();
        EntitiesSet entities = findAliveEntitiesOfTypeAtVector(absoluteMapCoordinates, EntityType.UNIT, EntityType.STRUCTURE);
        entities = entities.exclude(entity); // do not count self as blocking

        Cell cellByMapCoordinates = map.getCellByMapCoordinates(intendedMapCoordinatesToMoveTo);
        return new PassableResult(cellByMapCoordinates, entity, entities);
    }

    public Entity placeOnMap(Coordinate startCoordinate, EntityData entityData, Player player) {
        Entity createdEntity;
        Image originalImage = entityData.image;

        if (entityData.isTypeStructure()) {
            Image recoloredImage = recolorer.createCopyRecoloredToFaction(originalImage, player.getFaction());
            createdEntity = new Structure(
                    startCoordinate,
                    makeSpriteSheet(entityData, recoloredImage),
                    player,
                    entityData,
                    this
            );
            return placeOnMap(createdEntity);
        } else if (entityData.isTypeUnit()) {
            Image recoloredImage = recolorer.createCopyRecoloredToFaction(originalImage, player.getFaction());
            createdEntity = new Unit(
                    map,
                    startCoordinate,
                    makeRenderableWithFacingLogic(entityData, recoloredImage, entityData.turnSpeed),
                    makeRenderableWithFacingLogic(entityData, entityData.barrelImage, entityData.turnSpeedCannon),
                    new FadingSelectionCentered(entityData.getWidth(), entityData.getHeight()),
                    new HitPointBasedDestructibilityCentered(entityData.hitPoints, entityData.getWidth(), entityData.getHeight()),
                    player,
                    entityData,
                    this
            );
            return placeOnMap(createdEntity);
        } else if (entityData.isTypeProjectile()) {
            SpriteSheet spriteSheet = makeSpriteSheet(entityData, originalImage);
            createdEntity = new Projectile(startCoordinate, spriteSheet, player, entityData, this);
            return placeOnMap(createdEntity);
        } else if (entityData.isTypeParticle()) {
            Image recoloredImage = originalImage;
            if (entityData.recolor) {
                recoloredImage = recolorer.createCopyRecoloredToFaction(originalImage, player.getFaction());
            }
            SpriteSheet spriteSheet = makeSpriteSheet(entityData, recoloredImage);
            createdEntity = new Particle(startCoordinate, spriteSheet, entityData, this);
            return placeOnMap(createdEntity);
        } else if (entityData.isTypeSuperPower()) {
            throw new IllegalArgumentException("Don't use placeOnMap, but use method spawnSuperPower method instead.");
        } else {
            throw new IllegalArgumentException("Unknown type " + entityData.type);
        }
    }

    public <T extends Entity> T placeOnMap(T createdEntity) {
        return (T) addEntityToList(map.revealShroudFor(createdEntity));
    }

    public Entity addEntityToList(Entity entity) {
        if (entity.getPlayer() != null) {
            // create cyclic reference here!
            entity.getPlayer().addEntity(entity);
        }
        lastCreatedEntity = entity;
        entitiesSet.add(entity);
        return entity;
    }

    public void removeEntities(Predicate predicate) {
        Set<Entity> entitiesToRemove = filter(predicate);
        if (entitiesToRemove.size() == 0) return;

        for (Entity entity : entitiesToRemove) {
            removeEntity(entity);
        }

    }

    public void removeEntity(Entity entity) {
        entity.removeFromPlayerSet(entity);
        entity.destroy();
        entitiesSet.remove(entity);
    }

    public EntitiesSet getEntitiesSet() {
        return entitiesSet;
    }

    public EntitiesSet filter(PredicateBuilder predicateBuilder) {
        return entitiesSet.filter(predicateBuilder);
    }

    public EntitiesSet findEntitiesAt(Coordinate coordinate) {
        return filter(
                Predicate.builder().
                        vectorWithin(coordinate)
        );
    }

    public EntitiesSet filter(Predicate<Entity> predicate) {
        return entitiesSet.filter(predicate);
    }

    public Set<Entity> findMovableWithinRectangleForPlayer(Player player, Rectangle rectangle) {
        return filter(
                Predicate.builder().
                        isNotWithinAnotherEntity().
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

    public EntitiesSet findDestructibleEntities(Coordinate... absoluteMapCoordinates) {
        return filter(
                Predicate.builder().
                    isDestructible().
                    vectorWithin(absoluteMapCoordinates)
        );
    }

    public EntitiesSet findDestructibleEntities(Set<Coordinate> absoluteMapCoordinates) {
        return findDestructibleEntities(absoluteMapCoordinates.toArray(new Coordinate[absoluteMapCoordinates.size()]));
    }

    public EntitiesSet findAliveEntitiesWithinPlayableMapBoundariesOfType(EntityType... types) {
        return filter(
                Predicate.builder().
                        ofTypes(types).
                        isAlive().
                        isWithinPlayableMapBoundaries(map)

        );
    }

    public EntitiesSet findAliveEntitiesOfTypeAtVector(Coordinate absoluteMapCoordinates, EntityType... types) {
        return filter(
                Predicate.builder().
                        ofTypes(types).
                        isAlive().
                        vectorWithin(absoluteMapCoordinates)

        );
    }

    public EntitiesSet findEntitiesOfTypeAtVectorWithinDistance(Coordinate coordinate, float range, EntityType... types) {
        return filter(
                Predicate.builder().
                        ofTypes(types).
                        withinRange(coordinate, range)
        );
    }

    public EntitiesSet findRefineriesWithinDistance(Coordinate coordinate, float range, Player player) {
        return filter(
                Predicate.builder().
                        forPlayer(player).
                        isRefinery().
                        withinRange(coordinate, range)
        );
    }

    public EntitiesSet findDestructibleEntitiesWithinDistance(Coordinate coordinate, float range) {
        return filter(
                Predicate.builder().
                        isDestructible().
                        withinRange(coordinate, range)
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

    public Projectile placeProjectile(Coordinate coordinate, String id, Entity origin) {
        return (Projectile) placeOnMap(coordinate, EntityType.PROJECTILE, id, origin);
    }

    public Projectile placeProjectile(Coordinate coordinate, String id, Player player) {
        return (Projectile) placeOnMap(coordinate, EntityType.PROJECTILE, id, player);
    }

    public SpriteSheet makeSpriteSheet(EntityData entityData, Image recoloredImage) {
        return new SpriteSheet(recoloredImage, entityData.getWidth(), entityData.getHeight());
    }

    protected RenderQueueEnrichableWithFacingLogic makeRenderableWithFacingLogic(EntityData entityData, Image recoloredImage, float turnSpeed) {
        if (recoloredImage == null)
            try {
                return new NullRenderQueueEnrichableWithFacingLogic(entityData);
            } catch (SlickException e) {
                throw new IllegalStateException("Could not create NullRenderableWithFacingLogic() : " + e);
            }
        return new RenderQueueEnrichableWithFacingLogic(recoloredImage, entityData, turnSpeed);
    }

    /**
     * Is same logic as {@link #isPassable(Entity, MapCoordinate)} - but first checks if <code>mapCoordinate</code> is
     * within the map boundaries.
     * @param entity
     * @param mapCoordinate
     * @return
     */
    public PassableResult isPassableWithinMapBoundaries(Entity entity, MapCoordinate mapCoordinate) {
        if (map.isWithinPlayableMapBoundaries(mapCoordinate)) {
            return isPassable(entity, mapCoordinate);
        }
        return new PassableResult(entity, map.getCell(mapCoordinate));
    }

    /**
     * Super powers are special, they need to be created using this method.
     *
     * @param fireStarterCoordinate
     * @param superPowerEntityData
     * @param player
     * @param target
     * @return
     */
    public SuperPower spawnSuperPower(Coordinate fireStarterCoordinate, EntityData superPowerEntityData, Player player, Coordinate target) {
        SuperPower superPower = new SuperPower(Coordinate.create(0,0), superPowerEntityData, player, this);
        superPower.setFireStarterCoordinate(fireStarterCoordinate);
        superPower.setTarget(target);
        return placeOnMap(superPower);
    }

    public EntitiesSet findDestructibleSelectedEntitiesForPlayer(Player player) {
        return filter(new PredicateBuilder().selectedForPlayer(player).isDestructible());
    }

    public class PassableResult {
        private Cell cell; // the passable result is about this cell
        private Entity entity; // this result is requested by...
        private EntitiesSet entities; // and the cell has these entities

        public PassableResult(Entity entity, Cell cell) {
            this(cell, entity, EntitiesSet.empty());
        }

        public PassableResult(Cell cell, Entity entity, EntitiesSet entitiesOnCell) {
            this.entities = entitiesOnCell;
            this.cell = cell;
            this.entity = entity;
        }

        public boolean isPassable() {
            return entities.isEmpty() && cell.isPassable(entity);
        }

        public EntitiesSet getEntities() {
            return entities;
        }

        public boolean hasOne() {
            return entities.hasOne();
        }

        public Entity getFirstBlockingEntity() {
            return entities.getFirst();
        }
    }
}
