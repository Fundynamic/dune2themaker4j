package com.fundynamic.d2tm.game.entities.entitiesdata;


import com.fundynamic.d2tm.Game;
import com.fundynamic.d2tm.game.entities.EntityData;
import com.fundynamic.d2tm.game.entities.EntityNotFoundException;
import com.fundynamic.d2tm.game.entities.EntityType;
import com.fundynamic.d2tm.game.entities.entitiesdata.ini.IniDataStructure;
import com.fundynamic.d2tm.game.entities.entitiesdata.ini.IniDataUnit;
import com.fundynamic.d2tm.utils.StringUtils;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

import java.util.HashMap;

public class EntitiesData {

    public static final String UNKNOWN = "UNKNOWN";

    // units

    /**
     * ID of TRIKE unit
     */
    public static String TRIKE = "TRIKE";

    /**
     * ID of QUAD unit
     */
    public static String QUAD = "QUAD";

    /**
     * ID of TANK unit
     */
    public static String TANK = "TANK";

    public static String SOLDIER = "SOLDIER";
    public static String INFANTRY = "INFANTRY";

    // structures
    public static String CONSTRUCTION_YARD = "CONSTYARD";
    public static String REFINERY = "REFINERY";
    public static String WINDTRAP = "WINDTRAP";
    public static String LIGHT_FACTORY = "LIGHTFACTORY";

    // projectiles
    public static String BULLET = "RIFLE";

    /**
     * ID of large rocket
     */
    public static final String LARGE_ROCKET = "LARGE_ROCKET";

    // explosions
    public static String EXPLOSION_SMALL_UNIT = "WHEELED";

    private HashMap<String, EntityData> entitiesData;

    public EntitiesData() {
        entitiesData = new HashMap<>();
    }

    public void addProjectile(String id, String pathToImage, int widthInPixels, int heightInPixels, String explosionId, float moveSpeed, int damage, int facings) throws SlickException {
        EntityData entity = createEntity(id, pathToImage, null, widthInPixels, heightInPixels, EntityType.PROJECTILE, -1, moveSpeed, -1);
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
    public void addParticle(String id, String pathToImage, int widthInPixels, int heightInPixels, float framesPerSecond, boolean recolor) throws SlickException {
        EntityData entity = createEntity(id, pathToImage, null, widthInPixels, heightInPixels, EntityType.PARTICLE, -1, -1, -1);
        entity.animationSpeed = framesPerSecond;
        entity.recolor = recolor;
    }

    /**
     * Create and add a Structure EntityData to this collection.
     *
     * @throws SlickException
     */
    public EntityData addStructure(String id, IniDataStructure iniDataStructure) throws SlickException {
        EntityData entityData = createEntity(
                id,
                iniDataStructure.image,
                null,
                iniDataStructure.width,
                iniDataStructure.height,
                EntityType.STRUCTURE,
                iniDataStructure.sight,
                0F,
                iniDataStructure.hitpoints
        );

        // The buildRange is (for now) determined by the size of the structure.
        // Because the range is calculated from the center of the structure.
        // In order to make it 'fair' for larger structures (if any would appear),
        // we add 'half' of the structure to the range.
        //
        // For a constyard it is a square, so 64x64 pixels = 1 'half', meaning:
        //
        // (64/64)/2 = 0,5 * 32 (tile width) = 16 pixels
        //
        // A larger structure would be (width/height), ie a heavy factory thing would be:
        // (96/64)/2 = ,75 * 32 = 24 pixels.
        //
        // Although on every squared structure this would even out fine, but then the
        // 'buildRange' should have one tile extra because (again) it is calculated
        // from the center
        //
        // this is all weird , then again, fixing this would require to check for every cell
        // on the structure which basically makes calculating the 'can I place it in this distance' logic
        // Width*height times more consuming.
        //
        // Unless....
        //
        // We do the 'computed map' thing (where we have all entity data on a map attached), so we don't
        // need to do an expensive lookup in the EntityRepository
        //TODO: Do something about the above, for now accept its quirks
        float someRatio = (float)entityData.getWidth() / (float)entityData.getHeight();
        int extraFromCenter = (int)((someRatio / 2) * Game.TILE_SIZE); // this is seriously flawed :/ (too tired to fix now)
        // add additional '1' to get 'past the center and occupy one cell'.
        entityData.buildRange = extraFromCenter + ((1 + iniDataStructure.buildRangeInTiles) * Game.TILE_SIZE);
        entityData.entityBuilderType = iniDataStructure.getEntityBuilderType();
        entityData.buildTimeInSeconds = iniDataStructure.buildTimeInSeconds;
        entityData.buildList = iniDataStructure.buildList;
        entityData.buildCost = iniDataStructure.buildCost;

        if (!idProvided(iniDataStructure.explosion)) {
            if (!tryGetEntityData(EntityType.PARTICLE, iniDataStructure.explosion)) {
                throw new IllegalArgumentException("structure " + id + " [explosion] refers to non-existing [EXPLOSIONS/" + iniDataStructure.explosion + "]");
            }
            entityData.explosionId = iniDataStructure.explosion;
        }
        entityData.buildIcon = loadImage(iniDataStructure.buildIcon);

        return entityData;
    }

    public EntityData addUnit(String id, IniDataUnit iniDataUnit) throws SlickException {
        EntityData entityData = createEntity(
                id,
                iniDataUnit.pathToImage,
                iniDataUnit.pathToBarrelImage,
                iniDataUnit.width,
                iniDataUnit.height,
                EntityType.UNIT,
                iniDataUnit.sight,
                iniDataUnit.moveSpeed,
                iniDataUnit.hitpoints
        );

        entityData.attackRate = iniDataUnit.attackRate;
        entityData.attackRange = iniDataUnit.attackRange;
        entityData.turnSpeed = iniDataUnit.turnSpeed;
        entityData.turnSpeedCannon = iniDataUnit.turnSpeedCannon;
        entityData.animationSpeed = iniDataUnit.animationSpeed;
        entityData.buildTimeInSeconds = iniDataUnit.buildTimeInSeconds;
        entityData.buildIcon = loadImage(iniDataUnit.buildIcon);
        entityData.buildCost = iniDataUnit.buildCost;

        String weaponId = iniDataUnit.weaponId;

        if (!idProvided(weaponId)) {
            if (!tryGetEntityData(EntityType.PROJECTILE, weaponId)) {
                throw new IllegalArgumentException("unit " + id + " [weapon] refers to non-existing [WEAPONS/" + weaponId + "]");
            }
            entityData.weaponId = weaponId;
        }

        String explosionId = iniDataUnit.explosionId;

        if (!idProvided(explosionId)) {
            if (!tryGetEntityData(EntityType.PARTICLE, explosionId)) {
                throw new IllegalArgumentException("unit " + id + " [explosion] refers to non-existing [EXPLOSIONS/" + explosionId + "]");
            }
            entityData.explosionId = explosionId;
        }
        return entityData;
    }

    public boolean idProvided(String weaponId) {
        return UNKNOWN.equals(weaponId);
    }

    private EntityData createEntity(String id, String pathToImage, String pathToBarrelImage, int widthInPixels, int heightInPixels, EntityType entityType, int sight, float moveSpeed, int hitPoints) throws SlickException {
        if (tryGetEntityData(entityType, id)) {
            throw new IllegalArgumentException("Entity of type " + entityType + " already exists with id " + id + ". Known entities are:\n" + entitiesData);
        }
        EntityData entityData = new EntityData();
        entityData.image = loadImage(pathToImage);
        entityData.barrelImage = loadImage(pathToBarrelImage);
        entityData.setWidth(widthInPixels);
        entityData.setHeight(heightInPixels);
        entityData.type = entityType;
        entityData.sight = sight;
        entityData.moveSpeed = moveSpeed;
        entityData.hitPoints = hitPoints;
        entityData.key = EntityData.constructKey(entityType, id);
        entitiesData.put(entityData.key, entityData);
        return entityData;
    }

    protected Image loadImage(String pathToImage) throws SlickException {
        if (StringUtils.isEmpty(pathToImage)) {
            return null;
        }
        return createSlickImage(pathToImage);
    }

    protected Image createSlickImage(String pathToImage) throws SlickException {
        return new Image(pathToImage);
    }

    public EntityData getParticle(String id) {
        return getEntityData(EntityType.PARTICLE, id);
    }

    public EntityData getEntityData(EntityType entityType, String id) {
        return getEntityData(EntityData.constructKey(entityType, id));
    }

    public EntityData getEntityData(String key) {
        EntityData entityData = entitiesData.get(key);
        if (entityData == null) throw new EntityNotFoundException("Entity not found for key " + key + ". Known entities are:\n" + toString());
        return entityData;
    }

    private boolean tryGetEntityData(EntityType entityType, String id) {
        try {
            getEntityData(entityType, id);
            return true;
        } catch (EntityNotFoundException e) {
//            System.err.println(e);
            return false;
        }
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
