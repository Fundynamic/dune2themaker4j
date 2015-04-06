package com.fundynamic.d2tm.game.entities;

import com.fundynamic.d2tm.math.Vector2D;

public class Rectangle {

    private float width;
    private float height;
    private Vector2D src;
    private Vector2D dest;

    public static Rectangle create(Vector2D topLeft, Vector2D bottomRight) {
        return new Rectangle(topLeft.getX(), topLeft.getY(), bottomRight.getX(), bottomRight.getY());
    }

    public Rectangle(float srcX, float srcY, float destX, float destY) {
        this.src = Vector2D.create(Math.min(srcX, destX), Math.min(srcY, destY));
        this.dest = Vector2D.create(Math.max(srcX, destX), Math.max(srcY, destY));
        this.width = Math.abs(srcX - destX);
        this.height = Math.abs(srcY - destY);
    }

    public int getWidthAsInt() {
        return (int)width;
    }

    public int getHeightAsInt() {
        return (int)height;
    }

    public boolean isWithin(Vector2D vec) {
        return vec.getX() >= src.getX() && vec.getX() < dest.getX() && vec.getY() >= src.getY() && vec.getY() < dest.getY();
    }

    @Override
    public String toString() {
        return "Rectangle{" +
                "width=" + width +
                ", height=" + height +
                ", src=" + src +
                ", dest=" + dest +
                '}';
    }
}
