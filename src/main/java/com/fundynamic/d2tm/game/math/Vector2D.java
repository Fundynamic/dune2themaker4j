package com.fundynamic.d2tm.game.math;

import org.newdawn.slick.geom.Vector2f;

public class Vector2D {

    private final Vector2f vec;

    public static Vector2D zero() {
        return new Vector2D(0, 0);
    }

    public Vector2D(float x, float y) {
        this.vec = new Vector2f(x, y);
    }

    public int getX() {
        return (int) vec.getX();
    }

    public int getY() {
        return (int) vec.getY();
    }

    public Vector2D move(float xVelocity, float yVelocity) {
        float newX = vec.getX() + xVelocity;
        float newY = vec.getY() + yVelocity;
        return new Vector2D(newX, newY);
    }

    public String shortString() {
        return "{" + vec.getX() + ", " + vec.getX() + "}";
    }
    @Override
    public String toString() {
        return "Vector2D{" +
                "x=" + vec.getX() +
                ", y=" + vec.getX() +
                '}';
    }

    public Vector2D diff(Vector2D otherVector) {
        return new Vector2D(vec.getX() - otherVector.getX(), vec.getY() - otherVector.getY());
    }

    public int getXAsInt() {
        return (int) vec.getX();
    }

    public int getYAsInt() {
        return (int) vec.getY();
    }
}
