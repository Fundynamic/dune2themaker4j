package com.fundynamic.d2tm.game.entities.entitiesdata;


import com.fundynamic.d2tm.game.entities.EntityNotFoundException;
import com.fundynamic.d2tm.game.entities.EntityType;
import com.fundynamic.d2tm.game.entities.entitiesdata.ini.*;
import com.fundynamic.d2tm.game.types.EntityData;
import com.fundynamic.d2tm.game.types.SoundData;
import com.fundynamic.d2tm.utils.StringUtils;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.Sound;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

import static com.fundynamic.d2tm.game.map.Cell.TILE_SIZE;

/**
 * A collection of all the blue-prints of all the entities within the game. Usually read from a resource
 * like rules.ini by the {@link EntitiesDataReader}
 */
public class EntitiesData {

    public static final String UNKNOWN = "UNKNOWN";
    public static final String DEATHHAND = "DEATHHAND";

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

    /**
     * ID of HARVESTER
     */
    public static final String HARVESTER = "HARVESTER";

    /**
     * ID of SOLDIER
     */
    public static String SOLDIER = "SOLDIER";

    /**
     * ID of INFANTRY
     */
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
    private HashMap<String, SoundData> soundsData;

    public EntitiesData() {
        entitiesData = new HashMap<>();
        soundsData = new HashMap<>();
    }

    public void addSound(String id, String path) throws SlickException {
        SoundData soundData = new SoundData();
        soundData.sound = loadSound(path);
        soundsData.put(id, soundData);
    }

    public void addProjectile(String id, IniDataWeapon iniDataWeapon) throws SlickException {
        EntityData entityData = createEntity(
                id,
                iniDataWeapon.image,
                null,
                iniDataWeapon.widthInPixels,
                iniDataWeapon.heightInPixels,
                EntityType.PROJECTILE,
                -1,
                iniDataWeapon.moveSpeed,
                -1
        );

        entityData.damage = iniDataWeapon.damage;
        entityData.explosionId = iniDataWeapon.explosionId;
        entityData.soundData = getAndEnsureSoundId(iniDataWeapon.soundId, id); // for launching projectile?

        entityData.maxAscensionHeight = iniDataWeapon.ascendTo;
        entityData.maxAscensionAtFlightPercentage = iniDataWeapon.ascendAt;
        entityData.startToDescendPercentage = iniDataWeapon.descendAt;

        entityData.setFacingsAndCalculateChops(iniDataWeapon.facings);
    }

    public void addSuperPower(String id, IniDataSuperPower iniDataSuperPower) {
        if (tryGetEntityData(EntityType.SUPERPOWER, id)) {
            throw new IllegalArgumentException("Entity of type " + EntityType.SUPERPOWER + " already exists with id " + id + ". Known entities are:\n" + entitiesData);
        }
        try {
            EntityData entityData = new EntityData();
            entityData.key = EntityData.constructKey(EntityType.SUPERPOWER, id);
            entityData.name = id;
            entityData.type = EntityType.SUPERPOWER;
            entityData.buildIcon = loadImage(iniDataSuperPower.buildIcon);
            entityData.buildCost = iniDataSuperPower.buildCost;
            entityData.buildTimeInSeconds = iniDataSuperPower.buildTimeInSeconds;
            entityData.weaponId = getAndEnsureWeaponId(iniDataSuperPower.weaponId, id);
            // TODO: Do a two step building of entityData
            // 1) first read
            // 2) then link (and check), because we get into circular references, ie Explosions vs Super powers and such
            if (idProvided(iniDataSuperPower.explosionId)) {
                if (!tryGetEntityData(EntityType.PARTICLE, iniDataSuperPower.explosionId)) {
                    throw new IllegalArgumentException("super power " + id + " [explosion] refers to non-existing [EXPLOSIONS/" + iniDataSuperPower.explosionId + "]");
                }
                entityData.explosionId = iniDataSuperPower.explosionId;
            }

            entitiesData.put(entityData.key, entityData);
        } catch (SlickException e) {
            throw new IllegalArgumentException("Unable to load image: ", e);
        }
    }


    /**
     * Create and add particle to this collection.
     *
     * @param id
     * @param iniDataExplosion
     * @throws SlickException
     */
    public void addParticle(String id, IniDataExplosion iniDataExplosion) throws SlickException {
        EntityData entityData = createEntity(
                id,
                iniDataExplosion.image,
                null,
                iniDataExplosion.widthInPixels,
                iniDataExplosion.heightInPixels,
                EntityType.PARTICLE,
                -1,
                -1,
                -1
        );

        entityData.soundData = getAndEnsureSoundId(iniDataExplosion.soundId, id);
        entityData.animationSpeed = iniDataExplosion.framesPerSecond;
        entityData.recolor = iniDataExplosion.recolor;
    }

    private SoundData getAndEnsureSoundId(String soundId, String id) {
        if (!idProvided(soundId)) {
            return null;
        }

        if (!soundsData.containsKey(soundId)) {
            throw new IllegalArgumentException("entity " + id + " property [Sound] refers to non-existing [SOUNDS/" + soundId + "]");
        }
        return soundsData.get(soundId);
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

        entityData.isRefinery = iniDataStructure.refinery;
        entityData.onPlacementSpawnUnitId = getAndEnsureUnitId(iniDataStructure.onPlacementSpawn, id);
        entityData.powerConsumption = iniDataStructure.powerConsumption;
        entityData.powerProduction = iniDataStructure.powerProduction;
        entityData.minimumPowerProductionPercentage = iniDataStructure.minimumPowerProduction;

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
        int extraFromCenter = (int)((someRatio / 2) * TILE_SIZE); // this is seriously flawed :/ (too tired to fix now)
        // add additional '1' to get 'past the center and occupy one cell'.
        entityData.buildRange = extraFromCenter + ((1 + iniDataStructure.buildRangeInTiles) * TILE_SIZE);
        entityData.entityBuilderType = iniDataStructure.getEntityBuilderType();
        entityData.buildTimeInSeconds = iniDataStructure.buildTimeInSeconds;
        entityData.buildList = iniDataStructure.buildList;
        entityData.buildCost = iniDataStructure.buildCost;

        if (idProvided(iniDataStructure.explosion)) {
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
                iniDataUnit.hitPoints
        );

        entityData.attackRate = iniDataUnit.attackRate;
        entityData.attackRange = iniDataUnit.attackRange;
        entityData.turnSpeed = iniDataUnit.turnSpeed;
        entityData.turnSpeedCannon = iniDataUnit.turnSpeedCannon;
        entityData.animationSpeed = iniDataUnit.animationSpeed;
        entityData.buildTimeInSeconds = iniDataUnit.buildTimeInSeconds;
        entityData.buildIcon = loadImage(iniDataUnit.buildIcon);
        entityData.buildCost = iniDataUnit.buildCost;
        entityData.name = id;
        entityData.isHarvester = iniDataUnit.harvester;
        entityData.harvestCapacity = iniDataUnit.harvestCapacity;
        entityData.depositSpeed = iniDataUnit.depositSpeed;
        entityData.harvestSpeed = iniDataUnit.harvestSpeed;

        // TODO: add moveable property
        if (id.equals(EntitiesData.INFANTRY) || id.equals(EntitiesData.SOLDIER)) {
            entityData.hasMoveAnimation = true;
        }

        entityData.weaponId = getAndEnsureWeaponId(iniDataUnit.weaponId, id);

        String explosionId = iniDataUnit.explosionId;

        if (idProvided(explosionId)) {
            if (!tryGetEntityData(EntityType.PARTICLE, explosionId)) {
                throw new IllegalArgumentException("unit " + id + " [explosion] refers to non-existing [EXPLOSIONS/" + explosionId + "]");
            }
            entityData.explosionId = explosionId;
        }
        return entityData;
    }

    public String getAndEnsureWeaponId(String weaponId, String id) {
        if (idProvided(weaponId) && !tryGetEntityData(EntityType.PROJECTILE, weaponId)) {
            throw new IllegalArgumentException("entity " + id + " property [Weapon] refers to non-existing [WEAPONS/" + weaponId + "]");
        }
        return weaponId;
    }

    public String getAndEnsureUnitId(String unitId, String id) {
        if (idProvided(unitId) && !tryGetEntityData(EntityType.UNIT, unitId)) {
            throw new IllegalArgumentException("entity " + id + " has a property that refers to non-existing [UNITS/" + unitId + "]");
        }
        return unitId;
    }

    public boolean idProvided(String id) {
        return !UNKNOWN.equals(id);
    }

    private EntityData createEntity(String id, String pathToImage, String pathToBarrelImage, int widthInPixels, int heightInPixels, EntityType entityType, int sight, float moveSpeed, int hitPoints) throws SlickException {
        if (tryGetEntityData(entityType, id)) {
            throw new IllegalArgumentException("Entity of type " + entityType + " already exists with id " + id + ". Known entities are:\n" + entitiesData);
        }
        EntityData entityData = new EntityData();
        entityData.image = loadImage(pathToImage);
        entityData.barrelImage = loadImage(pathToBarrelImage);
        entityData.type = entityType;
        entityData.setWidth(widthInPixels);
        entityData.setHeight(heightInPixels);
        entityData.sight = sight;
        entityData.moveSpeed = moveSpeed;
        entityData.hitPoints = hitPoints;
        entityData.key = EntityData.constructKey(entityType, id);
        entityData.name = id;
        entitiesData.put(entityData.key, entityData);
        return entityData;
    }

    /**
     * Loads image from path and returns a Slick image object. Returns null when path to image is null or empty string.
     * @param pathToImage
     * @return
     * @throws SlickException
     */
    protected Image loadImage(String pathToImage) throws SlickException {
        if (StringUtils.isEmpty(pathToImage)) {
            return null;
        }
        return createSlickImage(pathToImage);
    }

    protected Sound loadSound(String path) throws SlickException {
        if (StringUtils.isEmpty(path)) {
            return null;
        }
        return new Sound(path);
    }

    /**
     * Creates a Slick Image.
     *
     * TODO: Use imageRepository, as we can use this for all base images. When an entity is created by the
     * entity repository then there a copy should be made and recolored, there we can also cache those things
     * based on image-name + color.
     * @param pathToImage
     * @return
     * @throws SlickException
     */
    protected Image createSlickImage(String pathToImage) throws SlickException {
        System.err.println("Reading " + pathToImage);
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

    public List<SoundData> getSounds() {
        Collection<SoundData> values = soundsData.values();
        return new ArrayList<>(values);
    }
}
