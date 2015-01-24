package com.fundynamic.d2tm.game.math;

public class Vector2D<T extends Number> {

    private final T x, y;

    public static Vector2D zero() {
        return new Vector2D(0, 0);
    }

    public Vector2D(T x, T y) {
        this.x = x;
        this.y = y;
    }

    public T getX() {
        return x;
    }

    public T getY() {
        return y;
    }

    public Vector2D<Float> move(T xVelocity, T yVelocity) {
        float newX = x.floatValue() + xVelocity.floatValue();
        float newY = y.floatValue() + yVelocity.floatValue();
        return new Vector2D<Float>(newX, newY);
    }

    public Vector2D<Integer> toInt() {
        int newX = x.intValue();
        int newY = y.intValue();
        return new Vector2D<>(newX, newY);
    }

    public String shortString() {
        return "{" + x + ", " + y + "}";
    }
    @Override
    public String toString() {
        return "Vector2D{" +
                "x=" + x +
                ", y=" + y +
                '}';
    }
}
