package com.fundynamic.d2tm.game.types;

import com.fundynamic.d2tm.game.entities.EntityType;
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

import static com.fundynamic.d2tm.game.map.Cell.TILE_SIZE;

/**
 * <h1>Overview</h1>
 * This is an object representation of an Entity. The {@link EntitiesData} class contains
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
 *      hitPoints=2000
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

    /**
     * the name used in the INI file (ie [QUAD] without [])
     */
    public String name;

    // Kind of entity it reflects
    public EntityType type;

    // Build related
    public EntityBuilderType entityBuilderType = EntityBuilderType.NONE;

    public float buildTimeInSeconds = 1.0F;
    public float buildRange = 0F;
    public int buildCost = -1;      // cost to build this
    public Image buildIcon;         // build icon
    public String buildList = "";

    public Image image;             // base image
    public Image barrelImage;       // barrelImage (top image)
    private int facings;

    private int width;              // in pixels
    private int height;             // in pixels
    private int widthInCells;       // in cells, derived from pixels
    private int heightInCells;      // in cells, derived from pixels

    public int maxAscensionHeight; // in pixels, how high a projectile can ascend when 'launched'
    public float startToDescendPercentage; // normalised value (between 0 and 1.0), when should descend be initiated?
    public float maxAscensionAtFlightPercentage; // normalised value (between 0 and 1.0), when should the projectile be at maxAscensionHeight during flight?

    public int sight;

    public float moveSpeed;         // the speed a unit moves: value is pixels in seconds.
    public float turnSpeed;         // the speed a unit turns: value is facing angles in seconds. < 1 means the value is times per second
    public float turnSpeedCannon;   // the speed a unit's barrel turns: value is facing angles in seconds. < 1 means the value is times per second

    public float attackRate;        // the speed a unit attacks: < 1 means the value is times per second
    public float attackRange;       // the range for a unit to attack in pixels

    public String weaponId = UNKNOWN;
    public int damage;

    public int hitPoints;           // initial hitPoints when spawned

    public String explosionId = UNKNOWN;

    public float animationSpeed;    // in frames per second, for animating

    public String key;              // key used in HashMap

    public boolean recolor;         // if 'true' then the particle will be recolored (into team color) before spawned

    // for turning
    private float chop = -1f;
    private float halfChop = -1f;

    public boolean isHarvester;     // if true, entity will execute harvesting logic, seeking spice, harvesting etc
    public boolean isRefinery;      // if true, entity will be something where spice can be delivered

    public SoundData soundData = null; // for playing sound if required

    public EntityData() {
    }

    public EntityData(EntityType entityType, int width, int height, int sight) {
        this.type = entityType;
        setWidth(width);
        setHeight(height);
        this.sight = sight;
    }

    public Image getFirstImage() {
        return image.getSubImage(0, 0, width, height);
    }

    public void setWidth(int width) {
        this.width = width;
        if (this.type == null) throw new IllegalStateException("You can only set width after you have set type");

        // units are always 1 cell in height (for now)
        if (this.type == EntityType.UNIT) {
            widthInCells = 1;
        } else {
            widthInCells = (int) Math.ceil((float) width / TILE_SIZE);
        }
    }

    public void setHeight(int height) {
        this.height = height;
        if (this.type == null) throw new IllegalStateException("You can only set height after you have set type");

        // units are always 1 cell in height (for now)
        if (this.type == EntityType.UNIT) {
            heightInCells = 1;
        } else {
            heightInCells = (int) Math.ceil((float) height / TILE_SIZE);
        }
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

//    public float getHalfChop() {
//        return halfChop;
//    }

    public int getFacings() {
        return facings;
    }

    public boolean hasExplosionId() {
        return !UNKNOWN.equals(explosionId);
    }

    public boolean hasWeaponId() {
        return !UNKNOWN.equals(weaponId);
    }

    public boolean hasSound() {
        return soundData != null;
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

    public float getRelativeDepositSpeed(float deltaInSeconds) {
        // 700 capacity
        // unloading 700 in 30 secs
        // 23,333333333 per second
//        float speed = 23.3333f;
        float speed = 70;
        return getRelativeSpeed(speed, deltaInSeconds);
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

    public boolean isTypeSuperPower() {
        return EntityType.SUPERPOWER.equals(this.type);
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
                int vecX = coordinate.getXAsInt() + (x * TILE_SIZE);
                int vecY = coordinate.getYAsInt() + (y * TILE_SIZE);
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
        Vector2D halfCell = Vector2D.create(TILE_SIZE / 2, TILE_SIZE / 2);
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        EntityData that = (EntityData) o;

        if (Float.compare(that.buildTimeInSeconds, buildTimeInSeconds) != 0) return false;
        if (Float.compare(that.buildRange, buildRange) != 0) return false;
        if (buildCost != that.buildCost) return false;
        if (facings != that.facings) return false;
        if (width != that.width) return false;
        if (height != that.height) return false;
        if (widthInCells != that.widthInCells) return false;
        if (heightInCells != that.heightInCells) return false;
        if (maxAscensionHeight != that.maxAscensionHeight) return false;
        if (Float.compare(that.startToDescendPercentage, startToDescendPercentage) != 0) return false;
        if (Float.compare(that.maxAscensionAtFlightPercentage, maxAscensionAtFlightPercentage) != 0) return false;
        if (sight != that.sight) return false;
        if (Float.compare(that.moveSpeed, moveSpeed) != 0) return false;
        if (Float.compare(that.turnSpeed, turnSpeed) != 0) return false;
        if (Float.compare(that.turnSpeedCannon, turnSpeedCannon) != 0) return false;
        if (Float.compare(that.attackRate, attackRate) != 0) return false;
        if (Float.compare(that.attackRange, attackRange) != 0) return false;
        if (damage != that.damage) return false;
        if (hitPoints != that.hitPoints) return false;
        if (Float.compare(that.animationSpeed, animationSpeed) != 0) return false;
        if (recolor != that.recolor) return false;
        if (Float.compare(that.chop, chop) != 0) return false;
        if (Float.compare(that.halfChop, halfChop) != 0) return false;
        if (isHarvester != that.isHarvester) return false;
        if (isRefinery != that.isRefinery) return false;
        if (name != null ? !name.equals(that.name) : that.name != null) return false;
        if (type != that.type) return false;
        if (entityBuilderType != that.entityBuilderType) return false;
        if (buildIcon != null ? !buildIcon.equals(that.buildIcon) : that.buildIcon != null) return false;
        if (buildList != null ? !buildList.equals(that.buildList) : that.buildList != null) return false;
        if (image != null ? !image.equals(that.image) : that.image != null) return false;
        if (barrelImage != null ? !barrelImage.equals(that.barrelImage) : that.barrelImage != null) return false;
        if (weaponId != null ? !weaponId.equals(that.weaponId) : that.weaponId != null) return false;
        if (explosionId != null ? !explosionId.equals(that.explosionId) : that.explosionId != null) return false;
        if (key != null ? !key.equals(that.key) : that.key != null) return false;
        return soundData != null ? soundData.equals(that.soundData) : that.soundData == null;
    }

    @Override
    public int hashCode() {
        int result = name != null ? name.hashCode() : 0;
        result = 31 * result + (type != null ? type.hashCode() : 0);
        result = 31 * result + (entityBuilderType != null ? entityBuilderType.hashCode() : 0);
        result = 31 * result + (buildTimeInSeconds != +0.0f ? Float.floatToIntBits(buildTimeInSeconds) : 0);
        result = 31 * result + (buildRange != +0.0f ? Float.floatToIntBits(buildRange) : 0);
        result = 31 * result + buildCost;
        result = 31 * result + (buildIcon != null ? buildIcon.hashCode() : 0);
        result = 31 * result + (buildList != null ? buildList.hashCode() : 0);
        result = 31 * result + (image != null ? image.hashCode() : 0);
        result = 31 * result + (barrelImage != null ? barrelImage.hashCode() : 0);
        result = 31 * result + facings;
        result = 31 * result + width;
        result = 31 * result + height;
        result = 31 * result + widthInCells;
        result = 31 * result + heightInCells;
        result = 31 * result + maxAscensionHeight;
        result = 31 * result + (startToDescendPercentage != +0.0f ? Float.floatToIntBits(startToDescendPercentage) : 0);
        result = 31 * result + (maxAscensionAtFlightPercentage != +0.0f ? Float.floatToIntBits(maxAscensionAtFlightPercentage) : 0);
        result = 31 * result + sight;
        result = 31 * result + (moveSpeed != +0.0f ? Float.floatToIntBits(moveSpeed) : 0);
        result = 31 * result + (turnSpeed != +0.0f ? Float.floatToIntBits(turnSpeed) : 0);
        result = 31 * result + (turnSpeedCannon != +0.0f ? Float.floatToIntBits(turnSpeedCannon) : 0);
        result = 31 * result + (attackRate != +0.0f ? Float.floatToIntBits(attackRate) : 0);
        result = 31 * result + (attackRange != +0.0f ? Float.floatToIntBits(attackRange) : 0);
        result = 31 * result + (weaponId != null ? weaponId.hashCode() : 0);
        result = 31 * result + damage;
        result = 31 * result + hitPoints;
        result = 31 * result + (explosionId != null ? explosionId.hashCode() : 0);
        result = 31 * result + (animationSpeed != +0.0f ? Float.floatToIntBits(animationSpeed) : 0);
        result = 31 * result + (key != null ? key.hashCode() : 0);
        result = 31 * result + (recolor ? 1 : 0);
        result = 31 * result + (chop != +0.0f ? Float.floatToIntBits(chop) : 0);
        result = 31 * result + (halfChop != +0.0f ? Float.floatToIntBits(halfChop) : 0);
        result = 31 * result + (isHarvester ? 1 : 0);
        result = 31 * result + (isRefinery ? 1 : 0);
        result = 31 * result + (soundData != null ? soundData.hashCode() : 0);
        return result;
    }
}
