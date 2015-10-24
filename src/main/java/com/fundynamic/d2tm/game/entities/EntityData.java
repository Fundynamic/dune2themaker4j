package com.fundynamic.d2tm.game.entities;

import com.fundynamic.d2tm.Game;
import com.fundynamic.d2tm.math.Vector2D;
import org.newdawn.slick.Image;

import java.util.ArrayList;
import java.util.List;

public class EntityData {

    private float chop = -1f;
    private float halfChop = -1f;

    public EntityType type;

    public Image image;

    private int width;   // in pixels
    private int height;  // in pixels
    private int widthInCells; // in cells, derived from pixels
    private int heightInCells; // in cells, derived from pixels

    public int sight;

    public float moveSpeed;   // the speed a unit moves: value is pixels in seconds.
    public float turnSpeed;   // the speed a unit turns: value is facing angles in seconds. < 1 means
    public float attackRate;  // the speed a unit attacks: the value is times per second

    public float attackRange; // the range for a unit to attack in pixels

    public int hitPoints;

    private int facings;

    public int damage;

    public String explosionId = "UNKNOWN";
    public String weaponId = "UNKNOWN";

    public float animationSpeed; // in frames per second
    public String key; // key used in HashMap

    public EntityData() {
    }

    public EntityData(int width, int height, int sight) {
        setWidth(width);
        setHeight(height);
        this.sight = sight;
    }

    public EntityData(int width, int height, int sight, float moveSpeed) {
        this(width, height, sight);
        this.moveSpeed = moveSpeed;
    }

    public Image getFirstImage() {
        return image.getSubImage(0, 0, width, height);
    }

    public void setWidth(int width) {
        this.width = width;
        widthInCells = (int) Math.ceil(width / Game.TILE_SIZE);
    }

    public void setHeight(int height) {
        this.height = height;
        heightInCells = (int) Math.ceil(height / Game.TILE_SIZE);
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
                ", type=" + type +
                ", halfChop=" + halfChop +
                ", image=" + image +
                ", width=" + width +
                ", height=" + height +
                ", sight=" + sight +
                ", moveSpeed=" + moveSpeed +
                ", hitPoints=" + hitPoints +
                ", facings=" + facings +
                ", damage=" + damage +
                ", explosionId='" + explosionId + '\'' +
                ", weaponId='" + weaponId + '\'' +
                ", animationSpeed=" + animationSpeed +
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
        return !"UNKNOWN".equals(explosionId);
    }

    public boolean hasWeaponId() {
        return !"UNKNOWN".equals(weaponId);
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
        return turnSpeed * deltaInSeconds;
    }

    public float getRelativeAttackRate(float deltaInSeconds) {
        return attackRate * deltaInSeconds;
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
    public List<Vector2D> getAllCellsAsVectors(Vector2D topLeftAbsCoordinates) {
        List<Vector2D> result = new ArrayList<>(widthInCells * heightInCells);
        for (int x = 0; x < widthInCells; x++) {
            for (int y = 0; y < heightInCells; y++) {
                int vecX = topLeftAbsCoordinates.getXAsInt() + x * Game.TILE_SIZE;
                int vecY = topLeftAbsCoordinates.getYAsInt() + y * Game.TILE_SIZE;
                result.add(Vector2D.create(vecX, vecY));
            }
        }
        return result;
    }
}
