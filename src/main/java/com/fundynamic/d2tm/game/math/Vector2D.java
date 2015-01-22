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

    public Vector2D<Float> move(float xVelocity, float yVelocity, float speed) {
        float newX = x.floatValue() + (speed * xVelocity);
        float newY = y.floatValue() + (speed * yVelocity);
        return new Vector2D<>(newX, newY);
    }

    public Vector2D<Integer> toInt() {
        int newX = x.intValue();
        int newY = y.intValue();
        return new Vector2D<>(newX, newY);
    }

    @Override
    public String toString() {
        return "Vector2D{" +
                "x=" + x +
                ", y=" + y +
                '}';
    }
}
