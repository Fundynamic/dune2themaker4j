package com.fundynamic.d2tm.game.math;


public class BoundedVector2D<T extends Number> extends Vector2D {

    private final T minX;
    private final T maxX;

    private final T minY;
    private final T maxY;


    public BoundedVector2D(T x, T y, T minX, T maxX, T minY, T maxY) {
        super(x, y);
        this.minX = minX;
        this.maxX = maxX;

        this.minY = minY;
        this.maxY = maxY;
    }

    @Override
    public BoundedVector2D move(float xVelocity, float yVelocity, float speed) {
        Vector2D newVec = super.move(xVelocity, yVelocity, speed);
        Number vecX = newVec.getX();
        Number vecY = newVec.getY();
        if (vecX.floatValue() < minX.floatValue()) vecX = minX;
        if (vecX.floatValue() > maxX.floatValue()) vecX = maxX;
        if (vecY.floatValue() < minY.floatValue()) vecY = minY;
        if (vecY.floatValue() > maxY.floatValue()) vecY = maxY;
        return new BoundedVector2D<>(vecX, vecY, minX, maxX, minY, maxY);
    }
}
