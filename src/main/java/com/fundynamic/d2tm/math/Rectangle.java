package com.fundynamic.d2tm.math;

public class Rectangle {

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
        this(x, y, x + dimensions.getXAsInt(), y + dimensions.getYAsInt());
    }

    public Rectangle(int srcX, int srcY, int destX, int destY) {
        this.topLeft = Vector2D.create(Math.min(srcX, destX), Math.min(srcY, destY));
        this.bottomRight = Vector2D.create(Math.max(srcX, destX), Math.max(srcY, destY));
    }

    public int getWidth() {
        return (int)Math.abs(this.topLeft.getX() - this.bottomRight.getX());
    }

    public int getHeight() {
        return (int)Math.abs(this.topLeft.getY() - this.bottomRight.getY());
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

    public int getBottomRightX() {
        return bottomRight.getXAsInt();
    }

    public int getBottomRightY() {
        return bottomRight.getYAsInt();
    }

    public Vector2D getTopLeft() {
        return topLeft;
    }

    public Vector2D getBottomRight() {
        return bottomRight;
    }

    public Vector2D getDimensions() {
        return Vector2D.create(getWidth(), getHeight());
    }

    public Vector2D makeSureVectorStaysWithin(Vector2D vec) {
        float x = Math.min(Math.max(vec.getX(), getTopLeftX()), getBottomRightX());
        float y = Math.min(Math.max(vec.getY(), getTopLeftY()), getBottomRightY());
        return Vector2D.create(x, y);
    }

    @Override
    public String toString() {
        return "Rectangle [@ " + topLeft.getXAsInt() + ", " + topLeft.getYAsInt() + " -> WXH " + getWidth() + " X " + getHeight() + "]";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Rectangle rectangle = (Rectangle) o;

        if (!topLeft.equals(rectangle.topLeft)) return false;
        return bottomRight.equals(rectangle.bottomRight);
    }

    @Override
    public int hashCode() {
        int result = topLeft.hashCode();
        result = 31 * result + bottomRight.hashCode();
        return result;
    }

    public Rectangle scaleContainCenter(Vector2D dimensions) {
        float rectAspectRatio = (float)getWidth() / getHeight();
        float containAspectRatio = dimensions.getX() / dimensions.getY();

        if (Math.abs(rectAspectRatio - containAspectRatio) <= 0.0001) {
            // same aspect ratio, return a copy of this rectangle
            return new Rectangle(getTopLeftX(), getTopLeftY(), getBottomRightX(), getBottomRightY());
        } else if (rectAspectRatio < containAspectRatio) {
            // use full width, scale and center the height
            float newHeight = dimensions.getY() * (getWidth() / dimensions.getX());
            float offset = (getHeight() - newHeight) / 2;
            return new Rectangle(
                getTopLeftX(), (int)(getTopLeftY() + offset),
                getBottomRightX(), (int)(getTopLeftY() + offset + newHeight));
        } else {
            // use full height, scale and center the width
            float newWidth = dimensions.getX() * (getHeight() / dimensions.getY());
            float offset = (getWidth() - newWidth) / 2;
            return new Rectangle(
                (int)(getTopLeftX() + offset), getTopLeftY(),
                (int)(getTopLeftX() + offset + newWidth), getBottomRightY());
        }
    }

    public void updateY(int value) {
        int width = getWidth();
        int height = getHeight();

        topLeft.update(topLeft.getX(), value);

        bottomRight.update(getTopLeftX() + width, getTopLeftY() + height);
    }
}
