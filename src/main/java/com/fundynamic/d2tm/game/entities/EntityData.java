package com.fundynamic.d2tm.game.entities;

import org.newdawn.slick.Image;

public class EntityData {

    private float chop = -1f;
    private float halfChop = -1f;

    public EntityType type;

    public Image image;

    public int width; // in pixels
    public int height; // in pixels

    public int sight;

    public float moveSpeed;
    public int hitPoints;

    private int facings;

    public int damage;
    public int explosionId = -1;
    public float animationSpeed; // in frames per second
    public int weaponId = -1;

    public EntityData() {
    }

    public EntityData(int width, int height, int sight) {
        this.width = width;
        this.height = height;
        this.sight = sight;
    }

    public EntityData(int width, int height, int sight, float moveSpeed) {
        this(width, height, sight);
        this.moveSpeed = moveSpeed;
    }

    public Image getFirstImage() {
        return image.getSubImage(0, 0, width, height);
    }

    @Override
    public String toString() {
        return "EntityData{" +
                "type=" + type +
                ", image=" + image +
                ", width=" + width +
                ", height=" + height +
                ", sight=" + sight +
                ", moveSpeed=" + moveSpeed +
                ", hitPoints=" + hitPoints +
                ", damage=" + damage +
                ", explosionId=" + explosionId +
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
}
