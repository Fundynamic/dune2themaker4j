package com.fundynamic.d2tm.math;

public class Rectangle {

    private int width;
    private int height;

    private Vector2D topLeft;
    private Vector2D bottomRight;

    public static Rectangle create(Coordinate absTopLeftInPixels, Coordinate absBottomRightInPixels) {
        return new Rectangle(absTopLeftInPixels.getXAsInt(), absTopLeftInPixels.getYAsInt(),
                absBottomRightInPixels.getXAsInt(), absBottomRightInPixels.getYAsInt());
    }

    public static Rectangle create(Vector2D absTopLeftInPixels, Vector2D absBottomRightInPixels) {
        return new Rectangle(absTopLeftInPixels.getXAsInt(), absTopLeftInPixels.getYAsInt(),
                absBottomRightInPixels.getXAsInt(), absBottomRightInPixels.getYAsInt());
    }

    public static Rectangle createWithDimensions(Vector2D absTopLeftInPixels, Vector2D dimensions) {
        return create(absTopLeftInPixels, absTopLeftInPixels.add(dimensions));
    }

    public Rectangle(int x, int y, Vector2D dimensions) {
        // cast one to float so the compiler knows we call the constructor below
        // and prevent a circular constructor calling
        this(x, y, x + dimensions.getXAsInt(), y + dimensions.getYAsInt());
    }

    public Rectangle(int srcX, int srcY, int destX, int destY) {
        this.topLeft = Vector2D.create(Math.min(srcX, destX), Math.min(srcY, destY));
        this.bottomRight = Vector2D.create(Math.max(srcX, destX), Math.max(srcY, destY));
        this.width = Math.abs(srcX - destX);
        this.height = Math.abs(srcY - destY);
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public boolean isVectorWithin(Vector2D vec) {
        boolean result = vec.getX() >= topLeft.getX() && vec.getX() < bottomRight.getX() && vec.getY() >= topLeft.getY() && vec.getY() < bottomRight.getY();
//        System.out.println("Testing if " + vec.getXAsInt() + "," + vec.getYAsInt() + " is within " + this + " --> " + result);
        return result;
    }

    public int getTopLeftX() {
        return topLeft.getXAsInt();
    }

    public int getTopLeftY() {
        return topLeft.getYAsInt();
    }

    public Vector2D getTopLeft() {
        return topLeft;
    }

    public Vector2D getDimensions() {
        return Vector2D.create(width, height);
    }

    @Override
    public String toString() {
        return "Rectangle [@ " + topLeft.getXAsInt() + ", " + topLeft.getYAsInt() + " -> WXH " + (topLeft.getXAsInt() + width) + " X " + (topLeft.getYAsInt() + height) + "]";
    }
}
