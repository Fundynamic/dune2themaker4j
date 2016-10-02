package com.fundynamic.d2tm.game.entities;

import com.fundynamic.d2tm.Game;
import com.fundynamic.d2tm.game.entities.entitiesdata.EntitiesData;
import com.fundynamic.d2tm.game.entities.entitiesdata.EntitiesDataReader;
import com.fundynamic.d2tm.game.entities.entitybuilders.EntityBuilderType;
import com.fundynamic.d2tm.math.Coordinate;
import com.fundynamic.d2tm.math.MapCoordinate;
import com.fundynamic.d2tm.math.Vector2D;
import com.fundynamic.d2tm.utils.StringUtils;
import org.newdawn.slick.Image;

import java.util.ArrayList;
import java.util.List;

/**
 * <h1>Overview</h1>
 * This is an object representation of an Entity within a `rules.ini` file. The {@link EntitiesData} class contains
 * all objects after reading the rules.ini file. The interpretation of the file and the construction of an {@link EntityData} class
 * is done by the {@link EntitiesDataReader}.
 *
 * <h2>Structures</h2>
 * <p>Example piece of rules.ini file for structures:</p>
 * <pre>
 *     [STRUCTURES]
 *
 *      [STRUCTURES/CONSTYARD]
 *      image=structures/2x2_constyard.png
 *      hitpoints=2000
 *      width=64
 *      height=64
 *      sight=5
 *      explosion=BOOM
 *
 * </pre>
 * <p>The structure has a reference to EXPLOSIONS which is implemented by (#{@link com.fundynamic.d2tm.game.entities.particle.Particle}</p>
 */
public class EntityData {

    public static final String UNKNOWN = "UNKNOWN";
    private float chop = -1f;
    private float halfChop = -1f;
    public float buildTimeInSeconds = 5.0F;

    public EntityType type;

    public Image image; // base image
    public Image barrelImage; // barrelImage (top image)
    public Image buildIcon; // build icon

    private int width;   // in pixels
    private int height;  // in pixels
    private int widthInCells; // in cells, derived from pixels
    private int heightInCells; // in cells, derived from pixels

    public int sight;

    public float moveSpeed;   // the speed a unit moves: value is pixels in seconds.
    public float turnSpeed;   // the speed a unit turns: value is facing angles in seconds. < 1 means the value is times per second
    public float turnSpeedCannon; // the speed a unit's barrel turns: value is facing angles in seconds. < 1 means the value is times per second
    public float attackRate;  // the speed a unit attacks: < 1 means the value is times per second

    public float attackRange; // the range for a unit to attack in pixels

    public int hitPoints;

    private int facings;

    public int damage;

    public String explosionId = "UNKNOWN";
    public String weaponId = "UNKNOWN";

    public float animationSpeed; // in frames per second
    public String key; // key used in HashMap

    public boolean recolor; // if 'true' then the particle will be recolored (into team color) before spawned
    public EntityBuilderType entityBuilderType = EntityBuilderType.NONE;
    public String buildList = "";

    public EntityData() {
    }

    public EntityData(int width, int height, int sight) {
        setWidth(width);
        setHeight(height);
        this.sight = sight;
    }

    public Image getFirstImage() {
        return image.getSubImage(0, 0, width, height);
    }

    public void setWidth(int width) {
        this.width = width;
        widthInCells = (int) Math.ceil((float) width / Game.TILE_SIZE);
    }

    public void setHeight(int height) {
        this.height = height;
        heightInCells = (int) Math.ceil((float) height / Game.TILE_SIZE);
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public int getWidthInCells() {
        return widthInCells;
    }

    public int getHeightInCells() {
        return heightInCells;
    }

    @Override
    public String toString() {
        return "EntityData{" +
                "chop=" + chop +
                ", halfChop=" + halfChop +
                ", type=" + type +
                ", image=" + image +
                ", barrelImage=" + barrelImage +
                ", width=" + width +
                ", height=" + height +
                ", widthInCells=" + widthInCells +
                ", heightInCells=" + heightInCells +
                ", sight=" + sight +
                ", moveSpeed=" + moveSpeed +
                ", turnSpeed=" + turnSpeed +
                ", turnSpeedCannon=" + turnSpeedCannon +
                ", attackRate=" + attackRate +
                ", attackRange=" + attackRange +
                ", hitPoints=" + hitPoints +
                ", facings=" + facings +
                ", damage=" + damage +
                ", explosionId='" + explosionId + '\'' +
                ", weaponId='" + weaponId + '\'' +
                ", animationSpeed=" + animationSpeed +
                ", key='" + key + '\'' +
                ", recolor=" + recolor +
                '}';
    }

    public boolean hasFacings() {
        return facings > 0;
    }

    public void setFacingsAndCalculateChops(int facings) {
        this.facings = facings;
        this.chop = 360F / facings;
        this.halfChop = chop / 2F;
    }

    public float getChop() {
        return chop;
    }

    public float getHalfChop() {
        return halfChop;
    }

    public int getFacings() {
        return facings;
    }

    public boolean hasExplosionId() {
        return !UNKNOWN.equals(explosionId);
    }

    public boolean hasWeaponId() {
        return !UNKNOWN.equals(weaponId);
    }

    public String getWeaponIdKey() {
        return constructKey(EntityType.PROJECTILE, weaponId);
    }

    public String getExplosionIdKey() {
        return constructKey(EntityType.PARTICLE, explosionId);
    }

    public static String constructKey(EntityType entityType, String id) {
        return entityType.toString() + "-" + id;
    }

    /**
     * This takes time into account as well. This makes the distance of moveSpeed equivalent to 1 second.
     *
     * @param deltaInSeconds
     * @return
     */
    public float getRelativeMoveSpeed(float deltaInSeconds) {
        return moveSpeed * deltaInSeconds;
    }

    /**
     * See @link getRelativeMoveSpeed
     * @param deltaInSeconds
     * @return
     */
    public float getRelativeTurnSpeed(float deltaInSeconds) {
        return getRelativeSpeed(turnSpeed, deltaInSeconds);
    }

    public static float getRelativeSpeed(float speed, float deltaInSeconds) {
        return speed * deltaInSeconds;
    }

    public float getRelativeAttackRate(float deltaInSeconds) {
        return getRelativeSpeed(attackRate, deltaInSeconds);
    }

    public boolean isTypeStructure() {
        return EntityType.STRUCTURE.equals(this.type);
    }

    public boolean isTypeUnit() {
        return EntityType.UNIT.equals(this.type);
    }

    public boolean isTypeParticle() {
        return EntityType.PARTICLE.equals(this.type);
    }

    public boolean isTypeProjectile() {
        return EntityType.PROJECTILE.equals(this.type);
    }

    /**
     * Given a topLeftX and topLeftY coordinate, calculate all cells that are being occupied by this
     * entity and return that as a list of coordinates. These coordinates are top-left coordinates of cells.
     *
     * @return
     */
    public List<MapCoordinate> getAllCellsAsCoordinates(Coordinate coordinate) {
        List<MapCoordinate> result = new ArrayList<>(widthInCells * heightInCells);
        for (int x = 0; x < widthInCells; x++) {
            for (int y = 0; y < heightInCells; y++) {
                int vecX = coordinate.getXAsInt() + (x * Game.TILE_SIZE);
                int vecY = coordinate.getYAsInt() + (y * Game.TILE_SIZE);
                result.add(Coordinate.create(vecX, vecY).toMapCoordinate());
            }
        }
        return result;
    }

    /**
     * Given a topLeftX and topLeftY coordinate, calculate all cells that are being occupied by this
     * entity and return that as a list of coordinates.
     *
     * The coordinates are corrected to be centered within a cell.
     *
     * @return
     */
    public List<Coordinate> getAllCellsAsCenteredCoordinates(Coordinate coordinate) {
        List<MapCoordinate> result = getAllCellsAsCoordinates(coordinate);

        List<Coordinate> centered = new ArrayList<>(result.size());
        Vector2D halfCell = Vector2D.create(Game.TILE_SIZE / 2, Game.TILE_SIZE / 2);
        for (MapCoordinate resultCoordinate : result) {
            centered.add(resultCoordinate.toCoordinate().add(halfCell));
        }
        return centered;
    }

    public Vector2D getHalfSize() {
        return Vector2D.create(width / 2, height / 2);
    }

    public Vector2D getSize() {
        return Vector2D.create(width, height);
    }

    public List<String> getEntityDataKeysToBuild() {
        return StringUtils.splitLenientToList(buildList, ",");
    }
}
