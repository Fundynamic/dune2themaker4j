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
                '}';
    }

    public Image getFirstImage() {
        return image.getSubImage(0, 0, width, height);
    }
}
