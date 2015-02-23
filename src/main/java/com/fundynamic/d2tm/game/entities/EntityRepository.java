package com.fundynamic.d2tm.game.entities;


import com.fundynamic.d2tm.game.entities.structures.Structure;
import com.fundynamic.d2tm.game.entities.units.Unit;
import com.fundynamic.d2tm.game.map.Map;
import com.fundynamic.d2tm.math.Vector2D;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

import java.util.HashMap;

//TODO: This class should be under test!
public class EntityRepository {

    public static int TRIKE = 0;
    public static int QUAD = 1;

    public static int CONSTRUCTION_YARD = 0;
    public static int REFINERY = 1;

    private final Map map;

    private HashMap<String, EntityData> entitiesData;

    public EntityRepository(Map map) throws SlickException {
        this(map, new HashMap<String, EntityData>());

        // TODO: read this data from an external (XML/JSON/YML/INI) file
        createUnit(QUAD, "units/quad.png", 32, 32);
        createUnit(TRIKE, "units/trike.png", 28, 26);
        createStructure(CONSTRUCTION_YARD, "structures/2x2_constyard.png", 64, 64);
        createStructure(REFINERY, "structures/3x2_refinery.png", 96, 64);
    }

    public EntityRepository(Map map, HashMap<String, EntityData> entitiesData) throws SlickException {
        this.map = map;
        this.entitiesData = entitiesData;
    }

    public void placeUnitOnMap(Vector2D topLeft, int id) {
        placeOnMap(topLeft, EntityType.UNIT, id);
    }

    public void placeStructureOnMap(Vector2D topLeft, int id) {
        placeOnMap(topLeft, EntityType.STRUCTURE, id);
    }

    public void placeOnMap(Vector2D topLeft, EntityType entityType, int id) {
        EntityData entityData = getEntityData(entityType, id);
        System.out.println("Placing " + entityData + " on map at " + topLeft);
        switch (entityType) {
            case STRUCTURE:
                map.placeStructure(new Structure(topLeft, entityData.image, entityData.width, entityData.height));
                break;
            case UNIT:
                map.placeUnit(new Unit(topLeft, entityData.image, entityData.width, entityData.height));
                break;
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
        entityData.image = loadImage(pathToImage);
        entityData.width = widthInPixels;
        entityData.height = heightInPixels;
        return entityData;
    }

    protected Image loadImage(String pathToImage) throws SlickException {
        return new Image(pathToImage);
    }

    protected EntityData getEntityData(EntityType entityType, int id) {
        EntityData entityData = entitiesData.get(constructKey(entityType, id));
        if (entityData == null) throw new EntityNotFoundException("Entity not found for entityType " + entityType + " and ID " + id + ". Possible entities are:\n" + entitiesData);
        return entityData;
    }

    public String constructKey(EntityType entityType, int id) {
        return entityType.toString() + "-" + id;
    }

    public enum EntityType {
        STRUCTURE, UNIT
    }

    public class EntityData {
        public EntityType type;
        public Image image;
        public int width;
        public int height;

        @Override
        public String toString() {
            return "EntityData{" +
                    "type=" + type +
                    ", image=" + image +
                    ", width=" + width +
                    ", height=" + height +
                    '}';
        }
    }
}
