package com.fundynamic.d2tm.game.entities;


import com.fundynamic.d2tm.game.entities.predicates.PredicateBuilder;
import com.fundynamic.d2tm.game.entities.structures.Structure;
import com.fundynamic.d2tm.game.entities.units.Unit;
import com.fundynamic.d2tm.game.map.CellAlreadyOccupiedException;
import com.fundynamic.d2tm.game.map.Map;
import com.fundynamic.d2tm.game.rendering.Recolorer;
import com.fundynamic.d2tm.math.Vector2D;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

import java.util.HashMap;
import java.util.Set;

public class EntityRepository {

    public static int TRIKE = 0;
    public static int QUAD = 1;

    public static int CONSTRUCTION_YARD = 0;
    public static int REFINERY = 1;

    private final Map map;

    private final Recolorer recolorer;

    private HashMap<String, EntityData> entitiesData;

    private EntitiesSet entitiesSet;

    public EntityRepository(Map map, Recolorer recolorer) throws SlickException {
        this(map, recolorer, new HashMap<String, EntityData>());

        // TODO: read this data from an external (XML/JSON/YML/INI) file
        createUnit(QUAD, "units/quad.png", 32, 32, 3, 1.5F, 200);
        createUnit(TRIKE, "units/trike.png", 28, 26, 4, 2.5F, 150);
        createStructure(CONSTRUCTION_YARD, "structures/2x2_constyard.png", 64, 64, 5, 1000);
        createStructure(REFINERY, "structures/3x2_refinery.png", 96, 64, 5, 1500);
    }

    public EntityRepository(Map map, Recolorer recolorer, HashMap<String, EntityData> entitiesData) throws SlickException {
        this.map = map;
        this.recolorer = recolorer;
        this.entitiesData = entitiesData;
        this.entitiesSet = new EntitiesSet();
    }

    public void placeUnitOnMap(Vector2D topLeft, int id, Player player) {
        placeOnMap(topLeft, EntityType.UNIT, id, player);
    }

    public void placeStructureOnMap(Vector2D topLeft, int id, Player player) {
        placeOnMap(topLeft, EntityType.STRUCTURE, id, player);
    }

    public Entity placeOnMap(Vector2D topLeft, EntityType entityType, int id, Player player) {
        EntityData entityData = getEntityData(entityType, id);
        return placeOnMap(topLeft, entityData, player);
    }

    public Entity placeOnMap(Vector2D topLeft, EntityData entityData, Player player) {
        System.out.println("Placing " + entityData + " on map at " + topLeft + " for " + player);
        try {
            Entity createdEntity;
            Image originalImage = entityData.image;
            Image recoloredImage = recolorer.recolor(originalImage, player.getFactionColor());

            switch (entityData.type) {
                case STRUCTURE:
                    createdEntity = new Structure(topLeft, recoloredImage, player, entityData);
                    entitiesSet.add(map.placeStructure((Structure) createdEntity));
                    break;
                case UNIT:
                    createdEntity = new Unit(map, topLeft, recoloredImage, player, entityData);
                    entitiesSet.add(map.placeUnit((Unit) createdEntity));
                    break;
                default:
                    throw new IllegalArgumentException("Unknown type!");
            }
            return createdEntity;
        } catch (CellAlreadyOccupiedException e) {
            throw new UnableToPlaceEntityOnMapException(e);
        }
    }

    public void createUnit(int id, String pathToImage, int widthInPixels, int heightInPixels, int sight, float moveSpeed, int hitPoints) throws SlickException {
        createEntity(id, pathToImage, widthInPixels, heightInPixels, EntityType.UNIT, sight, moveSpeed, hitPoints);
    }

    public void createStructure(int id, String pathToImage, int widthInPixels, int heightInPixels, int sight, int hitPoints) throws SlickException {
        createEntity(id, pathToImage, widthInPixels, heightInPixels, EntityType.STRUCTURE, sight, 0F, hitPoints);
    }

    public void removeEntities(Predicate predicate) {
        Set<Entity> entitiesToRemove = filter(predicate);
        if (entitiesToRemove.size() == 0) return;
        System.out.println("Size of all entities: " + entitiesSet.size());
        System.out.println("Removing following entities: " + entitiesToRemove);

        for (Entity entity : entitiesToRemove) {
            map.removeEntity(entity);
            entity.getPlayer().removeEntity(entity);
            entitiesSet.remove(entity);
        }

    }

    private void createEntity(int id, String pathToImage, int widthInPixels, int heightInPixels, EntityType entityType, int sight, float moveSpeed, int hitPoints) throws SlickException {
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

    public Set<Entity> getEntitiesSet() {
        return entitiesSet;
    }

    public Set<Entity> filter(PredicateBuilder predicateBuilder) {
        return entitiesSet.filter(predicateBuilder);
    }

    public Set<Entity> filter(Predicate<Entity> predicate) {
        return entitiesSet.filter(predicate);
    }

    public Set<Entity> findMovableWithinRectangleForPlayer(Player player, Rectangle rectangle) {
        return filter(
                Predicate.builder().
                        selectableMovableForPlayer(player).
                        withinArea(rectangle)
        );
    }
}
