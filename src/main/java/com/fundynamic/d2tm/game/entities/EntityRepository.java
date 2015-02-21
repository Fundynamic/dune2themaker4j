package com.fundynamic.d2tm.game.entities;


import com.fundynamic.d2tm.game.entities.structures.Structure;
import com.fundynamic.d2tm.game.entities.units.Unit;
import com.fundynamic.d2tm.game.map.Map;
import com.fundynamic.d2tm.math.Vector2D;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

import java.util.HashMap;

public class EntityRepository {

    public static int TRIKE = 0;
    public static int QUAD = 1;

    public static int CONSTRUCTION_YARD = 0;
    public static int REFINERY = 1;

    private final Map map;

    private HashMap<String, EntityData> entitiesData;

    public EntityRepository(Map map) throws SlickException {
        this.map = map;

        // TODO: read this data from an external (XML/JSON/YML/INI) file
        this.entitiesData = new HashMap<>();

        createUnit(QUAD, "units/quad.png", 32, 32);
        createUnit(TRIKE, "units/trike.png", 28, 26);
        createStructure(CONSTRUCTION_YARD, "structures/2x2_constyard.png", 64, 64);
        createStructure(REFINERY, "structures/3x2_refinery.png", 96, 64);
    }

    public void placeUnitOnMap(Vector2D topLeft, int id) {
        placeOnMap(topLeft, EntityType.UNIT, id);
    }

    public void placeStructureOnMap(Vector2D topLeft, int id) {
        placeOnMap(topLeft, EntityType.STRUCTURE, id);
    }

    public void placeOnMap(Vector2D topLeft, EntityType entityType, int id) {
        String entityKey = constructKey(entityType, id);
        EntityData entityData = entitiesData.get(entityKey);
        if (entityData == null) {
            throw new IllegalArgumentException("Unable to find entityData for type " + entityType + " and id " + id + ". Possible entities are:\n" + entityData);
        }
        System.out.println("Placing " + entityKey + " on map at " + topLeft);
        switch (entityType) {
            case STRUCTURE:
                map.placeStructure(new Structure(topLeft, entityData.image, entityData.width, entityData.height));
                break;
            case UNIT:
                map.placeUnit(new Unit(topLeft, entityData.image, entityData.width, entityData.height));
                break;
            default:
                throw new IllegalStateException("Don't know how to place entity type " + entityKey + " on map!");
        }
    }

    public void createUnit(int ID, String pathToImage, int widthInPixels, int heightInPixels) throws SlickException {
        EntityData entityData = createData(pathToImage, widthInPixels, heightInPixels);
        entityData.type = EntityType.UNIT;
        entitiesData.put(constructKey(EntityType.UNIT, ID), entityData);
    }

    public void createStructure(int ID, String pathToImage, int widthInPixels, int heightInPixels) throws SlickException {
        EntityData entityData = createData(pathToImage, widthInPixels, heightInPixels);
        entityData.type = EntityType.STRUCTURE;
        entitiesData.put(constructKey(EntityType.STRUCTURE, ID), entityData);
    }

    private EntityData createData(String pathToImage, int widthInPixels, int heightInPixels) throws SlickException {
        EntityData entityData = new EntityData();
        entityData.image = new Image(pathToImage);
        entityData.width = widthInPixels;
        entityData.height = heightInPixels;
        return entityData;
    }

    public String constructKey(EntityType type, int ID) {
        return type.toString() + "-" + ID;
    }

    public enum EntityType {
        STRUCTURE, UNIT
    }

    public class EntityData {
        public EntityType type;
        public Image image;
        public int width;
        public int height;
    }
}
