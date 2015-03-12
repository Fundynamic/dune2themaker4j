package com.fundynamic.d2tm.game.entities;


import com.fundynamic.d2tm.game.entities.structures.Structure;
import com.fundynamic.d2tm.game.entities.units.Unit;
import com.fundynamic.d2tm.game.map.CellAlreadyOccupiedException;
import com.fundynamic.d2tm.game.map.Map;
import com.fundynamic.d2tm.game.rendering.Recolorer;
import com.fundynamic.d2tm.math.Vector2D;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class EntityRepository {

    public static int TRIKE = 0;
    public static int QUAD = 1;

    public static int CONSTRUCTION_YARD = 0;
    public static int REFINERY = 1;

    private final Map map;

    private final Recolorer recolorer;

    private HashMap<String, EntityData> entitiesData;

    private Set<Entity> entities;

    public EntityRepository(Map map, Recolorer recolorer) throws SlickException {
        this(map, recolorer, new HashMap<String, EntityData>());

        // TODO: read this data from an external (XML/JSON/YML/INI) file
        createUnit(QUAD, "units/quad.png", 32, 32, 4);
        createUnit(TRIKE, "units/trike.png", 28, 26, 4);
        createStructure(CONSTRUCTION_YARD, "structures/2x2_constyard.png", 64, 64, 2);
        createStructure(REFINERY, "structures/3x2_refinery.png", 96, 64, 2);
    }

    public EntityRepository(Map map, Recolorer recolorer, HashMap<String, EntityData> entitiesData) throws SlickException {
        this.map = map;
        this.recolorer = recolorer;
        this.entitiesData = entitiesData;
        this.entities = new HashSet<>();
    }

    public void placeUnitOnMap(Vector2D topLeft, int id, Player player) {
        placeOnMap(topLeft, EntityType.UNIT, id, player);
    }

    public void placeStructureOnMap(Vector2D topLeft, int id, Player player) {
        placeOnMap(topLeft, EntityType.STRUCTURE, id, player);
    }

    public void placeOnMap(Vector2D topLeft, EntityType entityType, int id, Player player) {
        EntityData entityData = getEntityData(entityType, id);
        System.out.println("Placing " + entityData + " on map at " + topLeft + " for " + player);
        try {
            Image originalImage = entityData.image;
            Image recoloredImage = recolorer.recolor(originalImage, player.getFactionColor());

            switch (entityType) {
                case STRUCTURE:
                    entities.add(map.placeStructure(new Structure(topLeft, recoloredImage, entityData.width, entityData.height, entityData.sight, player)));
                    break;
                case UNIT:
                    entities.add(map.placeUnit(new Unit(map, topLeft, recoloredImage, entityData.width, entityData.height, entityData.sight, player)));
                    break;
            }
        } catch (CellAlreadyOccupiedException e) {
            e.printStackTrace();
        }
    }

    public void createUnit(int id, String pathToImage, int widthInPixels, int heightInPixels, int sight) throws SlickException {
        createEntity(id, pathToImage, widthInPixels, heightInPixels, EntityType.UNIT, sight);
    }

    public void createStructure(int id, String pathToImage, int widthInPixels, int heightInPixels, int sight) throws SlickException {
        createEntity(id, pathToImage, widthInPixels, heightInPixels, EntityType.STRUCTURE, sight);
    }

    private void createEntity(int id, String pathToImage, int widthInPixels, int heightInPixels, EntityType entityType, int sight) throws SlickException {
        if (tryGetEntityData(entityType, id)) {
            throw new IllegalArgumentException("Entity of type " + entityType + " already exists with id " + id + ". Known entities are:\n" + entitiesData);
        }
        EntityData entityData = new EntityData();
        entityData.image = loadImage(pathToImage);
        entityData.width = widthInPixels;
        entityData.height = heightInPixels;
        entityData.type = entityType;
        entityData.sight = sight;
        entitiesData.put(constructKey(entityType, id), entityData);
    }

    protected Image loadImage(String pathToImage) throws SlickException {
        return new Image(pathToImage);
    }

    protected EntityData getEntityData(EntityType entityType, int id) {
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

    public Set<Entity> getEntities() {
        return entities;
    }

    public enum EntityType {
        STRUCTURE, UNIT
    }

    public class EntityData {
        public EntityType type;
        public Image image;
        public int width;
        public int height;
        public int sight;

        @Override
        public String toString() {
            return "EntityData{" +
                    "type=" + type +
                    ", image=" + image +
                    ", width=" + width +
                    ", height=" + height +
                    ", sight=" + sight +
                    '}';
        }
    }
}
