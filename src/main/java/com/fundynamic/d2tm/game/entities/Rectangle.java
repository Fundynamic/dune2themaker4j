package com.fundynamic.d2tm.game.entities;

import com.fundynamic.d2tm.math.Vector2D;

public class Rectangle {

    private float width;
    private float height;
    private Vector2D src;
    private Vector2D dest;

    public static Rectangle create(Vector2D absTopLeftInPixels, Vector2D absBottomRightInPixels) {
        return new Rectangle(absTopLeftInPixels.getX(), absTopLeftInPixels.getY(), absBottomRightInPixels.getX(), absBottomRightInPixels.getY());
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

    public boolean isVectorWithin(Vector2D vec) {
        boolean result = vec.getX() >= src.getX() && vec.getX() < dest.getX() && vec.getY() >= src.getY() && vec.getY() < dest.getY();
        System.out.println("Testing if " + vec + " is within " + this + " --> " + result);
        return result;
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
