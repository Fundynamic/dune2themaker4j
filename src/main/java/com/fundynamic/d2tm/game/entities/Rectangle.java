package com.fundynamic.d2tm.game.entities;

import com.fundynamic.d2tm.math.Vector2D;

public class Rectangle {

    private float width;
    private float height;

    private Vector2D topLeft;
    private Vector2D bottomRight;

    public static Rectangle create(Vector2D absTopLeftInPixels, Vector2D absBottomRightInPixels) {
        return new Rectangle(absTopLeftInPixels.getX(), absTopLeftInPixels.getY(), absBottomRightInPixels.getX(), absBottomRightInPixels.getY());
    }

    public static Rectangle createWithDimensions(Vector2D absTopLeftInPixels, Vector2D dimensions) {
        return create(absTopLeftInPixels, absTopLeftInPixels.add(dimensions));
    }

    public Rectangle(int x, int y, int width, int height) {
        // cast one to float so the compiler knows we call the constructor below
        // and prevent a circular constructor calling
        this((float) x, y, x + width, y + height);
    }

    public Rectangle(float srcX, float srcY, float destX, float destY) {
        this.topLeft = Vector2D.create(Math.min(srcX, destX), Math.min(srcY, destY));
        this.bottomRight = Vector2D.create(Math.max(srcX, destX), Math.max(srcY, destY));
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
        boolean result = vec.getX() >= topLeft.getX() && vec.getX() < bottomRight.getX() && vec.getY() >= topLeft.getY() && vec.getY() < bottomRight.getY();
//        System.out.println("Testing if " + vec + " is within " + this + " --> " + result);
        return result;
    }

    public Vector2D getTopLeft() {
        return topLeft;
    }

    public Vector2D getSize() {
        return Vector2D.create(width, height);
    }

    @Override
    public String toString() {
        return "Rectangle{" +
                "width=" + width +
                ", height=" + height +
                ", topLeft=" + topLeft +
                ", bottomRight=" + bottomRight +
                '}';
    }
}
