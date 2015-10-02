package com.fundynamic.d2tm.game.entities;

import org.newdawn.slick.Image;

public class EntityData {

    public EntityType type;

    public Image image;

    public int width;
    public int height;

    public int sight;

    public float moveSpeed;
    public int hitPoints;

    public int damage;
    public int explosionId = -1;
    public float animationSpeed; // in frames per second

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
}
